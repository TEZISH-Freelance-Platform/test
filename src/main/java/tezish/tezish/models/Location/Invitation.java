package tezish.tezish.models.Location;

import jakarta.persistence.*;
import lombok.Data;
import tezish.tezish.models.Company.BusinessUser;
import tezish.tezish.models.Company.Company;

import java.time.LocalDateTime;

@Entity
@Data
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Location location;

    @ManyToOne
    private BusinessUser businessUser;

    private LocalDateTime expirationTime;

}
