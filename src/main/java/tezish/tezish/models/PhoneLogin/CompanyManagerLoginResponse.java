package tezish.tezish.models.PhoneLogin;

import lombok.Data;

@Data
public class CompanyManagerLoginResponse {
    private Long id;
    private String phoneNumber;
    private String role;
    private String token;

    public CompanyManagerLoginResponse(Long id, String phoneNumber, String role, String token) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.token = token;
    }
}
