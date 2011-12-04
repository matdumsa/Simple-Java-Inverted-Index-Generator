package info.mathieusavard.domain.queryprocessor.clustering;

import info.mathieusavard.domain.VectorTermSpace;
import info.mathieusavard.domain.WeightedDocument;

import java.util.Collection;
import java.util.LinkedList;

public class Cluster {

	private LinkedList<WeightedDocument> members;
	private String name;
	private VectorTermSpace cachedCentroid = null;


	public String getName() { return name; }

	public Cluster(String name) {
		this.name = name;
		members = new LinkedList<WeightedDocument>();
	}
	
	/**
	 * 
	 * @return Return the document that is the farther from the centroid and remove it from the collection
	 */
	public WeightedDocument poll() {
		return members.pollFirst();
	}

	public void addDocument(WeightedDocument d) {
		members.add(d);
	}

	public VectorTermSpace getCentroid() {
		if (cachedCentroid != null)
			return cachedCentroid;
		VectorTermSpace centroid = new VectorTermSpace();
		for (WeightedDocument d : members) {
			if (d != null)
				centroid = d.getVector().add(centroid);
		}
		centroid.divideBy(new Double(members.size()));
		cachedCentroid = centroid;
		return centroid;
	}
	
	public VectorTermSpace getCentroid(boolean force) {
		cachedCentroid = null;
		return getCentroid();
	}

	public Collection<WeightedDocument> subList(int i, int numberOfDocument) {
		if (members.size() < numberOfDocument)
			return members;
		return members.subList(i, numberOfDocument);
		
	}
	
	public LinkedList<WeightedDocument> getMembersAndRemove() {
		LinkedList<WeightedDocument> results = members;
		members = new LinkedList<WeightedDocument>();
		return results;
	}


}
