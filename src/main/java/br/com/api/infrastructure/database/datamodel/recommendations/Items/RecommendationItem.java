package br.com.api.infrastructure.database.datamodel.recommendations.Items;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.api.infrastructure.database.datamodel.recommendations.Recommendation;
import br.com.api.infrastructure.database.datamodel.tweets.Tweet;

@Entity
@Table(name = "recommendation_item")
public class RecommendationItem {

    @EmbeddedId
    private RecommendationItemPK id;

    @Column(name = "ranking")
    private int rank;

    @Column
    private Integer rating;

    @Column(name = "registration_rating")
    private Date registrationRating;

    @Column
    private double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recommendation", insertable = false, updatable = false)
    private Recommendation recommendation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tweet", insertable = false, updatable = false)
    private Tweet tweet;

    public RecommendationItemPK getId() {
        return id;
    }

    public void setId(RecommendationItemPK id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Date getRegistrationRating() {
        return registrationRating;
    }

    public void setRegistrationRating(Date registrationRating) {
        this.registrationRating = registrationRating;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Recommendation getRecommendation() {
        return this.recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public Tweet getTweet() {
        return this.tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
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
        RecommendationItem other = (RecommendationItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}