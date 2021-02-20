package br.com.api.authorization.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccountRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserAccountRepository _userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = _userAccountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User or Password invalid"));

        return new UserDetailsCustom(userAccount);
    }
}
