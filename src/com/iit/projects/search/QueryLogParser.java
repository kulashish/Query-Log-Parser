package com.iit.projects.search;

public class QueryLogParser {

	private static final String QUERYLOG_FILE = "C:\\Documents and Settings\\Administrator\\My Documents\\Search\\QLog.log";
	private static final String OUTPUT_FILE = "C:\\Documents and Settings\\Administrator\\My Documents\\Search\\QLog_out.log";
	private static final String[] SPAM_WORDS = { "rediff", "facebook", "movie" };
	private static final String QUERY_STRING = "search?";
	private static final String HTML_CONTENT = "text/html";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new QueryLogParser().parse(QUERYLOG_FILE);
	}

	public void parse(String filePath) {
		QueryLog log = null;
		try {
			log = new QueryLog(filePath);
			QueryLogLineFilter spamFilter = new QueryURLFilter();
			spamFilter.setFilterText(SPAM_WORDS);

			QueryLogLineFilter queryFilter = new QueryURLFilter();
			queryFilter.setFilterText(new String[] { QUERY_STRING });

			QueryLogLineFilter htmlFilter = new QueryContentTypeFilter();
			htmlFilter.setFilterText(new String[] { HTML_CONTENT });

			QueryLogLine line = null;

			while ((line = log.getNextLine()) != null) {
				if (spamFilter.applyFilter(line))
					continue;
				if (queryFilter.applyFilter(line)
						|| htmlFilter.applyFilter(line))
					log.groupByIP(line);
			}
			log.close();

		} catch (QueryLogIOException e) {
			e.printStackTrace();
		}
		try {
			log.unmarshall(OUTPUT_FILE);
		} catch (QueryLogOutputException e) {
			e.printStackTrace();
		}
	}

}
