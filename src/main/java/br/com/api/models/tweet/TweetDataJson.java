package br.com.api.models.tweet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.gson.annotations.SerializedName;
import br.com.api.models.entitytweet.EntityDataJson;
import br.com.api.models.publicmetrics.PublicMetricDataJson;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TweetDataJson implements Serializable {
    /**
    *
    */
    private static final long serialVersionUID = 1L;

    private long id;
    private String text;
    private String lang;
    private String type;
    private String source;
    private EntityDataJson entities;

    @SerializedName(value = "publicMetrics", alternate = "public_metrics")
    private PublicMetricDataJson publicMetrics;

    @SerializedName(value = "createdAt", alternate = "created_at")
    private Date createdAt;

    @SerializedName(value = "referencedTweets", alternate = "referenced_tweets")
    private List<TweetDataJson> referencedTweets;

    @SerializedName(value = "autorId", alternate = "author_id")
    private long autorId;

    public TweetDataJson() {
        this.referencedTweets = new ArrayList<>();
        this.entities = new EntityDataJson();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public EntityDataJson getEntities() {
        return this.entities;
    }

    public void setEntities(EntityDataJson entities) {
        this.entities = entities;
    }

    public PublicMetricDataJson getPublicMetrics() {
        return this.publicMetrics;
    }

    public void setPublicMetrics(PublicMetricDataJson publicMetrics) {
        this.publicMetrics = publicMetrics;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<TweetDataJson> getReferencedTweets() {
        return this.referencedTweets;
    }

    public void setReferencedTweets(List<TweetDataJson> referencedTweets) {
        this.referencedTweets = referencedTweets;
    }

    public long getAutorId() {
        return this.autorId;
    }

    public void setAutorId(long autorId) {
        this.autorId = autorId;
    }

    public boolean isQuoted() {
        return type != null ? type.equals("quoted") : false;
    }

    public boolean isRepliedTo() {
        return type != null ? type.equals("replied_to") : false;
    }

    public boolean isRetweet() {
        return type != null ? type.equals("retweeted") : false;
    }

    public boolean isReferencedTweet() {
        return isRetweet() || isRepliedTo() || isQuoted();
    }
}
