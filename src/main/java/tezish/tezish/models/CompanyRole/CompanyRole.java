package tezish.tezish.models.CompanyRole;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import tezish.tezish.models.Company.Company;


import java.util.List;

@Data
@Entity
@Table(name = "company_roles")
public class CompanyRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonManagedReference
    @JsonIgnore
    private Company company;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "company_roles_permissions",
            joinColumns = @JoinColumn(name = "company_role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @JsonManagedReference
    @JsonIgnore
    private List<Permission> permissions;
}
