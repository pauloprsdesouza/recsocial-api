package br.com.api.functional.recommendations;

import br.com.api.models.recommendations.GenerateRecommendationJson;
import br.com.api.models.recommendations.items.RecommendationItemJson;
import br.com.api.models.recommendations.items.UpdateItemRatingJson;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateRecommendationItemTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void testBla() throws Exception {
        UpdateItemRatingJson rating = new UpdateItemRatingJson();
        rating.setIdRecommendation(1);
        rating.setIdTweet(9999999);
        rating.setRating(5);

        ResultActions responseRecommendationsUpdateItem = mvc.perform(
                post("/recommendations/update-rating").contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(rating)));

        responseRecommendationsUpdateItem.andExpect(status().isUnprocessableEntity());
        responseRecommendationsUpdateItem.andExpect(
                content().string("Não foi possível encontrar a recomendação solicitada"));
    }

}
