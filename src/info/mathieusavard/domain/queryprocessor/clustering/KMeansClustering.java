package info.mathieusavard.domain.queryprocessor.clustering;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.WeightedDocument;
import info.mathieusavard.domain.corpus.Corpus;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import info.mathieusavard.technicalservices.BenchmarkRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KMeansClustering {

	private static final int NUMBER_OF_PASS = 5;
	private LinkedList<WeightedDocument> docList = new LinkedList<WeightedDocument>();
	private List<Cluster> clusterList = new ArrayList<Cluster>();
	private int k = 8;
	private DefaultInvertedIndex index;
	private int NUMBER_OF_THREAD = Runtime.getRuntime().availableProcessors();;

	public KMeansClustering(Corpus corpus, DefaultInvertedIndex index) {
		k = findOptimalNumberOfClusters(corpus, index);
		k=15;
		this.index = index;
		System.out.println("Clustering started for " + k + " clusters");
		for (int x=0; x< k; x++)
			clusterList.add(new Cluster("Cluster " + x));
		for (GenericDocument d : corpus) {
			docList.add((WeightedDocument) d);
		}
	}

	public Collection<WeightedDocument> peekAtClusters(int clusterNo, int numberOfDocument) {
		return clusterList.get(clusterNo).subList(0,numberOfDocument);
	}

	public void performClustering() {
		BenchmarkRow clusteringBenchmark = new BenchmarkRow("Clustering");
		clusteringBenchmark.start();
		System.out.println("Clustering: pre-processing the data");
		
		//Pre-process all postings to give them a unique id
		HashMap<String, Integer> termsToUniqueIds = new HashMap<String, Integer>();
		int c = 0;
		for (String s : index) {
			termsToUniqueIds.put(s, c++);
		}

		System.out.println("Initializing each document in a random cluster");
		LinkedList<ClusteringTask> clusteringTask = new LinkedList<ClusteringTask>();
		while (docList.isEmpty() == false) {
			WeightedDocument document = docList.poll();
			if (document.getVector() != null) {
	 			clusterList.get((int) (Math.random()*k)).addDocument(document);
	 			ClusteringTask task = new ClusteringTask(document, clusterList);
	 			clusteringTask.add(task);
			}
	 		
		}
		docList = null;
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);

		
		for (int passNo =1; passNo<= NUMBER_OF_PASS; passNo++) {
			for (Cluster cluster : clusterList) {
				cluster.getCentroid(true);
				cluster.getMembersAndRemove();
			}
			System.out.println("Clustering: pass " + passNo + "/" + NUMBER_OF_PASS);
			try {
				executor.invokeAll(clusteringTask);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		clusteringBenchmark.stop();
		System.out.println(clusteringBenchmark.toString());
	}


	
	//^ Fazli Can, Esen A. Ozkarahan (1990). "Concepts and effectiveness of the cover coefficient-based clustering methodology for text databases". ACM Transactions on Database Systems 15 (4): 483Ð517. doi:10.1145/99935.99938. especially see Section 2.7.
	private int findOptimalNumberOfClusters(Corpus c, DefaultInvertedIndex i) {
		int m = c.size();
		int n = i.size();
		int t = i.getAll().size();
		return (m*n)/t;
	}

	public int getClusterCount() {
		return k;
	}
}
