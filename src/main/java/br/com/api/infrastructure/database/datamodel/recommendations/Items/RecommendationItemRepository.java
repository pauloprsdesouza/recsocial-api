package br.com.api.infrastructure.database.datamodel.recommendations.Items;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface RecommendationItemRepository
        extends JpaRepository<RecommendationItem, RecommendationItemPK> {


    @Query(value = "SELECT * FROM recommendation_item WHERE id_recommendation = ?1 and id_tweet = ?2",
            nativeQuery = true)
    public Optional<RecommendationItem> withIdRecommendation(int idRecommendation, long idTweet);

    @Query(value = "SELECT * FROM recommendation_item rt "
            + "JOIN recommendation r on rt.id_recommendation = r.id "
            + "WHERE r.id_active_user = ?1 and r.recommendation_type = ?2 and rt.rating is null "
            + "order by rt.score desc", nativeQuery = true)
    public List<RecommendationItem> whithUserAndRecommendationsNotEvaluated(int idUser,
            String recommendationType);
}
