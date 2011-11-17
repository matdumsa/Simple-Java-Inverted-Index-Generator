package info.mathieusavard.domain;

import java.text.DecimalFormat;
import java.util.TreeMap;

public class TFIDFVector {

	private TreeMap<String, Double> vector = new TreeMap<String, Double>();

	public TFIDFVector() {}

	public TreeMap<String, Double> getVector() {
		return vector;
	}

	public void setVector(TreeMap<String, Double> vector) {
		this.vector = vector;
	}
	
	public String toString(){
		String result = "";
		DecimalFormat df = new DecimalFormat("0.000000000");
		for (int i = 0; i < vector.size(); i++){
			result = result +  vector.firstEntry().getKey() + ":" + df.format(vector.pollFirstEntry().getValue())+ " ";
		}
		return result;
	}
	
}
