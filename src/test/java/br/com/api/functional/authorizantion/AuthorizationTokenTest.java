package br.com.api.functional.authorizantion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.models.useraccount.SaveUserAccountJson;
import br.com.api.models.useraccount.UserAccountJson;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTokenTest {

        @Autowired
        private MockMvc mvc;

        @Test
        public void authorize() throws Exception {
                SaveUserAccountJson json = new SaveUserAccountJson();
                json.setUsername("email@email.com");

                ResultActions resultUsers = mvc.perform(
                                post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                                .content(new Gson().toJson(json)));

                resultUsers.andExpect(status().isOk());

                UserAccountJson userAccontJson = new Gson().fromJson(
                                resultUsers.andReturn().getResponse().getContentAsString(),
                                UserAccountJson.class);

                ResultActions resultDomains = mvc.perform(get("/domains/all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + userAccontJson.getToken()));

                resultDomains.andExpect(status().isOk());
        }

        @Test
        public void shouldBeUnauthorizeInvalidHeader() throws Exception {
                SaveUserAccountJson json = new SaveUserAccountJson();
                json.setUsername("email@email.com");

                ResultActions resultUsers = mvc.perform(
                                post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                                .content(new Gson().toJson(json)));

                resultUsers.andExpect(status().isOk());

                String token = resultUsers.andReturn().getResponse().getContentAsString();

                ResultActions resultDomains = mvc
                                .perform(get("/domains/all").contentType(MediaType.APPLICATION_JSON)
                                                .header("Authorization", token));

                resultDomains.andExpect(status().isForbidden());
        }

        @Test
        public void shouldBeUnauthorizeNotValidToken() throws Exception {
                SaveUserAccountJson json = new SaveUserAccountJson();
                json.setUsername("email@email.com");

                ResultActions resultUsers = mvc.perform(
                                post("/users/login").contentType(MediaType.APPLICATION_JSON)
                                                .content(new Gson().toJson(json)));

                resultUsers.andExpect(status().isOk());

                String token = resultUsers.andReturn().getResponse().getContentAsString();

                ResultActions resultDomains = mvc
                                .perform(get("/domains/all").contentType(MediaType.APPLICATION_JSON)
                                                .header("Authorization", "Bearer 123" + token));

                resultDomains.andExpect(status().isForbidden());
        }
}
