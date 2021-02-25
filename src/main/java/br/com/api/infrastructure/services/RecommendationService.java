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
import br.com.api.infrastructure.database.datamodel.tweets.Tweet;
import br.com.api.infrastructure.database.datamodel.tweets.TweetRepository;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUser;
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
    private RecommendationRepository _recommendations;
    @Autowired
    private TweetCleanerService _tweetCleanerService;
    @Autowired
    private StopWordService _stopWordService;
    @Autowired
    private TweetSentimentAnalysis _sentimentAnalysis;

    private List<Tweet> tweets;
    private boolean _isSentimentAnalysis;
    private double maxFollowersCount;
    private List<EntityTweet> entities;

    public RecommendationService() {
        this._isSentimentAnalysis = false;
        this.tweets = new ArrayList<>();
        this.maxFollowersCount = 0;
    }

    public List<RecommendationItem> getRecommendedItemsBy(RecommendationTypeEnum type,
            List<Long> idsEntities) {
        Set<Tweet> tweets =
                _tweets.getNotRecommendedTweets(HttpContext.getUserLogged().getId(), idsEntities);

        Map<Tweet, Double> tweetsScore = new HashMap<>();

        if (type == RecommendationTypeEnum.SocialCapial) {
            for (Tweet tweet : tweets) {
                tweetsScore.put(tweet, tweet.getScScore());
            }
        } else if (type == RecommendationTypeEnum.SocialCapitalSentiment) {
            for (Tweet tweet : tweets) {
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
        this.entities = _entities.findAll();
        this.tweets = _tweets.getAllWithoutReplyRetweet();

        this.maxFollowersCount = tweets.stream()
                .mapToLong(p -> p.getWhosPosted().getFollowersCount()).max().getAsLong();

        generateBaseline01Scores();
        generateCosineSimilarityScores();
        generateteSocialCapitalScoresWithSentimentAnalysis();
        generateteSocialCapitalScoresWithoutSentimentAnalysis();
    }

    private Map<Tweet, Double> calculateSimilaritiesBaseline01() {
        Map<Tweet, Double> tweets = new HashMap<>();
        Map<Tweet, double[]> tweetsFrequencyPCA = new HashMap<>();
        List<Tweet> tweetsEvaluated = getTweetsEvaluated();

        for (Tweet tweet : this.tweets) {
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

    private Map<Tweet, Double> calculateSimilaritiesCosineSimilarity() {
        Map<Tweet, Double> tweets = new HashMap<>();
        Map<Tweet, double[]> tweetsFrequencyPCA = new HashMap<>();
        List<Tweet> tweetsEvaluated = getTweetsEvaluated();

        for (Tweet tweet : this.tweets) {
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

        for (ReferencedTweet referencedTweet : tweet.getReferences()) {
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

        this.maxFollowersCount = tweets.stream()
                .mapToLong(p -> p.getWhosPosted().getFollowersCount()).max().getAsLong();

        return tweets;
    }

    private void generateBaseline01Scores() {
        for (EntityTweet entity : this.entities) {
            Map<Tweet, List<String>> tweetsKeywords = new HashMap<>();
            List<Tweet> tweetsToUpdate = new ArrayList<>();

            List<Tweet> tweetsByEntity = this.tweets.stream().filter(
                    p -> p.getEntities().stream().anyMatch(p1 -> p1.getId() == entity.getId()))
                    .collect(Collectors.toList());

            for (Tweet tweet : tweetsByEntity) {
                String text = _tweetCleanerService.setText(tweet.getText()).cleanMentions()
                        .cleanHashtag().cleanUrls().clean().getTextCleaned();

                StringTokenizer stringToken = new StringTokenizer(text);
                List<String> keywords = stringToken.getTokenList();

                keywords = _stopWordService.removeFromText(keywords).toList();
                keywords.addAll(tweet.getHashtags().stream().map(p -> p.getName())
                        .collect(Collectors.toList()));

                tweetsKeywords.put(tweet, keywords);
            }

            if (!tweetsByEntity.isEmpty()) {
                TextAnalysisByTFIDF textAnalysisFreq = new TextAnalysisByTFIDF(tweetsKeywords);
                double[][] matrix = textAnalysisFreq.createMatrix();

                Map<Tweet, double[]> tweetsFrequencyPCA = applyPCA(matrix, tweetsByEntity);

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
        for (EntityTweet entity : this.entities) {
            Map<Tweet, List<String>> tweetsKeywords = new HashMap<>();
            List<Tweet> tweetsToUpdate = new ArrayList<>();

            List<Tweet> tweetsByEntity = this.tweets.stream().filter(
                    p -> p.getEntities().stream().anyMatch(p1 -> p1.getId() == entity.getId()))
                    .collect(Collectors.toList());

            for (Tweet tweet : tweetsByEntity) {
                String text = _tweetCleanerService.setText(tweet.getText()).cleanMentions()
                        .cleanHashtag().cleanUrls().clean().getTextCleaned();

                StringTokenizer stringToken = new StringTokenizer(text);
                List<String> keywords = stringToken.getTokenList();

                keywords = _stopWordService.removeFromText(keywords).toList();
                tweetsKeywords.put(tweet, keywords);
            }

            if (!tweetsByEntity.isEmpty()) {
                TextAnalysisByFrequency textAnalysisFreq =
                        new TextAnalysisByFrequency(tweetsKeywords);

                double[][] matrix = textAnalysisFreq.createMatrix();

                Map<Tweet, double[]> tweetsFrequencyPCA = applyPCA(matrix, tweetsByEntity);

                for (Entry<Tweet, double[]> tweet : tweetsFrequencyPCA.entrySet()) {
                    tweet.getKey().setPca1Similarity(tweet.getValue()[0]);
                    tweet.getKey().setPca2Similarity(tweet.getValue()[1]);

                    tweetsToUpdate.add(tweet.getKey());
                }

                _tweets.saveAll(tweetsToUpdate);
            }
        }
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

        for (EntityTweet entity : this.entities) {
            List<Tweet> tweetsToUpdate = new ArrayList<>();

            List<Tweet> tweetsByEntity = this.tweets.stream().filter(
                    p -> p.getEntities().stream().anyMatch(p1 -> p1.getId() == entity.getId()))
                    .collect(Collectors.toList());

            for (Tweet tweet : tweetsByEntity) {
                double score = socialCapitalScoreCalculator(tweet);
                tweet.setScsaScore(score);

                tweetsToUpdate.add(tweet);
            }

            _tweets.saveAll(tweetsToUpdate);
        }
    }

    private void generateteSocialCapitalScoresWithoutSentimentAnalysis() {
        _isSentimentAnalysis = false;

        for (EntityTweet entity : this.entities) {
            List<Tweet> tweetsToUpdate = new ArrayList<>();

            List<Tweet> tweetsByEntity = this.tweets.stream().filter(
                    p -> p.getEntities().stream().anyMatch(p1 -> p1.getId() == entity.getId()))
                    .collect(Collectors.toList());

            for (Tweet tweet : tweetsByEntity) {
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
