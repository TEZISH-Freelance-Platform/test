package tezish.tezish.models.Response.UserResponse;

import lombok.Data;

@Data
public class UserLoginResponseDTO {
    private Long userId;
    private String role;
    private String name;
    private Long freelancerId;
    private Long businessUserId;
    private Long adminId;
    private String token;
}
