package info.mathieusavard.domain.index.spimi;
import info.mathieusavard.domain.Posting;
import info.mathieusavard.technicalservices.BenchmarkRow;
import info.mathieusavard.technicalservices.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class DefaultInvertedIndex implements IInvertedIndex {

	private AbstractMap<String, List<Posting>> map = new TreeMap<String, List<Posting>>();
	
	boolean add(String token, List<Posting> documentList) {
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
	
	private List<Posting> mergeTwoPostingList(List<Posting> a, List<Posting> b) {
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
			int idx = map.get(token).indexOf(new Posting(documentNumber,-1));
			if (idx >-1) {
				map.get(token).get(idx).add(1);
				return false;
			}
			else {
				map.get(token).add(new Posting(documentNumber,1)); // if this is a new document/token pair
				return true;
			}
		}
		else {
			// Not already present, create a new list of document name
			ArrayList<Posting> documentList = new ArrayList<Posting>();
			// Add this document to the list
			documentList.add(new Posting(documentNumber,1));
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
	
	@Override
	public synchronized void writeToFile(String path) {
		try {
			FileWriter fstream = new FileWriter(Constants.basepath + "/" + path);
			BufferedWriter out = new BufferedWriter(fstream);
			// For each token of the index
			for (String token : this) {
				// Write to the index file
				out.write(token + " ");
				for (Posting i : this.getSet(token))
					out.write(i.toString() + " ");
				out.write("\n");
			}
			// Close the index file.
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static DefaultInvertedIndex readFromFile(String path) {
		try {
			TreeMap<String, List<Posting>> newMap = new TreeMap<String, List<Posting>>();

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
						postingList[i++] = Posting.fromString(st.nextToken());
					}
				}
				
				newMap.put(term, new LinkedList<Posting>(Arrays.asList((postingList))));
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
}
