package tezish.tezish.repositories.JobShifts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import tezish.tezish.models.Job_Shift.Shift;
import jakarta.persistence.QueryHint;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Collection<Shift> findByJobId(Long id);


    List<Shift> findByStartDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Shift s WHERE s.isPriority = true AND s.priorityExpiresAt <= :now")
    List<Shift> findExpiredPriorityShifts(@Param("now") LocalDateTime now);

    @Query(nativeQuery = true, value = 
        "SELECT s.id as shift_id, j.id as job_id, j.position, l.location_name, " +
        "c.name as company_name, j.start_time, j.end_time, s.start_date, s.end_date, " +
        "j.freelancer_count as position_count, s.price, " +
        "SUM(CASE WHEN sa.status IN ('ACCEPTED_BY_ADMIN', 'ACCEPTED', 'FINISHED', 'NEED_ACCEPT_BY_FREELANCER') THEN 1 ELSE 0 END) as approved_by_admin, " +
        "SUM(CASE WHEN sa.status IN ('ACCEPTED', 'FINISHED') THEN 1 ELSE 0 END) as verified_by_admin, " +
        "COUNT(sa.id) as applied_count, s.archived as archived " +
        "FROM shift s " +
        "JOIN job j ON s.job_id = j.id " +
        "JOIN location l ON j.location_id = l.id " +
        "JOIN company c ON j.created_by = c.id " +
        "LEFT JOIN shift_application sa ON s.id = sa.shift_id " +
        "GROUP BY s.id, j.id, j.position, l.location_name, c.name, j.start_time, j.end_time, " +
        "s.start_date, s.end_date, j.freelancer_count, s.price, s.archived " +
        "ORDER BY s.is_priority DESC, s.start_date ASC")
    List<Object[]> findAllShiftOperations();

    @Query("SELECT s FROM Shift s JOIN FETCH s.job j LEFT JOIN FETCH j.createdBy WHERE s.archived = false AND s.visibleWindowNotified = false AND s.startDate BETWEEN :start AND :end")
    List<Shift> findNewlyVisibleShifts(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    @QueryHints({
        @QueryHint(name = "jakarta.persistence.cache.retrieveMode", value = "BYPASS"),
        @QueryHint(name = "jakarta.persistence.cache.storeMode", value = "BYPASS"),
        @QueryHint(name = "org.hibernate.cacheable", value = "false")
    })
    @Query("SELECT s FROM Shift s JOIN FETCH s.job j LEFT JOIN FETCH j.createdBy WHERE s.archived = false AND s.visibleWindowNotified = false AND s.visibleWindowNotificationScheduled = false AND s.startDate BETWEEN :start AND :end")
    List<Shift> findNewlyVisibleShiftsWithoutSchedule(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end);

    @Query("SELECT s FROM Shift s WHERE s.archived = false AND s.visibleWindowNotified = false AND s.visibleWindowNotificationScheduled = true AND s.visibleWindowNotifyAt IS NOT NULL AND s.visibleWindowNotifyAt <= :now")
    List<Shift> findDueVisibleWindowNotifications(@Param("now") LocalDateTime now);

    @QueryHints({
        @QueryHint(name = "jakarta.persistence.cache.retrieveMode", value = "BYPASS"),
        @QueryHint(name = "jakarta.persistence.cache.storeMode", value = "BYPASS"),
        @QueryHint(name = "org.hibernate.cacheable", value = "false")
    })
    @Query("SELECT s FROM Shift s JOIN FETCH s.job j WHERE s.archived = false AND s.visibleWindowNotified = false AND s.visibleWindowNotificationScheduled = true AND s.visibleWindowNotifyAt IS NOT NULL AND s.visibleWindowNotifyAt <= :now")
    List<Shift> findDueVisibleWindowNotificationsWithJob(@Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Shift s SET s.visibleWindowNotified = true WHERE s.id IN :ids")
    int markVisibleWindowNotified(@Param("ids") List<Long> ids);

    /**
     * Fetch shift with job, location, and company eagerly loaded
     * Used to avoid LazyInitializationException in shift-details endpoints
     */
    @Query("SELECT s FROM Shift s " +
           "JOIN FETCH s.job j " +
           "LEFT JOIN FETCH j.location l " +
           "LEFT JOIN FETCH j.createdBy c " +
           "WHERE s.id = :shiftId")
    java.util.Optional<Shift> findByIdWithJobAndRelations(@Param("shiftId") Long shiftId);

}
