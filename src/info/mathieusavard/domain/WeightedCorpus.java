package info.mathieusavard.domain;

import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import java.util.LinkedList;
import java.util.TreeMap;



/**
 * @author jeremiemartinez
 *
 */
public class WeightedCorpus extends Corpus {
	
	private static DefaultInvertedIndex index;  
	
	public static void closeIndex(){
		computeTFIDFVector();
		writeToDisk();
	}

	private static void computeTFIDFVector() {
		index = DefaultInvertedIndex.readFromFile("index.txt");
		TreeMap<GenericDocument, LinkedList<Posting>> data = index.getDocumentBasedIndex();
		for (GenericDocument gd : data.keySet()){
			WeightedDocument docCorpus = (WeightedDocument)findArticle(gd.getId());
			docCorpus.setVector(getTFIDFVector(data.get(docCorpus)));
		}
	}

	private static TFIDFVector getTFIDFVector(LinkedList<Posting> linkedList) {
		TFIDFVector vector = new TFIDFVector();
		for (Posting p : linkedList){
			vector.getVector().put(p.getTerm(), computeTFIDFScore(p));
		}
		return vector;
	}

	private static Double computeTFIDFScore(Posting p) {
		double tf = (1+Math.log(p.getOccurence()));
		double idf = Math.log(documentMap.size()/index.getSet(p.getTerm()).size());
		return tf*idf;
	}

}
