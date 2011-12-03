package com.iit.projects.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryLog {
	private String filePath;
	private BufferedReader reader;
	private Map<String, List<QueryLogLine>> logGroupedByIPMap;

	public QueryLog() {

	}

	public QueryLog(String path) throws QueryLogIOException {
		filePath = path;
		setReader();
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	public void setReader() throws QueryLogIOException {
		try {
			reader = new BufferedReader(new FileReader(filePath));
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

	public void groupByIP(QueryLogLine line) {
		if (null == logGroupedByIPMap)
			logGroupedByIPMap = new HashMap<String, List<QueryLogLine>>();
		List<QueryLogLine> queryLines = logGroupedByIPMap.get(line.getIP());
		if (null == queryLines) {
			queryLines = new ArrayList<QueryLogLine>();
			logGroupedByIPMap.put(line.getIP(), queryLines);
		}
		queryLines.add(line);
	}

	public void close() throws QueryLogIOException {
		if (null != reader)
			try {
				reader.close();
			} catch (IOException e) {
				throw new QueryLogIOException(e);
			}

	}

	public void unmarshall(String outputFile) throws QueryLogOutputException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
			Set<String> IPs = logGroupedByIPMap.keySet();
			Iterator<String> IPIter = IPs.iterator();
			List<QueryLogLine> lines = null;
			while (IPIter.hasNext()) {
				lines = logGroupedByIPMap.get(IPIter.next());
				for (QueryLogLine line : lines) {
					writer.write(line.getLine());
					writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			throw new QueryLogOutputException(e);
		}

	}
}
