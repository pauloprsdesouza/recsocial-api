package br.com.api.controllers;

import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.infrastructure.database.datamodel.domainstweet.DomainTweetRepository;
import br.com.api.models.domainstweet.DomainTweetJson;
import br.com.api.models.domainstweet.DomainTweetListJson;

@RestController
@RequestMapping("/domains")
public class DomainTweetController {

    private DomainTweetRepository _domains;

    public DomainTweetController(DomainTweetRepository domains) {
        _domains = domains;
    }

    @GetMapping("/all")
    public Set<DomainTweetJson> getAll() {
        return new DomainTweetListJson(_domains.findAll()).getDomainsTweet();
    }
}
