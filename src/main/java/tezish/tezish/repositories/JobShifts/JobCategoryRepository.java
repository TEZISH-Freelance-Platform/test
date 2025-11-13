package tezish.tezish.repositories.JobShifts;

import org.springframework.data.jpa.repository.JpaRepository;
import tezish.tezish.models.Job_Shift.JobCategory;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
}
