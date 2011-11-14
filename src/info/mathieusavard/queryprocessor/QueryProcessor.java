package info.mathieusavard.queryprocessor;

import info.mathieusavard.arithmetictree.InToPost;
import info.mathieusavard.arithmetictree.InvalidQueryException;
import info.mathieusavard.arithmetictree.QueryTree;
import info.mathieusavard.arithmetictree.QueryTreeBuilder;
import info.mathieusavard.indexgen.Article;
import info.mathieusavard.indexgen.ArticleFactory;
import info.mathieusavard.indexgen.BenchmarkRow;
import info.mathieusavard.indexgen.DefaultInvertedIndex;
import info.mathieusavard.indexgen.Posting;
import info.mathieusavard.indexgen.TokenizerThread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class QueryProcessor {

	private static DefaultInvertedIndex postingList = DefaultInvertedIndex.readFromFile("index.txt");

	private static Set<Posting> matchingDocId;
	private static Iterator<Posting> matchedIterator;
	private static BenchmarkRow matchingTime;
	private static BenchmarkRow pullingArticletime = new BenchmarkRow(null);

	public static long getMatchingTime() {
		return matchingTime.getDuration();
	}

	public static long getPullingTime() {
		return pullingArticletime.getDuration();
	}

	public static List<Article> performQuery(String query) throws InvalidQueryException {
		List<Article> result = new ArrayList<Article>();
		matchingDocId = findMatchingPostingId(query);
		if (matchingDocId == null || matchingDocId.isEmpty())
			return result;
		else {
			for (Posting idxid : matchingDocId) {
				result.add(ArticleFactory.findArticle(idxid.getDocumentId()));
			}
			return result;
		}
	}

	public static boolean performBufferedQuery(String query) throws InvalidQueryException {
		matchingDocId = findMatchingPostingId(query);
		if (matchingDocId == null || matchingDocId.isEmpty() == true)
			return false;
		matchedIterator = matchingDocId.iterator();
		return true;
	}

	public static boolean hasNext() {
		return matchedIterator.hasNext();
	}
	public static Article next() {
		pullingArticletime.start();
		Article a;
		if (matchedIterator.hasNext())
			a = ArticleFactory.findArticle(matchedIterator.next().getDocumentId());
		else
			a = null;
		pullingArticletime.stop();
		return a;
	}

	public static int size() {
		if (matchingDocId == null)
			return 0;
		return matchingDocId.size();
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
		query = query.replace(" ", "^");


		TokenizerThread tt = new TokenizerThread();
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
	private static Set<Posting> findMatchingPostingId(String query) throws InvalidQueryException {
		matchingTime = new BenchmarkRow(null);
		matchingTime.start();
		String compressedQuery = compressQuery(query);
		System.out.println(compressedQuery);
		String postfixRepresentation = InToPost.doTrans(compressedQuery);
		QueryTree qt = QueryTreeBuilder.getTree(postfixRepresentation);
		Set<Posting> resultSet = qt.getResult(postingList);
		matchingTime.stop();
		return resultSet;
	}


}
