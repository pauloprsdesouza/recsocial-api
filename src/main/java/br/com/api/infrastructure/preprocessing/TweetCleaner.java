package br.com.api.infrastructure.preprocessing;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

public class TweetCleaner {
	private String text;

	public TweetCleaner setText(String text) {
		this.text = text;

		return this;
	}

	public TweetCleaner clean() {
		this.text = this.text.replaceAll(RegularExpressions.GSM, "?")
				.replaceAll(RegularExpressions.SPECIAL_CHARACTERS, " ")
				.replaceAll(RegularExpressions.DOUBLE_WHITE_SPACES, " ")
				.replaceAll(RegularExpressions.INVISIBLE_CHARACTERS, " ")
				.replaceAll(RegularExpressions.QUOTES, " ").replaceAll("[\\d-]", StringUtils.EMPTY)
				.replaceAll("[\\s]+", " ");

		this.text = StringEscapeUtils.unescapeHtml4(text);
		this.text = StringUtils.stripAccents(text);

		return this;
	}

	public TweetCleaner cleanHashtag() {
		this.text = this.text.replaceAll(RegularExpressions.HASHTAG, "");
		return this;
	}

	public TweetCleaner cleanUrls() {
		this.text = this.text.replaceAll(RegularExpressions.URL, "");
		return this;
	}

	public TweetCleaner cleanMentions() {
		this.text = this.text.replaceAll(RegularExpressions.MENTION, "");
		return this;
	}

	public String getTextCleaned() {
		return this.text.toLowerCase();
	}
}
