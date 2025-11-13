package tezish.tezish.models.Company;

import jakarta.persistence.*;
import lombok.Data;
import tezish.tezish.models.Location.Location;

@Data
@Entity
public class BusinessCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;




}
