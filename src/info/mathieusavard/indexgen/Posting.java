package info.mathieusavard.indexgen;

public class Posting implements Comparable<Posting> {

	private int documentId;
	private int occurence;

	public Posting(int documentId, int occurence) {
		super();
		this.documentId = documentId;
		this.occurence = occurence;
	}

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public int getOccurence() {
		return occurence;
	}

	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}
	
	public void add(int add) {
		occurence += add;
	}
	
	@Override
	public int hashCode() {
		return getDocumentId();
	}
	@Override
	public boolean equals(Object o) {
		if ((o instanceof Posting) == false)
			return false;
		Posting otherPosting = (Posting) o;
		if (otherPosting.getDocumentId() == getDocumentId())
			return true;
		else
			return false;
	}
	
	
	public static Posting fromString(String input) {
		String[] parts =input.split(":");
		int documentId = Integer.parseInt(parts[0], Character.MAX_RADIX);
		int occurence = Integer.parseInt(parts[1], Character.MAX_RADIX);
		return new Posting(documentId, occurence);
	}
	
	public String toString() {
		return Integer.toString(getDocumentId(), Character.MAX_RADIX) + ":" + Integer.toString(getOccurence(), Character.MAX_RADIX);
	}

	@Override
	public int compareTo(Posting p) {
		return new Integer(this.getDocumentId()).compareTo(p.getDocumentId());
	}
}
