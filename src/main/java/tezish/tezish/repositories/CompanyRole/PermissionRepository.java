package tezish.tezish.repositories.CompanyRole;

import org.springframework.data.jpa.repository.JpaRepository;
import tezish.tezish.models.CompanyRole.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
