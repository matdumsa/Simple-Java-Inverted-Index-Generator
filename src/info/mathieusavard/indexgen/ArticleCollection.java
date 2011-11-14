package info.mathieusavard.indexgen;

import info.mathieusavard.utils.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
}
