package com.iit.projects.search;

import java.io.IOException;

public class QueryLogIOException extends Exception {

	public QueryLogIOException(IOException e) {
		super(e);
	}

}
