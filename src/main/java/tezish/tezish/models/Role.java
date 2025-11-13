package tezish.tezish.models;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;


public enum Role {
    FREELANCER,
    BUSINESS_USER,
    ADMIN;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + this.name());
    }
}
