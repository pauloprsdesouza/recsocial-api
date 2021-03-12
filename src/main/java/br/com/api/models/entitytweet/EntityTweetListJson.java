package br.com.api.models.entitytweet;

import java.util.List;
import java.util.stream.Collectors;

import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;

public class EntityTweetListJson {
    private List<EntityTweetJson> _entitiesTweetJson;

    public EntityTweetListJson(List<EntityTweet> entities) {
        _entitiesTweetJson = entities.stream().map(entity -> new EntityTweetJson(entity))
                .collect(Collectors.toList());
    }

    public List<EntityTweetJson> getEntitiesTweet() {
        return _entitiesTweetJson;
    }
}
