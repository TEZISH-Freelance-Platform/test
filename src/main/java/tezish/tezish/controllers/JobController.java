package tezish.tezish.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tezish.tezish.models.Company.LocationRequest;
import tezish.tezish.models.Job_Shift.*;
import tezish.tezish.models.Location.Location;
import tezish.tezish.models.Response.ShiftJobResponse.JobRequest;
import tezish.tezish.models.Response.ShiftJobResponse.JobShiftResponse;
import tezish.tezish.models.Response.ShiftJobResponse.JobUpdateResponse;
import tezish.tezish.models.Response.ShiftJobResponse.JobCreateResponse;
import tezish.tezish.models.Response.ShiftJobResponse.ShiftDetailsResponse;
import tezish.tezish.models.Response.ShiftJobResponse.JobCategoryResponse;
import tezish.tezish.models.Response.AdminResponse.LocationDetailsResponse;
import tezish.tezish.models.User;
import tezish.tezish.repositories.JobShifts.JobStatusRepository;
import tezish.tezish.services.JobShift.JobService;
import tezish.tezish.services.ShiftAuthorizationService;

import java.time.LocalDateTime;
import java.util.List;



@RestController
@AllArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173/*")
public class JobController {

    private final JobService jobService;
    private final JobStatusRepository jobStatusRepository;

    private final ShiftAuthorizationService shiftAuthorizationService;


    @PostMapping("/create-job")
    public ResponseEntity<JobCreateResponse> createJob(
            @RequestBody JobRequest jobRequest,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long companyId) {

        String email = tokenExtractor();
        User user = new User();

        Job createdJob;
        if (user.getAdmin() != null) {
            boolean isAdmin=true;
            createdJob = jobService.createJob(jobRequest, userId, companyId,isAdmin);
        } else if (user.getBusinessUser() != null) {
            boolean isAdmin=false;
            createdJob = jobService.createJob(jobRequest, user.getId(), companyId,isAdmin);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        JobCreateResponse response = new JobCreateResponse(
                createdJob.getId(),
                createdJob.getPosition(),
                createdJob.getPrice(),
                createdJob.getFreelancerCount(),
                createdJob.getLocation() != null ? createdJob.getLocation().getId() : null,
                createdJob.getStatus() != null ? createdJob.getStatus().getId() : null,
                createdJob.getShifts() != null ? createdJob.getShifts().stream()
                        .map(Shift::getId).collect(java.util.stream.Collectors.toList()) : java.util.Collections.emptyList()
        );
        return ResponseEntity.ok(response);
    }

    private static String tokenExtractor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) {
            throw new RuntimeException("Invalid principal type: " + principal.getClass().getName());
        }

        return userDetails.getUsername();
    }




    @GetMapping("/shifts/{shiftId}")
    public ResponseEntity<ShiftDetailsResponse> getShiftById(@PathVariable Long shiftId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = new User();

        shiftAuthorizationService.authorizeShiftAccess(shiftId, user);

        Shift shift = jobService.getShiftById(shiftId);
        if (shift == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ShiftDetailsResponse response = new ShiftDetailsResponse(
                shift.getId(),
                shift.getJob() != null ? shift.getJob().getId() : null,
                shift.getJob() != null ? shift.getJob().getPosition() : null,
                shift.getStartDate(),
                shift.getEndDate(),
                shift.getPrice(),
                shift.isArchived()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/business-user/jobs")
    public ResponseEntity<List<ShiftResponse>> getJobsForBusinessUser(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        String email = tokenExtractor();

        List<ShiftResponse> jobs = jobService.getAllUpcomingShiftsForBusinessUser(1L,startTime, endTime);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/manager/jobs")
    public ResponseEntity<List<ShiftResponse>> getJobsForManager(
            @RequestParam Long managerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<ShiftResponse> jobs = jobService.getAllUpcomingShiftsForManager(managerId, startTime, endTime);
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/edit-job")
    public ResponseEntity<JobUpdateResponse> updateJob(@RequestBody JobRequest jobRequest,Long shiftId) {
        try {
            Job job = jobService.updateJob(jobRequest, shiftId, true);
            JobUpdateResponse response = new JobUpdateResponse(
                    job.getId(),
                    job.getPosition(),
                    job.getPrice(),
                    job.getFreelancerCount(),
                    job.getLocation() != null ? job.getLocation().getId() : null,
                    job.getStatus() != null ? job.getStatus().getId() : null
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/create-location")
    public ResponseEntity<LocationDetailsResponse> createLocation(@RequestBody LocationRequest locationRequest) {
        Location location = jobService.createLocation(locationRequest);
        LocationDetailsResponse response = new LocationDetailsResponse(
                location.getId(),
                location.getLocationName(),
                location.getCoordinates(),
                location.getDescription()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job-statuses")
    public ResponseEntity<List<JobStatus>> getAllJobStatuses() {
        List<JobStatus> statuses = jobStatusRepository.findAll();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/job-categories")
    public ResponseEntity<List<JobCategoryResponse>> getJobCategoryWithTitles() {
        List<JobCategoryResponse> jobCategory = jobService.getJobCategoryWithTitles();
        if (jobCategory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobCategory);
    }

    @GetMapping("/shift-details/{shiftId}")
    public ResponseEntity<JobShiftResponse> getJobShiftDetails(@PathVariable Long shiftId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();



        JobShiftResponse response = jobService.getJobShiftResponseByShiftId(shiftId);
        return ResponseEntity.ok(response);
    }


}