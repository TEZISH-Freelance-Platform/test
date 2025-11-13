package tezish.tezish.models.Company;

import lombok.Data;

import java.util.List;

@Data
public class CompanyRoleRequest {
    private Long companyId;
    private String roleName;
    private List<Long> permissionIds;
}

