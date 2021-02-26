package br.com.api.infrastructure.database.datamodel.referencedtweets;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ReferencedTweetRepository
        extends JpaRepository<ReferencedTweet, ReferencedTweetPK> {

    @Query(value = "SELECT * FROM referenced_tweet WHERE id_reference_tweet = ?1",
            nativeQuery = true)
    public List<ReferencedTweet> getAllReferencesByIdReferenceTweet(long id);

}
