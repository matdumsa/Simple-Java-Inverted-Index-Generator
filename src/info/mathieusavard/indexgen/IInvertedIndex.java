package info.mathieusavard.indexgen;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public interface IInvertedIndex extends Iterable<String> {

	public abstract boolean add(String token, int id);

	public abstract Set<String> keySet();

	public abstract int size();

	public abstract AbstractSet<Integer> getSet(String token);

	public abstract Iterator<String> iterator();

	public abstract void clear();

	public abstract void writeToFile(String path);
	
	public abstract HashSet<Integer> getAll();

}