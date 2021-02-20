package br.com.api.infrastructure.preprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StopWord {

	private List<String> stopWordsEN;
	///private List<String> stopWordsPT;
	private List<String> stopWordsEmoticons;
	private List<String> stopWordsEmojis;
	private List<String> keyWords;

	public StopWord() {
		this.stopWordsEN = new ArrayList<>((readFile("/stopwords/stopwords-en.txt")));
		//this.stopWordsPT = new ArrayList<>((readFile("/stopwords/stopwords-pt-br.txt")));
		//this.stopWordsEmoticons = new ArrayList<>((readFile("/stopwords/emoticons.txt")));
		this.stopWordsEmojis = new ArrayList<>((readFile("/stopwords/emojis.txt")));
	}

	public StopWord removeFromText(List<String> words) {
		this.keyWords = new ArrayList<>();

		for (String word : words) {
			if (!word.isEmpty()) {
				String wordWithoutWhiteSpace = word.trim();

				boolean okEN = !stopWordsEN.stream().anyMatch(a -> a.equalsIgnoreCase(wordWithoutWhiteSpace));
				//boolean okPT = !stopWordsPT.stream().anyMatch(a -> a.equalsIgnoreCase(wordWithoutWhiteSpace));
				boolean okEmoticons = !stopWordsEmojis.stream()
						.anyMatch(a -> wordWithoutWhiteSpace.contains(a));

				if ((okEN && okEmoticons) && !wordWithoutWhiteSpace.equals("\\s+")
						&& wordWithoutWhiteSpace.length() > 2) {
					this.keyWords.add(wordWithoutWhiteSpace);
				}
			}
		}

		return this;
	}
	
	public List<String> toList() {
		return new ArrayList<>(this.keyWords);
	}

	private Set<String> readFile(String fileName) {
		BufferedReader br = null;
		Set<String> stopWords = new HashSet<>();
		try {
			InputStream file = StopWord.class.getResourceAsStream(fileName);
			br = new BufferedReader(new InputStreamReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				stopWords.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return stopWords;
	}

	public List<String> getStopWordsEmoticons() {
		return stopWordsEmoticons;
	}

	public List<String> getStopWordsEmojis() {
		return stopWordsEmojis;
	}
}
