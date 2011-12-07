package com.iit.projects.search;

import ml.options.Options;
import ml.options.Options.Multiplicity;
import ml.options.Options.Separator;

public class QueryLogParser {

	private static final int NUMBER_ARGS = 2;

	private QueryLog log;

	public QueryLogParser() {
		log = new QueryLog();
	}

	public QueryLogParser(String inFilePath, String outFilePath,
			boolean blnFormat) throws QueryLogIOException,
			QueryLogOutputException {
		log = new QueryLog();
		log.setReader(inFilePath);
		log.setWriter(blnFormat ? new QueryURLWriter(outFilePath)
				: new QueryLogWriter(outFilePath));
	}

	public QueryLogParser(String inFilePath, String outFilePath,
			String queryFile, boolean blnFormat) throws QueryLogIOException,
			QueryLogOutputException {
		this(inFilePath, outFilePath, blnFormat);
		log.setQueryFileWriter(queryFile);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = new Options(args, NUMBER_ARGS);
		options.getSet().addOption("q", Separator.EQUALS,
				Multiplicity.ZERO_OR_ONE);
		options.getSet().addOption("f", Multiplicity.ZERO_OR_ONE);

		if (!options.check()) {
			System.err
					.println("Usage: java QueryLogParser [-q=<queryoutfile>] [-f] <inlogfile> <outlogfile>");
			System.exit(1);
		}

		String queryFile = null;
		boolean blnFormat = false;
		if (options.getSet().isSet("q"))
			queryFile = options.getSet().getOption("q").getResultValue(0);
		if (options.getSet().isSet("f"))
			blnFormat = true;

		try {
			if (null != queryFile)
				new QueryLogParser(options.getSet().getData().get(0), options
						.getSet().getData().get(1), queryFile, blnFormat)
						.parse(true);
			else
				new QueryLogParser(options.getSet().getData().get(0), options
						.getSet().getData().get(1), blnFormat).parse();
		} catch (QueryLogIOException e) {
			e.printStackTrace();
		} catch (QueryLogOutputException e) {
			e.printStackTrace();
		}
	}

	public void parse() throws QueryLogOutputException, QueryLogIOException {
		parse(false);
	}

	public void parse(boolean outputQueries) throws QueryLogOutputException,
			QueryLogIOException {

		QueryLogLineFilter spamFilter = new LogSpamFilter();
		QueryLogLineFilter queryFilter = new QueryURLFilter();
		QueryLogLineFilter htmlFilter = new QueryContentTypeFilter();

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
		log.unmarshall(true);
		log.close();
	}

}
