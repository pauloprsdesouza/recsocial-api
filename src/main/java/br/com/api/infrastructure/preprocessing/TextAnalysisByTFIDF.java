package br.com.api.infrastructure.preprocessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.api.infrastructure.database.datamodel.tweets.Tweet;

public class TextAnalysisByTFIDF {
	private Map<Tweet, List<String>> tweetsKeywords;
	private Map<Tweet, Map<String, Double>> tweetsFrequencyKeywords;
	private Set<String> allDistictKeywords;

	public TextAnalysisByTFIDF(Map<Tweet, List<String>> tweetsKeywords) {
		this.tweetsKeywords = tweetsKeywords;
		this.allDistictKeywords = new HashSet<>();
		this.tweetsFrequencyKeywords = new HashMap<>();
		calculateFrequencyKeyWords();
	}

	private void calculateFrequencyKeyWords() {
		List<List<String>> documents = this.tweetsKeywords.entrySet().stream()
				.map(p -> p.getValue()).collect(Collectors.toList());

		for (Entry<Tweet, List<String>> tweet : this.tweetsKeywords.entrySet()) {
			Map<String, Double> frequencies = new HashMap<>();

			for (String term : tweet.getValue()) {
				double frequency = TFIDFCalculator.tfIdf(tweet.getValue(), documents, term);
				frequencies.put(term, frequency);

				this.allDistictKeywords.add(term);
			}

			tweetsFrequencyKeywords.put(tweet.getKey(), frequencies);
		}
	}

	public double[][] createMatrix() {
		double[][] matrixFrequency =
				new double[this.tweetsFrequencyKeywords.size()][this.allDistictKeywords.size()];
		int i = 0;

		for (Entry<Tweet, Map<String, Double>> tweet : this.tweetsFrequencyKeywords.entrySet()) {
			Map<String, Double> wordsFrequency = new HashMap<>();

			int index = 0;

			for (String word : this.allDistictKeywords) {
				Double frequency = tweet.getValue().get(word);
				if (frequency == null) {
					frequency = 0.0;
				}

				wordsFrequency.put(word, frequency);
				matrixFrequency[i][index] = frequency;

				index++;
			}

			i++;
		}

		return matrixFrequency;
	}
}
