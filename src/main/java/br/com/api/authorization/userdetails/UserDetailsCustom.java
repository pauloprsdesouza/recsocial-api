package br.com.api.authorization.userdetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;

public class UserDetailsCustom implements UserDetails {

    /**
    *
    */
    private static final long serialVersionUID = 1L;

    private UserAccount _userAccount;

    public UserDetailsCustom(UserAccount userAccount) {
        _userAccount = userAccount;
    }

    public UserAccount getUserAccount() {
        return _userAccount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return _userAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return _userAccount.getEmail();
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
