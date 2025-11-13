package tezish.tezish.models.Response.Invoice;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceDTO {
    private Long jobId;
    private String jobTitle;
    private String location;
    private String companyName;
    private boolean shiftFinished;
    private boolean paymentStatus;
    private int peopleNeeded;
    private int peopleAccepted;
    private LocalDateTime shiftStartDate;
    private List<FreelancerShiftInfoDTO> freelancerDetails;
}
