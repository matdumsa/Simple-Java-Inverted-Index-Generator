package info.mathieusavard.domain.reuters;

import info.mathieusavard.domain.index.IndexerThread;
import info.mathieusavard.technicalservices.HtmlEntities;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandlerListAll extends DefaultHandler {
	boolean isText = false;
	boolean isTitle = false;
	boolean isBody = true;

	private int id;
	private StringBuilder title;
	private StringBuilder text;

	public void startElement(String uri, String localName,String qName, 
			Attributes attributes) throws SAXException {

		if (qName.equals("REUTERS")) {
			id = Integer.parseInt(attributes.getValue("NEWID"));
			text = new StringBuilder();
			title = new StringBuilder();
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
			title.append(ch, start, length);
		}
		if (isText) {
			text.append(ch, start, length);
		}

	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if (qName.equals("TEXT")) {
			isText=false;
			ReutersDocument d = new ReutersDocument(id, HtmlEntities.encode(title.toString()), HtmlEntities.encode(text.toString()));
			IndexerThread.addDocument(d);
		}
		if (qName.equals("TITLE"))
			isTitle=false;
		if (qName.equals("BODY") ) {
			isBody=false;
		}
	}

}

