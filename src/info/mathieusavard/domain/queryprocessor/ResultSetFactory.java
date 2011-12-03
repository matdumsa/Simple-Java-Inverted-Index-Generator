package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;

import java.util.Collection;

public class ResultSetFactory {

	/**
	 * Generate a result set to be shown to the user
	 * @param ranked, true means that we will rank the resultset
	 * @param userInputQuery, the original query, as types by the user
	 * @param compressedInputQuery, the compressed query according to property file compression rule
	 * @param results a collection of posting
	 * @return the resultset, ranked or not according to usage.
	 */
	public static ResultSet createResultSet(DefaultInvertedIndex index, String userInputQuery, String compressedInputQuery, Collection<Posting> results) {
		return new ResultSet(index, userInputQuery,compressedInputQuery, results);
	}

	/**
	 * Generate a result set to be shown to the user
	 * @param ranked, true means that we will rank the resultset
	 * @param userInputQuery, the original query, as types by the user
	 * @param compressedInputQuery, the compressed query according to property file compression rule
	 * @param rankAccordingToQuery, the query used for ranking purposes (generally the compressed positive term of the compressed query.. A+B-C will give A+B)
	 * @param results a collection of posting
	 * @return the resultset, ranked or not according to usage.
	 */
	public static ResultSet createResultSet(DefaultInvertedIndex index, String userInputQuery, String compressedInputQuery, String rankAccordingToQuery, Collection<Posting> results) {
			return new RankedResultSet(index, userInputQuery,compressedInputQuery,  rankAccordingToQuery, results);
	}

	
}
