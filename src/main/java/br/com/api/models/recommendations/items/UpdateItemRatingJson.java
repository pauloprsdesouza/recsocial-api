package br.com.api.models.recommendations.items;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateItemRatingJson {
    @NotBlank(message = "Id da recomendação é obrigatório")
    private int idRecommendation;

    @NotBlank(message = "Id da recomendação é obrigatório")
    private long idTweet;

    @NotBlank(message = "Rating da avaliação é obrigatório")
    @Size(min = 1, max = 5, message = "Rating deve estar entre 1 e 5")
    private int rating;


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

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
