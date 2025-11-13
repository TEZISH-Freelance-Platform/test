package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

@Data
public class AdminBusinessUserEditRequest {
    private Long companyId;
    private String name;
    private String phoneNumber;
    private String email;
}


