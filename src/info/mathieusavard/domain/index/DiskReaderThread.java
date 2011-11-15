package info.mathieusavard.domain.index;

import info.mathieusavard.domain.Document;
import info.mathieusavard.technicalservices.Constants;

import java.io.IOException;
import java.util.Collection;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class DiskReaderThread extends Thread {

	private static Stack<String> collectionToParse = new Stack<String>();


	public DiskReaderThread() {
		super();
	}

	@Override
	public void run() {
		String nextCollection = null;
		synchronized(collectionToParse) {
			nextCollection = collectionToParse.pop();
		}
		while (nextCollection != null) {
			System.out.println("Reading collection " + nextCollection + " to memory");
			parseDom(nextCollection, new XMLHandlerListAll());
			synchronized(collectionToParse) {
				if (collectionToParse.size() == 0) break;
				nextCollection = collectionToParse.pop();
			}
		}
	}

	public static void addCollection(Collection<String> collection) {
		synchronized(collectionToParse) {
			collectionToParse.addAll(collection);
		}
	}
	public static void addCollection(String collection) {
		synchronized(collectionToParse) {
			collectionToParse.add(collection);
		}
	}

	private static void parseDom(String fullPath, DefaultHandler handler) {

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

	public static Document getArticleById(int id) {
		int collection = id/1000;
		String path = Constants.basepath + "/reut/" + collection + "/" + id + ".xml";
		XMlHandlerFindById h = new XMlHandlerFindById(id);
		parseDom(path, h);
		return h.getResult();
	}
}
