import java.util.HashMap;


public class Stemmer {

	private PorterStemmer instance = new PorterStemmer();
	private HashMap<String, String> cache = new HashMap<String, String>();
	
	public String stem(String word) {
		instance.add(word.toCharArray(), word.length());
		instance.stem();
		return instance.toString();
	}
	
	public String stemWithCache(String word) {
		//Is this response cached?
		if (cache.containsKey(word))
			return cache.get(word);
		
		String answer = stem(word);
		//Store in cache
		cache.put(word, answer);
		return answer;
	}

	public static void main(String[] args) {
		Stemmer s = new Stemmer();
		System.out.println("Container:" + s.stem("Container"));
	}
}
