package br.com.api.infrastructure.database.datamodel.twitterusers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TwitterUserRepository extends JpaRepository<TwitterUser, Long> {

    @Query(value = "SELECT * FROM twitter_user WHERE screen_name = ?1", nativeQuery = true)
    public Optional<TwitterUser> getUserByScreenName(String screenName);

    @Query(value = "SELECT * FROM twitter_user WHERE id IN (?1)", nativeQuery = true)
    public List<TwitterUser> containsIdsUsers(Set<Long> ids);
}
