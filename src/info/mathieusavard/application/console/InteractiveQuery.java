package info.mathieusavard.application.console;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.index.spimi.GenerateIndex;
import info.mathieusavard.domain.queryprocessor.QueryProcessor;
import info.mathieusavard.domain.queryprocessor.Result;
import info.mathieusavard.domain.queryprocessor.booleantree.InvalidQueryException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InteractiveQuery {

	private static final int MAX_RESULTS = 999;
	public static void main(String[] args) {

		String query = ""; // Line read from standard in
		
		System.out.println("Enter a search query (type 'quit' to exit and 'index' to re-index): ");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);

		
		while (!(query.equals("quit"))){
			try {
				query = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (query.equals("index")) {
				GenerateIndex.main(new String[] {});
			}
			else if (query.equals("getDocumentBasedIndex"))
				QueryProcessor.getIndex().getDocumentBasedIndex();
			else if (!(query.equals("quit"))){
				performQuery(query);
			}
			System.out.println("Enter a search query (type 'quit' to exit and 'index' to re-index): ");

		}
		
		System.out.println("Bye-bye");

	}
	
	private static void performQuery(String query) {
		//We buffer the query result not to load in memory 1000000 article is a generic query is used
		try {
			if (QueryProcessor.performBufferedQuery(query) == false) {
				System.out.println("Not found");
				return;
			}
		} catch (InvalidQueryException e) {
			System.out.println("Invalid query");
			return;
		}

		System.out.println("I found " + QueryProcessor.size() + " results in " + QueryProcessor.getMatchingTime()/1000.0 + "seconds");
		if (QueryProcessor.size() > MAX_RESULTS)
			System.out.println("showing the first " + MAX_RESULTS + " results");
			
		while (QueryProcessor.hasNext()) {
			Result r =  QueryProcessor.next();
			GenericDocument a = r.getResult();
			System.out.print(a.getId() + " - " + r.getRank());
			System.out.println("\t" + a.getTitle());
		}

		System.out.println("Done, I took " + QueryProcessor.getPullingTime()/1000.0 + "seconds pulling all the articles");

	}
}
