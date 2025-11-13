package tezish.tezish.services.JobShift;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import tezish.tezish.models.Company.BusinessUser;
import tezish.tezish.models.Company.Company;
import tezish.tezish.models.Company.LocationRequest;
import tezish.tezish.models.Job_Shift.*;
import tezish.tezish.models.Location.Location;
import tezish.tezish.models.Response.ShiftJobResponse.JobRequest;
import tezish.tezish.models.Response.ShiftJobResponse.JobShiftResponse;
import tezish.tezish.models.Response.ShiftJobResponse.ShiftJobResponse;
import tezish.tezish.models.Response.ShiftJobResponse.ShiftRequest;
import tezish.tezish.models.User;
import tezish.tezish.repositories.AdminUser.UserRepository;
import tezish.tezish.repositories.BusinessUser.BusinessUserRepository;
import tezish.tezish.repositories.BusinessUser.CompanyRepository;
import tezish.tezish.repositories.BusinessUser.LocationRepository;

import tezish.tezish.repositories.JobShifts.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final ShiftRepository shiftRepository;
    private final LocationRepository locationRepository;
    private final JobStatusRepository jobStatusRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    private final JobCategoryRepository jobCategoryRepository;
    private final JobTitleRepository jobTitleRepository;
    private final BusinessUserRepository businessUserRepository;



    @Transactional
    public Job createJob(JobRequest jobRequest, Long userId, Long companyId, boolean isAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getBusinessUser() == null) {
            throw new RuntimeException("User is not a business user.");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found."));

        Job job = new Job();
        job.setPosition(jobRequest.getPosition());
        
        JobTitle jobTitle = jobTitleRepository.findByTitle(jobRequest.getPosition());
        if (jobTitle == null) {
            throw new RuntimeException("Job title not found: " + jobRequest.getPosition());
        }
        
        job.setJobImage(jobTitle.getImageUrl());
        job.setEmoji(jobTitle.getEmoji());
        
        Location location = locationRepository.findById(jobRequest.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        job.setLocation(location);
        
        job.setPrice(jobRequest.getPrice());
        job.setFreelancerCount(jobRequest.getFreelancerCount());
        job.setProvidesDress(jobRequest.isProvidesDress());
        job.setProvidesMeal(jobRequest.isProvidesMeal());
        job.setProvidesTraining(jobRequest.isProvidesTraining());
        job.setProvidesTravelAllowance(jobRequest.isProvidesTravelAllowance());
        job.setRequiredDress(jobRequest.isRequiredDress());
        job.setRequiredHealthCard(jobRequest.isRequiredHealthCard());
        job.setRequiresID(jobRequest.isRequiresID());
        List<Long> managers = (jobRequest.getManagerIds() != null && !jobRequest.getManagerIds().isEmpty())
                ? jobRequest.getManagerIds()
                : (jobRequest.getManagerId() != null ? List.of(jobRequest.getManagerId()) : Collections.emptyList());
        job.setManagers(managers);
        job.setManager(managers.isEmpty() ? null : managers.get(0));
        job.setCreatedBy(company);
        job.setCreatedByAdmin(isAdmin);
        job.setCreatedByBusinessUser(user.getBusinessUser());
        
        JobStatus status = jobStatusRepository.findById(jobRequest.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status not found"));
        job.setStatus(status);
        job.setAdditionalInfo(jobRequest.getAdditionalInfo());

        List<Shift> shifts = new ArrayList<>();
        if (jobRequest.getShifts() != null && !jobRequest.getShifts().isEmpty()) {
            for (ShiftRequest sr : jobRequest.getShifts()) {
                Shift shift = new Shift();
                shift.setJob(job);
                shift.setStartDate(sr.getStartDate());
                shift.setEndDate(sr.getEndDate());
                shift.setPrice(job.getPrice());
                shifts.add(shift);
            }
        } else {
            throw new RuntimeException("At least one shift must be provided");
        }
        
        job.setShifts(shifts);
        job = jobRepository.save(job);

        return job;
    }


    private void markShiftsAsNotifiedForJobCreation(Job job) {
        try {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Baku"));
            LocalDateTime threeDaysFromNow = now.plusDays(3);
            
            for (Shift shift : job.getShifts()) {
                // Only mark shifts within the 3-day window as notified
                if (shift.getStartDate() != null && 
                    !shift.getStartDate().isBefore(now) && 
                    shift.getStartDate().isBefore(threeDaysFromNow)) {
                    // Set both flags to fully prevent scheduler from picking up this shift
                    shift.setVisibleWindowNotified(true);
                    shift.setVisibleWindowNotificationScheduled(true);
                    shift.setVisibleWindowNotifyAt(now); // Record when notification was sent
                    System.out.println("Marking shift " + shift.getId() + " as notified to prevent duplicate notifications");
                }
            }
            shiftRepository.saveAll(job.getShifts());
            System.out.println("Successfully marked shifts as notified for job: " + job.getId());
        } catch (Exception e) {
            System.err.println("Failed to mark shifts as notified for job " + job.getId() + ": " + e.getMessage());
            // Don't throw exception - this is a best-effort operation to prevent duplicates
        }
    }
    

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllUpcomingShiftsForBusinessUser(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BusinessUser businessUser = user.getBusinessUser();
        if (businessUser == null) {
            throw new RuntimeException("This user is not a business user.");
        }

        Company company = businessUser.getCompany();
        List<Job> jobs = jobRepository.findByCreatedBy(company);

        List<Shift> allShifts = jobs.stream()
                .flatMap(job -> job.getShifts().stream())
                .toList();

        LocalDateTime now = LocalDateTime.now().minusDays(4);
        List<Shift> filteredShifts = allShifts.stream()
                .filter(shift ->
                        !shift.isArchived() &&
                        !shift.getStartDate().isBefore(now) &&
                        (startTime == null || !shift.getStartDate().isBefore(startTime)) &&
                        (endTime == null || !shift.getStartDate().isAfter(endTime))
                )
                .sorted(Comparator.comparing(Shift::getStartDate))
                .toList();

        return filteredShifts.stream()
                .map(shift -> {
                    Job job = shift.getJob();
                    return new ShiftResponse(
                            shift.getId(),
                            job.getLocation().getLocationName(),
                            shift.getPrice(),
                            shift.getStartDate(),
                            shift.getEndDate(),
                            job.getFreelancerCount(),
                            String.format("%02d:%02d", shift.getStartDate().getHour(), shift.getStartDate().getMinute()),
                            String.format("%02d:%02d", shift.getEndDate().getHour(), shift.getEndDate().getMinute()),
                            job.getPosition(),
                            job.getJobImage(),
                            job.getEmoji()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getAllUpcomingShiftsForManager(Long managerId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Job> jobs = jobRepository.findByManager(managerId);

        List<Shift> allShifts = jobs.stream()
                .flatMap(job -> job.getShifts().stream())
                .toList();

        LocalDateTime now = LocalDateTime.now().minusDays(1);
        List<Shift> filteredShifts = allShifts.stream()
                .filter(shift -> 
                    !shift.isArchived() &&
                    !shift.getStartDate().isBefore(now) &&
                    (startTime == null || !shift.getStartDate().isBefore(startTime)) &&
                    (endTime == null || !shift.getStartDate().isAfter(endTime))
                )
                .sorted(Comparator.comparing(Shift::getStartDate))
                .toList();

        // 5. Map each Shift to a ShiftResponse DTO.
        return filteredShifts.stream()
                .map(shift -> {
                    Job job = shift.getJob();
                    return new ShiftResponse(
                            shift.getId(),
                            job.getLocation().getLocationName(),
                            shift.getPrice(),
                            shift.getStartDate(),
                            shift.getEndDate(),
                            job.getFreelancerCount(),
                            String.format("%02d:%02d", shift.getStartDate().getHour(), shift.getStartDate().getMinute()),
                            String.format("%02d:%02d", shift.getEndDate().getHour(), shift.getEndDate().getMinute()),
                            job.getPosition(),
                            job.getJobImage(),
                            job.getEmoji()
                    );
                })
                .collect(Collectors.toList());
    }

    public Shift getShiftById(Long shiftId) {
        return shiftRepository.findById(shiftId).orElse(null);
    }

    @Transactional
    public Job updateJob(JobRequest jobRequest, Long shiftId, boolean isBusinessUser) {
        Shift shift1 = getShiftById(shiftId);
        Job job = jobRepository.findById(shift1.getJob().getId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setPosition(jobRequest.getPosition());
        JobTitle jobTitle = jobTitleRepository.findByTitle(jobRequest.getPosition());
        job.setJobImage(jobTitle.getImageUrl());
        job.setPrice(jobRequest.getPrice());
        List<Long> managers = (jobRequest.getManagerIds() != null && !jobRequest.getManagerIds().isEmpty())
                ? jobRequest.getManagerIds()
                : (jobRequest.getManagerId() != null ? List.of(jobRequest.getManagerId()) : Collections.emptyList());
        job.setManagers(managers);
        job.setManager(managers.isEmpty() ? null : managers.get(0));
        job.setFreelancerCount(jobRequest.getFreelancerCount());
        job.setProvidesDress(jobRequest.isProvidesDress());
        job.setProvidesTraining(jobRequest.isProvidesTraining());
        job.setProvidesMeal(jobRequest.isProvidesMeal());
        job.setProvidesTravelAllowance(jobRequest.isProvidesTravelAllowance());
        job.setRequiredDress(jobRequest.isRequiredDress());
        job.setRequiredHealthCard(jobRequest.isRequiredHealthCard());
        job.setRequiresID(jobRequest.isRequiresID());

        Location location = locationRepository.findById(jobRequest.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        job.setLocation(location);

        JobStatus status = jobStatusRepository.findById(jobRequest.getStatusId())
                .orElseThrow(() -> new RuntimeException("Status not found"));
        job.setStatus(status);

        job.setAdditionalInfo(jobRequest.getAdditionalInfo());

        List<Shift> currentShifts = job.getShifts() != null ? job.getShifts() : new ArrayList<>();
        Map<Long, Shift> currentShiftsMap = currentShifts.stream()
                .collect(Collectors.toMap(Shift::getId, shift -> shift));
        Set<Long> requestShiftIds = new HashSet<>();
        List<Shift> activeShifts = new ArrayList<>();

        if (jobRequest.getShifts() != null && !jobRequest.getShifts().isEmpty()) {
            for (ShiftRequest shiftRequest : jobRequest.getShifts()) {
                requestShiftIds.add(shiftRequest.getShiftId());
                if (shiftRequest.getShiftId() != null && currentShiftsMap.containsKey(shiftRequest.getShiftId())) {
                    Shift shift = currentShiftsMap.get(shiftRequest.getShiftId());
                    shift.setStartDate(shiftRequest.getStartDate());
                    shift.setEndDate(shiftRequest.getEndDate());
                    activeShifts.add(shift);
                } else {
                    Shift newShift = new Shift();
                    newShift.setJob(job);
                    newShift.setStartDate(shiftRequest.getStartDate());
                    newShift.setEndDate(shiftRequest.getEndDate());
                    newShift = shiftRepository.save(newShift);
                    activeShifts.add(newShift);
                }
            }
        } else {
            throw new RuntimeException("At least one shift must be provided");
        }

        for (Shift shift : currentShifts) {
            if (shift.getId() != null && !requestShiftIds.contains(shift.getId())) {
                shift.setArchived(true);
                activeShifts.add(shift);
            }
        }

        job.setShifts(activeShifts);

        // Get data needed for Slack notification BEFORE saving (to avoid LazyInitializationException)
        String companyName = "N/A";
        String shiftTime = "N/A";
        
        try {
            if (job.getCreatedBy() != null) {
                companyName = job.getCreatedBy().getName();
            }
        } catch (Exception e) {
            // If lazy loading fails, use a fallback
            companyName = "N/A";
        }
        
        if (!activeShifts.isEmpty() && activeShifts.get(0).getStartDate() != null) {
            shiftTime = activeShifts.get(0).getStartDate().toString();
        }
        
        Job updatedJob = jobRepository.save(job);
        
        if (isBusinessUser) {
            String businessUserName = "BusinessUser";
            try {
                org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                    String email = userDetails.getUsername();
                    tezish.tezish.models.Company.BusinessUser businessUser = businessUserRepository.findAll().stream()
                        .filter(bu -> bu.getEmail().equals(email)).findFirst().orElse(null);
                    if (businessUser != null && businessUser.getName() != null) {
                        businessUserName = businessUser.getName();
                    }
                }
            } catch (Exception e) { /* ignore */ }
            String slackMsg = String.format(
                ":pencil2: *Shift Edited by Business User!*\n*User:* %s\n*Shift ID:* %d\n*Job:* %s\n*Company:* %s\n*Time:* %s",
                businessUserName, shiftId, job.getPosition(), companyName, shiftTime
            );

        }
        return updatedJob;
    }

    private boolean sameDate(LocalDateTime d1, LocalDateTime d2) {
        Date d11 = Date.from(d1.atZone(ZoneId.systemDefault()).toInstant());
        Date d12 = Date.from(d2.atZone(ZoneId.systemDefault()).toInstant());
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d11);
        cal2.setTime(d12);
        return cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR) ||
                cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH) ||
                cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH);
    }

    public Location createLocation(LocationRequest locationRequest) {
        Company company = companyRepository.findById(locationRequest.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Location location = new Location();
        location.setLocationName(locationRequest.getLocationName());
        location.setCompany(company);
        location.setCoordinates(locationRequest.getCoordinates());
        location.setDescription(locationRequest.getDescription());

        return locationRepository.save(location);
    }

    @Transactional(readOnly = true)
    public List<tezish.tezish.models.Response.ShiftJobResponse.JobCategoryResponse> getJobCategoryWithTitles() {
        return jobCategoryRepository.findAll().stream()
                .filter(cat -> !cat.isArchived())
                .map(cat -> {
                    List<tezish.tezish.models.Response.ShiftJobResponse.JobTitleResponse> titles = 
                        cat.getJobTitles().stream()
                            .filter(title -> !title.isArchived())
                            .map(title -> new tezish.tezish.models.Response.ShiftJobResponse.JobTitleResponse(
                                title.getId(),
                                title.getTitle(),
                                title.getImageUrl(),
                                title.getEmoji(),
                                title.isArchived()
                            ))
                            .collect(Collectors.toList());
                    
                    return new tezish.tezish.models.Response.ShiftJobResponse.JobCategoryResponse(
                        cat.getId(),
                        cat.getName(),
                        titles,
                        cat.isArchived()
                    );
                })
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ShiftNotFoundException extends RuntimeException {
        public ShiftNotFoundException(Long id) {
            super("Shift not found: " + id);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ShiftArchivedException extends RuntimeException {
        public ShiftArchivedException(Long id) {
            super("Shift is archived: " + id);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class JobNotFoundException extends RuntimeException {
        public JobNotFoundException(Long shiftId) {
            super("Job not found for shift: " + shiftId);
        }
    }

    @ResponseStatus(HttpStatus.GONE)
    public static class JobFinishedException extends RuntimeException {
        public JobFinishedException(Long shiftId) {
            super("Job is finished for shift: " + shiftId);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class JobArchivedException extends RuntimeException {
        public JobArchivedException(Long jobId) {
            super("Job is archived: " + jobId);
        }
    }

    @Transactional
    public JobShiftResponse getJobShiftResponseByShiftId(Long shiftId) {
        Shift shift = shiftRepository.findByIdWithJobAndRelations(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(shiftId));

        if (shift.isArchived()) {
            throw new ShiftArchivedException(shiftId);
        }

        Job job = shift.getJob();
        if (job == null) {
            throw new JobNotFoundException(shiftId);
        }

        boolean finished = shift.getStartDate().toLocalDate().isBefore(LocalDate.now());
        if (finished) {
            throw new JobFinishedException(shiftId);
        }

        if (job.isArchived()) {
            throw new JobArchivedException(job.getId());
        }

        JobShiftResponse resp = new JobShiftResponse();
        resp.setJobId(job.getId());
        resp.setPosition(job.getPosition());
        resp.setLocationId(job.getLocation().getId());
        resp.setShiftLocation(job.getLocation().getLocationName());
        resp.setShiftStartDate(shift.getStartDate());
        resp.setShiftEndDate(shift.getEndDate());
        resp.setShiftTime(String.format("%02d:%02d", shift.getStartDate().getHour(), shift.getStartDate().getMinute()) + 
                         " - " + 
                         String.format("%02d:%02d", shift.getEndDate().getHour(), shift.getEndDate().getMinute()));
        resp.setFreelancerCount(job.getFreelancerCount());
        resp.setFilled(job.getFreelancerCount() == 0);
        resp.setPrice(shift.getPrice());
        resp.setFinished(false);

        resp.setProvidesMeal(job.isProvidesMeal());
        resp.setProvidesTravelAllowance(job.isProvidesTravelAllowance());
        resp.setRequiresID(job.isRequiresID());
        resp.setProvidesTraining(job.isProvidesTraining());
        resp.setRequiredHealthCard(job.isRequiredHealthCard());
        resp.setAdditionalInfo(job.getAdditionalInfo());
        resp.setCompanyId(job.getCreatedBy().getId());
        Long firstManager = (job.getManagers() != null && !job.getManagers().isEmpty()) ? job.getManagers().get(0) : null;
        resp.setManager(firstManager);
        resp.setCreatedByBusinessUser(job.getCreatedByBusinessUser() != null ? job.getCreatedByBusinessUser().getId() : null);

        List<ShiftJobResponse> shiftList = shiftRepository.findByJobId(job.getId()).stream()
                .filter(s -> !s.isArchived())
                .map(s -> new ShiftJobResponse(
                        s.getId(),
                        s.getStartDate(),
                        s.getEndDate(),
                        String.format("%02d:%02d", s.getStartDate().getHour(), s.getStartDate().getMinute()),
                        String.format("%02d:%02d", s.getEndDate().getHour(), s.getEndDate().getMinute())))
                .collect(Collectors.toList());
        resp.setShifts(shiftList);

        return resp;
    }

    @Transactional(readOnly = true)
    public List<Job> getJobsByBusinessUserAndPositionAndLocation(BusinessUser businessUser, String positionName, Long locationId) {
        if (businessUser == null || positionName == null || locationId == null) return Collections.emptyList();
        // Use optimized query to eagerly fetch shifts and avoid LazyInitializationException
        return jobRepository.findByCreatedByBusinessUserOptimized(businessUser).stream()
                .filter(job -> positionName.equals(job.getPosition()) && job.getLocation() != null && locationId.equals(job.getLocation().getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Job> getJobsByCompanyAndPositionAndLocation(Company company, String positionName, Long locationId) {
        if (company == null || positionName == null || locationId == null) return Collections.emptyList();
        // Use optimized query to eagerly fetch shifts and avoid LazyInitializationException
        return jobRepository.findByCreatedByOptimized(company).stream()
                .filter(job -> positionName.equals(job.getPosition()) && job.getLocation() != null && locationId.equals(job.getLocation().getId()))
                .collect(Collectors.toList());
    }

    public ShiftResponse toShiftResponse(Shift shift) {
        if (shift == null || shift.getJob() == null) return null;
        Job job = shift.getJob();
        return new ShiftResponse(
                shift.getId(),
                job.getLocation() != null ? job.getLocation().getLocationName() : null,
                shift.getPrice(),
                shift.getStartDate(),
                shift.getEndDate(),
                job.getFreelancerCount(),
                shift.getStartDate() != null ? String.format("%02d:%02d", shift.getStartDate().getHour(), shift.getStartDate().getMinute()) : null,
                shift.getEndDate() != null ? String.format("%02d:%02d", shift.getEndDate().getHour(), shift.getEndDate().getMinute()) : null,
                job.getPosition(),
                job.getJobImage(),
                job.getEmoji()
        );
    }

    public ShiftTemplateResponse toShiftTemplateResponse(Shift shift) {
        if (shift == null || shift.getJob() == null) return null;
        Job job = shift.getJob();
        return new ShiftTemplateResponse(
                shift.getId(),
                job.getLocation() != null ? job.getLocation().getLocationName() : null,
                shift.getPrice(),
                job.getFreelancerCount(),
                job.getPosition(),
                job.getJobImage(),
                job.getEmoji(),
                (job.getManagers() != null && !job.getManagers().isEmpty()) ? job.getManagers().get(0) : null,
                job.getAdditionalInfo(),
                job.isProvidesTraining(),
                job.isProvidesMeal(),
                job.isRequiresID(),
                job.isProvidesTravelAllowance(),
                job.isProvidesDress(),
                job.isRequiredDress(),
                job.getCreatedBy() != null ? job.getCreatedBy().getId() : null,
                job.getCreatedBy() != null ? job.getCreatedBy().getName() : null
        );
    }
}

