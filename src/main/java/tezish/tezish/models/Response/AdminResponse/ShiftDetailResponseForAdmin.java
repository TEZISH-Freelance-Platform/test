package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

@Data
public class ShiftDetailResponseForAdmin {
    private String companyName;
    private Long companyId;
    private String companyProfilePic;
    private String companyInfo;
    private String socialNetworks;
    private ShiftDetailsForAdmin shiftDetails;
}
