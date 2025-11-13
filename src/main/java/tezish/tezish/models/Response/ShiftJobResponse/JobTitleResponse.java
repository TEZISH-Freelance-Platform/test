package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobTitleResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private String emoji;
    private boolean archived;
}

