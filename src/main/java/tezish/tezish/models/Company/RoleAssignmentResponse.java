package tezish.tezish.models.Company;

import lombok.Data;

import java.util.List;

@Data
public class RoleAssignmentResponse {
    private Long userId;
    private List<Long> roleIds;
}
