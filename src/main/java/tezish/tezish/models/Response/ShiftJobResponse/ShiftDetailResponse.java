package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;
import tezish.tezish.models.Job_Shift.OtherShift;

import java.util.List;

@Data
public class ShiftDetailResponse {
    private String jobTitle;
    private String companyName;
    private String companyProfilePic;
    private String location;
    private String locationDescription;
    private String locationCoordinates;
    private String companyInfo;
    private String socialNetworks;
    private ShiftDetails shiftDetails;
    private List<OtherShift> otherShifts;
    private Long manager;

}
