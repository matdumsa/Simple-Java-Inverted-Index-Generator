package info.mathieusavard.domain.index.spimi;

import info.mathieusavard.domain.Corpus;
import info.mathieusavard.domain.Posting;
import info.mathieusavard.technicalservices.Constants;

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
				buffReadArr[i] = new LineNumberReader(new FileReader(Constants.basepath + "/" + String.valueOf(i) + ".spimi"));
				lastLineRead[i]= null;
			}

			//Sweet, ready to go!
			String currentToken = findFirstToken(buffReadArr, lastLineRead);
			out.write(currentToken);
			while (currentToken != null) {				
				HashSet<Posting> postings = obtainPostingsForTokenInReaderArray(currentToken, buffReadArr, lastLineRead);
				for (Posting i : postings) {
					out.write(" " + i.toString());					
				}
				currentToken = findFirstToken(buffReadArr, lastLineRead);
				out.write("\n" + currentToken);
			}
			
			out.close();

			//Get rid of these files now.
			for (int i=0; i<=totalBlock; i++) {
				(new File(Constants.basepath + "/" + String.valueOf(i) + ".spimi")).delete();
			}
			
			Corpus.writeToDisk();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static HashSet<Posting> obtainPostingsForTokenInReaderArray(String currentToken, LineNumberReader[] buffReadArr, String[] lastLineRead) throws IOException {
		HashSet<Posting> result = new HashSet<Posting>();
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
					Posting p = Posting.fromString(s);
					if (result.add(p) == false)
						for (Posting p2 : result)
						{
							if (p.equals(p2))
								p2.add(p.getOccurence());
						}
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
