package info.mathieusavard.domain;

import info.mathieusavard.domain.index.spimi.DefaultInvertedIndex;
import info.mathieusavard.technicalservices.Pair;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadTFIDF extends Thread {

	private LinkedBlockingQueue<Pair<GenericDocument, LinkedList<Posting>>> workToDo;
	private DefaultInvertedIndex index;
	private double corpusSize = 0;

	public ThreadTFIDF (LinkedBlockingQueue<Pair<GenericDocument, LinkedList<Posting>>> workToDo, DefaultInvertedIndex d, int corpusSize){
		this.workToDo = workToDo;
		this.index = d;
		this.corpusSize = (double) corpusSize;
	}



	public void run(){
		while (workToDo.size() > 0) {
			Pair<GenericDocument, LinkedList<Posting>> pair;
			try {
				pair = workToDo.take();
				WeightedDocument wd = (WeightedDocument) pair.getFirst();
				wd.setVector(getTFIDFVector(pair.getSecond()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private VectorTermSpace getTFIDFVector(LinkedList<Posting> linkedList) {
		VectorTermSpace vector = new VectorTermSpace();
		if (linkedList != null){
			for (Posting p : linkedList){
				vector.getVector().put(p.getTerm(), computeTFIDFScore(p));
			}
		} 
		return vector;
	}

	private Double computeTFIDFScore(Posting p) {
		double tf = (double)p.getOccurence();//(1.0+Math.log(p.getOccurence()));
		double idf = Math.log(corpusSize/(double)index.getIDFScore(p.getTerm()));
		return tf*idf;
	}

}
