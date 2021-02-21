package br.com.api.infrastructure.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.text.StringTokenizer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
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
import br.com.api.infrastructure.utils.Metric;
import smile.projection.ProbabilisticPCA;

@Service
public class RecommendationService {

    private EntityTweetRepository _entities;
    private TweetRepository _tweets;
    private TweetCleanerService _tweetCleanerService;
    private StopWordService _stopWordService;

    private UserAccount _activeUser;
    private Set<Tweet> tweets;
    private Set<Tweet> tweetsByEntity;
    private boolean _isSentimentAnalysis;
    private double maxFollowersCount;
    private ProbabilisticPCA pca;

    public RecommendationService(EntityTweetRepository entities, TweetRepository tweets,
            TweetCleanerService tweetCleanerService, StopWordService stopWordService) {
        _entities = entities;
        _tweets = tweets;
        _tweetCleanerService = tweetCleanerService;
        _stopWordService = stopWordService;

        this._isSentimentAnalysis = false;
        this.tweetsByEntity = new HashSet<>();
        this.tweets = new HashSet<>();
        this.maxFollowersCount = 0;
    }


    public void generateAllRecommendations() {
        generateBaseline01Scores();
        generateCosineSimilarityScores();
        generateteSocialCapitalScoresWithSentimentAnalysis();
        generateteSocialCapitalScoresWithoutSentimentAnalysis();
    }

    public Map<Tweet, Double> calculateSimilaritiesBaseline01() {
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

                double score = Metric.cosineSimilarity(userProfile, tweet.getValue());

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

    public Map<Tweet, Double> calculateSimilaritiesCosineSimilarity() {
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

                double score = Metric.cosineSimilarity(userProfile, tweet.getValue());

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

    public RecommendationService setActiveUser(UserAccount user) {
        this._activeUser = user;
        return this;
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

    public RecommendationService withSentimentAnalysis() {
        TweetSentimentAnalysis.init();
        this._isSentimentAnalysis = true;

        return this;
    }


    private double getUserInfluence(TwitterUser twitterUser) {
        double followersCount = Metric.log2ToLog10(twitterUser.getFollowersCount());
        double likesCount = Metric.log2ToLog10(twitterUser.getLikesCount());
        double listedCount = Metric.log2ToLog10(twitterUser.getListedCount());
        double tweetsCount = Metric.log2ToLog10(twitterUser.getTweetsCount());

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

    private double socialCapitalScoreCalculator(Tweet tweet) {
        double userInfluenceScore = getUserInfluence(tweet.getWhosPosted());

        double frequencyLikes = Metric.log2ToLog10(tweet.getLikesCount());
        double frequencyRetweets = Metric.log2ToLog10(tweet.getRetweetsCount());
        double frequencyReplies = Metric.log2ToLog10(tweet.getRepliesCount());

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

    private List<Tweet> getTweetsEvaluated() {
        return _tweets.getRecommendedTweetsByRecommendationType(this._activeUser.getId());
    }

    private void generateBaseline01Scores() {
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

    private void generateCosineSimilarityScores() {
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

    private void generateteSocialCapitalScoresWithSentimentAnalysis() {
        for (Tweet tweet : this.tweets) {
            double score = socialCapitalScoreCalculator(tweet);
            tweet.setScsaScore(score);

            _tweets.save(tweet);
        }
    }

    private void generateteSocialCapitalScoresWithoutSentimentAnalysis() {
        for (Tweet tweet : this.tweets) {
            double score = socialCapitalScoreCalculator(tweet);
            tweet.setScScore(score);

            _tweets.save(tweet);
        }
    }
}
