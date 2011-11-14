package info.mathieusavard.indexgen;

import java.util.StringTokenizer;


public class Article {

	private Integer id;
	private String title;
	private String text;
		

	public Article(int id, String title, String text) {
		this.id = id;
		this.title = (title==null)?"??? UNKNOWN TITLE ???" : title.trim();
		this.text = (text==null)?"???" : text;
	}

	public Article(int id, String title, int length) {
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
		return ArticleFactory.findArticle(getId()).getText();
	}
	
	public String getTitle() {
		if (title != null) return title;

		return "";
	}
	
	private int length = -1;
	public int getLengthInWords() {
		if (length>=0) return length;
		StringTokenizer st = new StringTokenizer(getText());
		length=st.countTokens();
		return length;
	}
	
	/**
	 * Should be called in a crawler when you want to keep only the 
	 */
	public void clearContent() {
		text=null;
	}
	
}
