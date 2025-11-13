package tezish.tezish.models.Response.BusinessResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationAssignmentRequest {
    private Long userId;
    private Long locationId;
}
