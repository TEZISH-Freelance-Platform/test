package tezish.tezish.models.Response.UserResponse;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String email;
    private String password;
    private Long userId;
    private String role;
    private String token;
    private String name;
    private String businessPhoneNumber;
    private Long companyId;


    private Long freelancerId;
    private Long businessUserId;
    private Long adminId;
    private String fcmToken;
    private String deviceId;
}
