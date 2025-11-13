package tezish.tezish.models.Response.Guest;

import lombok.Data;

@Data
public class TokenRegistrationRequest {
    private String deviceId;
    private String fcmToken;

}
