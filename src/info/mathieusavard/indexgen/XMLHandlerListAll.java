package info.mathieusavard.indexgen;

import info.mathieusavard.utils.HtmlEntities;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandlerListAll extends DefaultHandler {


	
	ArrayList<Article> al = new ArrayList<Article>();

	boolean isText = false;
	boolean isTitle = false;
	boolean isBody = true;
	
	private int id;
	private String text = "";
	private String title = "";
		
	public void startElement(String uri, String localName,String qName, 
			Attributes attributes) throws SAXException {

		if (qName.equals("REUTERS")) {
				id = Integer.parseInt(attributes.getValue("NEWID"));
				text = "";
				title="";
		}

		if (qName.equals("TEXT")) {
			isText=true;			
		}
		if (qName.equals("TITLE")) {
			isTitle=true;
		}
		if (qName.equals("BODY"))
			isBody=true;

	}

	public void characters(char ch[], int start, int length) throws SAXException {
			
		if (isText && isTitle) {
			title += new String(ch, start, length);
		}
		if (isText) {
			text += new String(ch, start, length);
		}
		
	}
	
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		
		if (qName.equals("TEXT")) {
			isText=false;
			al.add(new Article(id, HtmlEntities.encode(title), HtmlEntities.encode(text)));
			title="";
			id=0;
			text="";
		}
		if (qName.equals("TITLE"))
			isTitle=false;
		if (qName.equals("BODY") ) {
			isBody=false;
		}
	
		}
	
	public ArrayList<Article> getResult() {
		return al;
	}
	 



}

