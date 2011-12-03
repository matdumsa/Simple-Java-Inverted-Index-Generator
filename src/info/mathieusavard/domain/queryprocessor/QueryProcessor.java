package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.index.IndexerThread;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import info.mathieusavard.domain.queryprocessor.booleantree.InfixToPostfix;
import info.mathieusavard.domain.queryprocessor.booleantree.InvalidQueryException;
import info.mathieusavard.domain.queryprocessor.booleantree.QueryTree;
import info.mathieusavard.domain.queryprocessor.booleantree.QueryTreeBuilder;
import info.mathieusavard.technicalservices.BenchmarkRow;
import info.mathieusavard.technicalservices.Property;

import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class QueryProcessor {

	private static DefaultInvertedIndex index = DefaultInvertedIndex.readFromFile("index.txt");

	private static Set<Posting> matchingDocId;
	private static BenchmarkRow matchingTime;
	private static BenchmarkRow pullingArticletime = new BenchmarkRow(null);
	
	private static String queryPositiveTerms;

	public static long getMatchingTime() {
		return matchingTime.getDuration();
	}

	public static long getPullingTime() {
		return pullingArticletime.getDuration();
	}

	public static ResultSet performBufferedQuery(String query) throws InvalidQueryException {
		matchingDocId = findMatchingPostingId(query);
		ResultSet result;
		if (!Property.getBoolean("enable_ranking")){
			if (matchingDocId == null)
				return null;
			else{
				result = ResultSetFactory.createResultSet(false, query, matchingDocId);
			}
		}
		else{
			if (matchingDocId == null || matchingDocId.isEmpty() == true)
				return null;
			else{
				result = ResultSetFactory.createResultSet(true, query, matchingDocId);
			}
		}
		return result;
	}

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

		IndexerThread tt = new IndexerThread("Query compressor");
		StringTokenizer st = new StringTokenizer(query,"^-+()", true);
		String compressedQuery = "";
		while (st.hasMoreTokens()) {	
			String token = st.nextToken();
			if (token.equals("^") || token.equals("-") || token.equals("+") || token.equals("(") || token.equals(")"))
				compressedQuery = compressedQuery+token;
			else
				compressedQuery = compressedQuery+tt.compressToken(token);
		}
		return compressedQuery;
	}
	public static Set<Posting> findMatchingPostingId(String query) throws InvalidQueryException {
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

	public static DefaultInvertedIndex getIndex() {
		return index;
	}


}
