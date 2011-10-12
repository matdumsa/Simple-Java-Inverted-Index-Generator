package info.mathieusavard.indexgen;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SGMPreprocessing {

	public static void preprocess(String sgmPath, String articlePath) {

		//Obtain the list of sgm files
		List<String> sgmFiles = Utils.getAllFiles(sgmPath, ".xml", true);

		//Obtain the list of articles
		List<String> articleFiles = Utils.getAllFiles(articlePath, ".article", true);

		for (String sgmFile : sgmFiles) {
			boolean found = false;
			for (String article : articleFiles) {
				if (article.startsWith(sgmFile)) {
					found = true;
					break;
				}
			}
			if (found == false) {
				processSgmFile(sgmPath, sgmFile, articlePath);
			}
		}
	}

	private static void processSgmFile (String sgmPath, String sgmFile, String articlePath) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {//read from file
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(new File(sgmPath + "/" + sgmFile));
			
			System.out.println("I found " + dom.getElementsByTagName("REUTERS").getLength() + " article in " + sgmFile);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}	}
}
