package tezish.tezish.models.Job_Shift;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class JobStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
