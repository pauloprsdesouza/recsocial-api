package br.com.api.functional.domainstweet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ListDomainsTweetTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldListAllDomainsTweet() throws Exception {
        ResultActions response =
                mvc.perform(get("/domains/all").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }
}
