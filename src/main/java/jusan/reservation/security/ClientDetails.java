package jusan.reservation.security;

import jusan.reservation.entity.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ClientDetails extends Client implements UserDetails {

    private String email;
    private String password;

    private static final List<GrantedAuthority> ROLES = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN"));

    public ClientDetails(Client person) {
        email = person.getEmail();
        password = person.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ROLES;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

