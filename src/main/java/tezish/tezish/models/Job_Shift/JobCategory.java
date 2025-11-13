package tezish.tezish.models.Job_Shift;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "job_category")
@Data
public class JobCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "jobCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JobTitle> jobTitles;

    @Column(name ="archive", nullable = false)
    private boolean archived=false;
}