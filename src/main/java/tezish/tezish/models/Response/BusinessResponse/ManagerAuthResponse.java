package tezish.tezish.models.Response.BusinessResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManagerAuthResponse {
    private Long managerId;
    private String phoneNumber;
    private String role;
    private String token;
}

