package br.com.api.authorization;

import org.springframework.security.core.context.SecurityContextHolder;
import br.com.api.authorization.userdetails.UserDetailsCustom;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;

public class HttpContext {
    public static UserAccount getUserLogged() {
        UserDetailsCustom userLogged = (UserDetailsCustom) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return userLogged.getUserAccount();
    }
}
