package br.com.api.infrastructure.preprocessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.api.infrastructure.database.datamodel.tweets.Tweet;

import java.util.Set;

public class TextAnalysisByFrequency {
	private Map<Tweet, double[]> tweetsMatrix;
	private Map<Tweet, List<String>> tweetsKeywords;
	private Map<Tweet, Map<String, Integer>> tweetsFrequencyKeywords;
	private Set<String> allDistictKeywords;

	public TextAnalysisByFrequency(Map<Tweet, List<String>> tweetsKeywords) {
		this.allDistictKeywords = new HashSet<>();
		this.tweetsFrequencyKeywords = new HashMap<>();
		this.tweetsKeywords = tweetsKeywords;
		this.tweetsMatrix = new HashMap<>();
		calculateFrequencyKeyWords();
	}

	private void calculateFrequencyKeyWords() {
		for (Entry<Tweet, List<String>> tweet : this.tweetsKeywords.entrySet()) {
			Map<String, Integer> frequencies = new HashMap<>();

			for (String term : tweet.getValue()) {
				Integer frequency = frequencies.get(term);

				if (frequency == null) {
					frequency = 0;
				} else {
					frequency++;
				}

				frequencies.put(term, frequency);

				this.allDistictKeywords.add(term);
			}

			this.tweetsFrequencyKeywords.put(tweet.getKey(), frequencies);
		}
	}

	public double[][] createMatrix() {
		double[][] matrixFrequency =
				new double[this.tweetsFrequencyKeywords.size()][this.allDistictKeywords.size()];
		int i = 0;

		for (Entry<Tweet, Map<String, Integer>> tweet : this.tweetsFrequencyKeywords.entrySet()) {
			Map<String, Integer> wordsFrequency = new HashMap<>();

			int index = 0;

			for (String word : this.allDistictKeywords) {
				Integer frequency = tweet.getValue().get(word);
				if (frequency == null) {
					frequency = 0;
				}

				wordsFrequency.put(word, frequency);
				matrixFrequency[i][index] = frequency;

				index++;
			}

			this.tweetsMatrix.put(tweet.getKey(), matrixFrequency[i]);

			i++;
		}

		return matrixFrequency;
	}

	public Map<Tweet, double[]> getTweetsMatrix() {
		return this.tweetsMatrix;
	}
}
