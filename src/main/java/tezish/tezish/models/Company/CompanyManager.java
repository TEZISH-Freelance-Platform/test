package tezish.tezish.models.Company;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;

@Entity
@Data
@Table(name = "company_manager")
public class CompanyManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String fullName;

    @Column(nullable = false, unique = true)
    String phoneNumber;

    private String role = "MANAGER";
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    @JsonIgnore
    private Company company;
}
