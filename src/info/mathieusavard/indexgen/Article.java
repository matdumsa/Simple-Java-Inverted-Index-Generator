package info.mathieusavard.indexgen;


public class Article {

	private Integer id;
	private String title;
	private String text;
		

	public Article(int id, String title, String text) {
		this.id = id;
		this.title = (title==null)?"??? UNKNOWN TITLE ???" : title;
		this.text = (text==null)?"???" : text;
	}

	public int getId() {
		if (id != null) return id;
		return 0;
	}
	
	public String getText() {
		if (text != null) return text;
		return "";
	}
	
	public String getTitle() {
		if (title != null) return title;

		return "";
	}
	
}
