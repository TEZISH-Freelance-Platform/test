package tezish.tezish.repositories.JobShifts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tezish.tezish.models.Company.BusinessUser;
import tezish.tezish.models.Company.Company;
import tezish.tezish.models.Job_Shift.Job;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCreatedBy(Company company);

    List<Job> findByCreatedByBusinessUser(BusinessUser businessUser);

    @Query("SELECT j FROM Job j WHERE SIZE(j.shifts) > 0")
    List<Job> findJobsWithShiftApplications();

    /**
     * Optimized query to fetch jobs with shifts and related entities in one query
     * This reduces the number of database calls needed for invoice generation
     */
    @Query("SELECT DISTINCT j FROM Job j " +
           "LEFT JOIN FETCH j.shifts s " +
           "LEFT JOIN FETCH j.location l " +
           "LEFT JOIN FETCH j.createdBy c " +
           "LEFT JOIN FETCH j.createdByBusinessUser bu " +
           "WHERE SIZE(j.shifts) > 0")
    List<Job> findJobsWithShiftApplicationsOptimized();

    /**
     * Optimized query to fetch jobs for a specific company with shifts and related entities
     */
    @Query("SELECT DISTINCT j FROM Job j " +
           "LEFT JOIN FETCH j.shifts s " +
           "LEFT JOIN FETCH j.location l " +
           "LEFT JOIN FETCH j.createdBy c " +
           "LEFT JOIN FETCH j.createdByBusinessUser bu " +
           "WHERE j.createdBy = :company")
    List<Job> findByCreatedByOptimized(@Param("company") Company company);

    /**
     * Optimized query to fetch jobs for a specific business user with shifts and related entities
     */
    @Query("SELECT DISTINCT j FROM Job j " +
           "LEFT JOIN FETCH j.shifts s " +
           "LEFT JOIN FETCH j.location l " +
           "LEFT JOIN FETCH j.createdBy c " +
           "LEFT JOIN FETCH j.createdByBusinessUser bu " +
           "WHERE j.createdByBusinessUser = :businessUser")
    List<Job> findByCreatedByBusinessUserOptimized(@Param("businessUser") BusinessUser businessUser);

    /**
     * Optimized query to find jobs by manager - uses two-step approach to avoid cartesian product
     * Step 1: Find job IDs for the manager
     * Updated to support backward compatibility: checks both new managerAssignments table AND old manager field
     */
    @Query("SELECT DISTINCT j.id FROM Job j " +
           "LEFT JOIN j.managerAssignments ma " +
           "WHERE ma.managerId = :managerId OR j.manager = :managerId")
    List<Long> findJobIdsByManager(@Param("managerId") Long managerId);
    
    /**
     * Step 2: Fetch jobs with all associations using IN clause
     * This avoids cartesian product from multiple JOIN FETCH with multiple managers
     */
    @Query("SELECT DISTINCT j FROM Job j " +
           "LEFT JOIN FETCH j.shifts s " +
           "LEFT JOIN FETCH j.location l " +
           "LEFT JOIN FETCH j.createdBy c " +
           "WHERE j.id IN :jobIds")
    List<Job> findByIdInWithAssociations(@Param("jobIds") List<Long> jobIds);
    
    /**
     * Convenience method for backward compatibility
     */
    default List<Job> findByManager(Long managerId) {
        List<Long> jobIds = findJobIdsByManager(managerId);
        if (jobIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return findByIdInWithAssociations(jobIds);
    }

    @Query(nativeQuery = true, value = 
        "SELECT s.id, s.id, j.position, j.job_image, j.emoji, c.name, " +
        "l.location_name, l.coordinates, " +
        "s.start_date, s.end_date, j.freelancer_count, s.price, " +
        "COUNT(sa.id) as accepted_count, " +
        "s.is_priority, s.priority_expires_at, s.message " +
        "FROM shift s " +
        "JOIN job j ON s.job_id = j.id " +
        "JOIN company c ON j.created_by = c.id " +
        "JOIN location l ON j.location_id = l.id " +
        "LEFT JOIN shift_application sa ON s.id = sa.shift_id AND sa.status IN ('ACCEPTED', 'FINISHED') " +
        "WHERE s.archived = false " +
        "GROUP BY s.id, j.position, j.job_image, j.emoji, c.name, " +
        "l.location_name, l.coordinates, " +
        "s.start_date, s.end_date, j.freelancer_count, s.price, " +
        "s.is_priority, s.priority_expires_at, s.message")
    List<Object[]> findAllPosts();

    /**
     * Highly optimized native query to fetch all invoice data in one query
     * This eliminates the N+1 problem completely by fetching everything in a single database call
     */
    @Query(nativeQuery = true, value = 
        "SELECT " +
        "    s.id as shift_id, " +
        "    j.position as job_title, " +
        "    COALESCE(l.location_name, 'N/A') as location, " +
        "    s.start_date, " +
        "    s.payment_status, " +
        "    c.name as company_name, " +
        "    j.freelancer_count as people_needed, " +
        "    sa.id as application_id, " +
        "    sa.freelancer_id, " +
        "    f.name as freelancer_name, " +
        "    sa.checkin_time, " +
        "    sa.checkout_time, " +
        "    u.deleted as freelancer_deleted " +
        "FROM shift s " +
        "JOIN job j ON s.job_id = j.id " +
        "JOIN company c ON j.created_by = c.id " +
        "LEFT JOIN location l ON j.location_id = l.id " +
        "LEFT JOIN shift_application sa ON s.id = sa.shift_id AND sa.status = 'FINISHED' " +
        "LEFT JOIN freelancer f ON sa.freelancer_id = f.id " +
        "LEFT JOIN users u ON u.freelancer_id = f.id " +
        "WHERE s.archived = false " +
        "ORDER BY s.start_date DESC")
    List<Object[]> findAllInvoiceDataOptimized();

    /**
     * Optimized query for business user invoices
     */
    @Query(nativeQuery = true, value = 
        "SELECT " +
        "    s.id as shift_id, " +
        "    j.position as job_title, " +
        "    COALESCE(l.location_name, 'N/A') as location, " +
        "    s.start_date, " +
        "    s.payment_status, " +
        "    c.name as company_name, " +
        "    j.freelancer_count as people_needed, " +
        "    sa.id as application_id, " +
        "    sa.freelancer_id, " +
        "    f.name as freelancer_name, " +
        "    sa.checkin_time, " +
        "    sa.checkout_time, " +
        "    u.deleted as freelancer_deleted " +
        "FROM shift s " +
        "JOIN job j ON s.job_id = j.id " +
        "JOIN company c ON j.created_by = c.id " +
        "LEFT JOIN location l ON j.location_id = l.id " +
        "LEFT JOIN shift_application sa ON s.id = sa.shift_id AND sa.status = 'FINISHED' " +
        "LEFT JOIN freelancer f ON sa.freelancer_id = f.id " +
        "LEFT JOIN users u ON u.freelancer_id = f.id " +
        "WHERE s.archived = false AND j.created_by = :companyId " +
        "ORDER BY s.start_date DESC")
    List<Object[]> findInvoiceDataByCompanyOptimized(@Param("companyId") Long companyId);

}

