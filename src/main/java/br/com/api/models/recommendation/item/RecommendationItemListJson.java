package br.com.api.models.recommendation.item;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;

public class RecommendationItemListJson {
    private List<RecommendationItemJson> _recommendationItemsJson;

    public RecommendationItemListJson(List<RecommendationItem> items) {
        _recommendationItemsJson = items.stream().map(item -> new RecommendationItemJson(item))
                .collect(Collectors.toList());
    }

    public List<RecommendationItemJson> getRecommendationItemsJson() {
        List<RecommendationItemJson> bla = _recommendationItemsJson.stream()
                .sorted(Comparator.comparing(RecommendationItemJson::getRank).reversed())
                .collect(Collectors.toList());
        return bla;
    }
}
