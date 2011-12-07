package com.iit.projects.search;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class QueryLogWriter {

	protected BufferedWriter writer;

	protected void writeCommonLineDetails(QueryLogLine line) throws IOException {
		writer.write(line.getMonth());
		writer.write(' ');
		writer.write(line.getDay());
		writer.write(' ');
		writer.write(line.getTime());
		writer.write(' ');
		writer.write(line.getIP());
		writer.write(' ');
	}

	public QueryLogWriter() {
	}

	public QueryLogWriter(String filePath) throws QueryLogOutputException {
		try {
			writer = new BufferedWriter(new FileWriter(filePath));
		} catch (IOException e) {
			throw new QueryLogOutputException(e);
		}
	}

	public void writeLog(String userIP, List<QueryLogLine> lines)
			throws QueryLogOutputException {
		try {
			for (QueryLogLine line : lines) {
				writer.write(line.getLine());
				writer.newLine();
			}
		} catch (IOException e) {
			throw new QueryLogOutputException(e);
		}
	}

	public void close() throws QueryLogOutputException {
		try {
			writer.close();
		} catch (IOException e) {
			throw new QueryLogOutputException(e);
		}

	}

}
