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
	
	@Override
	public Iterator<String> iterator() {
		return map.keySet().iterator();
	}
	public void clear() {
		map.clear();		
	}
}
