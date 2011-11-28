package info.mathieusavard.domain.queryprocessor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class ResultSet implements Iterable<Result>{

	Collection<Result> results = null;
	private String userInputQuery = null;
	private String suggestedQuery = null;
	// clusters /Êclasses blabla for future use

	public String getUserInputQuery() {
		return userInputQuery;
	}
	
	public ResultSet(String userInputQuery, Collection<Result> results) {
		super();
		this.userInputQuery = userInputQuery;
		this.results = new LinkedList<Result>();
		this.results.addAll(results);
	}
	
	public String getSuggestedQuery() {
		return suggestedQuery;
	}

	@Override
	public Iterator<Result> iterator() {
		return results.iterator();
	}
	
	public int size() {
		return results.size();
	}
	
	
}
