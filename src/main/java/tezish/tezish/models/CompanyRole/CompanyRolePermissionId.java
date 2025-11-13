package tezish.tezish.models.CompanyRole;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class CompanyRolePermissionId implements Serializable {

    private Long companyRole;
    private Long permission;

    public CompanyRolePermissionId(Long companyRole, Long permission) {
        this.companyRole = companyRole;
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyRolePermissionId that)) return false;
        return Objects.equals(companyRole, that.companyRole) &&
                Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyRole, permission);
    }

}
