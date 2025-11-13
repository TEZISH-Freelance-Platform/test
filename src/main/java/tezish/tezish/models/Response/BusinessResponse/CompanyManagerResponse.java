package tezish.tezish.models.Response.BusinessResponse;

import lombok.Data;

@Data
public class CompanyManagerResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;

    public CompanyManagerResponse(Long id, String fullName, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}
