package tezish.tezish.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tezish.tezish.ErrorHandling.exceptions.ShiftAuthorizationException;
import tezish.tezish.models.Company.BusinessUser;
import tezish.tezish.models.Company.Company;
import tezish.tezish.models.Company.CompanyManager;
import tezish.tezish.models.Job_Shift.Job;
import tezish.tezish.models.Job_Shift.Shift;
import tezish.tezish.models.User;
import tezish.tezish.repositories.BusinessUser.BusinessUserRepository;
import tezish.tezish.repositories.BusinessUser.CompanyManagerRepository;
import tezish.tezish.repositories.JobShifts.ShiftRepository;

@Service
public class ShiftAuthorizationService {
    private final ShiftRepository shiftRepository;
    private final BusinessUserRepository businessUserRepository;
    private final CompanyManagerRepository companyManagerRepository;

    public ShiftAuthorizationService(
            ShiftRepository shiftRepository,
            BusinessUserRepository businessUserRepository,
            CompanyManagerRepository companyManagerRepository) {
        this.shiftRepository = shiftRepository;
        this.businessUserRepository = businessUserRepository;
        this.companyManagerRepository = companyManagerRepository;
    }

    public void authorizeShiftAccess(Long shiftId, User user) {
        Shift shift = shiftRepository.findByIdWithJobAndRelations(shiftId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shift not found"));

        if (user.getAdmin() != null) {
            return;
        }

        Job job = shift.getJob();
        Company company = job.getCreatedBy();

        if (user.getBusinessUser() != null) {
            BusinessUser businessUser = businessUserRepository.findById(user.getBusinessUser().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Business user not found"));
            
            if (!company.getId().equals(businessUser.getCompany().getId())) {
                throw new ShiftAuthorizationException(
                    "This shift does not belong to your company!", 
                    ShiftAuthorizationException.SHIFT_NOT_BELONG_TO_COMPANY
                );
            }
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            CompanyManager manager = companyManagerRepository.findByPhoneNumber(username)
                    .orElse(null);

            if (manager != null) {
                if (!manager.getCompany().getId().equals(company.getId())) {
                    throw new ShiftAuthorizationException(
                        "This shift does not belong to your company!", 
                        ShiftAuthorizationException.SHIFT_NOT_BELONG_TO_COMPANY
                    );
                }

                if (job.getManagers() != null && !job.getManagers().isEmpty() && !job.getManagers().contains(manager.getId())) {
                    throw new ShiftAuthorizationException(
                        "You're not assigned to this shift!", 
                        ShiftAuthorizationException.MANAGER_NOT_ASSIGNED
                    );
                }
                return;
            }
        }

        throw new ShiftAuthorizationException(
            "You don't have permission to access this shift", 
            ShiftAuthorizationException.NO_PERMISSION
        );
    }
} 