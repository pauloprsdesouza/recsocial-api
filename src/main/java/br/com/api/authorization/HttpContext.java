package br.com.api.authorization;

import org.springframework.security.core.context.SecurityContextHolder;
import br.com.api.authorization.userdetails.UserDetailsCustom;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;

public class HttpContext {
    public static UserAccount getUserLogged() {
        UserDetailsCustom bla = (UserDetailsCustom) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return bla.getUserAccount();
    }
}
