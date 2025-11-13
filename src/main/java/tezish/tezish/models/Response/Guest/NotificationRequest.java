package tezish.tezish.models.Response.Guest;

import lombok.Data;

@Data
public class NotificationRequest {
    private String title;
    private String body;
    private String clickAction;
}