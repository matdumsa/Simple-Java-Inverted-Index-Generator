package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.corpus.CorpusFactory;

import java.util.Collection;
import java.util.HashSet;
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
	
	public ResultSet(String userInputQuery, Collection<Posting> results) {
		super();
		this.userInputQuery = userInputQuery;
		//if (results.size()==0) suggestedQuery
		this.results = new LinkedList<Result>();
		this.results.addAll(generateResult(userInputQuery, results));
	}
	
	private static Collection<Result> generateResult(String queryPositiveTerms, Collection<Posting> matchingDocument) {
			HashSet<Result> resultSet = new HashSet<Result>();
			for (Posting p : matchingDocument) 
				resultSet.add(new Result(CorpusFactory.getCorpus().findArticle(p.getDocumentId())));
			return resultSet;
	}
	
	public String getSuggestedQuery() {
		return suggestedQuery;
	}

	@Override
	public Iterator<Result> iterator() {
		return results.iterator();
	}
	
	public int size() {
		if (results == null){
			return 0;
		} else {
			return results.size();	
		}
	}
}
