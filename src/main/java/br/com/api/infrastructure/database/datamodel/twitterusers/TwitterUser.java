package br.com.api.infrastructure.database.datamodel.twitterusers;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.api.infrastructure.database.datamodel.tweets.Tweet;

@Entity
@Table(name = "twitter_user")
public class TwitterUser implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    private long id;

    @Column
    private String name;

    @Column(name = "screen_name")
    private String screenName;

    @Column
    private String email;

    @Column(name = "tweets_count")
    private int tweetsCount;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "followees_count")
    private int followeesCount;

    @Column(name = "followers_count")
    private int followersCount;

    @Column(name = "listed_count")
    private int listedCount;

    @Column
    private String location;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "token_secret")
    private String tokenSecret;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "registration_date")
    private Date registrationDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "follower", joinColumns = { @JoinColumn(name = "id_user") }, inverseJoinColumns = {
            @JoinColumn(name = "id_follower") })
    private Set<TwitterUser> followers;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "followee", joinColumns = { @JoinColumn(name = "id_user") }, inverseJoinColumns = {
            @JoinColumn(name = "id_followee") })
    private Set<TwitterUser> followees;

    @OneToMany(mappedBy = "postedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tweet> tweetsPosted;

    @OneToMany(mappedBy = "onTimelineOf", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tweet> tweetOfTimeline;

    @ManyToMany(mappedBy = "mentions", fetch = FetchType.LAZY)
    private Set<Tweet> tweetsMentioned;

    /*
     * @ManyToMany(fetch = FetchType.LAZY, mappedBy = "replies") private Set<Tweet>
     * tweetsReplied;
     */

    public TwitterUser() {
        this.tweetsPosted = new HashSet<>();
        this.followers = new HashSet<>();
        this.followees = new HashSet<>();
        this.tweetsMentioned = new HashSet<>();
        // this.tweetsReplied = new HashSet<>();
        this.tweetOfTimeline = new HashSet<>();
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

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getFolloweesCount() {
        return followeesCount;
    }

    public void setFolloweesCount(int followeesCount) {
        this.followeesCount = followeesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getListedCount() {
        return listedCount;
    }

    public void setListedCount(int listedCount) {
        this.listedCount = listedCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<TwitterUser> getFollowers() {
        return followers;
    }

    public Set<TwitterUser> getFollowees() {
        return followees;
    }

    public void setFollowers(Set<TwitterUser> followers) {
        this.followers = followers;
    }

    public void setFollowees(Set<TwitterUser> followees) {
        this.followees = followees;
    }

    public Set<Tweet> getTweetsPosted() {
        return tweetsPosted;
    }

    public void setTweetsPosted(Set<Tweet> tweetsPosted) {
        this.tweetsPosted = tweetsPosted;
    }

    public void addTweetOnTimeline(Tweet tweet) {
        this.tweetOfTimeline.add(tweet);
    }

    public Set<Tweet> getTweetOfTimeline() {
        return tweetOfTimeline;
    }

    public void setTweetOfTimeline(Set<Tweet> tweetOfTimeline) {
        this.tweetOfTimeline = tweetOfTimeline;
    }

    public Set<Tweet> getTweetsMentioned() {
        return tweetsMentioned;
    }

    public void setTweetsMentioned(Set<Tweet> tweetsMentioned) {
        this.tweetsMentioned = tweetsMentioned;
    }

    /*
     * public Set<Tweet> getTweetsReplied() { return tweetsReplied; }
     * 
     * public void setTweetsReplied(Set<Tweet> tweetsReplied) { this.tweetsReplied =
     * tweetsReplied; }
     */

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
        TwitterUser other = (TwitterUser) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
