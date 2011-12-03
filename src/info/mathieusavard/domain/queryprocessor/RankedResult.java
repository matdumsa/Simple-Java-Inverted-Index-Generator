package info.mathieusavard.domain.queryprocessor;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.Posting;

public class RankedResult extends Result{

	private double rank;
	
	public RankedResult(GenericDocument document, double rank) {
		super(document);
		this.rank = rank;
	}

	public RankedResult(GenericDocument document, double rank, Posting p) {
		super(document, p);
		this.rank = rank;
	}

	public double getRank() {
		return rank;
	}
	
	
	@Override
	/**
	 * USed for insertion in a sorted array.. the better the result the first you are
	 */
	public int compareTo(Result other) {
		if (other instanceof RankedResult){
			RankedResult rankOther = (RankedResult)other;
			if (rankOther.getRank() == this.rank)
				return  new Integer(this.getDocument().getId()).compareTo(other.getDocument().getId());
			return Double.compare(rankOther.getRank(),this.rank);
		} else{
			return -1;
		}
		
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Result))
			return false;
		return (((Result) o).getDocument().getId() == getDocument().getId());
	}
	
}
