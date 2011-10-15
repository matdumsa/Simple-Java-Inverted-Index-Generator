package info.mathieusavard.indexgen.spelling;
import info.mathieusavard.indexgen.DefaultPostingList;
import info.mathieusavard.indexgen.IPostingList;

public class fromIndex {

	public static void main(String[] args) {
		IPostingList i = DefaultPostingList.readFromFile("index.txt");
		for (String s : i) {
			System.out.println(i + " " + Soundex.soundex(s));
		}
	}
}
