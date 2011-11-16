package info.mathieusavard.domain.index;
import info.mathieusavard.domain.Corpus;
import info.mathieusavard.domain.index.compression.Stemmer;
import info.mathieusavard.domain.index.compression.StopwordRemover;
import info.mathieusavard.domain.index.spimi.SPIMIInvertedIndex;
import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.technicalservices.Property;
import info.mathieusavard.technicalservices.Utils;

import java.util.Stack;
import java.util.StringTokenizer;


public class IndexerThread extends Thread {

	//DICTIONARY COMPRESSION OPTION
	private static final boolean COMPRESSION_NO_NUMBER=Property.getBoolean("COMPRESSION_NO_NUMBER");
	private static final boolean COMPRESSION_CASE_FOLDING=Property.getBoolean("COMPRESSION_CASE_FOLDING");
	private static final boolean COMPRESSION_STOP_WORDS=Property.getBoolean("COMPRESSION_STOP_WORDS");
	private static final boolean COMPRESSION_STEMMING=Property.getBoolean("COMPRESSION_STEMMING");

	//create an instance of the stemmer wrapper for the PorterStemmer.
	private Stemmer stemmer = new Stemmer();
	private SPIMIInvertedIndex index;
	private static Stack<GenericDocument> filesToProcess = new Stack<GenericDocument>();
	//When set to true, the thread will sleep waiting for new document to index
	private static boolean noMoreDocumentsWillBeAdded = false;

	public IndexerThread(String tName) {
		super(tName);
	}

	public GenericDocument popOrWait() {
		try {
			synchronized(filesToProcess) {
				if (filesToProcess.size() == 0) {
					if (noMoreDocumentsWillBeAdded == true)
						return null;
					filesToProcess.wait();
					return popOrWait();
				}
				return filesToProcess.pop();
			}
		} catch (InterruptedException e) {
			return null;
		}
	}
	public void run() {
		index = new SPIMIInvertedIndex();

		GenericDocument d = popOrWait();
		while (d != null) {
			processDocument(d);
			Corpus.addArticle(d);
			d.clearContent();
			d = popOrWait();
		}

		index.writeToFile(""); // flush the last spimi shard

		synchronized(filesToProcess) {
			System.out.println(super.getName() + " says: I'm done working, deallocate.");
			filesToProcess.notifyAll(); //ÊTelling others it's fine to stop..			
		}
	}

	private void processDocument(GenericDocument d) {
		//Remove all &entities;
		String text = Utils.removeEntities(d.getText());
		//Tokenize
		StringTokenizer st = new StringTokenizer(text);
		int wordCount =0 ;
		while (st.hasMoreTokens()) {
			wordCount++;
			//Read the next token, put to lowercase
			String token = st.nextToken();
			token = compressToken(token);

			//If the token is not empty, add it
			if (token != null) {
				index.add(token, d.getId());
			}
		} // end of the for all token loop
		d.setLengthInWords(wordCount);
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

	public static void signalNoMoreDocumentsAreExpected() {
		noMoreDocumentsWillBeAdded=true;
		synchronized(filesToProcess) {
			filesToProcess.notify();
		}
	}

	public static void addDocument(GenericDocument d) {
		if (noMoreDocumentsWillBeAdded == true)
			throw new RuntimeException("You cannot add document is this tokenizer thread is not expecting them");
		synchronized(filesToProcess) {
			filesToProcess.push(d);
			filesToProcess.notify();
		}
	}
}
