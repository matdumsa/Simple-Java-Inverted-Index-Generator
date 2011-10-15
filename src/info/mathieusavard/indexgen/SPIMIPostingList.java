package info.mathieusavard.indexgen;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SPIMIPostingList implements IPostingList {

	private static final int MEMORY_SIZE = 100000;
	private static Integer TotalBlockCounter = 0;
	private int currentBlockNumber;
	private int currentSize = 0;
	private DefaultPostingList postingList = new DefaultPostingList();

	
	public SPIMIPostingList() {
		flushBlock();
	}

	private void flushBlock() {
		if (currentSize > 0) {
			postingList.writeToFile(String.valueOf(currentBlockNumber));
			postingList.clear();
		}
		
		currentSize = 0;		
	}

	private synchronized void acquireNewBlock() {
		currentBlockNumber = TotalBlockCounter++;
	}
	@Override
	public boolean add(String token, int id) {
		if (currentSize >= MEMORY_SIZE) {
			flushBlock();
			acquireNewBlock();
		}
		
		//If the defaultPostingList tells me this is a new term/doc I increment size
		if (postingList.add(token, id) == true)
			currentSize++;
		
		return true;
	}

	@Override
	public Set<String> keySet() {
		return postingList.keySet();
	}

	@Override
	public int size() {
		return postingList.size();
	}

	@Override
	public HashSet<Integer> get(String token) {
		return postingList.get(token);
	}

	@Override
	public Iterator<String> iterator() {
		return postingList.iterator();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void writeToFile(String path) {
		flushBlock();		
	}
	
	public synchronized static IPostingList reconcile() {
		System.out.println("Reconciling");
		DefaultPostingList finalIndex = null;
		for (int i=0; i<TotalBlockCounter; i++) {
			String blockPath = String.valueOf(i);
			if (i==0)
				finalIndex = DefaultPostingList.readFromFile(blockPath);
			else {
				finalIndex.mergeWith(DefaultPostingList.readFromFile(blockPath));
			}
			
			(new File(blockPath)).delete();
		}
		
		finalIndex.writeToFile("index.txt");
		return finalIndex;
	}

}
