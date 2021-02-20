package br.com.api.infrastructure.preprocessing;

import java.util.List;

public class TFIDFCalculator {
	private static double tf(List<String> doc, String term) {
		double result = 0;
		for (String word : doc) {
			if (term.equalsIgnoreCase(word))
				result++;
		}
		return result / doc.size();
	}

	private static double idf(List<List<String>> docs, String term) {
		double n = 0;
		for (List<String> doc : docs) {
			for (String word : doc) {
				if (term.equalsIgnoreCase(word)) {
					n++;
					break;
				}
			}
		}
		return Math.log(docs.size() / n);
	}

	public static double tfIdf(List<String> doc, List<List<String>> docs, String term) {
		return tf(doc, term) * idf(docs, term);

	}
}
