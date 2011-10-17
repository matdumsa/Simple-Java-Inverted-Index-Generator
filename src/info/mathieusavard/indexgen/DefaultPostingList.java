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
import java.util.StringTokenizer;
import java.util.TreeMap;


public class DefaultPostingList implements IPostingList {

	private TreeMap<String, HashSet<Integer>> map = new TreeMap<String, HashSet<Integer>>();
	
	private boolean add(String token, HashSet<Integer> documentList) {
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
				// Write to the index file
				out.write(token + " ");
				for (int i : this.get(token))
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
	
	public static DefaultPostingList readFromFile(String path) {
		try {
			DefaultPostingList index = new DefaultPostingList();
			File inputFile = new File(Constants.basepath + "/" + path);
			BenchmarkRow timer = new BenchmarkRow(null);
			timer.start();
			System.out.println("opening " + inputFile.getAbsolutePath());
			FileReader fstream = new FileReader(inputFile);
			BufferedReader in = new BufferedReader(fstream);
			// For each token of the index
			String line = in.readLine();

			
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line);
				boolean firstToken = true;
				String posting = null;
				HashSet<Integer> docSet = new HashSet<Integer>();
				
				while (st.hasMoreTokens()) {
					if (firstToken==true) {
						firstToken = false;
						posting = st.nextToken();
					}
					else {
						docSet.add(Integer.parseInt(st.nextToken(), Character.MAX_RADIX));
					}
				}

				index.add(posting, docSet);
				line = in.readLine();
			}

			in.close();
			timer.stop();
			System.out.println("index took me " + timer.getDuration()/1000.0 + "ms to open");
			return index;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
