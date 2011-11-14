package info.mathieusavard.domain.index.compression;


public class Stemmer {

	private PorterStemmer instance = new PorterStemmer();
	
	public String stem(String word) {
		instance.add(word.toCharArray(), word.length());
		instance.stem();
		return instance.toString();
	}
	
	public String stemWithCache(String word) {
		
		String answer = stem(word);
		return answer;
	}

	public static void main(String[] args) {
		Stemmer s = new Stemmer();
		System.out.println("Container:" + s.stem("Container"));
	}
}
