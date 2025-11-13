package tezish.tezish.models.CompanyRole;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import tezish.tezish.models.User;

@Setter
@Getter
@Entity
@Table(name = "user_company_roles")
@IdClass(UserCompanyRoleId.class)
public class UserCompanyRole {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "company_role_id")
    private CompanyRole companyRole;

}

