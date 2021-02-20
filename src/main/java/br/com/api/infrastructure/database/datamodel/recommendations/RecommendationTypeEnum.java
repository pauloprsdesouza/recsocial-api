package br.com.api.infrastructure.database.datamodel.recommendations;

import java.util.HashSet;
import java.util.Set;

public enum RecommendationTypeEnum {
    SocialCapial("SC"), SocialCapitalSentiment("SCSA"), CosineSimilarity("CS"), BaselineA("B1"), BaselineB("B2");

    private String value;

    RecommendationTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static RecommendationTypeEnum getValue(String value) {
        if (value.equals("SC")) {
            return SocialCapial;
        } else if (value.equals("SCSA")) {
            return SocialCapitalSentiment;
        } else if (value.equals("CS")) {
            return CosineSimilarity;
        } else if (value.equals("B1")) {
            return BaselineA;
        } else {
            return BaselineB;
        }
    }

    public static Set<RecommendationTypeEnum> getListOfRecommendationType() {
        Set<RecommendationTypeEnum> recommendationTypesOfString = new HashSet<>();
        recommendationTypesOfString.add(SocialCapial);
        recommendationTypesOfString.add(SocialCapitalSentiment);
        recommendationTypesOfString.add(CosineSimilarity);
        recommendationTypesOfString.add(BaselineA);

        return recommendationTypesOfString;
    }
}
