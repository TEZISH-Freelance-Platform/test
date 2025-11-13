package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

@Data
public class ShiftCountsDTO {
    private int totalPositions;          // Total positions available for this shift
    private int appliedCount;            // Total applications received
    private int approvedByAdmin;         // All approved applications (ACCEPTED_BY_ADMIN, ACCEPTED, FINISHED, NEED_ACCEPT_BY_FREELANCER)
    private int verifiedByFreelancer;    // Verified applications (ACCEPTED, FINISHED)
}
