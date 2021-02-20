package br.com.api.models.domainstweet;

import br.com.api.infrastructure.database.datamodel.domainstweet.DomainTweet;

public class DomainTweetJson {
    private long id;
    private String name;

    public DomainTweetJson(DomainTweet domain) {
        this.id = domain.getId();
        this.name = domain.getName();
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
