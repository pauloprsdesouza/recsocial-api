package br.com.api.controllers;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationRepository;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccountRepository;
import br.com.api.infrastructure.services.JwtService;
import br.com.api.models.usersaccount.SaveUserAccountJson;
import br.com.api.models.usersaccount.UserAccountJson;

@RestController
@RequestMapping("/users")
public class UserAccountController {
    private UserAccountRepository _users;
    private RecommendationRepository _recommendations;
    private PasswordEncoder _passwordEncoder;
    private JwtService _jwtService;

    public UserAccountController(UserAccountRepository users,
            RecommendationRepository recommendations, PasswordEncoder passwordEncoder,
            JwtService JwtService) {
        _users = users;
        _recommendations = recommendations;
        _passwordEncoder = passwordEncoder;
        _jwtService = JwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid SaveUserAccountJson json) {
        String password = _passwordEncoder.encode("#zWNn?6KrzedH)}");

        UserAccount user = _users.findByEmail(json.getUsername()).orElse(new UserAccount()
                .email(json.getUsername()).password(password).registrationDate(new Date()));

        String token = _jwtService.generateToken(user);

        user.setToken(token);

        _users.save(user);

        _recommendations.deleteAll(_recommendations.notFinished(user.getId()));

        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/view-instructions")
    public ResponseEntity<?> updateViewInstructions() throws Exception {
        UserAccount user = new UserAccount();

        user.setViewInstructions(true);

        _users.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        UserAccount user = new UserAccount();

        request.getSession().invalidate();

        user.setToken(null);

        _users.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/details")
    public ResponseEntity<UserAccountJson> details() {
        UserAccount user = new UserAccount();

        return ResponseEntity.ok().body(new UserAccountJson(user));
    }
}
