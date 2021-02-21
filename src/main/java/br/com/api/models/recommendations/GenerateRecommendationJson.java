package br.com.api.models.recommendations;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationTypeEnum;

public class GenerateRecommendationJson {

    @Size(min = 3, max = 3, message = "It's necessary three categories")
    private List<Long> idsEntities;

    @NotBlank(message = "Recommendation Type is Required")
    @Pattern(regexp = "(SC)}|(SCSA)|(CS)|(BA)")
    private String recommendationType;

    public String getRecommendationType() {
        return this.recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public GenerateRecommendationJson() {
        this.idsEntities = new ArrayList<>();
    }

    public List<Long> getIdsEntities() {
        return idsEntities;
    }

    public void setIdsEntities(List<Long> idsEntities) {
        this.idsEntities = idsEntities;
    }

    public boolean isSocialCapital() {
        return RecommendationTypeEnum
                .getValue(recommendationType) == RecommendationTypeEnum.SocialCapial;
    }

    public boolean isSocialCapitalSentimentAnalysis() {
        return RecommendationTypeEnum
                .getValue(recommendationType) == RecommendationTypeEnum.SocialCapitalSentiment;
    }

    public boolean isCosineSimilarity() {
        return RecommendationTypeEnum
                .getValue(recommendationType) == RecommendationTypeEnum.CosineSimilarity;
    }

    public boolean isBaselineA() {
        return RecommendationTypeEnum
                .getValue(recommendationType) == RecommendationTypeEnum.BaselineA;
    }
}
