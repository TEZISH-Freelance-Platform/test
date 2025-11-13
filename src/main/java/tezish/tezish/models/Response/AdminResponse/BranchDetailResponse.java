package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

@Data
public class BranchDetailResponse {
    private Long id;
    private String locationName;
    private String coordinates;
    private String description;

    public BranchDetailResponse(Long id, String locationName, String coordinates, String description) {
        this.id = id;
        this.locationName = locationName;
        this.coordinates = coordinates;
        this.description = description;
    }
}
