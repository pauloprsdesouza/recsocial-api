package br.com.api.models.recommendation.item;

import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;

public class RecommendationItemJson {
    private int idRecommendation;
    private long idTweet;

    public RecommendationItemJson(RecommendationItem recommendationItem) {
        this.idRecommendation = recommendationItem.getId().getIdRecommendation();
        this.idTweet = recommendationItem.getId().getIdTweet();
    }

    public int getIdRecommendation() {
        return this.idRecommendation;
    }

    public void setIdRecommendation(int idRecommendation) {
        this.idRecommendation = idRecommendation;
    }

    public long getIdTweet() {
        return this.idTweet;
    }

    public void setIdTweet(long idTweet) {
        this.idTweet = idTweet;
    }
}
