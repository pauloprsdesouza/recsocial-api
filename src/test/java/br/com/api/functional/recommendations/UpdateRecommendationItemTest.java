package br.com.api.functional.recommendations;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.models.recommendation.item.UpdateItemRatingJson;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateRecommendationItemTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithUserDetails("email@email.com")
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
