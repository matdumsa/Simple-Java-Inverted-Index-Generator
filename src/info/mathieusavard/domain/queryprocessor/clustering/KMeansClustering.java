package info.mathieusavard.domain.queryprocessor.clustering;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.WeightedDocument;
import info.mathieusavard.domain.corpus.Corpus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class KMeansClustering {


	private LinkedList<WeightedDocument> docList = new LinkedList<WeightedDocument>();
	private List<Cluster> clusterList = new ArrayList<Cluster>();
	private int corpusSize;

	public KMeansClustering(Corpus corpus, int k) {
		corpusSize = corpus.size();
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
		System.out.println("Initializing clusters with a random document");
		for (Cluster c : clusterList) {
			//Pick a random document
			int randomIndex = (int) (Math.random()*docList.size());
			WeightedDocument documentAtRandom = docList.get(randomIndex);
			docList.remove(randomIndex);
			c.addDocument(documentAtRandom);
		}

		System.out.println("Assigning all documents to a cluster");
		while (docList.size() > 0) {
			WeightedDocument document = docList.pollFirst();
			if (document.getVector() != null)
				findClosestCluster(document).addDocument(document);
		}

		for (int x=0; x<=5; x++) {
			for (Cluster c : clusterList) {
				//Recompute centroid location
				c.getCentroid(true);
			}

			System.out.println("Pass " + x);
			for (int y=0; y<= corpusSize/clusterList.size(); y++) {
				for (Cluster c : clusterList) {
					//Pick a document
					WeightedDocument document = c.poll();

					//Reassign it
					findClosestCluster(document).addDocument(document);
				}
			}
		}
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
}
