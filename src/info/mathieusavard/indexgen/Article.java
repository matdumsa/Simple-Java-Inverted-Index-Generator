package info.mathieusavard.indexgen;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Article {

	private Node fragment;
	private int id;
	
	protected Article(Node n) {
		this.fragment = n;
	}
	
	public Article (int id) {
		this.id = id;
		
		//Determine article collection
		int collection = id/1000;
		
		//open this collection
		ArticleCollection ac = new ArticleCollection(Constants.basepath + "reut/reut2-" + Utils.padWithZero(collection, 3) + ".xml");
		this.fragment = ac.getArticleById(String.valueOf(this.id)).fragment;
	}

	public int getId() {
		return Integer.parseInt(((Element) fragment).getAttribute("NEWID"));
	}
	
	public String getText() {
		return ((Element) fragment).getElementsByTagName("TEXT").item(0).getTextContent();
	}
	
	public String getTitle() {
		NodeList nl = ((Element) fragment).getElementsByTagName("TITLE");
		if (nl.getLength() == 0)
			return "???";
		else
		return nl.item(0).getTextContent();
		
	}
	
}
