package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCategoryResponse {
    private Long id;
    private String name;
    private List<JobTitleResponse> jobTitles;
    private boolean archived;
}

