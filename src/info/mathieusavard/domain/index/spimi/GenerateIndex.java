package info.mathieusavard.domain.index.spimi;
import info.mathieusavard.domain.Corpus;
import info.mathieusavard.domain.index.ParsableArticleCollection;
import info.mathieusavard.domain.index.TokenizerThread;
import info.mathieusavard.domain.index.XMLSharding;
import info.mathieusavard.technicalservices.Benchmark;
import info.mathieusavard.technicalservices.Constants;
import info.mathieusavard.technicalservices.Property;
import info.mathieusavard.technicalservices.Utils;

import java.util.ArrayList;
import java.util.Stack;


public class GenerateIndex {

	private static final int NUMBER_OF_WORKER_THREADS = Property.getInt("numOfThreads");
	private static final String DEFAULT_DIR = Constants.basepath + "/reut";
	private static final String DEFAULT_EXTENSION = ".xml";

	
	public static void main(String[] args) {

		Benchmark benchmark = new Benchmark();
		benchmark.startTimer("total");
		String directory = (args.length > 0) ? args[0] : DEFAULT_DIR;
		String extension = (args.length > 1) ? args[1] : DEFAULT_EXTENSION;
		
		//Open all files
		ArrayList<TokenizerThread> pool = new ArrayList<TokenizerThread>();
		Stack<ParsableArticleCollection> documentCollection = new Stack<ParsableArticleCollection>();

		if (Property.getBoolean("forceSharding"))
			XMLSharding.preprocess(directory, directory + "/fragment");

		for (String documentName : Utils.getAllFiles(directory, extension, false)) {
				ParsableArticleCollection d = new ParsableArticleCollection(documentName);
				documentCollection.push(d);
		} // end of the for all files loop

		final int DOC_PER_THREAD = documentCollection.size() / NUMBER_OF_WORKER_THREADS;

		benchmark.startTimer("starting-thread");
		for (int x=0; x<NUMBER_OF_WORKER_THREADS; x++) {
			String tName = "Worker-" + x;

			Stack<ParsableArticleCollection> subDocList = new Stack<ParsableArticleCollection>();
			for (int y=0; y<=DOC_PER_THREAD && documentCollection.isEmpty() == false; y++) {
				subDocList.push(documentCollection.pop());
			}

			TokenizerThread t1 = new TokenizerThread(tName, subDocList);
			t1.start();
			pool.add(t1);
		}
		
		documentCollection = null;
		benchmark.stopTimer("starting-thread");
		
		
		for (TokenizerThread t : pool) {
			try {
				benchmark.startTimer("waiting-thread");
				benchmark.startTimer("waiting-thread" + t.getName());
					t.join();
				benchmark.stopTimer("waiting-thread" + t.getName());
				benchmark.stopTimer("waiting-thread");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//Get rid of the pool and allow garbage collection of their resource
		pool = null;
		System.gc();
		
		//Time to write the index to a file
		benchmark.startTimer("writing-to-file");
		SPIMIReconciliation.reconciliate();
		benchmark.stopTimer("writing-to-file");

		benchmark.stopTimer("total");

		int size = DefaultInvertedIndex.readFromFile("index.txt").size();
		
		// Display some statistics
		System.out.println("The inverted index has been generated with " + size + " tokens");
		
		benchmark.reportOnAllTimer();
	}

}
