package com.iit.projects.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class QueryLog {
	private static final int MAX_QUERYLOG_BATCH = 1000;
	// private static final int MAX_QUERYLOG_BATCH = 5;
	private static final float BATCH_FRACTION = 0.5f;
	private BufferedReader reader;
	private Map<String, List<QueryLogLine>> logGroupedByIPMap;
	private BufferedWriter queryFileWriter;
	private QueryLogWriter outFileWriter;
	private int numLinesInMap = 0;

	public QueryLog() {

	}

	public QueryLog(String path) throws QueryLogIOException {
		setReader(path);
	}

	public QueryLog(String path, String queryFile) throws QueryLogIOException,
			QueryLogOutputException {
		this(path);
		setQueryFileWriter(queryFile);
	}

	public void setQueryFileWriter(String filePath)
			throws QueryLogOutputException {
		try {
			this.queryFileWriter = new BufferedWriter(new FileWriter(filePath));
		} catch (IOException e) {
			throw new QueryLogOutputException(e);
		}
	}

	public void outputQuery(QueryLogLine line) throws QueryLogOutputException {
		String query = null;
		if (null != line)
			query = line.getQuery();
		if (null != query)
			try {
				queryFileWriter.write(query);
				queryFileWriter.newLine();
			} catch (IOException e) {
				throw new QueryLogOutputException(e);
			}
	}

	public void setReader(String inFilePath) throws QueryLogIOException {
		try {
			this.reader = new BufferedReader(new FileReader(inFilePath));
		} catch (FileNotFoundException e) {
			throw new QueryLogIOException(e);
		}
	}

	public QueryLogLine getNextLine() throws QueryLogIOException {
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			throw new QueryLogIOException(e);
		}
		// System.out.println(line);
		QueryLogLine queryLine = null;
		if (null != line && !line.equalsIgnoreCase("null"))
			queryLine = new QueryLogLine(line);
		return queryLine;
	}

	public void groupByIP(QueryLogLine line) throws QueryLogOutputException {
		if (null == logGroupedByIPMap)
			logGroupedByIPMap = new LinkedHashMap<String, List<QueryLogLine>>(
					16, 0.75f, true);
		List<QueryLogLine> queryLines = logGroupedByIPMap.get(line.getIP());
		if (null == queryLines) {
			queryLines = new ArrayList<QueryLogLine>();
			logGroupedByIPMap.put(line.getIP(), queryLines);
		}
		queryLines.add(line);
		if (++numLinesInMap >= MAX_QUERYLOG_BATCH) {
			unmarshall(false);
			numLinesInMap = 0;
		}
	}

	public void close() throws QueryLogIOException, QueryLogOutputException {
		if (null != reader)
			try {
				reader.close();
			} catch (IOException e) {
				throw new QueryLogIOException(e);
			}
		if (null != queryFileWriter)
			try {
				queryFileWriter.close();
			} catch (IOException e) {
				throw new QueryLogOutputException(e);
			}
		if (null != outFileWriter)
			outFileWriter.close();
	}

	public void unmarshall(boolean unmarshallAll)
			throws QueryLogOutputException {

		Iterator<Entry<String, List<QueryLogLine>>> iter = logGroupedByIPMap
				.entrySet().iterator();
		List<QueryLogLine> lines = null;
		int numUnmarshall = unmarshallAll ? logGroupedByIPMap.size()
				: (int) (logGroupedByIPMap.size() * BATCH_FRACTION);
		Entry entry = null;
		while (iter.hasNext() && numUnmarshall-- > 0) {
			entry = iter.next();
			lines = (List<QueryLogLine>) entry.getValue();
			outFileWriter.writeLog((String) entry.getKey(), lines);
			iter.remove();
		}
	}

	public void setWriter(QueryLogWriter writer) {
		outFileWriter = writer;
	}
}
