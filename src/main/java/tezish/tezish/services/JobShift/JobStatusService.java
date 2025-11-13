package tezish.tezish.services.JobShift;

import org.springframework.stereotype.Service;
import tezish.tezish.models.Job_Shift.JobStatus;
import tezish.tezish.repositories.JobShifts.JobStatusRepository;

import java.util.List;

@Service
public class JobStatusService {
    private final JobStatusRepository jobStatusRepository;

    public JobStatusService(JobStatusRepository jobStatusRepository) {
        this.jobStatusRepository = jobStatusRepository;
    }

    public List<JobStatus> findAll() {
        return jobStatusRepository.findAll();
    }
}
