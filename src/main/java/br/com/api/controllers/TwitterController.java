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

        accounts.put("Lakers", 13);
        // accounts.put("U2", 1);
        // accounts.put("linkinpark", 1);
        // accounts.put("BonJovi", 1);
        // accounts.put("evanescence", 1);
        // accounts.put("pinkfloyd", 1);
        // accounts.put("scorpions", 1);
        // accounts.put("acdc", 1);
        // accounts.put("PearlJam", 1);
        // accounts.put("gunsnroses", 1);
        // accounts.put("ChiliPeppers", 1);

        // accounts.put("taylorswift13", 2);
        // accounts.put("katyperry", 2);
        // accounts.put("selenagomez", 2);
        // accounts.put("KimKardashian", 2);
        // accounts.put("rihanna", 2);
        // accounts.put("BrunoMars", 2);
        // accounts.put("ladygaga", 2);
        // accounts.put("johnlegend", 2);
        // accounts.put("jtimberlake", 2);
        // accounts.put("ArianaGrande", 2);

        // accounts.put("thelittleidiot", 3);
        // accounts.put("arminvanbuuren", 3);
        // accounts.put("R3HAB", 3);
        // accounts.put("bobsinclar", 3);
        // accounts.put("steveaoki", 3);
        // accounts.put("DonDiablo", 3);
        // accounts.put("davidguetta", 3);
        // accounts.put("afrojack", 3);
        // accounts.put("QUINTINOO", 3);
        // accounts.put("Zedd", 3);

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
