package br.com.api.functional.usersaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccountRepository;
import br.com.api.infrastructure.services.JwtService;

@SpringBootTest
public class CreateUserAccountTest {

    @Autowired
    private JwtService _jwtService;

    @Autowired
    private UserAccountRepository _users;

    @Autowired
    private PasswordEncoder _passwordEncoder;

    @Test
    public void createUserByGetSet() {
        UserAccount user = _users.findByEmail("email@email.com")
                .orElse(new UserAccount().email("email@email.com")
                        .password(_passwordEncoder.encode("#zWNn?6KrzedH)}"))
                        .registrationDate(new Date()));

        String token = _jwtService.generateToken(user);
        user.setToken(token);

        UserAccount userSaved = _users.save(user);

        assertNotEquals(userSaved.getId(), 0);
        assertEquals(user.getEmail(), userSaved.getEmail());
        assertEquals(user.getName(), userSaved.getName());
        assertEquals(user.getPassword(), userSaved.getPassword());
        assertEquals(user.getViewInstructions(), userSaved.getViewInstructions());
        assertEquals(user.getToken(), userSaved.getToken());
        assertEquals(user.getRegistrationDate(), userSaved.getRegistrationDate());
    }
}
