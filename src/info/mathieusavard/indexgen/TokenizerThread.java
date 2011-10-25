package info.mathieusavard.indexgen;
import info.mathieusavard.utils.Property;
import info.mathieusavard.utils.Utils;

import java.util.HashSet;
import java.util.Stack;
import java.util.StringTokenizer;


public class TokenizerThread extends Thread {

	//DICTIONARY COMPRESSION OPTION
	private static final boolean COMPRESSION_NO_NUMBER=Property.getBoolean("COMPRESSION_NO_NUMBER");
	private static final boolean COMPRESSION_CASE_FOLDING=Property.getBoolean("COMPRESSION_CASE_FOLDING");
	private static final boolean COMPRESSION_STOP_WORDS=Property.getBoolean("COMPRESSION_STOP_WORDS");
	private static final boolean COMPRESSION_STEMMING=Property.getBoolean("COMPRESSION_STEMMING");
	
	//create an instance of the stemmer wrapper for the PorterStemmer.
	private Stemmer stemmer = new Stemmer();
	private SPIMIInvertedIndex index;
	private Stack<Collection> filesToProcess;

	public TokenizerThread(String tName, Stack<Collection> filesToProcess) {
		this.filesToProcess = filesToProcess;
	}

	public TokenizerThread() {
		// TODO Auto-generated constructor stub
	}

	public void run() {
		index = new SPIMIInvertedIndex();
		while (filesToProcess.size() > 0) {
			Collection d = filesToProcess.pop();
			System.out.println("Starting collection"  + d.getFullPath());

			//Obtain all articles
			for (Article a : d.getArticles()) {
				int id = a.getId();
				String s = a.getText();
				processDocument(s, id);
			}
		System.out.println("Done w/"  + d.getFullPath());
		}

		index.writeToFile("");

	}

	private void processDocument(String strLine, int docId) {
		//This will store all the words found
		HashSet<String> words = new HashSet<String>();

		//Remove all &entities;
		strLine = Utils.removeEntities(strLine);
		//Tokenize
		StringTokenizer st = new StringTokenizer(strLine);
		TokenizerThread tt = new TokenizerThread();
		while (st.hasMoreTokens()) {
			//Read the next token, put to lowercase
			String token = st.nextToken();
			token = tt.compressToken(token);

			//If the token is not empty, add it
			if (token != null)
				words.add(token);

		} // end of the for all token loop


		// For all tokens of this file
		for (String token : words) {
			index.add(token, docId);
		}
	}
	
	public String compressToken(String token) {
		if (COMPRESSION_CASE_FOLDING)
			token = token.toLowerCase();

		if (COMPRESSION_NO_NUMBER)
			token = Utils.noNumber(token);

		if (token.isEmpty())
			return null;
		
		try {
			if (COMPRESSION_STEMMING)
				token = stemmer.stem(token);
		}
		catch(Exception e) {
			System.err.println("Error stemming " + token);
		}

		if (COMPRESSION_STOP_WORDS && StopwordRemover.stopwords.contains(token))
			return null;

		return token;
	}
}
