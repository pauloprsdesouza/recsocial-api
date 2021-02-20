package br.com.api.models.recommendations;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationTypeEnum;

public class SaveRecommendationJson {
    private Set<Long> idsEntities;

    @NotBlank(message = "ReCaptcha é obrigatório")
    private RecommendationTypeEnum recommendationType;

    public SaveRecommendationJson() {
        this.idsEntities = new HashSet<>();
    }

    public Set<Long> getIdsEntities() {
        return idsEntities;
    }

    public void setIdsEntities(Set<Long> idsEntities) {
        this.idsEntities = idsEntities;
    }

    public RecommendationTypeEnum getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = RecommendationTypeEnum.getValue(recommendationType);
    }
}
