package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;
import java.util.List;

@Data
public class InterestedCategoriesRequest {
    private List<String> categories;
}

