package br.com.api.infrastructure.database.datamodel.referencedtweets;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import br.com.api.infrastructure.database.datamodel.tweets.Tweet;

@Entity
@Table(name = "referenced_tweet")
public class ReferencedTweet implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ReferencedTweetPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tweet", updatable = false, insertable = false)
    private Tweet referencedTweet;

    public ReferencedTweetPK getId() {
        return this.id;
    }

    public void setId(ReferencedTweetPK id) {
        this.id = id;
    }

    public Tweet getReferencedTweet() {
        return this.referencedTweet;
    }

    public void setReferencedTweet(Tweet referencedTweet) {
        this.referencedTweet = referencedTweet;
    }
}
