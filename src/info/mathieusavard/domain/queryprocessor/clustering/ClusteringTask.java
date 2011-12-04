package info.mathieusavard.domain.queryprocessor.clustering;

import java.util.Collection;
import java.util.concurrent.Callable;

import info.mathieusavard.domain.WeightedDocument;

public class ClusteringTask implements Callable<Object>{

	private WeightedDocument document;
	private Collection<Cluster> clusterList;
	
	public ClusteringTask(WeightedDocument document, Collection<Cluster> clusterList) {
		this.document = document;
		this.clusterList = clusterList;
	}
	
	@Override
	public Object call() {
		findClosestCluster(document).addDocument(document);
		return null;
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
