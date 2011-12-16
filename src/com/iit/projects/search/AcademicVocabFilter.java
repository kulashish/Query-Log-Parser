package com.iit.projects.search;

public class AcademicVocabFilter extends QueryLogLineFilter {

	private static final String[] ACADEMIC_WORDS = { ".edu", ".ac.", "wiki",
			"wikipedia", "googlescholar", "wolfram" };

	public AcademicVocabFilter() {
		super();
		setFilterText(ACADEMIC_WORDS);
	}

	@Override
	public boolean applyFilter(QueryLogLine line) {
		return super.applyFilter(line.getLine());
	}

}
