package tezish.tezish.repositories.AdminUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tezish.tezish.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

        @Query("SELECT u FROM User u LEFT JOIN FETCH u.freelancer LEFT JOIN FETCH u.businessUser WHERE u.email = :email")
        Optional<User> findByEmailWithRoles(@Param("email") String email);
        Optional<User> findByResetPasswordToken(String resetPasswordToken);

    boolean existsByEmail(String email);

    List<User> findAllByFailedLoginAttemptsGreaterThan(int threshold);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.freelancer WHERE u.email = :email")
    Optional<User> findByEmailWithFreelancer(@Param("email") String email);
}

