package br.com.api.models.domaintweet;

import java.util.List;
import br.com.api.infrastructure.database.datamodel.domainstweet.DomainTweet;
import br.com.api.models.entitytweet.EntityTweetJson;
import br.com.api.models.entitytweet.EntityTweetListJson;

public class DomainTweetJson {
    private long id;
    private String name;
    private List<EntityTweetJson> entities;

    public DomainTweetJson(DomainTweet domain) {
        this.id = domain.getId();
        this.name = domain.getName();
        this.entities = new EntityTweetListJson(domain.getEntities()).getEntitiesTweet();
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

    public List<EntityTweetJson> getEntities() {
        return this.entities;
    }

}
