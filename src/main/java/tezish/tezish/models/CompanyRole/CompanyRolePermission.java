package tezish.tezish.models.CompanyRole;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "company_roles_permissions")
@IdClass(CompanyRolePermissionId.class)
public class CompanyRolePermission {

    @Id
    @ManyToOne
    @JoinColumn(name = "company_role_id", nullable = false)
    private CompanyRole companyRole;

    @Id
    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
