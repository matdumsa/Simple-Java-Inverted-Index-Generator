package info.mathieusavard.indexgen;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ParsableArticleCollection {

	private String fullPath;
	
	public ParsableArticleCollection(String fullPath) {
		super();
		this.fullPath = fullPath;
	}

	public String getFullPath() {
		return fullPath;
	}

	private void parseDom(DefaultHandler handler) {
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			// parse using builder to get DOM representation of the XML file
			saxParser.parse(fullPath, handler);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			//Do Nothing, handler throw this when they found their match!
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public List<Article> getArticles() {
		XMLHandlerListAll reader = new XMLHandlerListAll();
		parseDom(reader);
		return reader.getResult();
	}
	
	public Article getArticleById(String id) {
		XMlHandlerFindById h = new XMlHandlerFindById(Integer.parseInt(id));
		parseDom(h);
		return h.getResult();
	}
}
