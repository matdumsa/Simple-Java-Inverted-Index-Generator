package info.mathieusavard.domain.index.spimi;

import info.mathieusavard.domain.Posting;
import info.mathieusavard.technicalservices.Property;

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
		currentBlockNumber = acquireNewBlock();
	}

	private void flushBlock() {
		if (currentSize > 0) {
			System.out.println("Flushing block " + currentBlockNumber);
			postingList.writeToFile(String.valueOf(currentBlockNumber) + ".spimi");
			postingList = new DefaultInvertedIndex();
			currentBlockNumber = acquireNewBlock();
			currentSize = 0;
		}
		
	}

	private synchronized int acquireNewBlock() {
		return ++TotalBlockCounter;
	}
	
	@Override
	public boolean add(String token, int id) {
		if (currentSize >= MEMORY_SIZE) {
			flushBlock();
			currentBlockNumber = acquireNewBlock();
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
	public AbstractSet<Posting> getSet(String token) {
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
	
	@Override
	public HashSet<Posting> getAll() {
		return postingList.getAll();
	}

}
