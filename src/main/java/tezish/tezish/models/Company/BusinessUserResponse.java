package tezish.tezish.models.Company;

import lombok.Data;

@Data
public class BusinessUserResponse {
    private Long id;
    private Long businessId;
    private String name;
    private String phoneNumber;
    private String email;
    private String companyName;
    private Long companyId;

    public BusinessUserResponse(Long id, Long businessId, String name, String phoneNumber, String email, String companyName, Long companyId) {
        this.id = id;
        this.businessId = businessId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
        this.companyId = companyId;
    }
}
