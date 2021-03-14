package br.com.api.functional.recommendations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationTypeEnum;
import br.com.api.models.recommendation.GenerateRecommendationJson;
import br.com.api.models.recommendation.item.RecommendationItemJson;
import br.com.api.models.recommendation.item.UpdateItemRatingJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RemoveRecommendationsNotFinishedTest {
    @Autowired
    private MockMvc mvc;

    private GenerateRecommendationJson getJsonRequest(RecommendationTypeEnum type) {
        GenerateRecommendationJson json = new GenerateRecommendationJson();
        List<Long> idsEntities = new ArrayList<>();
        idsEntities.add(13L);
        idsEntities.add(14L);
        idsEntities.add(12L);

        json.setIdsEntities(idsEntities);
        json.setRecommendationType(type.getValue());

        return json;
    }

    private void updateRating(String response) throws Exception {
        Type tipoLista = new TypeToken<ArrayList<RecommendationItemJson>>() {
        }.getType();

        List<RecommendationItemJson> recommendedItems = new Gson().fromJson(response, tipoLista);

        Random random = new Random();

        for (RecommendationItemJson item : recommendedItems) {
            UpdateItemRatingJson rating = new UpdateItemRatingJson();
            rating.setIdRecommendation(item.getIdRecommendation());
            rating.setIdTweet(Long.valueOf(item.getIdTweet()));
            rating.setRating(random.nextInt(5 - 1) + 1);

            ResultActions responseRecommendationsUpdateItem = mvc.perform(
                    post("/recommendations/update-rating").contentType(MediaType.APPLICATION_JSON)
                            .content(new Gson().toJson(rating)));

            responseRecommendationsUpdateItem.andExpect(status().isOk());
        }
    }

    @Test
    @WithUserDetails("email@email.com")
    public void shouldRemoveAndNotFinishedRecommendations() throws Exception {
        ResultActions responseRecommendationsByType = mvc.perform(
                post("/recommendations/bytype").contentType(MediaType.APPLICATION_JSON).content(
                        new Gson().toJson(getJsonRequest(RecommendationTypeEnum.SocialCapial))));

        responseRecommendationsByType.andExpect(status().isOk());

        responseRecommendationsByType = mvc.perform(
                post("/recommendations/bytype").contentType(MediaType.APPLICATION_JSON).content(
                        new Gson().toJson(getJsonRequest(RecommendationTypeEnum.SocialCapial))));


        updateRating(responseRecommendationsByType.andReturn().getResponse().getContentAsString());

        responseRecommendationsByType.andExpect(status().isOk());
    }
}
