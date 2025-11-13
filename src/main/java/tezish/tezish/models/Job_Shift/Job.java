package tezish.tezish.models.Job_Shift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import tezish.tezish.models.Company.BusinessUser;
import tezish.tezish.models.Company.Company;
import tezish.tezish.models.Location.Location;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;
    private BigDecimal price;
    private int freelancerCount;
    private String additionalInfo;
    private String jobImage;
    public boolean providesTraining;
    public boolean providesMeal;
    public boolean requiresID;
    public boolean providesTravelAllowance;
    public boolean providesDress;
    public boolean requiredDress;
    public boolean requiredHealthCard;
    public Long manager;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "job", orphanRemoval = true)
    @JsonManagedReference
    private List<JobManagerAssignment> managerAssignments;

    public List<Long> getManagers() {
        // Check if managerAssignments collection is initialized (loaded)
        // If not initialized, accessing it would cause LazyInitializationException
        try {
            if (org.hibernate.Hibernate.isInitialized(managerAssignments) && 
                managerAssignments != null && !managerAssignments.isEmpty()) {
                return managerAssignments.stream().map(JobManagerAssignment::getManagerId).toList();
            }
        } catch (Exception e) {
            // If any error occurs, fall back to old manager field
        }
        // Fall back to old manager field for backward compatibility
        // This works because 'manager' is a simple field, not a lazy-loaded relationship
        if (manager != null) {
            return java.util.Collections.singletonList(manager);
        }
        return java.util.Collections.emptyList();
    }

    public void setManagers(List<Long> managerIds) {
        if (this.managerAssignments == null) this.managerAssignments = new java.util.ArrayList<>();
        else this.managerAssignments.clear();
        if (managerIds == null) {
            this.manager = null;
            return;
        }
        this.manager = managerIds.isEmpty() ? null : managerIds.get(0);
        for (Long id : managerIds) {
            JobManagerAssignment a = new JobManagerAssignment();
            a.setJob(this);
            a.setManagerId(id);
            this.managerAssignments.add(a);
        }
    }
    public String emoji;
    public boolean createdByAdmin;

    @Setter
    @Column(nullable = false)
    private boolean archived = false;

    @JsonManagedReference
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Company createdBy;

    @JsonManagedReference
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_business_user")
    private BusinessUser createdByBusinessUser;

    @JsonManagedReference
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    @JsonIgnore
    @JsonManagedReference
    private JobStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "job")
    @JsonManagedReference
    private List<Shift> shifts;

}
