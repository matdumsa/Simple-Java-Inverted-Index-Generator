package info.mathieusavard.indexgen;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


public class InvertedIndex implements Iterable<String> {

	private TreeMap<String, HashSet<String>> map = new TreeMap<String, HashSet<String>>();
	
	private void add(String token, HashSet<String> documentList) {
		if (map.containsKey(token)) {
			//add this document to the list of document that contains this token
			map.get(token).addAll(documentList);
		} else {
			map.put(token, documentList);
		}
	
	}
	public void add(String token, String id) {
		// If this token is already in our index
		if (map.containsKey(token)) {
			//add this document to the list of document that contains this token
			map.get(token).add(id);
		}
		else {
			// Not already present, create a new list of document name
			HashSet<String> documentList = new HashSet<String>();
			// Add this document to the list
			documentList.add(id);
			// Add this list of document to the index
			map.put(token, documentList);
		}
	}
	
	/*
	 * This method merges index b into index a.
	 */
	public synchronized void mergeWith(InvertedIndex b) {
		for (String tokenb : b) {
			this.add(tokenb, b.get(tokenb));
		}
	}
	
	public Set<String> keySet() {
		return map.keySet();
	}

	public int size() {
		return map.size();
	}

	public HashSet<String> get(String token) {
		return map.get(token);
	}
	
	public Iterator<String> iterator() {
		return map.keySet().iterator();
	}
	public void clear() {
		map.clear();		
	}
	
	public synchronized void writeToFile(String path) {
		try {
			FileWriter fstream = new FileWriter(path);
			BufferedWriter out = new BufferedWriter(fstream);
			// For each token of the index
			System.out.println("Writing index file");
			for (String token : this) {
				// Obtain the list of document that contain this token in a string
				String docList = this.get(token).toString();
				// Write to the index file
				out.write(token + "->" + docList + "\n");
			}
			// Close the index file.
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static InvertedIndex readFromFile(String path) {
		try {
			InvertedIndex index = new InvertedIndex();
			FileReader fstream = new FileReader(path);
			BufferedReader in = new BufferedReader(fstream);
			// For each token of the index
			String line = in.readLine();
			while (line != null) {
				String[] tokens = line.split("->");
				String token = tokens[0];
				String docList = tokens[1];
				docList.replace("[", "");
				docList.replace("]", "");
				HashSet<String> docSet = new HashSet<String>();
				docSet.addAll(Arrays.asList(docList.split(", ")));
				index.add(token, docSet);
				
				line = in.readLine();
			}

			in.close();
			return index;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
