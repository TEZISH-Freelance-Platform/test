package tezish.tezish.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tezish.tezish.models.Company.BusinessUser;
import tezish.tezish.models.CompanyRole.CompanyRole;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private LocalDateTime createdTime;

    private LocalDateTime lastLoginTime;

    @Column(nullable = false)
    private String password;
    private Integer failedLoginAttempts = 0;
    private LocalDateTime lastFailedAttempt;




    @JsonManagedReference
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "business_user_id")
    private BusinessUser businessUser;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id")
    private Admin admin;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonManagedReference
    private Role role;

    @Column()
    private String resetPasswordToken;

    @Column()
    private LocalDateTime resetPasswordTokenExpiry;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_company_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_role_id")
    )
    private List<CompanyRole> companyRoles;

    private boolean deleted = false;
    private LocalDateTime deletedAt;
}
