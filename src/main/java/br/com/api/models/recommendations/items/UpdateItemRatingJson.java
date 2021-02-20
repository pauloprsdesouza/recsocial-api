package br.com.api.models.recommendations.items;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateItemRatingJson {
    @NotBlank(message = "Id da recomendação é obrigatório")
    private int _idRecommendation;

    @NotBlank(message = "Id da recomendação é obrigatório")
    private int _idTweet;

    @NotBlank(message = "Rating da avaliação é obrigatório")
    @Size(min = 1, max = 5, message = "Rating deve estar entre 1 e 5")
    private int _rating;


    public int getIdRecommendation() {
        return _idRecommendation;
    }

    public void setIdRecommendation(int idRecommendation) {
        _idRecommendation = idRecommendation;
    }

    public int getIdTweet() {
        return _idTweet;
    }

    public void setIdTweet(int idTweet) {
        _idTweet = idTweet;
    }

    public int getRating() {
        return _rating;
    }

    public void setRating(int rating) {
        _rating = rating;
    }

}
