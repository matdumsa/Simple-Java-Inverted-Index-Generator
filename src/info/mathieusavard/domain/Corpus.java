package info.mathieusavard.domain;

import info.mathieusavard.technicalservices.Constants;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.TreeMap;

public class Corpus {

	private static TreeMap<Integer, Document> documentMap;

	public static synchronized void addArticle(Document a) {
		if (documentMap == null)
			documentMap = new TreeMap<Integer, Document>();
		documentMap.put(a.getId(), a);
	}
	
	public static void writeToDisk() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.basepath + "/articles.txt"));
			for (Integer i : documentMap.keySet()) {
				Document a = documentMap.get(i);
				out.write(i + ":" + a.getLengthInWords() + ":" + a.getTitle() + "\n");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readFromFisk() {
		documentMap = new TreeMap<Integer, Document>();
		try {
			LineNumberReader in = new LineNumberReader(new FileReader(Constants.basepath + "/articles.txt"));
			String line;
			line = in.readLine();
			while (line != null && line.length()>0) {
				try {
					String[] parts = line.split(":");
					int id = Integer.parseInt(parts[0]);
					int length = Integer.parseInt(parts[1]);
					String title = "???";
					if (parts.length > 2)
						title = parts[2].trim();
					documentMap.put(id, new Document(id, title, length));
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
		if (documentMap == null) readFromFisk();
		int ans=0;
		for (Document a : documentMap.values()) {
			ans+=a.getLengthInWords();
		}
		return ans;
	}

	public static Document findArticle(int documentId) {
		if (documentMap == null) readFromFisk();
		return documentMap.get(documentId);
	}

	public static int size() {
		if (documentMap == null) readFromFisk();
		return documentMap.size();
	}
}
