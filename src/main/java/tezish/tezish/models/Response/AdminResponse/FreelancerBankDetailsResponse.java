package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

@Data
public class FreelancerBankDetailsResponse {

   private  String bankName;
    private  String branchCode;
    private  String cardNumber;
    private  String currency;
    private  String iban;
}
