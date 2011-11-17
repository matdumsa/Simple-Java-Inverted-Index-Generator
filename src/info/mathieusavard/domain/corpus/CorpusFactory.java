/**
 * 
 */
package info.mathieusavard.domain.corpus;

import info.mathieusavard.technicalservices.Property;

/**
 * This class is responsible for creating the proper Corpus singleton.
 * The singleton effect is garanteed by the visibility of the corpus and weightedcorpus constructor
 * @author jeremiemartinez
 *
 */
public class CorpusFactory {

	//Default visibility makes it visible to corpus class so they can throw a runtime :P
	static Corpus corpus;

	public synchronized static Corpus getCorpus(){
		if (corpus == null) {
			if (Property.getBoolean("weightedCorpus") == true){
				corpus = new WeightedCorpus();
			} else{
				corpus = new Corpus();
			}
		}
		return corpus;
	}
}
