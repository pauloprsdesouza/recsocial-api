package br.com.api.models.entitytweet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;

public class EntityTweetListJson {
    private Set<EntityTweetJson> _entitiesTweetJson;

    public EntityTweetListJson(List<EntityTweet> entities) {
        _entitiesTweetJson = entities.stream().map(entity -> new EntityTweetJson(entity)).collect(Collectors.toSet());
    }

    public Set<EntityTweetJson> getEntitiesTweet() {
        return _entitiesTweetJson;
    }
}
