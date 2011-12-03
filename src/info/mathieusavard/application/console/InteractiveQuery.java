package info.mathieusavard.application.console;

import info.mathieusavard.domain.index.spimi.GenerateIndex;
import info.mathieusavard.domain.queryprocessor.QueryProcessor;
import info.mathieusavard.domain.queryprocessor.RankedResult;
import info.mathieusavard.domain.queryprocessor.Result;
import info.mathieusavard.domain.queryprocessor.ResultSet;
import info.mathieusavard.domain.queryprocessor.booleantree.InvalidQueryException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

public class InteractiveQuery {

	private final int MAX_RESULTS = 999;
	private ResultsPrinter resultPrinter;

	public static void main(String[] args) {
		InteractiveQuery i = new InteractiveQuery();

		i.setResultsPrinter(new ResultsPrinter() {

			@Override
			public void printResult(Result r) {
				if (r instanceof RankedResult){
					RankedResult rR = (RankedResult)r;
					System.out.print(rR.getResult().getId() + " - " + rR.getRank());
					System.out.println("\t" + rR.getResult().getTitle());
				} else
				{
					System.out.print(r.getResult().getId() + " - ");
					System.out.println("\t" + r.getResult().getTitle());
				}
			}});

		i.run();


	}

	public void setResultsPrinter(ResultsPrinter resultsPrinter) {
		this.resultPrinter = resultsPrinter;

	}

	public void run() {
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
	private void performQuery(String query) {
		//We buffer the query result not to load in memory 1000000 article is a generic query is used
		ResultSet resultSet = null;
		try {
			resultSet = QueryProcessor.performBufferedQuery(query);
			if ( resultSet == null) {
				System.out.println("Not found");
				return;
			}
		} catch (InvalidQueryException e) {
			System.out.println("Invalid query");
			return;
		}

		System.out.println("I found " + resultSet.size() + " results in " + QueryProcessor.getMatchingTime()/1000.0 + "seconds");
		if (resultSet.size() > MAX_RESULTS)
			System.out.println("showing the first " + MAX_RESULTS + " results");

		Iterator<Result> iterator = resultSet.iterator();
		while (iterator.hasNext()) {
			Result r =  iterator.next();
			resultPrinter.printResult(r);
		}

		System.out.println("Done, I took " + QueryProcessor.getPullingTime()/1000.0 + "seconds pulling all the articles");

	}
}
