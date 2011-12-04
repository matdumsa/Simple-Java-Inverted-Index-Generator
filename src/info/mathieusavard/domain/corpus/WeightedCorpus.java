package info.mathieusavard.domain.corpus;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.TaskComputeTFIDF;
import info.mathieusavard.domain.WeightedDocument;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import info.mathieusavard.technicalservices.BenchmarkRow;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * @author jeremiemartinez
 *
 */
public class WeightedCorpus extends Corpus {
	
	private DefaultInvertedIndex index;  
	private int NUMBER_OF_THREAD = Runtime.getRuntime().availableProcessors();;
	//Default constructor allow only the factory in this package to create instances
	public WeightedCorpus() {
		super();
	}
	
	
	public void closeIndex(){
		computeTFIDFVector();
		super.closeIndex();
	}

	private void computeTFIDFVector() {
		BenchmarkRow bench = new BenchmarkRow("Generating TFIFD with " + NUMBER_OF_THREAD + " workers");
		bench.start();
		
		index = DefaultInvertedIndex.readFromFile("index.txt");
		if (index.validate() == false)
			throw new RuntimeException("Invalid index, cannot compute TFIDF on an invalid index");
		TreeMap<GenericDocument, LinkedList<Posting>> data = index.getDocumentBasedIndex();
		System.out.println("Starting TF-IDF computing");
		
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
		for (GenericDocument gd : data.keySet()) {
			WeightedDocument document = (WeightedDocument) gd;
			LinkedList<Posting> postingList = data.get(gd);
			executor.submit(new TaskComputeTFIDF(document, postingList, index, super.size()));
		}
		
		executor.shutdown();
		

		System.out.println("Computing TF-IDF finished");
		bench.stop();
		System.out.println(bench.toString());
	}


}
