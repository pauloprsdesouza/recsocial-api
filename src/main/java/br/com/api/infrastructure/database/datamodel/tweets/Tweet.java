package br.com.api.infrastructure.database.datamodel.tweets;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;
import br.com.api.infrastructure.database.datamodel.referencedtweets.ReferencedTweet;
import br.com.api.infrastructure.database.datamodel.tags.Tag;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUser;
import br.com.api.infrastructure.database.datamodel.urls.URL;

@Entity
@Table(name = "tweet")
public class Tweet implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    private long id;

    @Column
    private String text;

    @Column(name = "is_retweet")
    private boolean isRetweet;

    @Column(name = "is_liked")
    private boolean isLiked;

    @Column(name = "is_quoted")
    private boolean isQuoted;

    @Column(name = "retweets_count")
    private int retweetsCount;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "quotes_count")
    private int quotesCount;

    @Column(name = "replies_count")
    private int repliesCount;

    @Column
    private String language;

    @Column
    private String source;

    @Column(name = "sc_score")
    private Double scScore;

    @Column(name = "scsa_score")
    private Double scsaScore;

    @Column(name = "pca1_similarity")
    private Double pca1Similarity;

    @Column(name = "pca2_similarity")
    private Double pca2Similarity;

    @Column(name = "pca1_b1")
    private Double pca1B1;

    @Column(name = "pca2_b1")
    private Double pca2B1;

    @Column(name = "registration_date")
    private Date registrationDate;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner_user")
    private TwitterUser postedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_timeline")
    private TwitterUser onTimelineOf;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "hashtag", joinColumns = {@JoinColumn(name = "id_tweet")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")})
    private Set<Tag> hashtags;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "address", joinColumns = {@JoinColumn(name = "id_tweet")},
            inverseJoinColumns = {@JoinColumn(name = "id_url")})
    private Set<URL> urls;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "mention", joinColumns = {@JoinColumn(name = "id_tweet")},
            inverseJoinColumns = {@JoinColumn(name = "id_user_mentioned")})
    private Set<TwitterUser> mentions;

    @OneToMany(mappedBy = "referencedTweet", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ReferencedTweet> references;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "context_annotation", joinColumns = {@JoinColumn(name = "id_tweet")},
            inverseJoinColumns = {@JoinColumn(name = "id_entity"), @JoinColumn(name = "id_domain")})
    private Set<EntityTweet> entities;

    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecommendationItem> recommendedItems;

    public Tweet() {
        this.hashtags = new HashSet<>();
        this.urls = new HashSet<>();
        this.mentions = new HashSet<>();
        this.references = new HashSet<>();
        this.entities = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public void setRetweet(boolean isRetweet) {
        this.isRetweet = isRetweet;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public int getRetweetsCount() {
        return retweetsCount;
    }

    public void setRetweetsCount(int retweetsCount) {
        this.retweetsCount = retweetsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getQuotesCount() {
        return quotesCount;
    }

    public void setQuotesCount(int quotesCount) {
        this.quotesCount = quotesCount;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(int repliesCount) {
        this.repliesCount = repliesCount;
    }

    public boolean isQuoted() {
        return isQuoted;
    }

    public void setQuoted(boolean isQuoted) {
        this.isQuoted = isQuoted;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getScScore() {
        return scScore;
    }

    public void setScScore(Double scScore) {
        this.scScore = scScore;
    }

    public Double getScsaScore() {
        return scsaScore;
    }

    public void setScsaScore(Double scsaScore) {
        this.scsaScore = scsaScore;
    }

    public Double getPca1Similarity() {
        return pca1Similarity;
    }

    public void setPca1Similarity(Double pca1Similarity) {
        this.pca1Similarity = pca1Similarity;
    }

    public Double getPca2Similarity() {
        return pca2Similarity;
    }

    public void setPca2Similarity(Double pca2Similarity) {
        this.pca2Similarity = pca2Similarity;
    }

    public Double getPca1B1() {
        return pca1B1;
    }

    public void setPca1B1(Double pca1b1) {
        pca1B1 = pca1b1;
    }

    public Double getPca2B1() {
        return pca2B1;
    }

    public void setPca2B1(Double pca2b1) {
        pca2B1 = pca2b1;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public TwitterUser getWhosPosted() {
        return postedBy;
    }

    public void setPostedBy(TwitterUser postedBy) {
        this.postedBy = postedBy;
    }

    public TwitterUser getOnTimelineOf() {
        return onTimelineOf;
    }

    public void setOnTimelineOf(TwitterUser onTimelineOf) {
        this.onTimelineOf = onTimelineOf;
        onTimelineOf.addTweetOnTimeline(this);
    }

    public Set<Tag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<Tag> hashtags) {
        this.hashtags = hashtags;
    }

    public Set<URL> getUrls() {
        return urls;
    }

    public void setUrls(Set<URL> urls) {
        this.urls = urls;
    }

    public Set<TwitterUser> getMentions() {
        return mentions;
    }

    public void setMentions(Set<TwitterUser> mentions) {
        this.mentions = mentions;
    }

    public Set<ReferencedTweet> getReferences() {
        return this.references;
    }

    public void setReferences(Set<ReferencedTweet> references) {
        this.references = references;
    }

    public Set<EntityTweet> getEntities() {
        return entities;
    }

    public void setEntities(Set<EntityTweet> entities) {
        this.entities = entities;
    }

    public void addAllUrls(Set<URL> urls) {
        this.urls.addAll(urls);
    }

    public void addUrl(URL url) {
        this.urls.add(url);
    }

    public void addAllHashtags(Set<Tag> hashtags) {
        this.hashtags.addAll(hashtags);
    }

    public void addHashTag(Tag hashtag) {
        this.hashtags.add(hashtag);
    }

    public void addAllMentions(Set<TwitterUser> mentions) {
        this.mentions.addAll(mentions);
    }

    public void addMention(TwitterUser twitterUser) {
        this.mentions.add(twitterUser);
    }

    public void addEntity(EntityTweet entityTweet) {
        this.entities.add(entityTweet);
    }

    public void addReference(ReferencedTweet tweet) {
        tweet.setReferencedTweet(this);
        this.references.add(tweet);
    }

    public Set<RecommendationItem> getRecommendedItems() {
        return recommendedItems;
    }

    public void setRecommendedItems(Set<RecommendationItem> recommendations) {
        this.recommendedItems = recommendations;
    }

    public Set<ReferencedTweet> getReplies() {
        return this.references.stream().filter(p -> p.getId().getTypeReference().equals("R"))
                .collect(Collectors.toSet());
    }

    public Set<ReferencedTweet> getRetweets() {
        return this.references.stream().filter(p -> p.getId().getTypeReference().equals("F"))
                .collect(Collectors.toSet());
    }

    public Set<ReferencedTweet> getQuotes() {
        return this.references.stream().filter(p -> p.getId().getTypeReference().equals("Q"))
                .collect(Collectors.toSet());
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
        Tweet other = (Tweet) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
