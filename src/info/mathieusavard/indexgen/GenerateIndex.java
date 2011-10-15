package info.mathieusavard.indexgen;
import java.util.ArrayList;


public class GenerateIndex {

	private static final int NUMBER_OF_WORKER_THREADS = 3;
	private static final String DEFAULT_DIR = "reut";
	private static final String DEFAULT_EXTENSION = ".xml";

	
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Usage: GenerateIndex pathToFiles [extension]");
		}

		Benchmark benchmark = new Benchmark();
		benchmark.startTimer("total");
		String directory = (args.length > 0) ? args[0] : DEFAULT_DIR;
		String extension = (args.length > 1) ? args[1] : DEFAULT_EXTENSION;
		
		//Open all files
		ArrayList<TokenizerThread> pool = new ArrayList<TokenizerThread>();
		ArrayList<ArticleCollection> documentCollection = new ArrayList<ArticleCollection>();
		for (String documentName : Utils.getAllFiles(directory, extension, false)) {
				ArticleCollection d = new ArticleCollection(documentName);
				documentCollection.add(d);
		} // end of the for all files loop

		final int DOC_PER_THREAD = documentCollection.size() / NUMBER_OF_WORKER_THREADS;

		benchmark.startTimer("starting-thread");
		for (int x=0; x<NUMBER_OF_WORKER_THREADS; x++) {
			String tName = "Worker-" + x;
			int idx_from = x*DOC_PER_THREAD;
			int idx_to = x*DOC_PER_THREAD+DOC_PER_THREAD;
			System.out.println("Launching thread " + tName + " with" + idx_from + ".." + idx_to);
			if (x==NUMBER_OF_WORKER_THREADS-1)
					idx_to = documentCollection.size();
			TokenizerThread t1 = new TokenizerThread(tName, documentCollection.subList(idx_from, idx_to));
			t1.start();
			pool.add(t1);
		}
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
		
		//Time to write the index to a file
		benchmark.startTimer("writing-to-file");
		IPostingList finalIndex = SPIMIPostingList.reconcile();
		benchmark.stopTimer("writing-to-file");


		benchmark.stopTimer("total");
		
		// Display some statistics
		System.out.println("The inverted index has " + finalIndex.size() + " token");
		
		benchmark.reportOnAllTimer();
	}

}
