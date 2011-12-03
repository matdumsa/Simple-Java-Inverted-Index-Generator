package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.corpus.CorpusFactory;
import info.mathieusavard.domain.queryprocessor.spelling.Spelling;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class ResultSet implements Iterable<Result>{

	Collection<Result> results = null;
	private String userInputQuery = null;
	private String suggestedQuery = null;
	private String compressedQuery = null;
	// clusters /Êclasses blabla for future use

	public String getCompressedQuery() {
		return compressedQuery;
	}

	public String getUserInputQuery() {
		return userInputQuery;
	}
	
	public ResultSet(String userInputQuery, String compressedInputQuery ,Collection<Posting> results) {
		super();
		this.userInputQuery = userInputQuery;
		this.compressedQuery = compressedInputQuery;
		if (results.size()==0) {
			suggestedQuery = createSuggestedQuery(userInputQuery);
		} else{
			this.results = new LinkedList<Result>();
			this.results.addAll(generateResult(results));
		}
	}
	
	private String createSuggestedQuery(String originalQuery){
		String[] words = originalQuery.split(" ");
		String result = "";
		Spelling spell;
		try {
			spell = new Spelling("big.txt");
			for (int i = 0; i < words.length; i++){
				result = result + spell.correct(words[i])  + " ";
			}
			this.results = generateResult(QueryProcessor.findMatchingPostingId(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static Collection<Result> generateResult(Collection<Posting> matchingDocument) {
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
