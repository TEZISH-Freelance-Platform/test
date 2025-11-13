package tezish.tezish.models.Response.UserResponse;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String deviceId;
}
