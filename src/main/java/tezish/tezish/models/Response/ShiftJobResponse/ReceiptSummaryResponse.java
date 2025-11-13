package tezish.tezish.models.Response.ShiftJobResponse;

import lombok.Data;
import tezish.tezish.models.Location.Location;

@Data
public class ReceiptSummaryResponse {
    private Long shiftId;
    private String jobName;
    private String companyName;
    private Location shiftLocation;
    private double fullPaymentAmount;
    private int numberOfAcceptedFreelancers;
    private boolean paid;

    public ReceiptSummaryResponse(Long id, String position, String name, Location location, double fullPaymentAmount, int size, boolean isPaid) {
        this.shiftId=id;
        this.jobName=position;
        this.companyName=name;
        this.shiftLocation=location;
        this.fullPaymentAmount=fullPaymentAmount;
        this.numberOfAcceptedFreelancers=size;
        this.paid=isPaid;
    }
}

