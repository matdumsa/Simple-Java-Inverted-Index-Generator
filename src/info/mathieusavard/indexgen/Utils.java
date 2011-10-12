package info.mathieusavard.indexgen;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class Utils {

	/**
	 * Look for files matching an extension in a given folder
	 * @param directoryPath path to look for
	 * @param extension extension to filter for
	 * @param relativePath if true will return the path of the file relative to the directoryPath. False will return a full path
	 * @return array of string, one string per found file
	 */
	public static List<String> getAllFiles(String directoryPath, final String extension, boolean relativePath) {
		File dir = new File(directoryPath);

		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	if (name.startsWith("."))
		    		return false;
		    	if (name.endsWith(extension))
		    		return true;
		    	return false;
		    }
		};
		
		String[] r = dir.list(filter);
		if (relativePath == false)
			for (int i=0; i<r.length; i++)
				r[i] = dir.getAbsolutePath() + "/" + r[i];

		List<String> result = new ArrayList<String>();
		if (r != null) {
			result = Arrays.asList(r);
		}
		return result;
	}
	
	static Pattern noTags = java.util.regex.Pattern.compile("\\<.*?\\>");
	public static String removeTags(String input) {
		return noTags.matcher(input).replaceAll("");
	}

	static Pattern noEntities = java.util.regex.Pattern.compile("\\&.*?\\;");
	public static String removeEntities(String input) {
		return noEntities.matcher(input).replaceAll("");
	}
	
	static Pattern noSpecialChar = java.util.regex.Pattern.compile("[^a-z|0-9]");
	public static String removeSpecialChar(String input) {
		return noSpecialChar.matcher(input).replaceAll("");
	}
	
	public static void main(String[] args) {
		List<String> files = Utils.getAllFiles("reuters21578", ".sgm", false);
		for (String f : files) {
			System.out.println(f);
		}
		
		System.out.println("H@#$ELLO world === " + removeSpecialChar("123H@#$ELLO world"));
	}
	
}
