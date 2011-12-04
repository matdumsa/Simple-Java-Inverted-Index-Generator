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

import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.SparseInstance;

public class KMeansClustering {

	private static final int NUMBER_OF_PASS = 5;
	private LinkedList<WeightedDocument> docList = new LinkedList<WeightedDocument>();
	private List<Cluster> clusterList = new ArrayList<Cluster>();
	private int k = 8;
	private DefaultInvertedIndex index;
	
	public KMeansClustering(Corpus corpus, DefaultInvertedIndex index) {
//		int k = findOptimalNumberOfClusters(corpus, index);
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

		
		KMeans clusterer = new KMeans(8,NUMBER_OF_PASS);		
		DefaultDataset clusteringDataset = new DefaultDataset();
		
		//Pre-process all postings to give them a unique id
		HashMap<String, Integer> termsToUniqueIds = new HashMap<String, Integer>();
		int c = 0;
		for (String s : index) {
			termsToUniqueIds.put(s, c++);
		}
		
		while (docList.isEmpty() == false) {
			WeightedDocument wd = docList.poll();
			//Creating a new SparseInstance with index.size dimensions, the non-specified dimensions will have the value 0
			SparseInstance i = new SparseInstance(index.size(), 0.0);
			if (wd.getVector() == null)
				continue;
			i.putAll(wd.getVector().getVector());
			clusteringDataset.add(i);
		}

		System.out.println("Clustering...");

		clusterer.cluster(clusteringDataset);

		
		clusteringBenchmark.stop();
		System.out.println(clusteringBenchmark.toString());
	}

	private Cluster findClosestCluster(WeightedDocument d) {
		Cluster closest = null;
		Double closestDistance = Double.MAX_VALUE;
		for (Cluster cluster : clusterList) {
			Double distance = cluster.getCentroid().getDistanceFromVector(d.getVector());
			if (distance < closestDistance) {
				closestDistance = distance;
				closest = cluster;
			}
		}
		return closest;
	}
	
	//^ Fazli Can, Esen A. Ozkarahan (1990). "Concepts and effectiveness of the cover coefficient-based clustering methodology for text databases". ACM Transactions on Database Systems 15 (4): 483Ð517. doi:10.1145/99935.99938. especially see Section 2.7.
	private int findOptimalNumberOfClusters(Corpus c, DefaultInvertedIndex i) {
		int m = c.size();
		int n = i.size();
		int t = i.getAll().size();
		return (m*n)/t;
	}
}
