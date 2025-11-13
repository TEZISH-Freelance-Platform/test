package tezish.tezish.models.Response.AdminResponse;

import lombok.Data;

import java.util.List;

@Data
public class CompanyDetailsResponse {
    private Long id;
    private String name;
    private String description;
    private java.math.BigDecimal dsmf;
    private java.math.BigDecimal companyBankAccounting;
    private List<LocationDetailsResponse> locations;

    public CompanyDetailsResponse(Long id, String name, String description, java.math.BigDecimal dsmf, java.math.BigDecimal companyBankAccounting, List<LocationDetailsResponse> locations) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dsmf = dsmf;
        this.companyBankAccounting = companyBankAccounting;
        this.locations = locations;
    }
}
