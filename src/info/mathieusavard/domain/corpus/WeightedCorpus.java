package info.mathieusavard.domain.corpus;

import info.mathieusavard.domain.GenericDocument;
import info.mathieusavard.domain.Posting;
import info.mathieusavard.domain.TFIDFVector;
import info.mathieusavard.domain.WeightedDocument;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;

import java.util.LinkedList;
import java.util.TreeMap;



/**
 * @author jeremiemartinez
 *
 */
public class WeightedCorpus extends Corpus {
	
	private DefaultInvertedIndex index;  
	
	//Default constructor allow only the factory in this package to create instances
	WeightedCorpus() {
		super();
	}
	public void closeIndex(){
		computeTFIDFVector();
		super.closeIndex();
	}

	private void computeTFIDFVector() {
		index = DefaultInvertedIndex.readFromFile("index.txt");
		TreeMap<GenericDocument, LinkedList<Posting>> data = index.getDocumentBasedIndex();
		for (GenericDocument gd : data.keySet()){
			if (gd instanceof WeightedDocument) {
				WeightedDocument docCorpus = (WeightedDocument)findArticle(gd.getId());
				docCorpus.setVector(getTFIDFVector(data.get(docCorpus)));
			}
		}
	}

	private TFIDFVector getTFIDFVector(LinkedList<Posting> linkedList) {
		TFIDFVector vector = new TFIDFVector();
		for (Posting p : linkedList){
			vector.getVector().put(p.getTerm(), computeTFIDFScore(p));
		}
		return vector;
	}

	private Double computeTFIDFScore(Posting p) {
		double tf = (1+Math.log(p.getOccurence()));
		double idf = Math.log(documentMap.size()/index.getSet(p.getTerm()).size());
		return tf*idf;
	}


}
