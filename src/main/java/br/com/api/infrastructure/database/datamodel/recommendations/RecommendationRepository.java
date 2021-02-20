package br.com.api.infrastructure.database.datamodel.recommendations;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {

    @Query(value = "SELECT * FROM recommendation r "
            + "WHERE r.id_active_user = ?1 and r.finished_date is null", nativeQuery = true)
    public Set<Recommendation> notFinished(int idUser);
}
