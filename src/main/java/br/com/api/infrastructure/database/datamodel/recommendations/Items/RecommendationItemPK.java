package br.com.api.infrastructure.database.datamodel.recommendations.Items;

import java.io.Serializable;
import javax.persistence.Column;

public class RecommendationItemPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "id_recommendation")
    private int idRecommendation;

    @Column(name = "id_tweet")
    private long idTweet;

    public RecommendationItemPK() {

    }

    public int getIdRecommendation() {
        return idRecommendation;
    }

    public void setIdRecommendation(int idRecommendation) {
        this.idRecommendation = idRecommendation;
    }

    public long getIdTweet() {
        return idTweet;
    }

    public void setIdTweet(long idTweet) {
        this.idTweet = idTweet;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + idRecommendation;
        result = prime * result + (int) (idTweet ^ (idTweet >>> 32));
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
        RecommendationItemPK other = (RecommendationItemPK) obj;
        if (idRecommendation != other.idRecommendation)
            return false;
        if (idTweet != other.idTweet)
            return false;
        return true;
    }

}
