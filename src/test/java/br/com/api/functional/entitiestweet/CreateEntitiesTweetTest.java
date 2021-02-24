package br.com.api.functional.entitiestweet;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.api.infrastructure.database.datamodel.domainstweet.DomainTweet;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetPK;
import br.com.api.infrastructure.database.datamodel.tweets.Tweet;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateEntitiesTweetTest {

    @Test
    public void shouldCompareEquals() {
        EntityTweet entityA = new EntityTweet();
        entityA.setId(new EntityTweetPK(1L, 1L));


        EntityTweet entityB = new EntityTweet();
        entityB.setId(new EntityTweetPK(2L, 1L));

        assertFalse(entityA.equals(entityB));
        assertTrue(entityA.equals(entityA));
        assertFalse(entityA.equals(null));
    }

    @Test
    public void createEntityTweet() {
        Tweet tweet = new Tweet();
        tweet.setId(1L);
        Set<Tweet> tweets = new HashSet<>();
        tweets.add(tweet);

        DomainTweet domain = new DomainTweet();
        domain.setId(1L);
        domain.setName("Domain A");
        domain.setDescription("Description A");

        EntityTweetPK pk = new EntityTweetPK(1L, 1L);

        EntityTweet entity = new EntityTweet();
        entity.setId(pk);
        entity.setName("Entity A");
        entity.setDescription("Description A");
        entity.setTweets(tweets);
        entity.setDomain(domain);


        assertEquals(entity.getId(), pk);
        assertEquals(entity.getName(), "Entity A");
        assertEquals(entity.getDescription(), "Description A");
        assertEquals(entity.getTweets(), tweets);
        assertEquals(entity.getDomain(), domain);
    }
}
