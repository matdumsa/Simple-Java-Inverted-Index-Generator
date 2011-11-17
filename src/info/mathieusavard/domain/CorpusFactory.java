/**
 * 
 */
package info.mathieusavard.domain;

import info.mathieusavard.technicalservices.Property;

/**
 * @author jeremiemartinez
 *
 */
public class CorpusFactory {

	private static Corpus corpus;
	
	static {
		if (Property.getBoolean("weightedCorpus") == true){
			corpus = new WeightedCorpus();
		} else{
			corpus = new Corpus();
		}
	}

	public static Corpus getCorpus(){
		return corpus;
	}
	
}
