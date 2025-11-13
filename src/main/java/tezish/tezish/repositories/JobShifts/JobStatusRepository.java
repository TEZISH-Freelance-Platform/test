package tezish.tezish.repositories.JobShifts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tezish.tezish.models.Job_Shift.JobStatus;

@Repository
public interface JobStatusRepository extends JpaRepository<JobStatus, Long> {
}

