package tezish.tezish.models.Response.BusinessResponse;

import lombok.Data;

import java.util.List;

@Data
public class BusinessUserEditRequest {
    private List<Long> locationIds; // List of location IDs to assign
    private String newPassword; // New password for the user
    private String name;
    private String phoneNumber;
    private String email;
}


