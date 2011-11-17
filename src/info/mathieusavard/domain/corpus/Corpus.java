package info.mathieusavard.domain.corpus;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.technicalservices.Constants;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TreeMap;

public class Corpus {

	protected TreeMap<Integer, GenericDocument> documentMap;
	private static Class<? extends GenericDocument> factory = GenericDocument.class;
	private boolean readOnly=false; //when a Corpus is created, it is read-write and when finalized, it becomes read-only
	
	//Default constructor allow only the factory in this package to create instances
	Corpus() {
		super();
		System.out.println("Creating a corpus");
		if (CorpusFactory.corpus != null)
			throw new RuntimeException("Oups.. you can't create a new corpus if there is already one in CorpusFactory");
	}

	/**
	 * When using a document type that extends from GenericDocument, the factory (class that contains fromString) that can
	 * create this type of document should be passed here.
	 * @param factory
	 */
	public static void setNewDocumentFactory(Class<? extends GenericDocument> factory) {
		Corpus.factory = factory;
	}
	
	public synchronized void addArticle(GenericDocument d) {
		if (documentMap == null)
			documentMap = new TreeMap<Integer, GenericDocument>();
		if (readOnly==true)
			throw new RuntimeException("Oups.. looks like you tried to add documents to a corpus that was finalized. It's now read-only.");
		documentMap.put(d.getId(), d);
	}
	
	public void closeIndex(){
		writeToDisk();
		readOnly=true;
	}
	
	protected void writeToDisk() {
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
		Corpus newCorpus = new Corpus();
		newCorpus.documentMap = new TreeMap<Integer, GenericDocument>();
		try {
			LineNumberReader in = new LineNumberReader(new FileReader(Constants.basepath + "/articles.txt"));
			String line;
			line = in.readLine();
			while (line != null && line.length()>0) {
				try {
					Method factoryMethod = factory.getDeclaredMethod("fromString", String.class);
					GenericDocument d = (GenericDocument) factoryMethod.invoke(null, line);
					newCorpus.documentMap.put(d.getId(), d);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					line = in.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		newCorpus.readOnly=true;
		
	}
	
	public int getTotalLength() {
		if (documentMap == null) readFromDisk();
		int ans=0;
		for (GenericDocument a : documentMap.values()) {
			ans+=a.getLengthInWords();
		}
		return ans;
	}

	public GenericDocument findArticle(int documentId) {
		if (documentMap == null) readFromDisk();
		return documentMap.get(documentId);
	}

	public int size() {
		if (documentMap == null) readFromDisk();
		return documentMap.size();
	}
}
