package com.iit.projects.search;

public abstract class QueryLogLineFilter {

	private String[] filterText;

	public void setFilterText(String[] filterText) {
		this.filterText = filterText;
	}

	public boolean applyFilter(String text) {
		boolean result = false;
		for (String filter : filterText)
			result |= text.indexOf(filter) != -1;
		return result;
	}

	abstract public boolean applyFilter(QueryLogLine line);

}
