package info.mathieusavard.domain.index;
import info.mathieusavard.domain.Corpus;
import info.mathieusavard.domain.Document;
import info.mathieusavard.domain.index.compression.Stemmer;
import info.mathieusavard.domain.index.compression.StopwordRemover;
import info.mathieusavard.domain.index.spimi.SPIMIInvertedIndex;
import info.mathieusavard.domain.index.spimi.SPIMIReconciliation;
import info.mathieusavard.technicalservices.Property;
import info.mathieusavard.technicalservices.Utils;

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
	private Stack<Document> filesToProcess;
	//When set to true, the thread will sleep waiting for new document to index
	private boolean acceptNewDocument = false;

	//When set to true, the thread will stop waiting for new document and return once it wakes up
	private boolean terminate = false;

	public TokenizerThread(String tName, Stack<ParsableArticleCollection> filesToProcess) {
		this.filesToProcess = new Stack<Document>();
		for (ParsableArticleCollection c : filesToProcess) {
			this.filesToProcess.addAll(c.getArticles());
		}
	}

	public TokenizerThread(String tName) {
		this.filesToProcess = null;
		acceptNewDocument = true;
		filesToProcess = new Stack<Document>();
	}

	public TokenizerThread() {

	}

	public Document popOrWait() {
		Document d = null;
		while (d==null) {
			synchronized(filesToProcess) {
				try {
					d = filesToProcess.pop();
				} catch (Exception e) {
					try {
						if (this.terminate==true)
							return null;
						filesToProcess.wait();
						if (this.terminate==true)
							return null;
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return d;
	}
	public void run() {
		index = new SPIMIInvertedIndex();

		Document d = popOrWait();
		while (d != null) {
			processDocument(d);
			Corpus.addArticle(d);
			d.clearContent();
			if (acceptNewDocument == false) {
				if (filesToProcess.size()>0)
					d = filesToProcess.pop();
				else
					d=null;
			}
			else {
				d = popOrWait();
			}
		}

		index.writeToFile(""); // flush the last spimi shard
		if (acceptNewDocument == true && terminate == true)
			SPIMIReconciliation.reconciliate();

	}

	private void processDocument(Document d) {
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

	public void stopThread() {
		this.terminate=true;
		synchronized(filesToProcess) {
			filesToProcess.notify();
		}
		System.out.println("Starting reconciliation");

	}

	public void addDocument(Document d) {
		if (this.acceptNewDocument == false)
			throw new RuntimeException("You cannot add document is this tokenizer thread is not expecting them");
		synchronized(filesToProcess) {
			filesToProcess.push(d);
			filesToProcess.notify();
		}
	}
}
