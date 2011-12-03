package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.Posting;

import java.util.Collection;

public class ResultSetFactory {

	public static ResultSet createResultSet(boolean ranked, String userInputQuery, String compressedInputQuery, Collection<Posting> results) {
		if (ranked) 
			return new RankedResultSet(userInputQuery,compressedInputQuery,  results);
		else
			return new ResultSet(userInputQuery,compressedInputQuery, results);
	}
	
	
}
