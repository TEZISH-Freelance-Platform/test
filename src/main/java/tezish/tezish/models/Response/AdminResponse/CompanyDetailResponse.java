package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;
import java.util.List;

@Data
public class CompanyDetailResponse {
    private Long id;
    private String name;
    private String description;
    private List<LocationDetailsResponse> locations;

    public CompanyDetailResponse(Long id, String name, String description, List<LocationDetailsResponse> locations) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.locations = locations;
    }
}
