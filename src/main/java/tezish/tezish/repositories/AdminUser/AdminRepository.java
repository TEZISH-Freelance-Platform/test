package tezish.tezish.repositories.AdminUser;

import org.springframework.data.jpa.repository.JpaRepository;
import tezish.tezish.models.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String currentUserEmail);

}
