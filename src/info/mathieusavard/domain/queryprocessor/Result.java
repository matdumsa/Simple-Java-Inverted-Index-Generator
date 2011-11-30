package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.Posting;

import java.util.HashSet;

public class Result implements Comparable<Result> {

	private GenericDocument result;
	private HashSet<Posting> matchesFor;
	
	public Result(GenericDocument result) {
		super();
		this.result = result;
	}

	public Result(GenericDocument result, Posting p) {
		super();
		this.result = result;
		matchesFor = new HashSet<Posting>();
		if (matchesFor.size() == 0)
			throw new RuntimeException("Why are you a result if you don't match for something!");
		matchesFor.add(p);
	}

	public GenericDocument getResult() {
		return result;
	}
	
	
	@Override
	/**
	 * USed for insertion in a sorted array.. the better the result the first you are
	 */
	public int compareTo(Result other) {
			return  new Integer(this.result.getId()).compareTo(other.getResult().getId());
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
