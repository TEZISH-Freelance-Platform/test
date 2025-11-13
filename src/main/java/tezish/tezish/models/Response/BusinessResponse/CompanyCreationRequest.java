package tezish.tezish.models.Response.BusinessResponse;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CompanyCreationRequest {
    private String name;
    private String description;
    private BigDecimal DSMF;
    private BigDecimal companyBankAccounting;
}