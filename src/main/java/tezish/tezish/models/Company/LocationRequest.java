package tezish.tezish.models.Company;

import lombok.Data;

@Data
public class LocationRequest {
    private Long companyId;
    private String locationName;
    private String coordinates;
    private String description;
}
