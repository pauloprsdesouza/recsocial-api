package br.com.api.infrastructure.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import br.com.api.infrastructure.database.datamodel.recommendations.Recommendation;
import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;

public class Metric {
    public static double cosineSimilarity(double[] docVector1, double[] docVector2) {
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

    public static double log2ToLog10(double value) {
        return value > 0 ? Math.log10(value) : 0;
    }

    public static double log10ToLog2(double value) {
        return Math.log10(value) / Math.log10(2);
    }

    public static double spearmansCorrelation(double[] listA, double[] listB) {
        double sumDifference = 0;
        double n = listA.length;

        for (int i = 0; i < n; i++) {
            sumDifference += Math.pow(listA[i] - listB[i], 2);
        }

        return 1 - ((6 * sumDifference) / (n * (Math.pow(n, 2) - 1)));
    }

    public static double calculateMAP(List<Recommendation> recommendations) {
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

    public static double calculatePrecisionAtN(List<Recommendation> recommendations, int n) {
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

    public static double calculateSpearmansCorrelation(List<Recommendation> recommendations) {
        List<Double> sps = new ArrayList<>();

        for (Recommendation recommendation : recommendations) {
            double totalSPS = 0.0;

            double[] originalOrder =
                    recommendation.getItems().stream().mapToDouble(p -> p.getRank()).toArray();

            double[] sortedItems = recommendation.getItems().stream()
                    .sorted(Comparator.comparing(RecommendationItem::getRank).reversed())
                    .mapToDouble(p -> p.getRank()).toArray();

            totalSPS = Metric.spearmansCorrelation(originalOrder, sortedItems);

            sps.add(totalSPS);
        }

        return sps.size() > 0 ? round(sps.stream().mapToDouble(p -> p).average().getAsDouble()) : 0;
    }

    public static double calculateMRR(List<Recommendation> recommendations) {
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

    public static double calculateNDCG(List<Recommendation> recommendations) {
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

    public static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
