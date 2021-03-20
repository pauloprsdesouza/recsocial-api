package br.com.api.infrastructure.database.datamodel.recommendations;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public enum RecommendationTypeEnum {
    SocialCapial("SC"), SocialCapitalSentiment("SCSA"), CosineSimilarity("CS"), BaselineA("B1");

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
        } else {
            return BaselineA;
        }
    }

    public static LinkedList<RecommendationTypeEnum> getListOfRecommendationType() {
        LinkedList<RecommendationTypeEnum> recommendationTypesOfString = new LinkedList<>();
        recommendationTypesOfString.add(SocialCapial);
        recommendationTypesOfString.add(SocialCapitalSentiment);
        recommendationTypesOfString.add(CosineSimilarity);
        recommendationTypesOfString.add(BaselineA);

        return recommendationTypesOfString;
    }
}
