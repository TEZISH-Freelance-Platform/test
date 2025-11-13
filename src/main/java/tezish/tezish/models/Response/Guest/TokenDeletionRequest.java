package tezish.tezish.models.Response.Guest;

import lombok.Data;

@Data
public class TokenDeletionRequest {
    private String fcmToken;
}
