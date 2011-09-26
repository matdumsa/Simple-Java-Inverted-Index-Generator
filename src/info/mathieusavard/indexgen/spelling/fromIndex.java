package info.mathieusavard.indexgen.spelling;
import info.mathieusavard.indexgen.InvertedIndex;

public class fromIndex {

	public static void main(String[] args) {
		InvertedIndex i = InvertedIndex.readFromFile("index.txt");
		for (String s : i) {
			System.out.println(i + " " + Soundex.soundex(s));
		}
	}
}
