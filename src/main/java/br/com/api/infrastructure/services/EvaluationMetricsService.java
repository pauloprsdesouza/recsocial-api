package br.com.api.infrastructure.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.api.infrastructure.database.datamodel.recommendations.Recommendation;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationRepository;
import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationTypeEnum;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;
import br.com.api.infrastructure.database.datamodel.tweets.TweetRepository;
import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccountRepository;
import br.com.api.infrastructure.utils.Metric;

@Service
public class EvaluationMetricsService {
    @Autowired
    private RecommendationRepository _recommendations;

    @Autowired
    private TweetRepository _tweets;

    @Autowired
    private UserAccountRepository _users;

    private List<Recommendation> socialCapitalRecommendations;
    private List<Recommendation> socialCapitalSentimentRecommendations;
    private List<Recommendation> cosineSimilarityRecommendations;
    private List<Recommendation> baselineARecommendations;
    private final String SC_COLOR = "rgba(4,93,183,0.9)";
    private final String SCSA_COLOR = "rgba(30, 93, 280, 0.9)";
    private final String CS_COLOR = "rgba(124,134,147,0.9)";
    private final String BA_COLOR = "rgba(153,163,178,0.9)";

    private final String SC_COLOR_TRANSPARENT = "rgba(4,93,183,0.1)";
    private final String SCSA_COLOR_TRANSPARENT = "rgba(30, 93, 280, 0.1)";
    private final String CS_COLOR_TRANSPARENT = "rgba(124,134,147,0.1)";
    private final String BA_COLOR_TRANSPARENT = "rgba(153,163,178,0.1)";

    public EvaluationMetricsService getInstance() {
        this.socialCapitalRecommendations = new ArrayList<>();
        this.socialCapitalSentimentRecommendations = new ArrayList<>();
        this.cosineSimilarityRecommendations = new ArrayList<>();
        this.baselineARecommendations = new ArrayList<>();
        this.generateEvaluation();

        return this;
    }

    private EvaluationMetricsService generateEvaluation() {
        List<Recommendation> recommendations = _recommendations.findAll();

        this.socialCapitalRecommendations = recommendations.stream()
                .filter(p -> p.getRecommendationType() == RecommendationTypeEnum.SocialCapial)
                .collect(Collectors.toList());

        this.socialCapitalSentimentRecommendations = recommendations.stream().filter(
                p -> p.getRecommendationType() == RecommendationTypeEnum.SocialCapitalSentiment)
                .collect(Collectors.toList());

        this.cosineSimilarityRecommendations = recommendations.stream()
                .filter(p -> p.getRecommendationType() == RecommendationTypeEnum.CosineSimilarity)
                .collect(Collectors.toList());

        this.baselineARecommendations = recommendations.stream()
                .filter(p -> p.getRecommendationType() == RecommendationTypeEnum.BaselineA)
                .collect(Collectors.toList());

        return this;
    }

    private JsonArray getResultsMAP() {
        double mapSC = calculateMAP(socialCapitalRecommendations);
        double mapSCSA = calculateMAP(socialCapitalSentimentRecommendations);
        double mapCS = calculateMAP(cosineSimilarityRecommendations);
        double mapBA = calculateMAP(baselineARecommendations);

        JsonArray mapResultsJsonMAP = new JsonArray();
        mapResultsJsonMAP.add(mapSC);
        mapResultsJsonMAP.add(mapSCSA);
        mapResultsJsonMAP.add(mapCS);
        mapResultsJsonMAP.add(mapBA);

        return mapResultsJsonMAP;
    }

    private JsonArray getResultsNDCG() {
        double ndcgSC = calculateNDCG(socialCapitalRecommendations);
        double ndcgSCSA = calculateNDCG(socialCapitalSentimentRecommendations);
        double ndcgCS = calculateNDCG(cosineSimilarityRecommendations);
        double ndcgBA = calculateNDCG(baselineARecommendations);

        JsonArray ndcgResultsJson = new JsonArray();
        ndcgResultsJson.add(ndcgSC);
        ndcgResultsJson.add(ndcgSCSA);
        ndcgResultsJson.add(ndcgCS);
        ndcgResultsJson.add(ndcgBA);

        return ndcgResultsJson;
    }

    private JsonArray getResultsSPS() {
        double spsSC = calculateSpearmansCorrelation(socialCapitalRecommendations);
        double spsSCSA = calculateSpearmansCorrelation(socialCapitalSentimentRecommendations);
        double spsCS = calculateSpearmansCorrelation(cosineSimilarityRecommendations);
        double spsBA = calculateSpearmansCorrelation(baselineARecommendations);

        JsonArray mapResultsJsonWSPS = new JsonArray();
        mapResultsJsonWSPS.add(spsSC);
        mapResultsJsonWSPS.add(spsSCSA);
        mapResultsJsonWSPS.add(spsCS);
        mapResultsJsonWSPS.add(spsBA);

        return mapResultsJsonWSPS;
    }

    private JsonArray getResultsMRR() {
        double mrrSC = calculateMRR(socialCapitalRecommendations);
        double mrrSCSA = calculateMRR(socialCapitalSentimentRecommendations);
        double mrrCS = calculateMRR(cosineSimilarityRecommendations);
        double mrrBA = calculateMRR(baselineARecommendations);

        JsonArray mapResultsJsonMRR = new JsonArray();
        mapResultsJsonMRR.add(mrrSC);
        mapResultsJsonMRR.add(mrrSCSA);
        mapResultsJsonMRR.add(mrrCS);
        mapResultsJsonMRR.add(mrrBA);

        return mapResultsJsonMRR;
    }

    private JsonArray getResultsPrecision() {
        JsonArray precisionAtNResultsJson = new JsonArray();

        JsonArray borderDash = new JsonArray();
        borderDash.add(5);
        borderDash.add(8);

        JsonArray borderDashPointed = new JsonArray();
        borderDashPointed.add(12);
        borderDashPointed.add(3);
        borderDashPointed.add(3);

        for (RecommendationTypeEnum recommendationType : RecommendationTypeEnum
                .getListOfRecommendationType()) {
            JsonArray resultsAlgorithmJson = new JsonArray();
            JsonArray backgrounds = new JsonArray();
            JsonObject dataset = new JsonObject();

            for (int i = 0; i < 10; i++) {
                if (recommendationType == RecommendationTypeEnum.SocialCapial) {
                    resultsAlgorithmJson
                            .add(calculatePrecisionAtN(socialCapitalRecommendations, i + 1));
                    backgrounds.add(this.SC_COLOR_TRANSPARENT);
                    dataset.addProperty("borderColor", this.SC_COLOR);
                    dataset.add("borderDash", borderDash);
                    dataset.addProperty("order", -7);
                } else if (recommendationType == RecommendationTypeEnum.SocialCapitalSentiment) {
                    resultsAlgorithmJson.add(
                            calculatePrecisionAtN(socialCapitalSentimentRecommendations, i + 1));
                    backgrounds.add(this.SCSA_COLOR_TRANSPARENT);
                    dataset.addProperty("borderColor", this.SCSA_COLOR);
                    dataset.add("borderDash", borderDashPointed);
                    dataset.addProperty("order", -8);
                } else if (recommendationType == RecommendationTypeEnum.CosineSimilarity) {
                    resultsAlgorithmJson
                            .add(calculatePrecisionAtN(cosineSimilarityRecommendations, i + 1));
                    backgrounds.add(this.CS_COLOR_TRANSPARENT);
                    dataset.addProperty("borderColor", this.CS_COLOR);
                    dataset.addProperty("order", -9);
                } else if (recommendationType == RecommendationTypeEnum.BaselineA) {
                    resultsAlgorithmJson
                            .add(calculatePrecisionAtN(baselineARecommendations, i + 1));
                    backgrounds.add(this.BA_COLOR_TRANSPARENT);
                    dataset.addProperty("borderColor", this.BA_COLOR);
                    dataset.addProperty("order", -10);
                }
            }

            dataset.addProperty("label", recommendationType.getValue());
            dataset.add("data", resultsAlgorithmJson);
            dataset.add("backgroundColor", backgrounds);

            dataset.addProperty("lineTension", 0.0);
            // dataset.addProperty("fill", false);

            precisionAtNResultsJson.add(dataset);
        }

        return precisionAtNResultsJson;
    }

    private JsonArray getSummaryByRating() {
        JsonArray datasetsJson = new JsonArray();

        Map<RecommendationTypeEnum, Map<Integer, Double>> ratingsFrequencies = new HashMap<>();

        for (RecommendationTypeEnum recommendationType : RecommendationTypeEnum
                .getListOfRecommendationType()) {
            Map<Integer, Double> algorithmRatingsFrequency = new HashMap<>();
            JsonArray backgrounds = new JsonArray();

            if (recommendationType == RecommendationTypeEnum.SocialCapial) {
                algorithmRatingsFrequency = getRatings(this.socialCapitalRecommendations);
            } else if (recommendationType == RecommendationTypeEnum.SocialCapitalSentiment) {
                algorithmRatingsFrequency = getRatings(this.socialCapitalSentimentRecommendations);
            } else if (recommendationType == RecommendationTypeEnum.CosineSimilarity) {
                algorithmRatingsFrequency = getRatings(this.cosineSimilarityRecommendations);
            } else if (recommendationType == RecommendationTypeEnum.BaselineA) {
                algorithmRatingsFrequency = getRatings(this.baselineARecommendations);
            }

            ratingsFrequencies.put(recommendationType, algorithmRatingsFrequency);

            JsonArray algorithmRatingsFrequencyJson = new JsonArray();

            for (Entry<Integer, Double> ratingFrequency : algorithmRatingsFrequency.entrySet()) {
                algorithmRatingsFrequencyJson.add(round(ratingFrequency.getValue()));

                if (recommendationType == RecommendationTypeEnum.SocialCapial) {
                    backgrounds.add(this.SC_COLOR);
                } else if (recommendationType == RecommendationTypeEnum.SocialCapitalSentiment) {
                    backgrounds.add(this.SCSA_COLOR);
                } else if (recommendationType == RecommendationTypeEnum.CosineSimilarity) {
                    backgrounds.add(this.CS_COLOR);
                } else if (recommendationType == RecommendationTypeEnum.BaselineA) {
                    backgrounds.add(this.BA_COLOR);
                }
            }

            JsonObject dataset = new JsonObject();
            dataset.addProperty("label", recommendationType.getValue());
            dataset.add("data", algorithmRatingsFrequencyJson);
            dataset.add("backgroundColor", backgrounds);

            datasetsJson.add(dataset);
        }

        JsonArray meanRatingsByAlgorithm = new JsonArray();
        JsonArray stDevMinusRatingsByAlgorithm = new JsonArray();
        JsonArray stDevPlusRatingsByAlgorithm = new JsonArray();

        for (int i = 1; i <= 5; i++) {
            List<Double> ratings = new ArrayList<>();

            for (Entry<RecommendationTypeEnum, Map<Integer, Double>> algorithm : ratingsFrequencies
                    .entrySet()) {
                ratings.add(algorithm.getValue().get(i));
            }

            double mean = ratings.stream().mapToDouble(p -> p).average().getAsDouble();
            double standardDeviation = 0;

            for (Double num : ratings) {
                standardDeviation += Math.pow(num - mean, 2);
            }

            standardDeviation = Math.sqrt(standardDeviation / ratings.size());

            meanRatingsByAlgorithm.add(round(mean));

            stDevMinusRatingsByAlgorithm.add(round(mean - standardDeviation));
            stDevPlusRatingsByAlgorithm.add(round(mean + standardDeviation));
        }

        JsonObject datasetMean = new JsonObject();
        datasetMean.addProperty("label", "Média");
        datasetMean.add("data", meanRatingsByAlgorithm);
        datasetMean.addProperty("type", "line");
        datasetMean.addProperty("fill", "false");
        datasetMean.addProperty("borderColor", "rgba(220,53,69)");
        datasetMean.addProperty("backgroundColor", "rgba(220,53,69)");
        datasetMean.addProperty("order", -10);

        JsonArray borderDash = new JsonArray();
        borderDash.add(5);
        borderDash.add(8);

        JsonObject datasetStDevMinus = new JsonObject();
        datasetStDevMinus.addProperty("label", "Desvio Padrão -");
        datasetStDevMinus.add("data", stDevMinusRatingsByAlgorithm);
        datasetStDevMinus.addProperty("type", "line");
        datasetStDevMinus.addProperty("fill", "false");
        datasetStDevMinus.addProperty("borderColor", "rgba(255,193,7)");
        datasetStDevMinus.addProperty("backgroundColor", "rgba(255,193,7)");
        datasetStDevMinus.addProperty("order", -10);
        datasetStDevMinus.add("borderDash", borderDash);

        JsonObject datasetStDevPlus = new JsonObject();
        datasetStDevPlus.addProperty("label", "Desvio Padrão +");
        datasetStDevPlus.add("data", stDevPlusRatingsByAlgorithm);
        datasetStDevPlus.addProperty("type", "line");
        datasetStDevPlus.addProperty("fill", "false");
        datasetStDevPlus.addProperty("borderColor", "rgba(40,167,69)");
        datasetStDevPlus.addProperty("backgroundColor", "rgba(40,167,69)");
        datasetStDevPlus.add("borderDash", borderDash);

        datasetStDevPlus.addProperty("order", -10);

        datasetsJson.add(datasetMean);
        datasetsJson.add(datasetStDevMinus);
        datasetsJson.add(datasetStDevPlus);

        return datasetsJson;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private Map<Integer, Double> getRatings(List<Recommendation> recommendations) {
        Map<Integer, Double> frequencyRatings = new HashMap<>();

        double r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;

        for (Recommendation recommendation : recommendations) {
            for (RecommendationItem item : recommendation.getItems()) {
                switch (item.getRating()) {
                    case 1: {
                        r1++;
                        break;
                    }
                    case 2: {
                        r2++;
                        break;
                    }
                    case 3: {
                        r3++;
                        break;
                    }
                    case 4: {
                        r4++;
                        break;
                    }
                    case 5: {
                        r5++;
                        break;
                    }
                }
            }

            double totalRecommendations = r1 + r2 + r3 + r4 + r5;

            frequencyRatings.put(1, r1 / totalRecommendations);
            frequencyRatings.put(2, r2 / totalRecommendations);
            frequencyRatings.put(3, r3 / totalRecommendations);
            frequencyRatings.put(4, r4 / totalRecommendations);
            frequencyRatings.put(5, r5 / totalRecommendations);
        }

        return frequencyRatings;
    }

    private JsonObject getDataSummary() {
        long totalRecommendations = _recommendations.count();
        long totalTweets = _tweets.count();
        long totalUsers = _users.count();

        JsonObject dataSummary = new JsonObject();
        dataSummary.addProperty("totalRecommendations", totalRecommendations);
        dataSummary.addProperty("totalTweets", totalTweets);
        dataSummary.addProperty("totalUsers", totalUsers);

        return dataSummary;
    }

    public JsonObject getResultsJson() {
        JsonArray labels = new JsonArray();

        for (RecommendationTypeEnum recommendationType : RecommendationTypeEnum
                .getListOfRecommendationType()) {
            labels.add(recommendationType.getValue());
        }

        JsonObject resultsJson = new JsonObject();
        resultsJson.add("labels", labels);

        JsonArray backgrounds = new JsonArray();
        backgrounds.add(this.SC_COLOR);
        backgrounds.add(this.SCSA_COLOR);
        backgrounds.add(this.CS_COLOR);
        backgrounds.add(this.BA_COLOR);

        JsonObject datasetMAP = new JsonObject();
        datasetMAP.addProperty("label", "MAP");
        datasetMAP.add("data", getResultsMAP());
        datasetMAP.add("backgroundColor", backgrounds);
        resultsJson.add("map", datasetMAP);

        JsonObject datasetMRR = new JsonObject();
        datasetMRR.addProperty("label", "MRR");
        datasetMRR.add("data", getResultsMRR());
        datasetMRR.add("backgroundColor", backgrounds);
        resultsJson.add("mrr", datasetMRR);

        JsonObject datasetSPS = new JsonObject();
        datasetSPS.addProperty("label", "SPS");
        datasetSPS.add("data", getResultsSPS());
        datasetSPS.add("backgroundColor", backgrounds);
        resultsJson.add("sps", datasetSPS);

        JsonObject datasetNDCG = new JsonObject();
        datasetNDCG.addProperty("label", "NDCG");
        datasetNDCG.add("data", getResultsNDCG());
        datasetNDCG.add("backgroundColor", backgrounds);
        resultsJson.add("ndcg", datasetNDCG);

        resultsJson.add("precision", getResultsPrecision());
        resultsJson.add("resultsByRatings", getSummaryByRating());
        resultsJson.add("dataSummary", getDataSummary());

        return resultsJson;
    }

    private double calculateMAP(List<Recommendation> recommendations) {
        List<Double> aveps = new ArrayList<>();

        for (Recommendation recommendation : recommendations) {
            double totalPrecisions = 0.0;
            double relevantItems = 1;
            double index = 1;

            List<RecommendationItem> sortedItems = recommendation.getItems().stream()
                    .sorted(Comparator.comparing(RecommendationItem::getScore).reversed())
                    .collect(Collectors.toList());

            for (RecommendationItem item : sortedItems) {
                if (item.getRating() > 3) {
                    totalPrecisions += relevantItems / index;
                    relevantItems++;
                }

                index++;
            }

            aveps.add(totalPrecisions / relevantItems);
        }

        return aveps.size() > 0 ? round(aveps.stream().mapToDouble(p -> p).average().getAsDouble())
                : 0;
    }

    private double calculatePrecisionAtN(List<Recommendation> recommendations, int n) {
        List<Double> precisions = new ArrayList<>();

        for (Recommendation recommendation : recommendations) {
            double relevantItems = 0;

            List<RecommendationItem> sortedItems = recommendation.getItems().stream()
                    .sorted(Comparator.comparing(RecommendationItem::getScore).reversed()).limit(n)
                    .collect(Collectors.toList());

            for (RecommendationItem item : sortedItems) {
                if (item.getRating() > 3) {
                    relevantItems++;
                }
            }

            precisions.add(relevantItems / n);
        }

        return precisions.size() > 0
                ? round(precisions.stream().mapToDouble(p -> p).average().getAsDouble())
                : 0;
    }

    private double calculateSpearmansCorrelation(List<Recommendation> recommendations) {
        List<Double> sps = new ArrayList<>();

        for (Recommendation recommendation : recommendations) {
            double totalSPS = 0.0;

            double[] originalOrder =
                    recommendation.getItems().stream().mapToDouble(p -> p.getRank()).toArray();

            double[] sortedItems = recommendation.getItems().stream()
                    .sorted(Comparator.comparing(RecommendationItem::getRank).reversed())
                    .mapToDouble(p -> p.getRank()).toArray();

            totalSPS = spearmans(originalOrder, sortedItems);

            sps.add(totalSPS);
        }

        return sps.size() > 0 ? round(sps.stream().mapToDouble(p -> p).average().getAsDouble()) : 0;
    }

    private double spearmans(double[] listA, double[] listB) {
        double sumDifference = 0;
        double n = listA.length;

        for (int i = 0; i < n; i++) {
            sumDifference += Math.pow(listA[i] - listB[i], 2);
        }

        return 1 - ((6 * sumDifference) / (n * (Math.pow(n, 2) - 1)));
    }

    private double calculateMRR(List<Recommendation> recommendations) {
        List<Double> mrrs = new ArrayList<>();

        for (Recommendation recommendation : recommendations) {
            double totalMRR = 0.0;
            double index = 1;

            List<RecommendationItem> sortedItems = recommendation.getItems().stream()
                    .sorted(Comparator.comparing(RecommendationItem::getScore).reversed())
                    .collect(Collectors.toList());

            for (RecommendationItem item : sortedItems) {
                if (item.getRating() > 3) {
                    totalMRR = 1 / index;
                    break;
                }

                index++;
            }

            mrrs.add(totalMRR);
        }

        return mrrs.size() > 0 ? round(mrrs.stream().mapToDouble(p -> p).average().getAsDouble())
                : 0;
    }

    private double calculateNDCG(List<Recommendation> recommendations) {
        double totalNDCG = 0;

        for (Recommendation recommendation : recommendations) {
            double dcg = 0;
            double idcg = 0;

            List<RecommendationItem> sortedItemsByScore = recommendation.getItems().stream()
                    .sorted(Comparator.comparing(RecommendationItem::getScore).reversed())
                    .collect(Collectors.toList());

            List<RecommendationItem> sortedItemsByRating = recommendation.getItems().stream()
                    .sorted(Comparator.comparing(RecommendationItem::getRating).reversed())
                    .collect(Collectors.toList());

            for (RecommendationItem item : sortedItemsByScore) {
                double denominator = Metric.log10ToLog2(item.getRank()) + 1;
                dcg += (Math.pow(item.getRating(), 2) - 1) / denominator;
            }

            int i = 1;
            for (RecommendationItem item : sortedItemsByRating) {
                double denominator = Metric.log10ToLog2(i) + 1;
                idcg += (Math.pow(item.getRating(), 2) - 1) / denominator;
                i++;
            }

            if (dcg > 0) {
                totalNDCG += dcg / idcg;
            }
        }

        return round(1.0 / recommendations.size() * totalNDCG);
    }
}
