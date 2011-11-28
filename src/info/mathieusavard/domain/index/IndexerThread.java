package info.mathieusavard.domain.index;
import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.corpus.CorpusFactory;
import info.mathieusavard.domain.index.compression.Stemmer;
import info.mathieusavard.domain.index.compression.StopwordRemover;
import info.mathieusavard.domain.index.spimi.SPIMIInvertedIndex;
import info.mathieusavard.technicalservices.Property;
import info.mathieusavard.technicalservices.Utils;

import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class IndexerThread extends Thread {

	//DICTIONARY COMPRESSION OPTION
	private static final boolean COMPRESSION_NO_NUMBER=Property.getBoolean("COMPRESSION_NO_NUMBER");
	private static final boolean COMPRESSION_CASE_FOLDING=Property.getBoolean("COMPRESSION_CASE_FOLDING");
	private static final boolean COMPRESSION_STOP_WORDS=Property.getBoolean("COMPRESSION_STOP_WORDS");
	private static final boolean COMPRESSION_STEMMING=Property.getBoolean("COMPRESSION_STEMMING");
	private static final LinkedList<IndexerThread> threadList = new LinkedList<IndexerThread>();
	
	//create an instance of the stemmer wrapper for the PorterStemmer.
	private Stemmer stemmer = new Stemmer();
	private SPIMIInvertedIndex index;
	private static LinkedBlockingQueue<GenericDocument> documentToProcess = new LinkedBlockingQueue<GenericDocument>(500);
	//When set to true, the thread will sleep waiting for new document to index
	private static boolean noMoreDocumentsWillBeAdded = false;

	public IndexerThread(String tName) {
		super(tName);
		synchronized(threadList) {
			threadList.add(this);			
		}
	}

	public void run() {
		index = new SPIMIInvertedIndex();

		try {
			GenericDocument d = documentToProcess.take();
			while (d != null) {
				processDocument(d);
				CorpusFactory.getCorpus().addArticle(d);
				d.clearContent();
				d = documentToProcess.take();
			}
		} catch (InterruptedException e) {
			//Hmm.. I was interrupted.. Maybe work is over, check that out..
			if (documentToProcess.size() == 0 && noMoreDocumentsWillBeAdded == true)
			{
				// All right.. this is over
				index.flushBlock(); // flush the last spimi shard
				index = null;
				System.out.println(super.getName() + " says: I'm done working, deallocate.");
			}
			else {
				// Keep going on.
				run();
			}
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
		for (Thread t: threadList) {
			//Wake the sleepers 
			t.interrupt();
		}
	}

	public static void addDocument(GenericDocument d) {
		if (noMoreDocumentsWillBeAdded == true)
			throw new RuntimeException("You cannot add document is this indexer thread is not expecting them");
		try { 
			if (documentToProcess.offer(d, 10, TimeUnit.SECONDS) == false) {
				System.err.println("The queue got full and 10 seconds elapsed.. I'm dropping this document");
			}
		}
		catch (InterruptedException e) {
			//Looks like I got interrupted but resume
			addDocument(d);
		}
	}
}
