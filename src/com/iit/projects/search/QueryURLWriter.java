package com.iit.projects.search;

import java.io.IOException;
import java.util.List;

public class QueryURLWriter extends QueryLogWriter {

	public QueryURLWriter() {
		super();
	}

	public QueryURLWriter(String filePath) throws QueryLogOutputException {
		super(filePath);
	}

	public void writeLog(String userIP, List<QueryLogLine> lines)
			throws QueryLogOutputException {
		try {
			boolean blnQueryFound = false;
			QueryLogLineFilter queryFilter = new QueryURLFilter();
			for (QueryLogLine line : lines) {
				if (!blnQueryFound)
					if (!queryFilter.applyFilter(line.getLine()))
						continue;
					else {
						blnQueryFound = true;
						writeQuery(line);
					}
				else
					writeURL(line);
			}
		} catch (IOException e) {
			throw new QueryLogOutputException(e);
		}
	}

	private void writeURL(QueryLogLine line) throws IOException {
		writeCommonLineDetails(line);
		writer.write(line.getURL());
		writer.newLine();
	}

	private void writeQuery(QueryLogLine line) throws IOException {
		writer.newLine();
		writeCommonLineDetails(line);
		writer.write(line.getQuery());
		writer.newLine();
	}
}
