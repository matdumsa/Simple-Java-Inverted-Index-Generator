package info.mathieusavard.indexgen;

import info.mathieusavard.utils.Property;

import java.io.File;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SPIMIInvertedIndex implements IInvertedIndex {

	private static final int MEMORY_SIZE = Property.getInt("SPIMI_MEMORY_SIZE");
	private static Integer TotalBlockCounter = 0;
	private int currentBlockNumber;
	private int currentSize = 0;
	private DefaultInvertedIndex postingList = new DefaultInvertedIndex();

	public SPIMIInvertedIndex() {
		flushBlock();
		acquireNewBlock();
	}

	private void flushBlock() {
		if (currentSize > 0) {
			System.out.println("Flushing block " + currentBlockNumber);
			postingList.writeToFile(String.valueOf(currentBlockNumber));
		}
		
	}

	private synchronized void acquireNewBlock() {
		postingList = new DefaultInvertedIndex();
		currentBlockNumber = TotalBlockCounter++;
		currentSize = 0;
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
	public AbstractSet<Integer> getSet(String token) {
		return postingList.getSet(token);
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
	
	public static int getTotalBlock() {
		return TotalBlockCounter-1;
	}
	
	public synchronized static IInvertedIndex reconcile() {
		System.out.println("Reconciling");
		DefaultInvertedIndex finalIndex = null;
		for (int i=0; i<TotalBlockCounter; i++) {
			String blockPath = String.valueOf(i);
			if (i==0)
				finalIndex = DefaultInvertedIndex.readFromFile(blockPath);
			else {
				finalIndex.mergeWith(DefaultInvertedIndex.readFromFile(blockPath));
			}
			
			(new File(blockPath)).delete();
		}
		
		finalIndex.writeToFile("index.txt");
		return finalIndex;
	}

	@Override
	public HashSet<Integer> getAll() {
		return postingList.getAll();
	}

}
