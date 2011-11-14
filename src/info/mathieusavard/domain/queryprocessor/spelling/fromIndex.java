package info.mathieusavard.domain.queryprocessor.spelling;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import info.mathieusavard.domain.index.spimi.IInvertedIndex;

public class fromIndex {

	public static void main(String[] args) {
		IInvertedIndex i = DefaultInvertedIndex.readFromFile("index.txt");
		for (String s : i) {
			System.out.println(i + " " + Soundex.soundex(s));
		}
	}
}
