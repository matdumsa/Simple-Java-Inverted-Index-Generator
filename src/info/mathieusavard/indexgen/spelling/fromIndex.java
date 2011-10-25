package info.mathieusavard.indexgen.spelling;
import info.mathieusavard.indexgen.DefaultInvertedIndex;
import info.mathieusavard.indexgen.IInvertedIndex;

public class fromIndex {

	public static void main(String[] args) {
		IInvertedIndex i = DefaultInvertedIndex.readFromFile("index.txt");
		for (String s : i) {
			System.out.println(i + " " + Soundex.soundex(s));
		}
	}
}
