package tezish.tezish.models.Response.RoleResponse;

import lombok.Data;

@Data
public class PasswordResetRequest  {
    String token;
    String newPassword;
    String confirmPassword;
}
