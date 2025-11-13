package tezish.tezish.services.AdminUser;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tezish.tezish.models.Company.CompanyManager;
import tezish.tezish.models.User;
import tezish.tezish.repositories.AdminUser.UserRepository;
import tezish.tezish.repositories.BusinessUser.CompanyManagerRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyManagerRepository companyManagerRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Find the user by email
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//
//        // Check if the user is a BusinessUser
//        BusinessUser businessUser = businessUserRepository.findByEmail(username).orElse(null);
//        Admin admin = adminRepository.findByEmail(username).orElse(null);
//
//        Set<GrantedAuthority> authorities;
//
//        if (businessUser != null) {
//            // Log the roles/permissions for debugging
//            System.out.println("Permissions for BusinessUser: " + businessUser.getEmail() + " are: " + businessUser.getPermissions());
//
//            // Map permissions to GrantedAuthority
//            authorities = businessUser.getPermissions().stream()
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toSet());
//
//        } else if (admin != null) {
//            // Log the roles/permissions for debugging
//            System.out.println("Admin detected for user: " + admin.getEmail());
//
//            // Admin gets a special authority
//            authorities = Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        } else {
//            throw new UsernameNotFoundException("User has no associated BusinessUser or Admin record.");
//        }
//
//        return new UserDetailsImpl(user.getEmail(), user.getPassword(), authorities);
//    }
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        // Fetch the user and roles in one query
//        User user = userRepository.findByEmailWithRoles(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//
//        return UserPrincipal.create(user); // UserPrincipal is a custom implementation that extends UserDetails
//    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrPhone) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmailWithRoles(usernameOrPhone);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<GrantedAuthority> authorities = Optional.ofNullable(user.getCompanyRoles())
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                    .distinct()
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword() != null ? user.getPassword() : "",
                    authorities
            );
        }

        CompanyManager manager = companyManagerRepository
                .findByPhoneNumber(usernameOrPhone)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Neither User nor CompanyManager found with identifier: " + usernameOrPhone
                ));

        String managerRole = manager.getRole();
        List<GrantedAuthority> managerAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + managerRole)
        );

        return new org.springframework.security.core.userdetails.User(
                manager.getPhoneNumber(),
                "",
                managerAuthorities
        );
    }




}
