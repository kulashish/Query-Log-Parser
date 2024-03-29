package com.iit.projects.search;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.iit.projects.search.util.StringDecoder;

public class QueryLogLine {

	private static final int MONTH_INDEX = 0;
	private static final int DAY_INDEX = 1;
	private static final int TIME_INDEX = 2;
	private static final int IP_INDEX = 7;
	private static final int URL_INDEX = 11;
	private static final int CONTENTTYPE_INDEX = 13;

	private String line;
	private String IP;
	private String URL;
	private String month;
	private String day;
	private String time;
	private String contentType;

	public QueryLogLine(String line) throws QueryLogIOException {
		// System.out.println("Creating Query Line");
		this.line = line;
		// System.out.println(line);
		StringTokenizer tokenizer = new StringTokenizer(line);
		List<String> tokens = new ArrayList<String>();
		while (tokenizer.hasMoreElements())
			tokens.add(tokenizer.nextToken());
		int numTokens = tokens.size();
		if (null != tokens) {
			setMonth(numTokens > MONTH_INDEX ? tokens.get(MONTH_INDEX) : null);
			setDay(numTokens > DAY_INDEX ? tokens.get(DAY_INDEX) : null);
			setTime(numTokens > TIME_INDEX ? tokens.get(TIME_INDEX) : null);
			setIP(numTokens > IP_INDEX ? tokens.get(IP_INDEX) : null);
			setURL(numTokens > URL_INDEX ? tokens.get(URL_INDEX) : null);
			setContentType(numTokens > CONTENTTYPE_INDEX ? tokens
					.get(CONTENTTYPE_INDEX) : null);
		}
		// System.out.println("Created Query Line");
	}

	public String getLine() {
		return line;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String ip) {
		IP = ip;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String url) throws QueryLogIOException {
		try {
			// System.out.println("Before decoding : " + url);
			URL = StringDecoder.decode(url);
			// System.out.println("After decoding : " + URL);
		} catch (UnsupportedEncodingException e) {
			throw new QueryLogIOException(e);
		} catch (IllegalArgumentException e) {
			URL = null;
		}
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getQuery() {
		int startIdx = URL != null ? URL.indexOf("q=") : -1;
		int endIdx = URL != null ? URL.indexOf('&', startIdx + 2) : -1;
		return startIdx != -1 ? endIdx != -1 ? URL.substring(startIdx, endIdx)
				: URL.substring(startIdx) : null;
	}

}
