package tezish.tezish.repositories.JobShifts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tezish.tezish.models.Job_Shift.JobTitle;

public interface JobTitleRepository extends JpaRepository<JobTitle, Long> {
    JobTitle findByTitle(String title);
    
    @Query("SELECT jt FROM JobTitle jt JOIN FETCH jt.jobCategory WHERE jt.title = :title")
    JobTitle findByTitleWithCategory(@Param("title") String title);
}
