package info.mathieusavard.domain.queryprocessor.spelling;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Spelling {

	private DefaultInvertedIndex index;
	
	public Spelling(DefaultInvertedIndex index) throws IOException {
		this.index = index;
	}

	private final ArrayList<String> edits(String word) {
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
		for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
		for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
		for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
		return result;
	}

	public final String correct(String word) {
		if(index.getIDFScore(word) > 0) return word;
		ArrayList<String> list = edits(word);
		HashMap<Integer, String> candidates = new HashMap<Integer, String>();
		for(String s : list) if(index.getIDFScore(s) > 0) candidates.put(index.getIDFScore(s),s);
		if(candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
		for(String s : list) for(String w : edits(s)) if(index.getIDFScore(w) > 0) candidates.put(index.getIDFScore(w),w);
		return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
	}


	
}
