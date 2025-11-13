package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private Long postId;
    private String companyName;
    private String jobName;
    private String jobImage;
    private Long shiftId;
    private String shiftStartTime;
    private String shiftEndTime;
    private LocalDateTime shiftStartDate;
    private LocalDateTime shiftEndDate;
    private String price;
    private String emoji;
    private int freelancerNeeded;
    private int freelancerAccepted;
    private boolean priority;
    private String message;
}
