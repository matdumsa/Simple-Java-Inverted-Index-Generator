import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class GenerateIndex {

	private static final int NUMBER_OF_WORKER_THREADS = 2;
	private static final String DEFAULT_DIR = "reut";
	private static final String DEFAULT_EXTENSION = ".sgm";

	
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Usage: GenerateIndex pathToFiles [extension]");
		}

		Benchmark benchmark = new Benchmark();
		benchmark.startTimer("total");
		String directory = (args.length > 0) ? args[0] : DEFAULT_DIR;
		String extension = (args.length > 1) ? args[1] : DEFAULT_EXTENSION;

		//Step 1: get a list of all files
		String[] files = Utils.getAllFiles(directory, extension, true);
		
		//Open all files
		ArrayList<TokenizerThread> pool = new ArrayList<TokenizerThread>();
		ArrayList<Document> documentCollection = new ArrayList<Document>();
		for (String documentName : files) {
				String fileID = documentName.replace("reut2-00", "").replace("reut2-0", "").replace(extension, "");
				Document d = new Document(directory + "/" + documentName, Integer.parseInt(fileID));
				documentCollection.add(d);
		} // end of the for all files loop

		final int DOC_PER_THREAD = documentCollection.size() / NUMBER_OF_WORKER_THREADS;
		InvertedIndex finalIndex = new InvertedIndex();

		benchmark.startTimer("starting-thread");
		for (int x=0; x<NUMBER_OF_WORKER_THREADS; x++) {
			String tName = "Worker-" + x;
			int idx_from = x*DOC_PER_THREAD;
			int idx_to = x*DOC_PER_THREAD+DOC_PER_THREAD;
			System.out.println("Launching thread " + tName + " with" + idx_from + ".." + idx_to);
			if (x==NUMBER_OF_WORKER_THREADS-1)
					idx_to = documentCollection.size();
			TokenizerThread t1 = new TokenizerThread(tName, documentCollection.subList(idx_from, idx_to), finalIndex);
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

		
		//Time to write the index to a file
		benchmark.startTimer("writing-to-file");
		try {
			FileWriter fstream = new FileWriter("index.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			// For each token of the index
			System.out.println("Writing index file");
			for (String token : finalIndex) {
				// Obtain the list of document that contain this token in a string
				String docList = finalIndex.get(token).toString();
				// Write to the index file
				out.write(token + "->" + docList + "\n");
			}
			// Close the index file.
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		benchmark.stopTimer("writing-to-file");


		benchmark.stopTimer("total");
		
		// Display some statistics
		System.out.println("The inverted index has " + finalIndex.size() + " token");
		
		benchmark.reportOnAllTimer();
	}

}
