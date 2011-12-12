package com.iit.projects.search;

public class LogSpamFilter extends QueryLogLineFilter {

	private static final String[] SPAM_WORDS = { "rediff", "facebook", "movie", ".jpg", ".png", ".js" };

	public LogSpamFilter() {
		super();
		setFilterText(SPAM_WORDS);
	}

	@Override
	public boolean applyFilter(QueryLogLine line) {
		return super.applyFilter(line.getLine());
	}

}
