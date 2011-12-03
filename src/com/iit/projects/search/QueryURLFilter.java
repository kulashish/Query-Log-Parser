package com.iit.projects.search;

public class QueryURLFilter extends QueryLogLineFilter {

	@Override
	public boolean applyFilter(QueryLogLine line) {
		return super.applyFilter(line.getURL());
	}

}
