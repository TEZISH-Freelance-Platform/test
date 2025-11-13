package tezish.tezish.services.BusinessUser;

import org.springframework.stereotype.Service;
import tezish.tezish.models.Company.Company;
import tezish.tezish.repositories.BusinessUser.CompanyRepository;

import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }
}
