package br.com.api.authorization.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccountRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserAccountRepository _userAccountRepository;

    @Autowired
    private PasswordEncoder _passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = _userAccountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User or Password invalid"));

        return User.builder().username(userAccount.getEmail())
                .password(_passwordEncoder.encode("123")).roles("ADMIN").build();
    }
}
