package info.mathieusavard.domain.corpus;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.ThreadTFIDF;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import info.mathieusavard.technicalservices.BenchmarkRow;
import info.mathieusavard.technicalservices.Pair;

import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;



/**
 * @author jeremiemartinez
 *
 */
public class WeightedCorpus extends Corpus {
	
	private DefaultInvertedIndex index;  
	private Stack<ThreadTFIDF> pool = new Stack<ThreadTFIDF>();
	private int NUMBER_OF_THREAD = 4;
	//Default constructor allow only the factory in this package to create instances
	public WeightedCorpus() {
		super();
	}
	
	
	public void closeIndex(){
		computeTFIDFVector();
		super.closeIndex();
	}

	private void computeTFIDFVector() {
		BenchmarkRow bench = new BenchmarkRow("Generating TFIFD");
		bench.start();
		
		index = DefaultInvertedIndex.readFromFile("index.txt");
		if (index.validate() == false)
			throw new RuntimeException("Invalid index, cannot compute TFIDF on an invalid index");
		TreeMap<GenericDocument, LinkedList<Posting>> data = index.getDocumentBasedIndex();
		System.out.println("Starting TF-IDF computing");
		LinkedBlockingQueue<Pair<GenericDocument, LinkedList<Posting>>> workToDo = new LinkedBlockingQueue<Pair<GenericDocument, LinkedList<Posting>>>();
		
		for (GenericDocument gd : data.keySet()) {
			Pair<GenericDocument, LinkedList<Posting>> pair = new Pair<GenericDocument, LinkedList<Posting>>(gd, data.get(gd));
			workToDo.add(pair);
		}
		
		for (int i = 0; i < NUMBER_OF_THREAD; i++){
			ThreadTFIDF thread = new ThreadTFIDF(workToDo, index, this.size());
			pool.add(thread);
			thread.start();
		}
		for (ThreadTFIDF t : pool){
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Computing TF-IDF finished");
		bench.stop();
		System.out.println(bench.toString());
	}


}
