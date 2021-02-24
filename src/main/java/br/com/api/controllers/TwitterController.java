package br.com.api.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetPK;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetRepository;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUser;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUserRepository;
import br.com.api.infrastructure.services.TwitterV2Service;

@RestController
@RequestMapping("/twitter")
public class TwitterController {

    @Autowired
    private TwitterV2Service _twitterService;

    @Autowired
    private EntityTweetRepository _entities;

    @Autowired
    private TwitterUserRepository _twitterUsers;

    @GetMapping("/extract-data")
    public ResponseEntity<?> extractData() {
        Map<String, Integer> accounts = new HashMap<>();

        // accounts.put("MiamiHEAT", 13);
        // accounts.put("Bucks", 13);
        accounts.put("chicagobulls", 13);
        // accounts.put("DetroitPistons", 13);
        // accounts.put("LAClippers", 13);
        // accounts.put("nyknicks", 13);
        // accounts.put("Lakers", 13);
        // accounts.put("sixers", 13);
        // accounts.put("HoustonRockets", 13);
        // accounts.put("cavs", 13);

        for (Entry<String, Integer> account : accounts.entrySet()) {
            EntityTweet entity =
                    _entities.findById(new EntityTweetPK((long) account.getValue(), 4L)).get();

            try {
                TwitterUser twitterUser = _twitterUsers.getUserByScreenName(account.getKey())
                        .orElse(_twitterService.getInstance().createUser(account.getKey()));

                _twitterService.getInstance().parseResultJson(twitterUser, entity);
            } catch (Exception ex) {
                return ResponseEntity.unprocessableEntity().body(ex.getMessage());
            }
        }

        return ResponseEntity.ok().build();
    }
}
