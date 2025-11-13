package tezish.tezish.repositories.Scheduler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tezish.tezish.models.Scheduler.ScheduledJobLock;

import java.time.LocalDateTime;

@Repository
public interface ScheduledJobLockRepository extends JpaRepository<ScheduledJobLock, Long> {

    @Modifying
    @Query("DELETE FROM ScheduledJobLock l WHERE l.jobName = :jobName")
    int deleteByJobName(@Param("jobName") String jobName);
    

    @Modifying
    @Query("DELETE FROM ScheduledJobLock l WHERE l.lockedAt < :threshold")
    int deleteByLockedAtBefore(@Param("threshold") LocalDateTime threshold);
}

