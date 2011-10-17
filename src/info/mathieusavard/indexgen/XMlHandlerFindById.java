package info.mathieusavard.indexgen;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMlHandlerFindById extends DefaultHandler {


	

	boolean isText = false;
	boolean isTitle = false;
	boolean isBody = true;
	
	private int id;
	private String text = "";
	private String title = "";
	private boolean found = false;
	private Article a;
	
	public XMlHandlerFindById(int id) {
		this.id = id;
	}
	
	public void startElement(String uri, String localName,String qName, 
			Attributes attributes) throws SAXException {

		if (qName.equals("REUTERS")) {
			if (Integer.parseInt(attributes.getValue("NEWID")) == id) {
				found = true;
				text = "";
				title="";
			}
		}

		if (qName.equals("TEXT") && found) {
			isText=true;			
		}
		if (qName.equals("TITLE") && found) {
			isTitle=true;
		}
		if (qName.equals("BODY") && found)
			isBody=true;

	}

	public void characters(char ch[], int start, int length) throws SAXException {
			
		if (isText && isTitle && found) {
			title = new String(ch, start, length);
		}
		if (isText && isBody && found) {
			text += new String(ch, start, length);
		}
		
	}
	
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if (qName.equals("TEXT") && found)
			isText=false;
		if (qName.equals("TITLE") && found)
			isTitle=false;
		if (qName.equals("BODY") && found) {
			isBody=false;
			a = new Article(id, title, text);
			throw new SAXException("done");	
		}
	
		}
	
	public Article getResult() {
		return a;
	}
	 




}

