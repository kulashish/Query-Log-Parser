package com.iit.projects.search;

public class QueryContentTypeFilter extends QueryLogLineFilter {

	@Override
	public boolean applyFilter(QueryLogLine line) {
		return super.applyFilter(line.getContentType());
	}

}
