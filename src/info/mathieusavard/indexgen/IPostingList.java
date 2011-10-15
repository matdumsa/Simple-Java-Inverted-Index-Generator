package info.mathieusavard.indexgen;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public interface IPostingList extends Iterable<String> {

	public abstract boolean add(String token, int id);

	public abstract Set<String> keySet();

	public abstract int size();

	public abstract HashSet<Integer> get(String token);

	public abstract Iterator<String> iterator();

	public abstract void clear();

	public abstract void writeToFile(String path);

}