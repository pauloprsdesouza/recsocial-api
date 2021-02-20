package br.com.api.infrastructure.preprocessing;

public class RegularExpressions {
	public static final String HASHTAG = "#[\\S]+";
	public static final String URL = "http.*?[\\S]+";
	public static final String MENTION = "@[\\S]+";
	
	public static final String CONSECUTIVE_CHARS = "([a-z])\\1{1,}";
	public static final String DOUBLE_WHITE_SPACES = "\\s{2}|\\u00A0|\\u008C\\u07D3";
	public static final String SPECIAL_CHARACTERS = "[^a-zA-Z ]+";
	public static final String HAHAHAHAHAHA = "(?:ha|h){3,}";
	public static final String INVISIBLE_CHARACTERS = "\\ +";
	public static final String QUOTES = "\\“|\\”";
	public static final String GSM = "\\u008C|\\u07d3";
}
