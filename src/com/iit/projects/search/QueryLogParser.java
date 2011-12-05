package com.iit.projects.search;


public class QueryLogParser {

	// private static final String QUERYLOG_FILE =
	// "C:\\Documents and Settings\\Administrator\\My Documents\\Search\\combined-sample.log";
	// private static final String OUTPUT_FILE =
	// "C:\\Documents and Settings\\Administrator\\My Documents\\Search\\combined-sample_out.log";
	private static final String[] SPAM_WORDS = { "rediff", "facebook", "movie" };
	private static final String QUERY_STRING = "search?";
	private static final String HTML_CONTENT = "text/html";
	private static final int NUMBER_ARGS = 2;

	private String queryLogFile;
	private String outFile;

	public QueryLogParser() {

	}

	public QueryLogParser(String inFilePath, String outFilePath) {
		queryLogFile = inFilePath;
		outFile = outFilePath;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != NUMBER_ARGS) {
			System.err.println("Usage: QueryLogParser <inlogfile> <outfile>");
			System.exit(1);
		}
		new QueryLogParser(args[0], args[1]).parse();
	}

	public void parse() {
		QueryLog log = null;
		try {
			log = new QueryLog(queryLogFile);
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
			log.unmarshall(outFile);
		} catch (QueryLogOutputException e) {
			e.printStackTrace();
		}
	}

}
