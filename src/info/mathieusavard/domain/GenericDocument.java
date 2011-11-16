package info.mathieusavard.domain;

public class GenericDocument {

	protected Integer id;
	protected String title;
	protected String text;
	protected int length = -1;

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public GenericDocument() {
		super();
	}

	public GenericDocument(int id, String title) {
		this.id =id;
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}
	public GenericDocument(int id, String title, int length) {
		this.id =id;
		this.title = title;
		this.length = length;
	}

	public int getLengthInWords() {
		return length;
	}

	public void setLengthInWords(int length) {
		this.length = length;
	}

	/**
	 * Should be called in a crawler when you want to keep only the 
	 */
	public void clearContent() {
		text=null;
	}

	
	public String toString() {
		return getId() + ":" + getLengthInWords() + getTitle();
	}
	
	public static GenericDocument fromString(String input) {
		String[] parts = input.split(":");
		int id = Integer.parseInt(parts[0]);
		int length = Integer.parseInt(parts[1]);
		String title = "???";
		if (parts.length > 2)
			title = parts[2].trim();
		return new GenericDocument(id, title, length);
	}
}