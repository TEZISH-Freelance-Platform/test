package tezish.tezish.repositories.Scheduler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tezish.tezish.models.Scheduler.ScheduledJobExecution;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledJobExecutionRepository extends JpaRepository<ScheduledJobExecution, Long> {
    

    long countByStartTimeAfter(LocalDateTime startTime);
    

    long countByJobNameAndStartTimeAfter(String jobName, LocalDateTime startTime);
    

    long countByJobNameAndStatusAndStartTimeAfter(
            String jobName, String status, LocalDateTime startTime);

    @Query("SELECT AVG(e.durationMs) FROM ScheduledJobExecution e " +
           "WHERE e.jobName = :jobName AND e.status = 'SUCCESS' " +
           "AND e.startTime > :startTime")
    Double getAverageDurationByJobNameAndStartTimeAfter(
            @Param("jobName") String jobName, 
            @Param("startTime") LocalDateTime startTime);


    List<ScheduledJobExecution> findByJobNameAndStatusAndStartTimeBefore(
            String jobName, String status, LocalDateTime startTime);


}

