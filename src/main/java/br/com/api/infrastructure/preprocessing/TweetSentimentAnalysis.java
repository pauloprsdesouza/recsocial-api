package br.com.api.infrastructure.preprocessing;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;

public class TweetSentimentAnalysis {
	private static StanfordCoreNLP pipeline;
	private static TweetCleaner tweetCleaner;
	private static StopWord stopWords;
	private static StringTokenizer stringToken;

	public static void init() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
		stopWords = new StopWord();
		tweetCleaner = new TweetCleaner();
	}

	public static int findSentimentByPreProcessingTweet(String tweet) {
		String textCleaned = tweetCleaner.setText(tweet).cleanMentions().cleanHashtag().cleanUrls()
				.clean().getTextCleaned();
		stringToken = new StringTokenizer(textCleaned);
		List<String> keywords = stringToken.getTokenList();
		keywords = stopWords.removeFromText(keywords).toList();

		String text = StringUtils.join(keywords, " ");

		int mainSentiment = 0;
		if (text != null && text.length() > 0) {
			int longest = 0;
			Annotation annotation = pipeline.process(text);
			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence.get(SentimentAnnotatedTree.class);
				// 0 = very negative, 1 = negative, 2 = neutral, 3 = positive, and 4 = very
				// positive.
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				// SimpleMatrix sentiment_new = RNNCoreAnnotations.getPredictions(tree);
				String partText = sentence.toString();
				if (partText.length() > longest) {
					mainSentiment = sentiment;
					longest = partText.length();
				}
			}
		}
		return mainSentiment;
	}
}
