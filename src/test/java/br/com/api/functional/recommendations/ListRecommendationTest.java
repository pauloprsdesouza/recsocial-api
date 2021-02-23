package br.com.api.functional.recommendations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.models.recommendation.GenerateRecommendationJson;
import br.com.api.models.recommendation.item.RecommendationItemJson;
import br.com.api.models.recommendation.item.UpdateItemRatingJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

                ResultActions responseRecommendationsByType =
                                mvc.perform(post("/recommendations/bytype")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new Gson().toJson(json)));

                Type tipoLista = new TypeToken<ArrayList<RecommendationItemJson>>() {
                }.getType();

                List<RecommendationItemJson> recommendedItems =
                                new Gson().fromJson(
                                                responseRecommendationsByType.andReturn()
                                                                .getResponse().getContentAsString(),
                                                tipoLista);

                Random random = new Random();

                for (RecommendationItemJson item : recommendedItems) {
                        UpdateItemRatingJson rating = new UpdateItemRatingJson();
                        rating.setIdRecommendation(item.getIdRecommendation());
                        rating.setIdTweet(item.getIdTweet());
                        rating.setRating(random.nextInt(5 - 1) + 1);

                        ResultActions responseRecommendationsUpdateItem =
                                        mvc.perform(post("/recommendations/update-rating")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(new Gson().toJson(rating)));

                        responseRecommendationsUpdateItem.andExpect(status().isOk());
                }

                responseRecommendationsByType.andExpect(status().isOk());
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
                json.setRecommendationType("SCSA");

                ResultActions responseRecommendationsByType =
                                mvc.perform(post("/recommendations/bytype")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new Gson().toJson(json)));

                Type tipoLista = new TypeToken<ArrayList<RecommendationItemJson>>() {
                }.getType();

                List<RecommendationItemJson> recommendedItems =
                                new Gson().fromJson(
                                                responseRecommendationsByType.andReturn()
                                                                .getResponse().getContentAsString(),
                                                tipoLista);

                Random random = new Random();

                for (RecommendationItemJson item : recommendedItems) {
                        UpdateItemRatingJson rating = new UpdateItemRatingJson();
                        rating.setIdRecommendation(item.getIdRecommendation());
                        rating.setIdTweet(item.getIdTweet());
                        rating.setRating(random.nextInt(5 - 1) + 1);

                        ResultActions responseRecommendationsUpdateItem =
                                        mvc.perform(post("/recommendations/update-rating")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(new Gson().toJson(rating)));

                        responseRecommendationsUpdateItem.andExpect(status().isOk());
                }

                responseRecommendationsByType.andExpect(status().isOk());
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
                json.setRecommendationType("CS");

                ResultActions responseRecommendationsByType =
                                mvc.perform(post("/recommendations/bytype")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new Gson().toJson(json)));

                Type tipoLista = new TypeToken<ArrayList<RecommendationItemJson>>() {
                }.getType();

                List<RecommendationItemJson> recommendedItems =
                                new Gson().fromJson(
                                                responseRecommendationsByType.andReturn()
                                                                .getResponse().getContentAsString(),
                                                tipoLista);

                Random random = new Random();

                for (RecommendationItemJson item : recommendedItems) {
                        UpdateItemRatingJson rating = new UpdateItemRatingJson();
                        rating.setIdRecommendation(item.getIdRecommendation());
                        rating.setIdTweet(item.getIdTweet());
                        rating.setRating(random.nextInt(5 - 1) + 1);

                        ResultActions responseRecommendationsUpdateItem =
                                        mvc.perform(post("/recommendations/update-rating")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(new Gson().toJson(rating)));

                        responseRecommendationsUpdateItem.andExpect(status().isOk());
                }

                responseRecommendationsByType.andExpect(status().isOk());
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
                json.setRecommendationType("BA");

                ResultActions responseRecommendationsByType =
                                mvc.perform(post("/recommendations/bytype")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new Gson().toJson(json)));

                Type tipoLista = new TypeToken<ArrayList<RecommendationItemJson>>() {
                }.getType();

                List<RecommendationItemJson> recommendedItems =
                                new Gson().fromJson(
                                                responseRecommendationsByType.andReturn()
                                                                .getResponse().getContentAsString(),
                                                tipoLista);

                Random random = new Random();

                for (RecommendationItemJson item : recommendedItems) {
                        UpdateItemRatingJson rating = new UpdateItemRatingJson();
                        rating.setIdRecommendation(item.getIdRecommendation());
                        rating.setIdTweet(item.getIdTweet());
                        rating.setRating(random.nextInt(5 - 1) + 1);

                        ResultActions responseRecommendationsUpdateItem =
                                        mvc.perform(post("/recommendations/update-rating")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(new Gson().toJson(rating)));

                        responseRecommendationsUpdateItem.andExpect(status().isOk());
                }

                responseRecommendationsByType.andExpect(status().isOk());
        }

        @Test
        @Order(5)
        @WithUserDetails("paulo.prsdesouza@gmail.com")
        public void shouldToFinishEvaluations() throws Exception {
                ResultActions response = mvc.perform(get("/recommendations/finished-evaluations")
                                .contentType(MediaType.APPLICATION_JSON));

                response.andExpect(status().isOk());
        }
}
