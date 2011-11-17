package info.mathieusavard.domain.index.spimi;
import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.corpus.CorpusFactory;
import info.mathieusavard.technicalservices.BenchmarkRow;
import info.mathieusavard.technicalservices.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class DefaultInvertedIndex implements IInvertedIndex {

	private AbstractMap<String, ArrayList<Posting>> map = new TreeMap<String, ArrayList<Posting>>();
	
	boolean add(String token, ArrayList<Posting> documentList) {
		if (token == null ) return false;
		if (map.containsKey(token)) {
			//add this document to the list of document that contains this token
			map.put(token, mergeTwoPostingList(map.get(token), documentList));
			return true;
		} else {
			map.put(token, documentList);
			return false;
		}
	
	}
	
	private ArrayList<Posting> mergeTwoPostingList(ArrayList<Posting> a, ArrayList<Posting> b) {
		for (Posting p1:a) {
			int idxInB = b.indexOf(p1);
			if (idxInB == -1)
				b.add(p1);
			else
				b.get(idxInB).add(p1.getOccurence());
		}
		return b;
	}
	@Override
	public boolean add(String token, int documentNumber) {
		// If this token is already in our index
		if (map.containsKey(token)) {
			//add this document to the list of document that contains this token
			int idx = map.get(token).indexOf(new Posting(token, documentNumber,-1));
			if (idx >-1) {
				map.get(token).get(idx).add(1);
				return false;
			}
			else {
				map.get(token).add(new Posting(token, documentNumber,1)); // if this is a new document/token pair
				return true;
			}
		}
		else {
			// Not already present, create a new list of document name
			ArrayList<Posting> documentList = new ArrayList<Posting>();
			// Add this document to the list
			documentList.add(new Posting(token, documentNumber,1));
			// Add this list of document to the index
			map.put(token, documentList);
			return true;
		}
	}
	
	/*
	 * This method merges index b into index a.
	 */
	public synchronized void mergeWith(DefaultInvertedIndex b) {
		for (String tokenb : b) {
			this.add(tokenb, b.map.get(tokenb));
		}
	}
	
	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public AbstractSet<Posting> getSet(String token) {
		List<Posting> c = map.get(token);
		if (c == null)
			return new HashSet<Posting>();

		HashSet<Posting> r = new HashSet<Posting>(map.get(token).size());
		for (Posting n : map.get(token))
			r.add(n);
		return r;
	}
	
	
	@Override
	public Iterator<String> iterator() {
		return map.keySet().iterator();
	}
	@Override
	public void clear() {
		map.clear();		
	}
	
	public synchronized void writeToFile(String path) {
		try {
			StringBuilder sb = new StringBuilder();
			FileWriter fstream = new FileWriter(Constants.basepath + "/" + path);
			// For each token of the index
			for (String token : this) {
				// Write to the index file
				sb.append(token + " ");
				for (Posting i : this.getSet(token))
					sb.append(i.toString() + " ");
				sb.append("\n");
			}
			fstream.write(sb.toString());
			// Close the index file.
			fstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static DefaultInvertedIndex readFromFile(String path) {
		try {
			TreeMap<String, ArrayList<Posting>> newMap = new TreeMap<String, ArrayList<Posting>>();

			File inputFile = new File(Constants.basepath + "/" + path);
			BenchmarkRow timer = new BenchmarkRow(null);
			System.out.println("opening " + inputFile.getAbsolutePath());
			FileReader fstream = new FileReader(inputFile);
			BufferedReader in = new BufferedReader(fstream);
			// For each token of the index
			String line = in.readLine();

			
			String term = null;
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line);
				boolean firstToken = true;
				Posting[] postingList = new Posting[st.countTokens()-1];
				int i=0;
				while (st.hasMoreTokens()) {
					if (firstToken==true) {
						firstToken = false;
						term = st.nextToken();
					}
					else {
						postingList[i++] = Posting.fromString(term, st.nextToken());
					}
				}
				
				newMap.put(term, new ArrayList<Posting>(Arrays.asList((postingList))));
				line = in.readLine();
			}

			in.close();
			timer.stop();
			System.out.println("index took me " + timer.getDuration()/1000.0 + "ms to open");

			DefaultInvertedIndex dii = new DefaultInvertedIndex();
			dii.map = newMap;
			return dii;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get all possible postings
	 */
	public HashSet<Posting> getAll() {
		HashSet<Posting> all = new HashSet<Posting>();
		for (String s : map.keySet()) {
			all.addAll(map.get(s));
		}
		return all;
	}
	
	/*
	 * Used for tf-idf
	 */
	public TreeMap<GenericDocument, LinkedList<Posting>> getDocumentBasedIndex() {
		TreeMap<GenericDocument, LinkedList<Posting>> result = new TreeMap<GenericDocument, LinkedList<Posting>>();
		
		BenchmarkRow br = new BenchmarkRow("Document based index");
		br.start();
		for (Collection<Posting> collection : map.values()) {
			for (Posting p : collection) {
				GenericDocument doc = CorpusFactory.getCorpus().findArticle(p.getDocumentId());
				if (result.containsKey(doc))
					result.get(doc).add(p);
				else {
					LinkedList<Posting> ll = new LinkedList<Posting>();
					ll.add(p);
					result.put(doc, ll);
				}
			}
		}
		br.stop();
		System.out.println("Document based index took " + br.getDuration() + " to run");
		return result;
	}
}
