package br.com.api.functional.recommendations;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.models.recommendations.GenerateRecommendationJson;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ListRecommendationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @Order(1)
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldListRecommendationsBySocialCapital() throws Exception {
        GenerateRecommendationJson json = new GenerateRecommendationJson();
        List<Long> idsEntities = new ArrayList<>();
        idsEntities.add(1L);
        idsEntities.add(2L);
        idsEntities.add(3L);

        json.setIdsEntities(idsEntities);
        json.setRecommendationType("SC");

        ResultActions response = mvc.perform(post("/recommendations/bytype")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(json)));

        response.andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldListRecommendationsBySocialCapitalSentimentAnalysis() throws Exception {
        GenerateRecommendationJson json = new GenerateRecommendationJson();
        List<Long> idsEntities = new ArrayList<>();
        idsEntities.add(1L);
        idsEntities.add(2L);
        idsEntities.add(3L);

        json.setIdsEntities(idsEntities);
        json.setRecommendationType("SC");

        ResultActions response = mvc.perform(post("/recommendations/bytype")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(json)));

        response.andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldListRecommendationsByCosineSimilarity() throws Exception {
        GenerateRecommendationJson json = new GenerateRecommendationJson();
        List<Long> idsEntities = new ArrayList<>();
        idsEntities.add(1L);
        idsEntities.add(2L);
        idsEntities.add(3L);

        json.setIdsEntities(idsEntities);
        json.setRecommendationType("SC");

        ResultActions response = mvc.perform(post("/recommendations/bytype")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(json)));

        response.andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void shouldListRecommendationsByBaselineA() throws Exception {
        GenerateRecommendationJson json = new GenerateRecommendationJson();
        List<Long> idsEntities = new ArrayList<>();
        idsEntities.add(1L);
        idsEntities.add(2L);
        idsEntities.add(3L);

        json.setIdsEntities(idsEntities);
        json.setRecommendationType("SC");

        ResultActions response = mvc.perform(post("/recommendations/bytype")
                .contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(json)));

        response.andExpect(status().isOk());
    }
}
