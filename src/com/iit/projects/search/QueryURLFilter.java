package com.iit.projects.search;

public class QueryURLFilter extends QueryLogLineFilter {

	private static final String QUERY_STRING = "search?";

	public QueryURLFilter() {
		super();
		setFilterText(new String[] { QUERY_STRING });
	}

	@Override
	public boolean applyFilter(QueryLogLine line) {
		return super.applyFilter(line.getURL());
	}

}
