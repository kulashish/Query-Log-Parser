package com.iit.projects.search;

import java.io.IOException;

public class QueryLogOutputException extends Exception {

	public QueryLogOutputException(IOException e) {
		super(e);
	}

}
