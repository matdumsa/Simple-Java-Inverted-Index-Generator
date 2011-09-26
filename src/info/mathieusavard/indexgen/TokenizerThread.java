package info.mathieusavard.indexgen;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;


public class TokenizerThread extends Thread {

	//create an instance of the stemmer wrapper for the PorterStemmer.
	private Stemmer stemmer = new Stemmer();
	private InvertedIndex index = new InvertedIndex();;
	private List<Document> filesToProcess;
	private String myName;
	private InvertedIndex finalIndex;

	public TokenizerThread(String tName, List<Document> filesToProcess, InvertedIndex finalIndex) {
		this.filesToProcess = filesToProcess;
		myName = tName;
		this.finalIndex = finalIndex;
	}
	public void run() {
		for (Document d : filesToProcess) {
			processDocument(d);
		}
		finalIndex.mergeWith(index);

	}

	private void processDocument(Document doc) {
		try {
			//This will store all the words found
			HashSet<String> words = new HashSet<String>();
			FileInputStream fstream = new FileInputStream(doc.getFullPath());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			while ((strLine = br.readLine()) != null)   {
				//Remove all <TAGS>
				strLine = Utils.removeTags(strLine);
				//Remove all &entities;
				strLine = Utils.removeEntities(strLine);
				//Tokenize
				StringTokenizer st = new StringTokenizer(strLine);
				while (st.hasMoreTokens()) {
					//Read the next token, put to lowercase
					String cleanedToken = st.nextToken().toLowerCase();

					//Remove special characters
					cleanedToken = Utils.removeSpecialChar(cleanedToken);

					//Stem
					cleanedToken = stemmer.stem(cleanedToken);

					//If the word is not a stop word and is not empty string add it
					if (StopwordRemover.stopwords.contains(cleanedToken) == false && cleanedToken.isEmpty() == false)
						words.add(cleanedToken);
				}

			} // end of the for all token loop


			// For all tokens of this file
			for (String token : words) {
				index.add(token, String.valueOf(doc.getId()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
