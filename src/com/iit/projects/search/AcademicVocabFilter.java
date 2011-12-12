package com.iit.projects.search;

public class AcademicVocabFilter extends QueryLogLineFilter {

	private static final String[] ACADEMIC_WORDS = { ".edu", ".ac.in", "wiki",
			"wikipedia" };

	public AcademicVocabFilter() {
		super();
		setFilterText(ACADEMIC_WORDS);
	}

	@Override
	public boolean applyFilter(QueryLogLine line) {
		return super.applyFilter(line.getLine());
	}

}
