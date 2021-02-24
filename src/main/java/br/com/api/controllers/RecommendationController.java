package br.com.api.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.authorization.HttpContext;
import br.com.api.infrastructure.database.datamodel.recommendations.Recommendation;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationRepository;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItemRepository;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import br.com.api.infrastructure.services.RecommendationService;
import br.com.api.models.recommendation.GenerateRecommendationJson;
import br.com.api.models.recommendation.item.RecommendationItemListJson;
import br.com.api.models.recommendation.item.UpdateItemRatingJson;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {
    @Autowired
    private RecommendationService _recService;
    @Autowired
    private RecommendationRepository _recommendations;
    @Autowired
    private RecommendationItemRepository _recommendationItemRepository;

    @PostMapping("/bytype")
    public ResponseEntity<?> getRecommendationsByType(
            @RequestBody GenerateRecommendationJson json) {

        List<RecommendationItem> recommendedRankedItems = _recService
                .getRecommendedItemsBy(json.getRecommendationTypeEnum(), json.getIdsEntities());

        return ResponseEntity.ok().body(new RecommendationItemListJson(recommendedRankedItems)
                .getRecommendationItemsJson());
    }

    @PostMapping("/update-rating")
    public ResponseEntity<?> updateRating(@RequestBody UpdateItemRatingJson json) {
        Optional<RecommendationItem> result = _recommendationItemRepository
                .withIdRecommendation(json.getIdRecommendation(), json.getIdTweet());

        if (result.isPresent()) {
            RecommendationItem recommendationItem = result.get();

            recommendationItem.setRating(json.getRating());
            recommendationItem.setRegistrationRating(null);
            recommendationItem.setRegistrationRating(new Date());
            _recommendationItemRepository.save(recommendationItem);
        } else {
            return ResponseEntity.unprocessableEntity()
                    .body("Não foi possível encontrar a recomendação solicitada");
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/finished-evaluations")
    public ResponseEntity<?> finishedEvaluations() {
        UserAccount user = HttpContext.getUserLogged();

        Set<Recommendation> recommendations = _recommendations.notFinished(user.getId());

        for (Recommendation recommendation : recommendations) {
            recommendation.setFinishedDate(new Date());
        }

        _recommendations.saveAll(recommendations);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/generate")
    public ResponseEntity<?> generate() {

        _recService.generateAllScores();

        return ResponseEntity.ok().build();
    }
}
