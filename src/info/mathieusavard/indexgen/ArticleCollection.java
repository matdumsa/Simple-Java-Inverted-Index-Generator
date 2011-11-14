package info.mathieusavard.indexgen;

import info.mathieusavard.utils.Constants;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.TreeMap;

public class ArticleCollection {

	private static TreeMap<Integer, Article> documentMap = new TreeMap<Integer, Article>();

	public static synchronized void addArticle(Article a) {
		documentMap.put(a.getId(), a);
	}
	
	public static void writeToDisk() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.basepath + "/articles.txt"));
			for (Integer i : documentMap.keySet()) {
				Article a = documentMap.get(i);
				out.write(i + ":" + a.getLengthInWords() + ":" + a.getTitle() + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readFromFisk() {
		try {
			LineNumberReader in = new LineNumberReader(new FileReader(Constants.basepath + "/articles.txt"));
			String line;
			line = in.readLine();
			while (line != null) {
				String[] parts = line.split(":");
				int id = Integer.parseInt(parts[0]);
				int length = Integer.parseInt(parts[1]);
				String title = parts[2].trim();
				documentMap.put(id, new Article(id, title, length));
				line = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getTotalLength() {
		int ans=0;
		for (Article a : documentMap.values()) {
			ans+=a.getLengthInWords();
		}
		return ans;
	}
}
