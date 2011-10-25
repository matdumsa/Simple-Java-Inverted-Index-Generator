package info.mathieusavard.indexgen;

import info.mathieusavard.utils.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashSet;

public class SPIMIReconciliation {

	public static void reconciliate() {
		int totalBlock = SPIMIInvertedIndex.getTotalBlock();

		LineNumberReader[] buffReadArr = new LineNumberReader[totalBlock+1];
		String[] lastLineRead = new String[totalBlock+1];
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(Constants.basepath + "/index.txt"));

			for (int i=0; i<=totalBlock; i++) {
				buffReadArr[i] = new LineNumberReader(new FileReader(Constants.basepath + "/" + String.valueOf(i)));
				lastLineRead[i]= null;
			}

			//Sweet, ready to go!
			String currentToken = findFirstToken(buffReadArr, lastLineRead);
			out.write(currentToken);
			while (currentToken != null) {				
				HashSet<Integer> postings = writePostingToFile(currentToken, buffReadArr, lastLineRead);
				for (int i : postings) {
					out.write(" " + Integer.toString(i, Character.MAX_RADIX));					
				}
				currentToken = findFirstToken(buffReadArr, lastLineRead);
				out.write("\n" + currentToken);
			}

			//Get rid of these files now.
			for (int i=0; i<=totalBlock; i++) {
				(new File(Constants.basepath + "/" + String.valueOf(i))).delete();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static HashSet<Integer> writePostingToFile(String currentToken, LineNumberReader[] buffReadArr, String[] lastLineRead) throws IOException {
		HashSet<Integer> result = new HashSet<Integer>();
		for (int i=0; i<buffReadArr.length; i++ ) {
			String line = lastLineRead[i];
			if (line == null)
				continue;
			String token = readUntil(line,' ');
			if (token.equals(currentToken) == false) {
				continue;
			}
			else {
				lastLineRead[i] = null; //reset that line to allow the reading of another one
				boolean skip = true;
				for (String s : line.split(" ")){
					if (skip) {
						skip=false;
						continue;
					}
					result.add(Integer.parseInt(s, Character.MAX_RADIX));
				}
			}
		}
		return result;
	}

	private static String findFirstToken(LineNumberReader[] buffReadArr, String[] lastLineRead) throws IOException {
		String smallestToken = null;
		for (int i =0; i< buffReadArr.length; i++) {
			if (lastLineRead[i] == null)
				lastLineRead[i] = buffReadArr[i].readLine();
			String line = lastLineRead[i];
			if (line == null)
				continue;
			String token = readUntil(line,' ');
			if (smallestToken == null)
				smallestToken = token;
			else
				if (token.compareTo(smallestToken) < 0)
					smallestToken = token;
		}
		return smallestToken;
	}

	private static String readUntil(String line, char cha) {
		return line.substring(0, line.indexOf(cha));
	}
}
