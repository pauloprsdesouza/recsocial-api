package br.com.api.infrastructure.services;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
import br.com.api.infrastructure.database.datamodel.referencedtweets.ReferencedTweet;
import br.com.api.infrastructure.database.datamodel.referencedtweets.ReferencedTweetPK;
import br.com.api.infrastructure.database.datamodel.referencedtweets.ReferencedTweetRepository;
import br.com.api.infrastructure.database.datamodel.tags.Tag;
import br.com.api.infrastructure.database.datamodel.tags.TagRepository;
import br.com.api.infrastructure.database.datamodel.tweets.Tweet;
import br.com.api.infrastructure.database.datamodel.tweets.TweetRepository;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUser;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUserRepository;
import br.com.api.infrastructure.database.datamodel.urls.URL;
import br.com.api.infrastructure.database.datamodel.urls.URLRepository;
import br.com.api.models.hashtag.HashtagDataJson;
import br.com.api.models.tweet.TweetDataJson;
import br.com.api.models.twitteruser.TwitterUserDataJson;
import br.com.api.models.url.UrlDataJson;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

@Service
@RequestScope
public class TwitterV2Service {
    @Autowired
    private TweetRepository _tweets;
    @Autowired
    private TagRepository _tags;
    @Autowired
    private URLRepository _urls;
    @Autowired
    private TwitterUserRepository _twitterUsers;
    @Autowired
    private TwitterHTTPService _twitterHTTP;
    @Autowired
    private ReferencedTweetRepository _references;

    private TwitterUser _activeUser;
    private EntityTweet _entityTweet;
    private Set<Tag> _extractedTags;
    private Set<Tweet> _extractedTweets;
    private Set<TwitterUser> _extractedUsers;
    private Set<URL> _extractedUrls;
    private JsonObject _searchResult;
    private Entry<Tweet, TweetDataJson> _originalTweet;

    private Twitter _twitterInstance;

    public TwitterV2Service getInstance() {
        _extractedUsers = new HashSet<>(_twitterUsers.findAll());
        _extractedTweets = new HashSet<>(_tweets.findAll());
        _extractedTags = new HashSet<>(_tags.findAll());
        _extractedUrls = new HashSet<>(_urls.findAll());

        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true).setOAuthConsumerKey("3KuXNQgK5fovNNl2VyfpVqWWV")
                .setOAuthConsumerSecret("P4OUCI9HJKkA2qSgKlmLFMWp1proUVHTU2djEzDFyQ7XcR7V3A")
                .setOAuthAccessToken("958669543261515776-XzRPcz3DIWRKzOsuEHvJNrPufRmoo4f")
                .setOAuthAccessTokenSecret("mPbUyVfBnrKcA16POJ0d0vFC6SorncifK0PvDWDMp0bvk");
        TwitterFactory tf = new TwitterFactory(cb.build());

        _twitterInstance = tf.getInstance();

        return this;
    }


    public void parseResultJson(TwitterUser user, EntityTweet entity) throws Exception {
        this._activeUser = user;
        this._entityTweet = entity;

        if (_searchResult == null) {
            _searchResult = this._twitterHTTP.getInstance()
                    .getTweetsFromTimelineUserId(user.getId()).getResult();
        }

        JsonElement existsTweets = _searchResult.get("data");

        if (existsTweets != null) {
            Type typeToken = new TypeToken<List<TweetDataJson>>() {
            }.getType();

            List<TweetDataJson> tweetsDataJson = new Gson().fromJson(existsTweets, typeToken);

            addTweetsTeste(tweetsDataJson);
            addTweetsReferences(tweetsDataJson);
        }

        _twitterUsers.save(this._activeUser);
    }

    @Transactional
    private void addTweetsTeste(List<TweetDataJson> tweetsDataJson) throws Exception {
        for (TweetDataJson tweetData : tweetsDataJson) {
            Tweet tweet = getTweetFromId(tweetData.getId());
            if (tweet != null) {
                tweet.setOnTimelineOf(_activeUser);

                _extractedTweets.add(tweet);
            }
        }

        _tweets.saveAll(_extractedTweets);
    }

    @Transactional
    private void addTweetsReferences(List<TweetDataJson> tweetsDataJson) throws Exception {
        for (TweetDataJson tweetData : tweetsDataJson) {
            Tweet tweet = getTweetFromId(tweetData.getId());

            if (tweet != null) {
                for (TweetDataJson tweetDataReferenced : tweetData.getReferencedTweets()) {
                    Tweet originalTweet = getTweetFromId(tweetDataReferenced.getId());

                    if (originalTweet != null) {
                        ReferencedTweetPK referencedTweetPK = new ReferencedTweetPK();
                        referencedTweetPK.setIdReferenceTweet(tweet.getId());
                        referencedTweetPK.setIdTweet(originalTweet.getId());

                        ReferencedTweet referencedTweet = new ReferencedTweet();
                        referencedTweet.setId(referencedTweetPK);

                        if (tweetDataReferenced.isRepliedTo()) {
                            referencedTweetPK.setTypeReference("R");
                        } else if (tweetDataReferenced.isRetweet()) {
                            tweet.setRetweet(true);
                            referencedTweetPK.setTypeReference("F");
                        } else {
                            tweet.setRetweet(true);
                            tweet.setQuoted(true);
                            referencedTweetPK.setTypeReference("Q");
                        }

                        originalTweet.setOnTimelineOf(originalTweet.getWhosPosted());
                        originalTweet.addReference(referencedTweet);

                        _extractedTweets.add(originalTweet);
                    }
                }
            }
        }

        _tweets.saveAll(_extractedTweets);
    }

    public TwitterV2Service withSearchResult(JsonObject searchResult) {
        _searchResult = searchResult;
        return this;
    }

    private Tweet createTweetFromTweetJson(TweetDataJson tweetData) throws Exception {
        Tweet tweet = new Tweet();
        tweet.setId(tweetData.getId());
        tweet.setLanguage(tweetData.getLang());
        tweet.setSource(tweetData.getSource());
        tweet.setText(tweetData.getText());
        tweet.setPostedBy(checkUserAccount(tweetData.getAutorId()));
        tweet.addEntity(this._entityTweet);
        tweet.setRetweetsCount(tweetData.getPublicMetrics().getRetweetCount());
        tweet.setLikesCount(tweetData.getPublicMetrics().getLikeCount());
        tweet.setQuotesCount(tweetData.getPublicMetrics().getQuoteCount());
        tweet.setRepliesCount(tweetData.getPublicMetrics().getReplyCount());
        tweet.setRegistrationDate(tweetData.getCreatedAt());

        for (TwitterUserDataJson userData : tweetData.getEntities().getMentions()) {
            TwitterUser userMentioned = checkUserAccount(userData.getUserName());

            if (userMentioned != null) {
                tweet.addMention(userMentioned);
            }
        }

        for (UrlDataJson urlData : tweetData.getEntities().getUrls()) {
            URL url = _extractedUrls.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(urlData.getName())).findFirst()
                    .orElse(null);

            if (url == null) {
                url = new URL(urlData.getName());
                _extractedUrls.add(url);
            }

            tweet.addUrl(url);
        }

        _urls.saveAll(_extractedUrls);

        for (HashtagDataJson hashtagData : tweetData.getEntities().getHashtags()) {
            Tag tag = _extractedTags.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(hashtagData.getName())).findFirst()
                    .orElse(null);

            if (tag == null) {
                tag = new Tag(hashtagData.getName());
                _extractedTags.add(tag);
            }

            tweet.addHashTag(tag);
        }

        _tags.saveAll(_extractedTags);

        return tweet;
    }

    private Tweet getTweetFromId(long id) throws Exception {
        Tweet tweet =
                _extractedTweets.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

        try {
            if (tweet == null) {
                JsonObject result = _twitterHTTP.getInstance().getASingleTweetById(id).getResult();

                if (result.get("errors") != null) {
                    return null;
                }
                TweetDataJson tweetData = new Gson().fromJson(result.get("data").getAsJsonObject(),
                        TweetDataJson.class);

                tweet = createTweetFromTweetJson(tweetData);
            }
        } catch (Exception ex) {
            return null;
        }

        return tweet;
    }

    private TwitterUser checkUserAccount(String screenName) throws Exception {
        TwitterUser twitterUser =
                _extractedUsers.stream().filter(p -> p.getScreenName().equalsIgnoreCase(screenName))
                        .findFirst().orElse(null);

        try {
            if (twitterUser == null) {
                twitterUser = _twitterUsers.getUserByScreenName(screenName).orElse(
                        getUserAccountFromTwitterUser(_twitterInstance.showUser(screenName)));

                _extractedUsers.add(twitterUser);
                _twitterUsers.save(twitterUser);
            }
        } catch (Exception ex) {
            throw new Exception("Couldn't possible to get user by screenName from Twitter");
        }

        return twitterUser;
    }

    private TwitterUser checkUserAccount(long userId) throws Exception {
        TwitterUser twitterUser =
                _extractedUsers.stream().filter(p -> p.getId() == userId).findFirst().orElse(null);

        try {
            if (twitterUser == null) {
                twitterUser = _twitterUsers.findById(userId)
                        .orElse(getUserAccountFromTwitterUser(_twitterInstance.showUser(userId)));

                _extractedUsers.add(twitterUser);
                _twitterUsers.save(twitterUser);
            }
        } catch (Exception ex) {
            throw new Exception("Couldn't possible to get user by id from Twitter");
        }

        return twitterUser;
    }

    public TwitterUser createUser(String screenName) throws Exception {
        try {
            TwitterUser twitterUser =
                    getUserAccountFromTwitterUser(this._twitterInstance.showUser(screenName));

            _twitterUsers.save(twitterUser);

            return twitterUser;
        } catch (TwitterException ex) {
            if (ex.getStatusCode() == TwitterException.NOT_FOUND) {
                throw new Exception("User not found");
            } else {
                throw new Exception(ex.getErrorMessage());
            }
        }
    }

    private TwitterUser getUserAccountFromTwitterUser(User user) {
        TwitterUser twitterUser = new TwitterUser();
        twitterUser.setId(user.getId());
        twitterUser.setName(user.getName());
        twitterUser.setScreenName(user.getScreenName());
        twitterUser.setProfileImageUrl(user.getBiggerProfileImageURL());
        twitterUser.setLikesCount(user.getFavouritesCount());
        twitterUser.setFolloweesCount(user.getFriendsCount());
        twitterUser.setFollowersCount(user.getFollowersCount());
        twitterUser.setLocation(user.getLocation());
        twitterUser.setRegistrationDate(user.getCreatedAt());
        twitterUser.setListedCount(user.getListedCount());
        twitterUser.setTweetsCount(user.getStatusesCount());
        twitterUser.setVerified(user.isVerified());

        return twitterUser;
    }
}
