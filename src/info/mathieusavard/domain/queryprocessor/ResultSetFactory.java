package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.Posting;

import java.util.Collection;

public class ResultSetFactory {

	public static ResultSet createResultSet(boolean ranked, String userInputQuery, Collection<Posting> results) {
		if (ranked) 
			return new RankedResultSet(userInputQuery,  results);
		else
			return new ResultSet(userInputQuery, results);
	}
	
	
}
