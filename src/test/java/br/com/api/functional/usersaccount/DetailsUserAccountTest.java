package br.com.api.functional.usersaccount;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
public class DetailsUserAccountTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldRealizeLogin() throws Exception {
        ResultActions response =
                mvc.perform(get("/users/details").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(content().json(
                "{'email':'paulo.prsdesouza@gmail.com','viewInstructions':true, 'registrationDate':'2021-02-20T19:18:09.000+00:00'}"));
    }
}
