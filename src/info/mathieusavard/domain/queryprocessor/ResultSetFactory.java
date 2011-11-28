package info.mathieusavard.domain.queryprocessor;

import java.util.Collection;

public class ResultSetFactory {

	public static ResultSet createResultSet(boolean ranked, String userInputQuery, Collection<Result> results) {
		if (ranked) 
			return new RankedResultSet(userInputQuery,  results);
		else
			return new ResultSet(userInputQuery, results);
	}
}
