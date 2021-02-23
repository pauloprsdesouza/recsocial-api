package br.com.api.infrastructure.services;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
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
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

@Service
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

    private TwitterUser _activeUser;
    private EntityTweet _entityTweet;
    private Set<Tag> _extractedTags;
    private Set<Tweet> _extractedTweets;
    private Set<TwitterUser> _extractedUsers;
    private Set<URL> _extractedUrls;

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

    @Transactional
    public TwitterV2Service parseResultJson(TwitterUser user, EntityTweet entity) {
        try {
            this._activeUser = user;
            this._entityTweet = entity;

            JsonObject searchResult = this._twitterHTTP.getInstance()
                    .getTweetsFromTimelineUserId(user.getId()).getResult();

            JsonElement existsTweets = searchResult.get("data");

            if (existsTweets != null) {
                Type typeToken = new TypeToken<List<TweetDataJson>>() {
                }.getType();

                List<TweetDataJson> tweetsDataJson = new Gson().fromJson(existsTweets, typeToken);

                for (TweetDataJson tweetData : tweetsDataJson) {
                    Tweet tweet = getTweetFromId(tweetData.getId());

                    if (tweet == null) {
                        tweet = createTweetFromTweetJson(tweetData);
                    }

                    tweet.setOnTimelineOf(_activeUser);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        _twitterUsers.save(this._activeUser);

        return this;
    }

    private Tweet createTweetFromTweetJson(TweetDataJson tweetData) {
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
                    .orElse(new URL(urlData.getName()));

            tweet.addUrl(url);
        }

        for (HashtagDataJson hashtagData : tweetData.getEntities().getHashtags()) {
            Tag tag = _extractedTags.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(hashtagData.getName())).findFirst()
                    .orElse(new Tag(hashtagData.getName()));

            tweet.addHashTag(tag);
        }

        for (TweetDataJson tweetReferencedData : tweetData.getReferencedTweets()) {
            Tweet originalTweet = getTweetFromId(tweetReferencedData.getId());

            if (originalTweet != null) {
                if (tweetReferencedData.isRepliedTo()) {
                    originalTweet.addReply(tweet);
                } else {
                    tweet.setQuoted(tweetReferencedData.isQuoted());
                    tweet.setRetweet(tweetReferencedData.isRetweet());
                    originalTweet.addRetweet(tweet);
                }

                originalTweet.setOnTimelineOf(originalTweet.getWhosPosted());
            }
        }

        return tweet;
    }

    private Tweet getTweetFromId(long id) {
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

                _extractedTweets.add(tweet);
            }
        } catch (Exception ex) {
            return null;
        }

        return tweet;
    }

    private TwitterUser checkUserAccount(String screenName) {
        TwitterUser twitterUser =
                _extractedUsers.stream().filter(p -> p.getScreenName().equalsIgnoreCase(screenName))
                        .findFirst().orElse(null);

        try {
            if (twitterUser == null) {
                twitterUser = _twitterUsers.getUserByScreenName(screenName).orElse(
                        getUserAccountFromTwitterUser(_twitterInstance.showUser(screenName)));

                _extractedUsers.add(twitterUser);
            }
        } catch (Exception ex) {
            return null;
        }

        return twitterUser;
    }

    private TwitterUser checkUserAccount(long userId) {
        TwitterUser twitterUser =
                _extractedUsers.stream().filter(p -> p.getId() == userId).findFirst().orElse(null);

        try {
            if (twitterUser == null) {
                twitterUser = _twitterUsers.findById(userId)
                        .orElse(getUserAccountFromTwitterUser(_twitterInstance.showUser(userId)));

                _extractedUsers.add(twitterUser);
            }
        } catch (Exception ex) {
            return null;
        }

        return twitterUser;
    }

    public TwitterUser createUser(String screenName) {
        try {
            TwitterUser twitterUser =
                    getUserAccountFromTwitterUser(this._twitterInstance.showUser(screenName));

            _twitterUsers.save(twitterUser);

            return twitterUser;
        } catch (Exception e) {
            return null;
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
