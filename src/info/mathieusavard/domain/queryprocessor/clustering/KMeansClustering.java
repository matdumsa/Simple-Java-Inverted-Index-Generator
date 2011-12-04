package info.mathieusavard.domain.queryprocessor.clustering;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.WeightedDocument;
import info.mathieusavard.domain.corpus.Corpus;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import info.mathieusavard.technicalservices.BenchmarkRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class KMeansClustering {

	private static final int NUMBER_OF_PASS = 5;
	private LinkedList<WeightedDocument> docList = new LinkedList<WeightedDocument>();
	private List<Cluster> clusterList = new ArrayList<Cluster>();

	public KMeansClustering(Corpus corpus, DefaultInvertedIndex index) {
		int k = findOptimalNumberOfClusters(corpus, index);
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
		System.out.println("Initializing each document in a random cluster");

		while (docList.isEmpty() == false) {
			WeightedDocument document = docList.poll();
			if (document.getVector() != null)
				clusterList.get((int) (Math.random()*5)).addDocument(document);
		}

		for (int x=1; x<=NUMBER_OF_PASS; x++) {
			for (Cluster c : clusterList) {
				//Recompute centroid location and remove all documents from cluster for re-asssignment
				c.getCentroid(true);
				docList.addAll(c.getMembersAndRemove());
			}

			System.out.println("Pass " + x + " of " + NUMBER_OF_PASS);
			while (docList.isEmpty() == false) {
				//Pick a document
				WeightedDocument document = docList.poll();

				//Reassign it
				findClosestCluster(document).addDocument(document);
			}
		}
		
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
