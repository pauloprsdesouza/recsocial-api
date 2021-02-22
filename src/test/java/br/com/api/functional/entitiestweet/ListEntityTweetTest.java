package br.com.api.functional.entitiestweet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
public class ListEntityTweetTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldListAllEntitiesTweetByDomain() throws Exception {
        ResultActions response =
                mvc.perform(get("/entities/bydomain/1").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldRespond404ForInexistentId() throws Exception {
        ResultActions response =
                mvc.perform(get("/entities/bydomain/").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldReturnEmpityArrayForUnregistredId() throws Exception {
        ResultActions response =
                mvc.perform(get("/entities/bydomain/999").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
    }
}
