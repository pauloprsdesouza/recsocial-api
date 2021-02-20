package br.com.api.models.domainstweet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.api.infrastructure.database.datamodel.domainstweet.DomainTweet;

public class DomainTweetListJson {
    private Set<DomainTweetJson> _domainsTweetJson;

    public DomainTweetListJson(List<DomainTweet> domains) {
        _domainsTweetJson = domains.stream().map(domain -> new DomainTweetJson(domain)).collect(Collectors.toSet());
    }

    public Set<DomainTweetJson> getDomainsTweet() {
        return _domainsTweetJson;
    }
}
