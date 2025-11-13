package tezish.tezish.controllers;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tezish.tezish.ErrorHandling.AlreadyExist.ManagerAlreadyExistsException;
import tezish.tezish.ErrorHandling.exceptions.ResourceNotFoundException;
import tezish.tezish.models.Company.BusinessUser;
import tezish.tezish.models.Company.Company;
import tezish.tezish.models.Company.CompanyManager;
import tezish.tezish.models.Job_Shift.JobStatus;
import tezish.tezish.models.Job_Shift.Shift;
import tezish.tezish.models.Response.BusinessResponse.*;
import tezish.tezish.models.Response.FreelancerResponse.FreelancerApplicationDTO;
import tezish.tezish.repositories.BusinessUser.BusinessUserRepository;
import tezish.tezish.repositories.BusinessUser.CompanyManagerRepository;
import tezish.tezish.repositories.BusinessUser.CompanyRepository;
import tezish.tezish.repositories.JobShifts.ShiftRepository;
import tezish.tezish.services.BusinessUser.BusinessUserService;
import tezish.tezish.services.JobShift.JobStatusService;
import tezish.tezish.services.JobShift.PostService;
import tezish.tezish.models.Response.ShiftJobResponse.PostResponse;
import tezish.tezish.models.Location.Location;
import tezish.tezish.models.Company.LocationRequest;
import tezish.tezish.services.BusinessUser.LocationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/business-user")
public class BusinessUserController {

    private static final Logger log = LoggerFactory.getLogger(BusinessUserController.class);
    private final BusinessUserService businessUserService;
    private final JobStatusService jobStatusService;
    private final CompanyManagerRepository companyManagerRepository;
    private final BusinessUserRepository businessUserRepository;
    private final CompanyRepository companyRepository;
    private final ShiftRepository shiftRepository;
    private final LocationService locationService;
    private final PostService postService;

    @GetMapping("/company-locations")
    public ResponseEntity<?> getLocationsByBusinessUserId(@RequestParam Long userId) throws Exception {
        return businessUserService.getLocationsByBusinessUserId(userId);
    }

    @GetMapping("/posts")
    public ResponseEntity<java.util.List<PostResponse>> getBusinessPosts(
            @RequestParam(required = false) Integer week,
            @RequestParam(required = false) Integer year
    ) {
        int w = (week != null) ? Math.max(1, Math.min(53, week)) : java.time.LocalDate.now().get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
        int y = (year != null) ? year : java.time.LocalDate.now().get(java.time.temporal.WeekFields.ISO.weekBasedYear());
        java.time.LocalDate weekStart = java.time.LocalDate.now()
                .with(java.time.temporal.WeekFields.ISO.weekBasedYear(), y)
                .with(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear(), w)
                .with(java.time.temporal.WeekFields.ISO.dayOfWeek(), 1);
        java.time.LocalDate weekEnd = weekStart.plusDays(6);
        java.util.List<PostResponse> result = postService.getBusinessPostsForDateRange(weekStart, weekEnd);
        return ResponseEntity.ok(result);
    }


    public class StatusController {
        @GetMapping("/statuses")
        public ResponseEntity<List<JobStatus>> getStatuses() {
            List<JobStatus> statuses = jobStatusService.findAll();
            return ResponseEntity.ok(statuses);
        }
    }



    @PostMapping("/register-manager")
    public ResponseEntity<CompanyManagerResponse> registerCompanyManager(
            @RequestBody CompanyManagerRegistrationRequest request,
            @RequestParam Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "No company associated with this business user"
                ));

        if (companyManagerRepository.existsByCompanyAndPhoneNumber(company, request.getPhoneNumber())) {
            throw new ManagerAlreadyExistsException("Bu nömrə ilə artıq menecer mövcuddur");
        }

        CompanyManager manager = new CompanyManager();
        manager.setFullName(request.getFullName());
        manager.setPhoneNumber(request.getPhoneNumber());
        manager.setCompany(company);
        manager.setRole("MANAGER");
        manager.setToken(null);

        CompanyManager saved = companyManagerRepository.save(manager);
        
        CompanyManagerResponse response = new CompanyManagerResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getPhoneNumber()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/company-managers")
    public ResponseEntity<List<CompanyManagerResponse>> getCompanyManagers(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long businessUserId) {

        Company company;

        if (businessUserId != null) {
            BusinessUser businessUser = businessUserRepository.findById(businessUserId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business user not found"));
            company = businessUser.getCompany();
            if (company == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found for the given business user");
            }
        } else if (companyId != null) {
            company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either companyId or businessUserId must be provided");
        }

        List<CompanyManager> managers = companyManagerRepository.findByCompany(company);
        List<CompanyManagerResponse> responses = managers.stream()
                .map(manager -> new CompanyManagerResponse(
                        manager.getId(),
                        manager.getFullName(),
                        manager.getPhoneNumber()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get-price-info")
    public ResponseEntity<CompanyInfoResponse> getPriceInfo(@RequestParam Long businessUserId) {
        BusinessUser businessUser = businessUserRepository.findById(businessUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business user not found"));

        Company company = businessUser.getCompany();
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found for this business user");
        }

        CompanyInfoResponse companyPriceInfoResponse = new CompanyInfoResponse();
        companyPriceInfoResponse.setDSMF(company.getDSMF());
        companyPriceInfoResponse.setCompanyBankAccounting(company.getCompanyBankAccounting());

        return ResponseEntity.ok(companyPriceInfoResponse);
    }

    @Transactional
    @PostMapping("/archive-shift")
    public ResponseEntity<String> archiveShift(@RequestParam Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift not found"));

        shift.setArchived(true);

        String businessUserName = "Unknown BusinessUser";
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
                String email = userDetails.getUsername();
                BusinessUser businessUser = businessUserRepository.findAll().stream()
                    .filter(bu -> bu.getEmail().equals(email)).findFirst().orElse(null);
                if (businessUser != null && businessUser.getName() != null) {
                    businessUserName = businessUser.getName();
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        String slackMsg = String.format(
            ":warning: *Shift Archived by Business User!*\n*User:* %s\n*Shift ID:* %d\n*Job:* %s\n*Company:* %s\n*Time:* %s",
            businessUserName, shift.getId(), shift.getJob().getPosition(), shift.getJob().getCreatedBy().getName(),
            shift.getStartDate() != null ? shift.getStartDate().toString() : "N/A"
        );


        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Shift archived successfully");
    }

    @PutMapping("/edit-manager/{managerId}")
    @Transactional
    public ResponseEntity<CompanyManagerResponse> editCompanyManager(
            @PathVariable Long managerId,
            @RequestBody CompanyManagerRegistrationRequest request) {

        CompanyManager manager = companyManagerRepository.findById(managerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));

        // Update full name if provided f
        if (request.getFullName() != null) {
            manager.setFullName(request.getFullName());
        }

        // Update phone number if changed; enforce global uniqueness
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(manager.getPhoneNumber())) {
            companyManagerRepository.findByPhoneNumber(request.getPhoneNumber())
                    .filter(existing -> !existing.getId().equals(manager.getId()))
                    .ifPresent(existing -> { throw new ManagerAlreadyExistsException("Bu nömrə ilə artıq menecer mövcuddur"); });
            manager.setPhoneNumber(request.getPhoneNumber());
        }

        CompanyManager saved = companyManagerRepository.save(manager);
        
        CompanyManagerResponse response = new CompanyManagerResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getPhoneNumber()
        );
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit-location/{locationId}")
    public ResponseEntity<?> editLocation(@RequestParam Long userId, @PathVariable Long locationId, @RequestBody LocationRequest request) {
        BusinessUser user = businessUserRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Business user not found");
        }
        Location location = locationService.findById(locationId).orElse(null);
        if (location == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location not found");
        }
        if (location.getCompany() == null || !location.getCompany().getId().equals(user.getCompany().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to edit this location");
        }
        location.setLocationName(request.getLocationName());
        location.setCoordinates(request.getCoordinates());
        location.setDescription(request.getDescription());
        locationService.save(location);
        return ResponseEntity.ok("Location updated successfully");
    }
}




