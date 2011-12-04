package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.index.IndexerThread;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import info.mathieusavard.domain.queryprocessor.booleantree.InfixToPostfix;
import info.mathieusavard.domain.queryprocessor.booleantree.InvalidQueryException;
import info.mathieusavard.domain.queryprocessor.booleantree.QueryTree;
import info.mathieusavard.domain.queryprocessor.booleantree.QueryTreeBuilder;
import info.mathieusavard.domain.queryprocessor.spelling.Spelling;
import info.mathieusavard.technicalservices.BenchmarkRow;
import info.mathieusavard.technicalservices.Property;

import java.util.Set;
import java.util.StringTokenizer;

public class QueryProcessor {

	private static DefaultInvertedIndex index = DefaultInvertedIndex.readFromFile("index.txt");

	private static Set<Posting> matchingDocId;
	private static BenchmarkRow matchingTime;
	private static BenchmarkRow pullingArticletime = new BenchmarkRow(null);
	private static IndexerThread queryCompressor = new IndexerThread("Query Compressor");
	
	private static String queryPositiveTerms;

	public static long getMatchingTime() {
		return matchingTime.getDuration();
	}

	public static long getPullingTime() {
		return pullingArticletime.getDuration();
	}

	public static ResultSet performQuery(String query) throws InvalidQueryException {
		return performQuery(query, 1);
	}

	private static ResultSet performQuery(String query, int attempt) throws InvalidQueryException {
		matchingDocId = findPostingForQuery(query);
		String newQuery = null;
		if (matchingDocId.size() == 0) {
			if (attempt > 2)
				return null;
			newQuery = suggestNewQueryBySpellChecking(query);
			System.out.println("Nothing was found for '" + query + "', Ill search for '" + newQuery + "' instead");
				performQuery(newQuery, attempt++);
		}

		ResultSet result;
		if (Property.getBoolean("enable_ranking")){
			result = ResultSetFactory.createResultSet(index, query,compressQuery(query), queryPositiveTerms, matchingDocId);
		}
		else{
			result = ResultSetFactory.createResultSet(index, query, compressQuery(query),matchingDocId);
		}
		result.setSuggestedQuery(newQuery);
		return result;
	}

	/**
	 * Takes a user input query and compress it the way the corpus is compressed at indexation time
	 * @param query
	 * @return
	 */
	private static String compressQuery(String query) {
		query = query.trim();

		query = query.replace("( ", "(");
		query = query.replace(" )", ")");
		query = query.replace(" NOT", " not");
		query = query.replace("NOT ", "not ");
		query = query.replace(" AND", " and");
		query = query.replace("AND ", "and ");
		query = query.replace(" OR", " or");
		query = query.replace("OR ", "or ");
		query = query.replace(" and not ", "^-");
		query = query.replace(" and not", "^-");
		query = query.replace(" or not ", "+-");
		query = query.replace(" or not", "+-");
		query = query.replace(" and ", "^");
		query = query.replace(" and", "^");
		query = query.replace("(not", "(-");

		query = query.replace(" or", "+");
		query = query.replace("^ ", "^");
		query = query.replace("- ", "-");
		query = query.replace("+ ", "+");
		query = query.replace(" not ", "^-");
		query = query.replace(" not", "^-");
		query = query.replace("not ", "-");

		if (Property.getBoolean("DefaultToOr") == true)
			//Setting the default boolean operator between words to OR
			query = query.replace(" ", "+");
		else
			//Setting the default boolean operator between words to AND
			query = query.replace(" ", "^");

		StringTokenizer st = new StringTokenizer(query,"^-+()", true);
		String compressedQuery = "";
		while (st.hasMoreTokens()) {	
			String token = st.nextToken();
			if (token.equals("^") || token.equals("-") || token.equals("+") || token.equals("(") || token.equals(")"))
				compressedQuery = compressedQuery+token;
			else
				compressedQuery = compressedQuery+queryCompressor.compressToken(token);
		}
		return compressedQuery;
	}
	
	public static Set<Posting> findPostingForQuery(String query) throws InvalidQueryException {
		matchingTime = new BenchmarkRow(null);
		matchingTime.start();
		String compressedQuery = compressQuery(query);
		System.out.println(compressedQuery);
		String postfixRepresentation = InfixToPostfix.doTrans(compressedQuery);
		QueryTree qt = QueryTreeBuilder.getTree(postfixRepresentation);
		Set<Posting> resultSet = qt.getResult(index);
		queryPositiveTerms = qt.getAllMatchedTerms();
		matchingTime.stop();
		
		return resultSet;
	}


	private static String suggestNewQueryBySpellChecking(String originalQuery){
		String[] words = originalQuery.split(" ");
		String result = "";
		Spelling spell;
		try {
			spell = new Spelling(index);
			for (int i = 0; i < words.length; i++){
				result = result + spell.correct(words[i])  + " ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static DefaultInvertedIndex getIndex() {
		return index;
	}

}
