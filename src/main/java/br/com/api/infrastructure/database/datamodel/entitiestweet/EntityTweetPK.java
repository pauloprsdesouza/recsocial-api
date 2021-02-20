package br.com.api.infrastructure.database.datamodel.entitiestweet;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class EntityTweetPK implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty("idEntity")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_domain")
    private Long idDomain;

    public EntityTweetPK(Long id, Long idDomain) {
        this.id = id;
        this.idDomain = idDomain;
    }

    public EntityTweetPK(Long id) {
        this.id = id;
    }

    public EntityTweetPK() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDomain() {
        return idDomain;
    }

    public void setIdDomain(Long idDomain) {
        this.idDomain = idDomain;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((idDomain == null) ? 0 : idDomain.hashCode());
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
        EntityTweetPK other = (EntityTweetPK) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (idDomain == null) {
            if (other.idDomain != null)
                return false;
        } else if (!idDomain.equals(other.idDomain))
            return false;
        return true;
    }
}
