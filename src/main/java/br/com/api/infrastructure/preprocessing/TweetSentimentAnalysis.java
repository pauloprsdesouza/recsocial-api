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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetSentimentAnalysis {
	@Autowired
	private TweetCleanerService _tweetCleaner;

	@Autowired
	private StopWordService _stopWords;

	private StanfordCoreNLP _pipeline;

	public TweetSentimentAnalysis() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		_pipeline = new StanfordCoreNLP(props);
	}

	public int findSentimentByPreProcessingTweet(String tweet) {
		String textCleaned = _tweetCleaner.setText(tweet).cleanMentions().cleanHashtag().cleanUrls()
				.clean().getTextCleaned();
		StringTokenizer stringToken = new StringTokenizer(textCleaned);
		List<String> keywords = stringToken.getTokenList();
		keywords = _stopWords.removeFromText(keywords).toList();

		String text = StringUtils.join(keywords, " ");

		int mainSentiment = 0;
		if (text != null && text.length() > 0) {
			int longest = 0;
			Annotation annotation = _pipeline.process(text);
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
