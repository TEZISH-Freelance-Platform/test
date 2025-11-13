package tezish.tezish.models.CompanyRole;

import java.io.Serializable;
import java.util.Objects;

public class UserCompanyRoleId implements Serializable {
    private final Long user;
    private final Long companyRole;

    public UserCompanyRoleId(Long user, Long companyRole) {
        this.user = user;
        this.companyRole = companyRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCompanyRoleId that)) return false;
        return Objects.equals(user, that.user) &&
                Objects.equals(companyRole, that.companyRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, companyRole);
    }
}

