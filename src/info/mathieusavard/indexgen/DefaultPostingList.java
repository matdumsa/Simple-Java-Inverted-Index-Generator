package info.mathieusavard.indexgen;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;


public class DefaultPostingList implements IPostingList {

	private TreeMap<String, HashSet<Integer>> map = new TreeMap<String, HashSet<Integer>>();
	
	private boolean add(String token, HashSet<Integer> documentList) {
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
			if (map.get(token).add(id) == true) // if this is a new document/token pair
				return true;
			else
				return false;
		}
		else {
			// Not already present, create a new list of document name
			HashSet<Integer> documentList = new HashSet<Integer>();
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
	public synchronized void mergeWith(IPostingList b) {
		for (String tokenb : b) {
			this.add(tokenb, b.get(tokenb));
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
	public HashSet<Integer> get(String token) {
		return map.get(token);
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
			FileWriter fstream = new FileWriter(path);
			BufferedWriter out = new BufferedWriter(fstream);
			// For each token of the index
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
	
	public static DefaultPostingList readFromFile(String path) {
		try {
			DefaultPostingList index = new DefaultPostingList();
			File inputFile = new File(Constants.basepath + "/" + path);
			System.out.println("opening " + inputFile.getAbsolutePath());
			FileReader fstream = new FileReader(inputFile);
			BufferedReader in = new BufferedReader(fstream);
			// For each token of the index
			String line = in.readLine();
			Pattern onlyPostingList = java.util.regex.Pattern.compile("[^0-9|\\s]");

			while (line != null) {
				String[] tokens = line.split("->");
				String token = tokens[0];
				String docList = tokens[1];
				
				HashSet<Integer> docSet = new HashSet<Integer>();
				
				for (String s : onlyPostingList.matcher(docList).replaceAll("").split(" ")) {
					docSet.add(Integer.parseInt(s));
				}

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
