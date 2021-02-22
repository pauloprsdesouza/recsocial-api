package br.com.api.functional.usersaccount;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class LogoutUserAccountTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldlogout() throws Exception {
        ResultActions response =
                mvc.perform(get("/users/logout").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }
}
