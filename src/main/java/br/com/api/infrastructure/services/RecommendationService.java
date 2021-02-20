package br.com.api.infrastructure.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.text.StringTokenizer;
import org.springframework.stereotype.Service;

import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetRepository;
import br.com.api.infrastructure.database.datamodel.tweets.Tweet;
import br.com.api.infrastructure.database.datamodel.tweets.TweetRepository;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUser;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
import br.com.api.infrastructure.preprocessing.StopWordService;
import br.com.api.infrastructure.preprocessing.TextAnalysisByFrequency;
import br.com.api.infrastructure.preprocessing.TextAnalysisByTFIDF;
import br.com.api.infrastructure.preprocessing.TweetCleanerService;
import br.com.api.infrastructure.preprocessing.TweetSentimentAnalysis;
import smile.projection.ProbabilisticPCA;

@Service
public class RecommendationService {

    private EntityTweetRepository _entities;
    private TweetRepository _tweets;
    private TweetCleanerService _tweetCleanerService;
    private StopWordService _stopWordService;

    public RecommendationService(EntityTweetRepository entities, TweetRepository tweets,
            TweetCleanerService tweetCleanerService, StopWordService stopWordService) {
        _entities = entities;
        _tweets = tweets;
        _tweetCleanerService = tweetCleanerService;
        _stopWordService = stopWordService;
    }

    private UserAccount _activeUser;
    private Set<Tweet> tweets;
    private Set<Tweet> tweetsByEntity;
    private boolean _isSentimentAnalysis;
    private double maxFollowersCount;
    private ProbabilisticPCA pca;

    public RecommendationService getInstance() {
        this._isSentimentAnalysis = false;
        this.tweetsByEntity = new HashSet<>();
        this.tweets = new HashSet<>();
        this.maxFollowersCount = 0;

        return this;
    }

    public RecommendationService withSentimentAnalysis() {
        TweetSentimentAnalysis.init();
        this._isSentimentAnalysis = true;
        return this;
    }

    private double calculateLog10(double value) {
        return value > 0 ? Math.log10(value) : 0;
    }

    private List<Tweet> getTweetsEvaluated() {
        List<Tweet> tweets =
                _tweets.getRecommendedTweetByRecommendationType(this._activeUser.getId());

        return tweets;
    }

    private double cosineSimilarity(double[] docVector1, double[] docVector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double cosineSimilarity = 0.0;

        for (int i = 0; i < docVector1.length; i++) {
            dotProduct += docVector1[i] * docVector2[i];
            magnitude1 += Math.pow(docVector1[i], 2);
            magnitude2 += Math.pow(docVector2[i], 2);
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return cosineSimilarity;
    }

    public void generateBaseline01Scores() {
        StringTokenizer stringToken;
        Map<Tweet, List<String>> tweetsKeywords = new HashMap<>();

        for (Tweet tweet : this.tweets) {
            String text = this._tweetCleanerService.setText(tweet.getText()).cleanMentions()
                    .cleanHashtag().cleanUrls().clean().getTextCleaned();

            stringToken = new StringTokenizer(text);
            List<String> keywords = stringToken.getTokenList();

            keywords = this._stopWordService.removeFromText(keywords).toList();
            keywords.addAll(tweet.getHashtags().stream().map(p -> p.getName())
                    .collect(Collectors.toList()));

            tweetsKeywords.put(tweet, keywords);
        }

        TextAnalysisByTFIDF tfIDF = new TextAnalysisByTFIDF(tweetsKeywords);
        double[][] matrix = tfIDF.createMatrix();

        Map<Tweet, double[]> tweetsFrequencyPCA = null;// applyPCA(matrix, this.tweets);

        for (Entry<Tweet, double[]> tweet : tweetsFrequencyPCA.entrySet()) {
            tweet.getKey().setPca1B1(tweet.getValue()[0]);
            tweet.getKey().setPca2B1(tweet.getValue()[1]);
            _tweets.save(tweet.getKey());
        }
    }

    public void generateCosineSimilarityScores() {
        StringTokenizer stringToken;
        Map<Tweet, List<String>> tweetsKeywords = new HashMap<>();
        List<EntityTweet> entities = _entities.findAll();

        for (EntityTweet entity : entities) {
            List<Tweet> tweetsByEntity = this.tweets.stream().filter(
                    p -> p.getEntities().stream().anyMatch(p1 -> p1.getId() == entity.getId()))
                    .collect(Collectors.toList());

            for (Tweet tweet : tweetsByEntity) {
                String text = this._tweetCleanerService.setText(tweet.getText()).cleanMentions()
                        .cleanHashtag().cleanUrls().clean().getTextCleaned();

                stringToken = new StringTokenizer(text);
                List<String> keywords = stringToken.getTokenList();

                keywords = this._stopWordService.removeFromText(keywords).toList();
                keywords.addAll(tweet.getHashtags().stream().map(p -> p.getName())
                        .collect(Collectors.toList()));

                tweetsKeywords.put(tweet, keywords);
            }

            TextAnalysisByFrequency textAnalysisFreq = new TextAnalysisByFrequency(tweetsKeywords);
            double[][] matrix = textAnalysisFreq.createMatrix();

            // Map<Tweet, double[]> tweetsFrequencyPCA = applyPCA(matrix, tweetsByEntity);

            Map<Tweet, double[]> tweetsFrequencyPCA = null;// applyPCA(matrix, tweetsByEntity);

            for (Entry<Tweet, double[]> tweet : tweetsFrequencyPCA.entrySet()) {
                tweet.getKey().setPca1Similarity(tweet.getValue()[0]);
                tweet.getKey().setPca2Similarity(tweet.getValue()[1]);
                _tweets.save(tweet.getKey());
            }
        }
    }

    public void generateteSocialCapitalScores() {
        for (Tweet tweet : this.tweets) {
            double score = socialCapitalScoreCalculator(tweet);

            if (this._isSentimentAnalysis) {
                tweet.setScsaScore(score);
            } else {
                tweet.setScScore(score);
            }

            _tweets.save(tweet);
        }
    }

    public Map<Tweet, Double> calculateCosineSimilarityScores() {
        Map<Tweet, Double> tweets = new HashMap<>();
        Map<Tweet, double[]> tweetsFrequencyPCA = new HashMap<>();
        List<Tweet> tweetsEvaluated = getTweetsEvaluated();

        for (Tweet tweet : this.tweetsByEntity) {
            double[] pcaValue = new double[2];
            pcaValue[0] = tweet.getPca1Similarity();
            pcaValue[1] = tweet.getPca2Similarity();

            tweetsFrequencyPCA.put(tweet, pcaValue);
        }

        for (Entry<Tweet, double[]> tweet : tweetsFrequencyPCA.entrySet()) {
            for (Tweet tweetEvaluated : tweetsEvaluated) {
                double[] userProfile = new double[2];
                userProfile[0] = tweetEvaluated.getPca1Similarity();
                userProfile[1] = tweetEvaluated.getPca2Similarity();

                double score = cosineSimilarity(userProfile, tweet.getValue());

                Double scoreMAP = tweets.get(tweet.getKey());

                if (scoreMAP == null) {
                    tweets.put(tweet.getKey(), score);
                } else if (score > scoreMAP) {
                    tweets.put(tweet.getKey(), score);
                }
            }
        }

        return tweets;
    }

    public RecommendationService setTweetsByEntity(Set<Tweet> tweets) {
        this.tweetsByEntity = tweets;

        this.maxFollowersCount = tweets.stream()
                .mapToLong(p -> p.getWhosPosted().getFollowersCount()).max().getAsLong();

        return this;
    }

    public RecommendationService withAllTweets() {
        this.tweets = _tweets.getAllWithoutReplyRetweet();

        this.maxFollowersCount = this.tweets.stream()
                .mapToLong(p -> p.getWhosPosted().getFollowersCount()).max().getAsLong();

        return this;
    }

    public Map<Tweet, Double> calculateBaseline01Scores() {
        Map<Tweet, Double> tweets = new HashMap<>();
        Map<Tweet, double[]> tweetsFrequencyPCA = new HashMap<>();
        List<Tweet> tweetsEvaluated = getTweetsEvaluated();

        for (Tweet tweet : this.tweetsByEntity) {
            double[] pcaValue = new double[2];
            pcaValue[0] = tweet.getPca1B1();
            pcaValue[1] = tweet.getPca2B1();

            tweetsFrequencyPCA.put(tweet, pcaValue);
        }

        for (Entry<Tweet, double[]> tweet : tweetsFrequencyPCA.entrySet()) {
            for (Tweet tweetEvaluated : tweetsEvaluated) {
                double[] userProfile = new double[2];
                userProfile[0] = tweetEvaluated.getPca1Similarity();
                userProfile[1] = tweetEvaluated.getPca2Similarity();

                double score = cosineSimilarity(userProfile, tweet.getValue());

                Double scoreMAP = tweets.get(tweet.getKey());

                if (scoreMAP == null) {
                    tweets.put(tweet.getKey(), score);
                } else if (score > scoreMAP) {
                    tweets.put(tweet.getKey(), score);
                }
            }
        }

        return tweets;
    }

    private double getUserInfluence(TwitterUser twitterUser) {
        double followersCount = calculateLog10(twitterUser.getFollowersCount());
        double likesCount = calculateLog10(twitterUser.getLikesCount());
        double listedCount = calculateLog10(twitterUser.getListedCount());
        double tweetsCount = calculateLog10(twitterUser.getTweetsCount());

        // Popularity Metric
        double popularity = 1 - Math.pow(Math.E, -0.2 * followersCount);

        if (followersCount == 0) {
            followersCount = this.maxFollowersCount;
        }

        if (listedCount == 0) {
            listedCount = 1;
        }

        // Reputation Ratio Metric
        double reputationRatio = followersCount / listedCount;

        if (twitterUser.isVerified()) {
            reputationRatio += 1;
        }

        return (popularity + likesCount + tweetsCount) / reputationRatio;
    }

    public RecommendationService setActiveUser(UserAccount user) {
        this._activeUser = user;
        return this;
    }

    private double socialCapitalScoreCalculator(Tweet tweet) {
        double userInfluenceScore = getUserInfluence(tweet.getWhosPosted());

        double frequencyLikes = calculateLog10(tweet.getLikesCount());
        double frequencyRetweets = calculateLog10(tweet.getRetweetsCount());
        double frequencyReplies = calculateLog10(tweet.getRepliesCount());

        double totalInUserfluenceScoreMentioned = 0;

        for (TwitterUser user : tweet.getMentions()) {
            totalInUserfluenceScoreMentioned += getUserInfluence(user);
        }

        double score = 0;

        if (tweet.getOriginalTweetByReply() != null) {
            score += socialCapitalScoreCalculator(tweet.getOriginalTweetByReply());
        }

        if (tweet.getOriginalTweetByRetweet() != null) {
            if (tweet.isQuoted()) {
                score += socialCapitalScoreCalculator(tweet.getOriginalTweetByRetweet());
            }
        }

        double totalInUserfluenceScoreRetweet = 0;

        score += (frequencyLikes + frequencyRetweets + totalInUserfluenceScoreMentioned
                + totalInUserfluenceScoreRetweet + frequencyReplies) * userInfluenceScore;

        double sentimentWeight = 0;

        if (this._isSentimentAnalysis) {
            int sentimentClass =
                    TweetSentimentAnalysis.findSentimentByPreProcessingTweet(tweet.getText());

            // 0 = very negative, 1 = negative, 2 = neutral, 3 = positive, and 4 = very
            // positive.
            if (sentimentClass > 2) {
                sentimentWeight = 1.5;
            } else if (sentimentClass == 2) {
                sentimentWeight = 1;
            } else {
                sentimentWeight = 0.5;
            }

            score *= sentimentWeight;
        }

        return score;
    }
}
