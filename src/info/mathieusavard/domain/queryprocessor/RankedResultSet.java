package info.mathieusavard.domain.queryprocessor;

import java.util.Collection;

public class RankedResultSet extends ResultSet {

	public RankedResultSet(String userInputQuery, Collection<Result> results) {
		super(userInputQuery, results);

		super.results = super.results; //Here we should assign a RANKED LIST to super.results.
	}

}
