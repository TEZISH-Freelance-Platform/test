package tezish.tezish.models.Location;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import tezish.tezish.models.Company.BusinessUser;
import tezish.tezish.models.Company.Company;
import tezish.tezish.models.Job_Shift.Job;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String locationName;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference
    @JsonIgnore
    private Company company;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Job> jobs;
    private String coordinates;
    private String description;

    


    @ManyToMany
    @JsonIgnore
    @JsonBackReference
    @JoinTable(
            name = "location_business_user",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "business_user_id")
    )
    private Set<BusinessUser> businessUsers = new HashSet<>();

    public void addBusinessUser(BusinessUser businessUser) {
        this.businessUsers.add(businessUser);
    }

}