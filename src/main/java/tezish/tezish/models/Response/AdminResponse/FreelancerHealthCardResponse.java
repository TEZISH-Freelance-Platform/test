package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

@Data
public class FreelancerHealthCardResponse {
    private Long freelancerId;
    private boolean hasHealthCard;
    private Long healthCardId;
    private String imageUrl;
    private String expirationDate; // ISO date string
    private boolean verified;
}



