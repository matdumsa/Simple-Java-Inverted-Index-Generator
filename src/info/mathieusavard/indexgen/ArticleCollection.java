package info.mathieusavard.indexgen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ArticleCollection {

	private String fullPath;
	private Document dom = null;
	private static HashMap<String, Document> documentCache = new HashMap<String, Document>();
	public static boolean ENABLE_DOM_CACHING = false;
	
	public ArticleCollection(String fullPath) {
		super();
		this.fullPath = fullPath;
	}

	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	private void parseDom() {
		if (ENABLE_DOM_CACHING == true && documentCache.containsKey(this.fullPath)) {
			dom = documentCache.get(this.fullPath);
			return;
		}
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(fullPath);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (ENABLE_DOM_CACHING) {
			documentCache.clear();
			documentCache.put(this.fullPath, dom);
		}
	}
	public List<Article> getArticles() {
		ArrayList<Article> result = new ArrayList<Article>();

		if (dom == null) parseDom();

		//Obtain all the REUTERS element
		NodeList nl = dom.getElementsByTagName("REUTERS");
		for (int i=0; i<nl.getLength(); i++) {
			Node n = nl.item(i);
			result.add(new Article(n));
		}

		return result;
	}
	
	public Article getArticleById(String id) {
		if (dom == null) parseDom();

		//Obtain all the REUTERS element
		NodeList nl = dom.getElementsByTagName("REUTERS");
		for (int i=0; i<nl.getLength(); i++) {
			Node n = nl.item(i);
			if (((Element) n).getAttribute("NEWID").equals(id))
			return new Article(n);
		}
		return null;
	}
}
