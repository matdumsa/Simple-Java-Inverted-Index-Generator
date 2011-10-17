package info.mathieusavard.indexgen;


public class Article {

	private Integer id;
	private String title;
	private String text;
		
	public Article (int id) {
		this.id = id;
		
		//Determine article collection
		int collection = id/1000;
		
		//open this collection
		ArticleCollection ac = new ArticleCollection(Constants.basepath + "reut/reut2-" + Utils.padWithZero(collection, 3) + ".xml");
		Article a = ac.getArticleById(String.valueOf(this.id));
		this.id = a.id;
		this.text = a.text;
		this.title = a.title;
	}

	public Article(int id, String title, String text) {
		this.id = id;
		this.title = title;
		this.text = text;
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
