package br.com.api.infrastructure.database.datamodel.entitiestweet;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.api.infrastructure.database.datamodel.domainstweet.DomainTweet;
import br.com.api.infrastructure.database.datamodel.tweets.Tweet;

@Entity
@Table(name = "entity")
public class EntityTweet implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private EntityTweetPK id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_domain", insertable = false, updatable = false)
    private DomainTweet domain;

    @ManyToMany(mappedBy = "entities", fetch = FetchType.LAZY)
    private Set<Tweet> tweets;

    public EntityTweet() {
    }

    public EntityTweetPK getId() {
        return id;
    }

    public void setId(EntityTweetPK id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DomainTweet getDomain() {
        return null;
    }

    public void setDomain(DomainTweet domain) {
        this.domain = domain;
    }

    public Set<Tweet> getTweets() {
        return this.tweets;
    }

    public void setTweets(Set<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityTweet other = (EntityTweet) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
