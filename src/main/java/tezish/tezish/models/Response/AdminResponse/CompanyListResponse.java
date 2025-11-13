package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

@Data
public class CompanyListResponse {
    private Long id;
    private String name;
    private String profilePic;

    public CompanyListResponse(Long id, String name, String profilePic) {
        this.id = id;
        this.name = name;
        this.profilePic = profilePic;
    }
}
