package tezish.tezish.repositories.BusinessUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tezish.tezish.models.Company.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.locations WHERE c.id = :id")
    Optional<Company> findByIdWithLocations(@Param("id") Long id);
    
    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.locations")
    List<Company> findAllWithLocations();
}
