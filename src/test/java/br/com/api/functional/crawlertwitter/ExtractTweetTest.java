package br.com.api.functional.crawlertwitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetPK;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetRepository;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUser;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUserRepository;
import br.com.api.infrastructure.services.TwitterV2Service;

@SpringBootTest
@AutoConfigureMockMvc
public class ExtractTweetTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TwitterV2Service _twitterService;

    @Autowired
    private TwitterUserRepository _twitterUsers;

    @Autowired
    private EntityTweetRepository _entities;

    private JsonObject _jsonTweetQuotes;
    private JsonObject _jsonTweetTimeline;
    private JsonObject _jsonTweetReplies;
    private JsonObject _jsonTweetRetweets;

    @BeforeEach
    public void createJson() {
        try {
            String inputStreamQuotes = new String(
                    Files.readAllBytes(Paths.get("src/test/resources/tweetquotes.json")));
            _jsonTweetQuotes = new Gson().fromJson(inputStreamQuotes, JsonObject.class);

            String inputStreamTimeline = new String(
                    Files.readAllBytes(Paths.get("src/test/resources/tweettimeline.json")));
            _jsonTweetTimeline = new Gson().fromJson(inputStreamTimeline, JsonObject.class);

            String inputStreamReplies = new String(
                    Files.readAllBytes(Paths.get("src/test/resources/tweetreplies.json")));
            _jsonTweetReplies = new Gson().fromJson(inputStreamReplies, JsonObject.class);

            String inputStreamRetweets = new String(
                    Files.readAllBytes(Paths.get("src/test/resources/tweetretweets.json")));
            _jsonTweetRetweets = new Gson().fromJson(inputStreamRetweets, JsonObject.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @Order(1)
    @WithUserDetails("paulo.prsdesouza@gmail.com")
    public void extractTweets() throws Exception {
        ResultActions response =
                mvc.perform(get("/twitter/extract-data").contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }

    @Test
    @Order(2)
    public void shouldReturnUserNotFound() {
        String userName = "123545684";

        try {
            _twitterUsers.getUserByScreenName(userName)
                    .orElse(_twitterService.getInstance().createUser(userName));
        } catch (Exception ex) {
            assertEquals("User not found", ex.getMessage());
        }
    }

    @Test
    @Order(3)
    public void shouldParseJsonTimeline() {
        String userName = "MiamiHEAT";

        try {
            TwitterUser twitterUser = _twitterUsers.getUserByScreenName(userName)
                    .orElse(_twitterService.getInstance().createUser(userName));

            EntityTweet entity = _entities.findById(new EntityTweetPK(13L, 4L)).get();

            _twitterService.getInstance().withSearchResult(_jsonTweetTimeline)
                    .parseResultJson(twitterUser, entity);

        } catch (Exception ex) {
            assertEquals("User not found", ex.getMessage());
        }
    }

    @Test
    @Order(4)
    public void shouldParseJsonReplies() {
        String userName = "MiamiHEAT";

        try {
            TwitterUser twitterUser = _twitterUsers.getUserByScreenName(userName)
                    .orElse(_twitterService.getInstance().createUser(userName));

            EntityTweet entity = _entities.findById(new EntityTweetPK(13L, 4L)).get();

            _twitterService.getInstance().withSearchResult(_jsonTweetReplies)
                    .parseResultJson(twitterUser, entity);

        } catch (Exception ex) {
            ex.printStackTrace();
            assertEquals("User not found", ex.getMessage());
        }
    }

    @Test
    @Order(5)
    public void shouldParseJsonRetweets() {
        String userName = "MiamiHEAT";

        try {
            TwitterUser twitterUser = _twitterUsers.getUserByScreenName(userName)
                    .orElse(_twitterService.getInstance().createUser(userName));

            EntityTweet entity = _entities.findById(new EntityTweetPK(13L, 4L)).get();

            _twitterService.getInstance().withSearchResult(_jsonTweetRetweets)
                    .parseResultJson(twitterUser, entity);

        } catch (Exception ex) {
            assertEquals("User not found", ex.getMessage());
        }
    }

    @Test
    @Order(6)
    public void shouldParseJsonQuotes() {
        String userName = "MiamiHEAT";

        try {
            TwitterUser twitterUser = _twitterUsers.getUserByScreenName(userName)
                    .orElse(_twitterService.getInstance().createUser(userName));

            EntityTweet entity = _entities.findById(new EntityTweetPK(13L, 4L)).get();

            _twitterService.getInstance().withSearchResult(_jsonTweetQuotes)
                    .parseResultJson(twitterUser, entity);

        } catch (Exception ex) {
            assertEquals("User not found", ex.getMessage());
        }
    }
}
