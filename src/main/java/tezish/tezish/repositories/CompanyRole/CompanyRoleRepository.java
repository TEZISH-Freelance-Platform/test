package tezish.tezish.repositories.CompanyRole;

import org.springframework.data.jpa.repository.JpaRepository;
import tezish.tezish.models.CompanyRole.CompanyRole;

import java.util.List;


public interface CompanyRoleRepository extends JpaRepository<CompanyRole, Long> {
    List<CompanyRole> findAllByCompany_Id(Long companyId);
}
