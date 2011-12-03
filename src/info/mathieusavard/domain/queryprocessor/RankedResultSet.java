package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.corpus.CorpusFactory;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;

import java.util.Collection;
import java.util.TreeSet;

public class RankedResultSet extends ResultSet {

	private static DefaultInvertedIndex index = DefaultInvertedIndex.readFromFile("index.txt");
	
	public RankedResultSet(String userInputQuery, Collection<Posting> results) {
		super(userInputQuery, results);
		super.results = generateResult(userInputQuery, results); //Here we should assign a RANKED LIST to super.results.
	}
	
	private static Collection<Result> generateResult(String queryPositiveTerms, Collection<Posting> matchingDocument) {
			TreeSet<Result> results = new TreeSet<Result>();
			// Looking to rank each document in regards to query positive terms.
			for (Posting p : matchingDocument) {
				RankedResult result = makeRank(CorpusFactory.getCorpus().findArticle(p.getDocumentId()),queryPositiveTerms);
				results.add(result);

			}
			System.out.println("Done ranking " +matchingDocument.size() +":"+ results.size() + " results");
			return results;
	}

	//This methods applies Okapi BM25
	private static RankedResult makeRank(GenericDocument abstractDocument, String queryPositiveTerms) {
		double N = CorpusFactory.getCorpus().size();	//corpus size
		double k1 = 1.5;
		double b = 0.75;
		double avgDl = CorpusFactory.getCorpus().getTotalLength()/N;
		double result =0;
		for (String term : queryPositiveTerms.split(" ")) {
			double numberOfDocumentContainingT = index.getSet(term).size();
			double idfQI = Math.log((N - numberOfDocumentContainingT + 0.5)/(numberOfDocumentContainingT+0.5));
			double termFrequencyInDocument = 0;
			// Looking for termFrequencyInDocument
			for (Posting p : index.getSet(term))
				if (p.getDocumentId() == abstractDocument.getId()){
					termFrequencyInDocument = p.getOccurence();
				}
			double top = termFrequencyInDocument*(k1+1);
			double bottom = termFrequencyInDocument+k1*(1-b+b*(abstractDocument.getLengthInWords()/avgDl));
			result += idfQI*(top/bottom);
		}
		return new RankedResult(abstractDocument, result);
	}

	
}
