package info.mathieusavard.domain;

import info.mathieusavard.technicalservices.Constants;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.TreeMap;

public class Corpus {

	private static TreeMap<Integer, GenericDocument> documentMap;

	public static synchronized void addArticle(GenericDocument d) {
		if (documentMap == null)
			documentMap = new TreeMap<Integer, GenericDocument>();
		documentMap.put(d.getId(), d);
	}
	
	public static void writeToDisk() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.basepath + "/articles.txt"));
			for (Integer i : documentMap.keySet()) {
				GenericDocument a = documentMap.get(i);
				out.write(a.toString() + "\n");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readFromDisk() {
		documentMap = new TreeMap<Integer, GenericDocument>();
		try {
			LineNumberReader in = new LineNumberReader(new FileReader(Constants.basepath + "/articles.txt"));
			String line;
			line = in.readLine();
			while (line != null && line.length()>0) {
				try {
					GenericDocument d = GenericDocument.fromString(line);
					documentMap.put(d.getId(), d);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					line = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getTotalLength() {
		if (documentMap == null) readFromDisk();
		int ans=0;
		for (GenericDocument a : documentMap.values()) {
			ans+=a.getLengthInWords();
		}
		return ans;
	}

	public static GenericDocument findArticle(int documentId) {
		if (documentMap == null) readFromDisk();
		return documentMap.get(documentId);
	}

	public static int size() {
		if (documentMap == null) readFromDisk();
		return documentMap.size();
	}
}
