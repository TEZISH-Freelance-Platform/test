package tezish.tezish.repositories.BusinessUser;


import org.springframework.data.jpa.repository.JpaRepository;
import tezish.tezish.models.Company.Company;
import tezish.tezish.models.Company.CompanyManager;

import java.util.List;
import java.util.Optional;

public interface CompanyManagerRepository extends JpaRepository<CompanyManager, Long> {
    List<CompanyManager> findByCompany(Company company);
    Optional<CompanyManager> findByPhoneNumber(String phoneNumber);
    boolean existsByCompanyAndPhoneNumber(Company company, String phoneNumber);

}
