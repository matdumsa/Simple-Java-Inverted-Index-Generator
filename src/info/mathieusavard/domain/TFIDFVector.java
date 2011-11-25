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
		DecimalFormat df = new DecimalFormat("0.00");
		for (int i = 0; i < vector.size(); i++){
			result = result +  vector.firstEntry().getKey() + ":" + df.format(vector.pollFirstEntry().getValue())+ " ";
		}
		return result;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof TFIDFVector){
			TFIDFVector v = (TFIDFVector)o;
			if (v.getVector().size()!=this.vector.size()){
				return false;
			} else {
			for (String s : vector.keySet()){
				for (String s2 : v.getVector().keySet()){
					if (!s.equals(s2) || v.getVector().get(s2)!=this.vector.get(s)){
						return false;
					}
				}
				
			}
			return true;
			}
		} else{
			return false;
		}
	}
	
}
