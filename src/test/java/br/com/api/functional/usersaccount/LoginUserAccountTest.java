package br.com.api.functional.usersaccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccountRepository;
import br.com.api.models.useraccount.SaveUserAccountJson;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginUserAccountTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserAccountRepository _users;

    @Test
    public void shouldRealizeLogin() throws Exception {
        SaveUserAccountJson json = new SaveUserAccountJson();
        json.setUsername("paulo.prsdesouza@gmail.com");

        ResultActions response = mvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(json)));

        response.andExpect(status().isOk());

        UserAccount user = _users.findByEmail("paulo.prsdesouza@gmail.com").get();

        assertEquals(user.getToken(), response.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void shouldBeValiddateEmail() throws Exception {
        SaveUserAccountJson params = new SaveUserAccountJson();
        params.setUsername("paulo.prsdesouza");

        mvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(params))).andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("[\"Por favor forneça um email válido\"]"));

    }
}
