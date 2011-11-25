package info.mathieusavard.domain;

import info.mathieusavard.domain.corpus.WeightedCorpus;
import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;

import java.util.LinkedList;
import java.util.TreeMap;

public class ThreadTFIDF extends Thread {

	private TreeMap<GenericDocument, LinkedList<Posting>> data;
	private WeightedCorpus corpus;
	private int NUMBER_MAX_DOC;
	private DefaultInvertedIndex index;
	private static Integer currentArticle = 0;

	public ThreadTFIDF (TreeMap<GenericDocument, LinkedList<Posting>> data, WeightedCorpus c, DefaultInvertedIndex d){
		this.data = data;
		this.corpus = c;
		this.index = d;
		NUMBER_MAX_DOC = data.size();
	}



	public void run(){
		int articleToProcess;
		synchronized(currentArticle){
			articleToProcess = currentArticle;
			currentArticle++;
		}
		while (articleToProcess < NUMBER_MAX_DOC){
			synchronized(currentArticle){
				articleToProcess = currentArticle;
				currentArticle++;
			}
			if ((articleToProcess % 500) == 0 ){
				System.out.println(articleToProcess + " Vectors TFIDF computed");
			}
			WeightedDocument docCorpus = (WeightedDocument)corpus.findArticle(articleToProcess);
			if (docCorpus!=null){
				docCorpus.setVector(getTFIDFVector(data.get(docCorpus)));
			}
		}
	}

	private TFIDFVector getTFIDFVector(LinkedList<Posting> linkedList) {
		TFIDFVector vector = new TFIDFVector();
		if (linkedList != null){
			for (Posting p : linkedList){
				vector.getVector().put(p.getTerm(), computeTFIDFScore(p));
			}
		} 
		return vector;
	}

	private Double computeTFIDFScore(Posting p) {
		double tf = (double)p.getOccurence();//(1.0+Math.log(p.getOccurence()));
		double idf = Math.log((double)data.size()/(double)index.getIDFScore(p.getTerm()));
		return tf*idf;
	}

}
