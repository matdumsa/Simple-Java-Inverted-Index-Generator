package info.mathieusavard.indexgen;

import info.mathieusavard.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLSharding {

	public static void preprocess(String sgmPath, String articlePath) {

		//Obtain the list of sgm files
		List<String> sgmFiles = Utils.getAllFiles(sgmPath, ".xml", false);

		for (String sgmFile : sgmFiles) {
				processSgmFile(sgmPath, sgmFile);
		}
	}

	private static void processSgmFile (String sgmPath, String sgmFile) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {//read from file
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(new File(sgmFile));
			
			NodeList nl = dom.getElementsByTagName("REUTERS");
			for (int i =0; i<nl.getLength(); i++) {
				Element e = (Element) nl.item(i);
				int id = Integer.parseInt(e.getAttribute("NEWID"));
				String articlePath = sgmPath +"/" + id/1000;
				makeDirectory(articlePath);
				writeFragmentToDisk(e, articlePath + "/" + id + ".xml");
			}
			System.out.println("I found " + dom.getElementsByTagName("REUTERS").getLength() + " article in " + sgmFile);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}	}
	
	private static void writeFragmentToDisk(Element element, String path) {

		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(element);
			StreamResult result = new StreamResult(new File(path));
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	private static void makeDirectory(String path) {
		File f = new File(path);
		f.mkdir();
	}
}