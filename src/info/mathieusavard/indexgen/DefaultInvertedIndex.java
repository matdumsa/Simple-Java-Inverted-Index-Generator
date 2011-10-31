package info.mathieusavard.indexgen;
import info.mathieusavard.utils.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class DefaultInvertedIndex implements IInvertedIndex {

	private AbstractMap<String, AbstractCollection<Integer>> map = new TreeMap<String, AbstractCollection<Integer>>();
	
	boolean add(String token, AbstractCollection<Integer> documentList) {
		if (token == null ) return false;
		if (map.containsKey(token)) {
			//add this document to the list of document that contains this token
			map.get(token).addAll(documentList);
			return true;
		} else {
			map.put(token, documentList);
			return false;
		}
	
	}
	@Override
	public boolean add(String token, int id) {
		// If this token is already in our index
		if (map.containsKey(token)) {
			//add this document to the list of document that contains this token
			if (map.get(token).contains(id))
				return false;
			else {
				map.get(token).add(id); // if this is a new document/token pair
				return true;
			}
		}
		else {
			// Not already present, create a new list of document name
			ArrayList<Integer> documentList = new ArrayList<Integer>();
			// Add this document to the list
			documentList.add(id);
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
	public AbstractSet<Integer> getSet(String token) {
		AbstractCollection<Integer> c = map.get(token);
		if (c == null)
			return new HashSet<Integer>();

		HashSet<Integer> r = new HashSet<Integer>(map.get(token).size());
		for (int n : map.get(token))
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
				for (int i : this.getSet(token))
					out.write(Integer.toString(i, Character.MAX_RADIX) +" ");
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
			TreeMap<String, AbstractCollection<Integer>> newMap = new TreeMap<String, AbstractCollection<Integer>>();

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
				Integer[] postingList = new Integer[st.countTokens()-1];
				int i=0;
				while (st.hasMoreTokens()) {
					if (firstToken==true) {
						firstToken = false;
						term = st.nextToken();
					}
					else {
						postingList[i++] = Integer.parseInt(st.nextToken(), Character.MAX_RADIX);
					}
				}
				
				newMap.put(term, new LinkedList<Integer>(Arrays.asList((postingList))));
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

	public HashSet<Integer> getAll() {
		HashSet<Integer> all = new HashSet<Integer>();
		for (String s : map.keySet()) {
			all.addAll(map.get(s));
		}
		return all;
	}
}
