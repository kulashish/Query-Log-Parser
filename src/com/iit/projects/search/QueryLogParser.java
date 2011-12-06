package com.iit.projects.search;

import ml.options.Options;
import ml.options.Options.Multiplicity;
import ml.options.Options.Separator;

public class QueryLogParser {

	// private static final String QUERYLOG_FILE =
	// "C:\\Documents and Settings\\Administrator\\My Documents\\Search\\combined-sample.log";
	// private static final String OUTPUT_FILE =
	// "C:\\Documents and Settings\\Administrator\\My Documents\\Search\\combined-sample_out.log";
	private static final String[] SPAM_WORDS = { "rediff", "facebook", "movie" };
	private static final String QUERY_STRING = "search?";
	private static final String HTML_CONTENT = "text/html";
	private static final int NUMBER_ARGS = 2;

	private String outFile;

	private QueryLog log;

	public QueryLogParser() {

	}

	public QueryLogParser(String inFilePath, String outFilePath) throws QueryLogIOException {
		log = new QueryLog(inFilePath);
		outFile = outFilePath;
	}

	public QueryLogParser(String inFilePath, String outFilePath,
			String queryFile) throws QueryLogIOException,
			QueryLogOutputException {
		this(inFilePath, outFilePath);
		if (queryFile != null)
			log = new QueryLog(inFilePath, queryFile);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = new Options(args, NUMBER_ARGS);
		options.getSet().addOption("q", Separator.EQUALS,
				Multiplicity.ZERO_OR_ONE);
		if (!options.check()) {
			System.err.println("Usage: QueryLogParser [-q=<queryoutfile>] <inlogfile> <outlogfile>");
			System.exit(1);
		}
		String queryFile = null;
		if (options.getSet().isSet("q"))
			queryFile = options.getSet().getOption("q").getResultValue(0);
		try {
			if (null != queryFile)
				new QueryLogParser(options.getSet().getData().get(0), options
						.getSet().getData().get(1), queryFile).parse(true);
			else
				new QueryLogParser(options.getSet().getData().get(0), options
						.getSet().getData().get(1)).parse();
		} catch (QueryLogIOException e) {
			e.printStackTrace();
		} catch (QueryLogOutputException e) {
			e.printStackTrace();
		}
	}

	public void parse() {
		parse(false);
	}

	public void parse(boolean outputQueries) {
		try {
			QueryLogLineFilter spamFilter = new QueryURLFilter();
			spamFilter.setFilterText(SPAM_WORDS);

			QueryLogLineFilter queryFilter = new QueryURLFilter();
			queryFilter.setFilterText(new String[] { QUERY_STRING });

			QueryLogLineFilter htmlFilter = new QueryContentTypeFilter();
			htmlFilter.setFilterText(new String[] { HTML_CONTENT });

			QueryLogLine line = null;
			boolean blnQuery = false;
			boolean blnHtml = false;

			while ((line = log.getNextLine()) != null) {
				if (spamFilter.applyFilter(line))
					continue;
				blnQuery = queryFilter.applyFilter(line);
				blnHtml = htmlFilter.applyFilter(line);
				if (blnHtml) {
					log.groupByIP(line);
					if (outputQueries && blnQuery) {
						log.outputQuery(line);
					}
				}
			}
			log.close();
			log.unmarshall(outFile);
		} catch (QueryLogIOException e) {
			e.printStackTrace();
		} catch (QueryLogOutputException e) {
			e.printStackTrace();
		}
	}

}
