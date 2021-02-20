package br.com.api.models.entitiestweet;

import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;

public class EntityTweetJson {
    private long id;
    private String name;

    public EntityTweetJson(EntityTweet entity) {
        this.id = entity.getId().getId();
        this.name = entity.getName();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
