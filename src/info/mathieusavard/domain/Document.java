package info.mathieusavard.domain;

import info.mathieusavard.domain.index.DiskReaderThread;



public class Document {

	private Integer id;
	private String title;
	private String text;
		

	public Document(int id, String title, String text) {
		this.id = id;
		this.title = (title==null)?"??? UNKNOWN TITLE ???" : title.trim().replace('\n', ' ');
		this.text = (text==null)?"???" : text;
	}

	public Document(int id, String title, int length) {
		this.id =id;
		this.title = title;
		this.length = length;
	}

	public int getId() {
		if (id != null) return id;
		return 0;
	}
	
	public String getText() {
		if (text != null) return text;
		//Make article act as a proxy here
		return DiskReaderThread.getArticleById(getId()).getText();
	}
	
	public String getTitle() {
		if (title != null) return title;

		return "";
	}
	
	private int length = -1;
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
	
}
