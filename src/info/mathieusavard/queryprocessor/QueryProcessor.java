package info.mathieusavard.queryprocessor;

import info.mathieusavard.indexgen.Article;
import info.mathieusavard.indexgen.ArticleCollection;
import info.mathieusavard.indexgen.BenchmarkRow;
import info.mathieusavard.indexgen.DefaultPostingList;
import info.mathieusavard.indexgen.TokenizerThread;
import info.mathieusavard.utils.SetOperation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class QueryProcessor {

	private static DefaultPostingList postingList = DefaultPostingList.readFromFile("index.txt");
	
	private static Set<Integer> matchingDocId;
	private static Iterator<Integer> matchedIterator;
	private static BenchmarkRow matchingTime = new BenchmarkRow(null);
	private static BenchmarkRow pullingArticletime = new BenchmarkRow(null);
	
	public static long getMatchingTime() {
		return matchingTime.getDuration();
	}

	public static long getPullingTime() {
		return pullingArticletime.getDuration();
	}

	public static List<Article> performQuery(String query) {
		List<Article> result = new ArrayList<Article>();
		matchingDocId = findMatchingPostingId(query);
		if (matchingDocId == null || matchingDocId.isEmpty())
			return result;
		else {
			for (int idxid : matchingDocId) {
				result.add(new Article(idxid));
			}
			return result;
		}
	}
	
	public static boolean performBufferedQuery(String query) {
		ArticleCollection.ENABLE_DOM_CACHING = true;
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
			a = new Article(matchedIterator.next());
		else
			a = null;
		pullingArticletime.stop();
		return a;
	}
	
	public static int size() {
		return matchingDocId.size();
	}
	
	private static Set<Integer> findMatchingPostingId(String query) {
		matchingTime.start();
		//Tokenize query
		StringTokenizer st = new StringTokenizer(query);
		ArrayList<String> tokenList = new ArrayList<String>();
		TokenizerThread tt = new TokenizerThread();
		while (st.hasMoreTokens()) {	
			String token = tt.compressToken(st.nextToken());
			if (token != null)
				tokenList.add(token);
		}

		Set<Integer> resultSet = null;
		for (String token : tokenList) {
			if (resultSet == null) //first pass, look token list
			{
				resultSet = postingList.get(token);
				if (resultSet == null) {
					matchingTime.stop();
					return null; // not found					
				}
			}
			
			resultSet = SetOperation.intersection(resultSet, postingList.get(token));
			if (resultSet == null || resultSet.isEmpty()) {
				resultSet = null;
				break;
			}
		}
		
		matchingTime.stop();
		return resultSet;
	}


}
