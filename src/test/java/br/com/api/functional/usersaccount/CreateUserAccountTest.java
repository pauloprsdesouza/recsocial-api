package br.com.api.functional.usersaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccountRepository;
import br.com.api.infrastructure.services.JwtService;

@SpringBootTest
public class CreateUserAccountTest {

    @Autowired
    private JwtService _jwtService;

    @Autowired
    private UserAccountRepository _users;

    @Test
    public void createUserByGetSet() {
        UserAccount user = new UserAccount();
        user.setEmail("email@email.com");
        user.setName("Teste name");
        user.setViewInstructions(false);

        Date registrationDate = new Date();
        user.setRegistrationDate(registrationDate);

        String password = UUID.randomUUID().toString();
        user.setPassword(password);

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
