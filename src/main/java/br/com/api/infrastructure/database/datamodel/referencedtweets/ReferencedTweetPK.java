package br.com.api.infrastructure.database.datamodel.referencedtweets;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReferencedTweetPK implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "id_tweet")
    private long idTweet;

    @Column(name = "id_reference_tweet")
    private long idReferenceTweet;

    @Column(name = "type_reference")
    private String typeReference;


    public long getIdTweet() {
        return this.idTweet;
    }

    public void setIdTweet(long idTweet) {
        this.idTweet = idTweet;
    }

    public long getIdReferenceTweet() {
        return this.idReferenceTweet;
    }

    public void setIdReferenceTweet(long idReferenceTweet) {
        this.idReferenceTweet = idReferenceTweet;
    }

    public String getTypeReference() {
        return this.typeReference;
    }

    public void setTypeReference(String typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ReferencedTweetPK)) {
            return false;
        }
        ReferencedTweetPK referencedTweetPK = (ReferencedTweetPK) o;
        return idTweet == referencedTweetPK.idTweet
                && idReferenceTweet == referencedTweetPK.idReferenceTweet
                && Objects.equals(typeReference, referencedTweetPK.typeReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTweet, idReferenceTweet, typeReference);
    }
}
