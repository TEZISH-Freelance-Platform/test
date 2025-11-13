package tezish.tezish.models.Response.AdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String email;
    private String name;
    private Long specificRoleId;
    private String role;
    private Long adminId;
    private Long businessUserId;
    private Long freelancerId;

    public UserResponse() {

    }
}

