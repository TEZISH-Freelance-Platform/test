package tezish.tezish.models.Response.FreelancerResponse;

import lombok.Data;
import java.util.List;

@Data
public class EducationAndLanguagesRequest {
    private String education;
    private List<String> languages;
}
