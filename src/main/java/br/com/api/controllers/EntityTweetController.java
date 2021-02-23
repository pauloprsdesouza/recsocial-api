package br.com.api.controllers;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetRepository;
import br.com.api.models.entitytweet.EntityTweetJson;
import br.com.api.models.entitytweet.EntityTweetListJson;

@RestController
@RequestMapping("/entities")
public class EntityTweetController {

    private EntityTweetRepository _entities;

    public EntityTweetController(EntityTweetRepository entities) {
        _entities = entities;
    }

    @GetMapping("/bydomain/{idDomain}")
    public Set<EntityTweetJson> getByDomain(@PathVariable long idDomain) {
        return new EntityTweetListJson(_entities.withIdDomain(idDomain)).getEntitiesTweet();
    }
}
