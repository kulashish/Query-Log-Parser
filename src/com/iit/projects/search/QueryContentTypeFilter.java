package com.iit.projects.search;


public class QueryContentTypeFilter extends QueryLogLineFilter {
	
	private static final String HTML_CONTENT = "text/html";
	
	public QueryContentTypeFilter(){
		super();
		setFilterText(new String[]{HTML_CONTENT});
	}

	@Override
	public boolean applyFilter(QueryLogLine line) {
		return super.applyFilter(line.getContentType());
	}

}
