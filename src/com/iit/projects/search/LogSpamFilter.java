package com.iit.projects.search;

public class LogSpamFilter extends QueryLogLineFilter {

	private static final String[] SPAM_WORDS = { "rediff", "facebook", "movie",
			".jpg", ".png", ".js", "sex", ".gif", "nude", "porn", "hot",
			"kiss", "wallpap", "naked", "piss", "marry", "marri", "pyar",
			"pyaar", "desi", "erotic", "girl", "pop", "rock", "women", "woman",
			"cric", "saree" };

	public LogSpamFilter() {
		super();
		setFilterText(SPAM_WORDS);
	}

	@Override
	public boolean applyFilter(QueryLogLine line) {
		return super.applyFilter(line.getLine());
	}

}
