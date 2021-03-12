package br.com.api.infrastructure.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.text.StringTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.api.authorization.HttpContext;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetRepository;
import br.com.api.infrastructure.database.datamodel.recommendations.Recommendation;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationRepository;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationTypeEnum;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItemPK;
import br.com.api.infrastructure.database.datamodel.referencedtweets.ReferencedTweet;
import br.com.api.infrastructure.database.datamodel.referencedtweets.ReferencedTweetRepository;
import br.com.api.infrastructure.database.datamodel.tweets.Tweet;
import br.com.api.infrastructure.database.datamodel.tweets.TweetRepository;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUser;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUserRepository;
import br.com.api.infrastructure.preprocessing.StopWordService;
import br.com.api.infrastructure.preprocessing.TextAnalysisByFrequency;
import br.com.api.infrastructure.preprocessing.TextAnalysisByTFIDF;
import br.com.api.infrastructure.preprocessing.TweetCleanerService;
import br.com.api.infrastructure.preprocessing.TweetSentimentAnalysis;
import br.com.api.infrastructure.utils.Metric;
import smile.feature.WinsorScaler;
import smile.projection.PCA;

@Service
public class RecommendationService {

    @Autowired
    private EntityTweetRepository _entities;
    @Autowired
    private TweetRepository _tweets;
    @Autowired
    private TwitterUserRepository _users;
    @Autowired
    private RecommendationRepository _recommendations;
    @Autowired
    private ReferencedTweetRepository _tweetReferences;
    @Autowired
    private TweetCleanerService _tweetCleanerService;
    @Autowired
    private StopWordService _stopWordService;
    @Autowired
    private TweetSentimentAnalysis _sentimentAnalysis;

    private List<Tweet> _tweetsInMemory;
    private boolean _isSentimentAnalysis;
    private double _maxFollowersCount;
    private List<EntityTweet> _entitiesInMemory;

    public RecommendationService() {
        this._isSentimentAnalysis = false;
        this._tweetsInMemory = new ArrayList<>();
        this._maxFollowersCount = 0;
    }

    public List<RecommendationItem> getRecommendedItemsBy(RecommendationTypeEnum type,
            List<Long> idsEntities) {
        if (type == RecommendationTypeEnum.SocialCapial) {
            _recommendations
                    .deleteAll(_recommendations.notFinished(HttpContext.getUserLogged().getId()));
        }

        _tweetsInMemory =
                _tweets.getNotRecommendedTweets(HttpContext.getUserLogged().getId(), idsEntities);

        Map<Tweet, Double> tweetsScore = new HashMap<>();

        if (type == RecommendationTypeEnum.SocialCapial) {
            for (Tweet tweet : _tweetsInMemory) {
                tweetsScore.put(tweet, tweet.getScScore());
            }
        } else if (type == RecommendationTypeEnum.SocialCapitalSentiment) {
            for (Tweet tweet : _tweetsInMemory) {
                tweetsScore.put(tweet, tweet.getScsaScore());
            }
        } else if (type == RecommendationTypeEnum.CosineSimilarity) {
            tweetsScore = calculateSimilaritiesCosineSimilarity();
        } else {
            tweetsScore = calculateSimilaritiesBaseline01();
        }

        return saveRecommendations(tweetsScore, type.getValue());
    }

    public void generateAllScores() {
        _entitiesInMemory = _entities.findAll();

        _maxFollowersCount =
                _users.findAll().stream().mapToInt(p -> p.getFollowersCount()).max().getAsInt();

        generateBaseline01Scores();
        generateCosineSimilarityScores();
        generateteSocialCapitalScoresWithSentimentAnalysis();
        generateteSocialCapitalScoresWithoutSentimentAnalysis();
    }

    private Map<Tweet, Double> calculateSimilaritiesBaseline01() {
        Map<Tweet, Double> tweets = new HashMap<>();
        List<Tweet> tweetsEvaluated = getTweetsEvaluated();

        for (Tweet tweetEvaluated : tweetsEvaluated) {
            for (Tweet tweet : _tweetsInMemory) {
                double score = Metric.cosineSimilarity(tweetEvaluated.getPCAByBaseline1(),
                        tweet.getPCAByBaseline1());

                Double scoreMAP = tweets.get(tweet);

                if (scoreMAP == null) {
                    tweets.put(tweet, score);
                } else if (score > scoreMAP) {
                    tweets.put(tweet, score);
                }
            }
        }

        return tweets;
    }

    private Map<Tweet, Double> calculateSimilaritiesCosineSimilarity() {
        Map<Tweet, Double> tweets = new HashMap<>();
        List<Tweet> tweetsEvaluated = getTweetsEvaluated();

        for (Tweet tweet : _tweetsInMemory) {
            for (Tweet tweetEvaluated : tweetsEvaluated) {
                double score = Metric.cosineSimilarity(tweetEvaluated.getPCAByCosineSimilarity(),
                        tweet.getPCAByCosineSimilarity());

                Double scoreMAP = tweets.get(tweet);

                if (scoreMAP == null) {
                    tweets.put(tweet, score);
                } else if (score > scoreMAP) {
                    tweets.put(tweet, score);
                }
            }
        }

        return tweets;
    }

    private double getUserInfluence(TwitterUser twitterUser) {
        double followersCount = Metric.log2ToLog10(twitterUser.getFollowersCount());
        double likesCount = Metric.log2ToLog10(twitterUser.getLikesCount());
        double listedCount = Metric.log2ToLog10(twitterUser.getListedCount());
        double tweetsCount = Metric.log2ToLog10(twitterUser.getTweetsCount());

        // Popularity Metric
        double popularity = 1 - Math.pow(Math.E, -0.2 * followersCount);

        if (followersCount == 0) {
            followersCount = _maxFollowersCount;
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

        for (ReferencedTweet referencedTweet : _tweetReferences
                .getAllReferencesByIdReferenceTweet(tweet.getId())) {
            score += socialCapitalScoreCalculator(referencedTweet.getReferencedTweet());
        }

        double totalInUserfluenceScoreRetweet = 0;

        score += (frequencyLikes + frequencyRetweets + totalInUserfluenceScoreMentioned
                + totalInUserfluenceScoreRetweet + frequencyReplies) * userInfluenceScore;

        double sentimentWeight = 0;

        if (_isSentimentAnalysis) {
            int sentimentClass =
                    _sentimentAnalysis.findSentimentByPreProcessingTweet(tweet.getText());

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
        List<Tweet> tweets = _tweets
                .getRecommendedTweetsByRecommendationType(HttpContext.getUserLogged().getId());

        if (!tweets.isEmpty()) {
            _maxFollowersCount = tweets.stream()
                    .mapToLong(p -> p.getWhosPosted().getFollowersCount()).max().getAsLong();
        }

        return tweets;
    }

    private void generateBaseline01Scores() {
        for (EntityTweet entity : this._entitiesInMemory) {
            Map<Tweet, List<String>> tweetsKeywords = new HashMap<>();
            List<Tweet> tweetsToUpdate = new ArrayList<>();

            List<Tweet> tweets = entity.getTweets().stream()
                    .filter(p -> p.getReferences().isEmpty()).collect(Collectors.toList());

            for (Tweet tweet : entity.getTweets()) {
                List<String> keywords = preprocessingText(tweet.getText());
                keywords.addAll(tweet.getHashtags().stream().map(p -> p.getName())
                        .collect(Collectors.toList()));

                tweetsKeywords.put(tweet, keywords);
            }

            if (!tweets.isEmpty()) {
                TextAnalysisByTFIDF textAnalysisFreq = new TextAnalysisByTFIDF(tweetsKeywords);
                double[][] matrix = textAnalysisFreq.createMatrix();

                Map<Tweet, double[]> tweetsFrequencyPCA = applyPCA(matrix, tweets);

                for (Entry<Tweet, double[]> tweet : tweetsFrequencyPCA.entrySet()) {
                    tweet.getKey().setPca1B1(tweet.getValue()[0]);
                    tweet.getKey().setPca2B1(tweet.getValue()[1]);

                    tweetsToUpdate.add(tweet.getKey());
                }

                _tweets.saveAll(tweetsToUpdate);
            }
        }
    }

    private void generateCosineSimilarityScores() {
        for (EntityTweet entity : this._entitiesInMemory) {
            Map<Tweet, List<String>> tweetsKeywords = new HashMap<>();
            List<Tweet> tweetsToUpdate = new ArrayList<>();

            List<Tweet> tweets = entity.getTweets().stream()
                    .filter(p -> p.getReferences().isEmpty()).collect(Collectors.toList());

            for (Tweet tweet : tweets) {
                tweetsKeywords.put(tweet, preprocessingText(tweet.getText()));
            }

            if (!tweets.isEmpty()) {
                TextAnalysisByFrequency textAnalysisFreq =
                        new TextAnalysisByFrequency(tweetsKeywords);

                double[][] matrix = textAnalysisFreq.createMatrix();

                Map<Tweet, double[]> tweetsFrequencyPCA = applyPCA(matrix, tweets);

                for (Entry<Tweet, double[]> tweet : tweetsFrequencyPCA.entrySet()) {
                    tweet.getKey().setPca1Similarity(tweet.getValue()[0]);
                    tweet.getKey().setPca2Similarity(tweet.getValue()[1]);

                    tweetsToUpdate.add(tweet.getKey());
                }

                _tweets.saveAll(tweetsToUpdate);
            }
        }
    }

    private List<String> preprocessingText(String tweetText) {
        String text = _tweetCleanerService.setText(tweetText).cleanMentions().cleanHashtag()
                .cleanUrls().clean().getTextCleaned();

        StringTokenizer stringToken = new StringTokenizer(text);
        List<String> keywords = stringToken.getTokenList();

        return _stopWordService.removeFromText(keywords).toList();
    }

    private Map<Tweet, double[]> applyPCA(double[][] matrix, List<Tweet> tweets) {
        WinsorScaler scaler = WinsorScaler.fit(matrix, 0.01, 0.99);
        double[][] matrixFrequencies = scaler.transform(matrix);

        PCA pca = PCA.fit(matrixFrequencies);
        pca.setProjection(2);
        double[][] result = pca.project(matrixFrequencies);
        Map<Tweet, double[]> tweetFrequencyPCA = new HashMap<>();

        int i = 0;
        for (Tweet tweet : tweets) {
            tweetFrequencyPCA.put(tweet, result[i]);
            i++;
        }

        return tweetFrequencyPCA;
    }

    private void generateteSocialCapitalScoresWithSentimentAnalysis() {
        _isSentimentAnalysis = true;

        for (EntityTweet entity : this._entitiesInMemory) {
            List<Tweet> tweetsToUpdate = new ArrayList<>();

            List<Tweet> tweets = entity.getTweets().stream()
                    .filter(p -> p.getReferences().isEmpty()).collect(Collectors.toList());

            for (Tweet tweet : tweets) {
                double score = socialCapitalScoreCalculator(tweet);
                tweet.setScsaScore(score);

                tweetsToUpdate.add(tweet);
            }

            _tweets.saveAll(tweetsToUpdate);
        }
    }

    private void generateteSocialCapitalScoresWithoutSentimentAnalysis() {
        _isSentimentAnalysis = false;

        for (EntityTweet entity : this._entitiesInMemory) {
            List<Tweet> tweetsToUpdate = new ArrayList<>();

            List<Tweet> tweets = entity.getTweets().stream()
                    .filter(p -> p.getReferences().isEmpty()).collect(Collectors.toList());

            for (Tweet tweet : tweets) {
                double score = socialCapitalScoreCalculator(tweet);
                tweet.setScScore(score);

                tweetsToUpdate.add(tweet);
            }

            _tweets.saveAll(tweetsToUpdate);
        }
    }

    private List<RecommendationItem> saveRecommendations(Map<Tweet, Double> tweetsScore,
            String type) {
        Recommendation recommendation = new Recommendation();
        recommendation.setUser(HttpContext.getUserLogged());
        recommendation.setRegistrationDate(new Date());
        recommendation.setRecommendationType(type);

        recommendation = _recommendations.save(recommendation);

        Set<RecommendationItem> recommendedItems = new HashSet<>();

        for (Entry<Tweet, Double> tweet : tweetsScore.entrySet()) {
            RecommendationItemPK itemPK = new RecommendationItemPK();
            itemPK.setIdTweet(tweet.getKey().getId());
            itemPK.setIdRecommendation(recommendation.getId());

            RecommendationItem item = new RecommendationItem();
            item.setScore(tweet.getValue());
            item.setId(itemPK);

            recommendedItems.add(item);
        }

        LinkedList<RecommendationItem> recommendedRankedItems = new LinkedList<>(recommendedItems
                .stream().sorted(Comparator.comparing(RecommendationItem::getScore).reversed())
                .limit(10).collect(Collectors.toCollection(LinkedList::new)));

        int i = 1;
        for (RecommendationItem item : recommendedRankedItems) {
            item.setRank(i++);
        }

        recommendation.setItems(recommendedRankedItems);

        _recommendations.save(recommendation);

        return recommendedRankedItems;
    }
}
