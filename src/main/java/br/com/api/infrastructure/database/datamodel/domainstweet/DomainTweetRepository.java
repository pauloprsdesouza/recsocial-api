package br.com.api.infrastructure.database.datamodel.domainstweet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainTweetRepository extends JpaRepository<DomainTweet, Long> {

}
