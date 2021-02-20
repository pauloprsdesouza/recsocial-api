package br.com.api.functional.usersaccount;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.models.usersaccount.SaveUserAccountJson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginUserAccountTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldSaveToDatabase() throws Exception {
        SaveUserAccountJson params = new SaveUserAccountJson();
        params.setUsername("paulo.prsdesouza@gmail.com");

        ResultActions response = mvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(params)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("paulo.prsdesouza@gmail.com")));
    }

    @Test
    public void shouldValidateEmail() throws Exception {
        SaveUserAccountJson params = new SaveUserAccountJson();
        params.setUsername("paulo.prsdesouza@gmail.com");


        ResultActions response = mvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(params)));

        response.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldBeValidEmail() throws Exception {
        SaveUserAccountJson params = new SaveUserAccountJson();
        params.setUsername("paulo.prsdesouza");

        mvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(params))).andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Por favor forneça um email válido"));

    }
}
