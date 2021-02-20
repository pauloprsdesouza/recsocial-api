package br.com.api.infrastructure.database.datamodel.domainstweet;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;

@Entity
@Table(name = "domain")
public class DomainTweet implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EntityTweet> entities;

    public DomainTweet() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<EntityTweet> getEntities() {
        return entities;
    }

    public void setEntities(Set<EntityTweet> entities) {
        this.entities = entities;
    }

    public void addEntity(EntityTweet entity) {
        this.entities.add(entity);
        entity.setDomain(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DomainTweet other = (DomainTweet) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
