package tezish.tezish.models.Response.Guest;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceNotificationRequest extends NotificationRequest {
    private String deviceId;

}
