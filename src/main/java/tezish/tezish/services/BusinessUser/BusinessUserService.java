package tezish.tezish.services.BusinessUser;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tezish.tezish.ErrorHandling.LoginExceptionHandling.UserNotFoundException;
import tezish.tezish.models.Company.*;
import tezish.tezish.models.Response.AdminResponse.LocationDetailsResponse;
import tezish.tezish.models.User;
import tezish.tezish.repositories.AdminUser.UserRepository;
import tezish.tezish.repositories.BusinessUser.BusinessUserRepository;
import tezish.tezish.repositories.BusinessUser.CompanyManagerRepository;
import tezish.tezish.repositories.JobShifts.JobRepository;
import tezish.tezish.services.AdminUser.UserDetailsServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessUserService {

    private static final Logger log = LoggerFactory.getLogger(BusinessUserService.class);
    private final BusinessUserRepository businessUserRepository;

    private final JobRepository jobRepository;
    private final UserRepository userRepository;


    private final CompanyManagerRepository companyManagerRepository;

    private final UserDetailsServiceImpl userDetailsServiceImpl;





    @Transactional(readOnly = true)
    public ResponseEntity<?> getLocationsByBusinessUserId(Long userId) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (user.getBusinessUser() == null) {
            throw new Exception("Business user not found for user with id: " + userId);
        }
        Long businessUserId = user.getBusinessUser().getId();
        BusinessUser businessUser =businessUserRepository.findById(businessUserId).orElse(null);
        assert businessUser != null;
        Company company = businessUser.getCompany();
        if (company == null) {
            throw new RuntimeException("Business user does not belong to any company.");
        }

        // Map locations to DTOs within the transaction to avoid lazy initialization exception
        List<LocationDetailsResponse> locationResponses = company.getLocations().stream()
                .map(location -> new LocationDetailsResponse(
                        location.getId(),
                        location.getLocationName(),
                        location.getCoordinates(),
                        location.getDescription()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(locationResponses);
    }

    @Transactional
    public CompanyManager loginOrRegisterByPhoneNumber(String phoneNumber) {
        Optional<CompanyManager> optionalManager =
                companyManagerRepository.findByPhoneNumber(phoneNumber);

        CompanyManager manager;
        if (optionalManager.isPresent()) {
            manager = optionalManager.get();
        } else {
            manager = new CompanyManager();
            manager.setPhoneNumber(phoneNumber);
            manager.setRole("MANAGER");
            manager = companyManagerRepository.save(manager);
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(manager.getPhoneNumber());
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getAuthorities()
                );


        manager = companyManagerRepository.save(manager);

        return manager;
    }

}
