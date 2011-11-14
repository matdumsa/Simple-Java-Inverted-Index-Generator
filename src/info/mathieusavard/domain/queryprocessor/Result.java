package info.mathieusavard.domain.queryprocessor;

import java.util.HashSet;

import info.mathieusavard.domain.Document;
import info.mathieusavard.domain.Posting;

public class Result implements Comparable<Result> {

	private Document result;
	private double rank;
	private HashSet<Posting> matchesFor;
	
	public Result(Document result, double rank) {
		super();
		this.result = result;
		this.rank = rank;
	}

	public Result(Document result, double rank, Posting p) {
		super();
		this.result = result;
		this.rank = rank;
		matchesFor = new HashSet<Posting>();
		matchesFor.add(p);
	}

	public Document getResult() {
		return result;
	}
	public double getRank() {
		return rank;
	}
	
	
	@Override
	/**
	 * USed for insertion in a sorted array.. the better the result the first you are
	 */
	public int compareTo(Result other) {
		return Double.compare(other.rank,this.rank);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Result))
			return false;
		return (((Result) o).getResult().getId() == getResult().getId());
	}
	public void addPosting(Posting posting) {
		if (matchesFor == null)
			matchesFor = new HashSet<Posting>();
		matchesFor.add(posting);
	}
	
	
}
