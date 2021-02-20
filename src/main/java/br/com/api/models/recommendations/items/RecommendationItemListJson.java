package br.com.api.models.recommendations.items;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;

public class RecommendationItemListJson {
    private Set<RecommendationItemJson> _recommendationItemsJson;

    public RecommendationItemListJson(List<RecommendationItem> items) {
        _recommendationItemsJson = items.stream().map(item -> new RecommendationItemJson(item))
                .collect(Collectors.toSet());
    }

    public Set<RecommendationItemJson> getRecommendationItemsJson() {
        return _recommendationItemsJson;
    }
}
