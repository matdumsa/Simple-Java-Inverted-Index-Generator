package info.mathieusavard.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class VectorTermSpace {

	private Map<String, Double> vector;


	public VectorTermSpace() {
		vector = new TreeMap<String, Double>();
	}

	public VectorTermSpace(Map<String, Double> vector) {
		this.vector = vector;
	}

	public Map<String, Double> getVector() {
		return vector;
	}

	public void setVector(Map<String, Double> vector) {
		this.vector = vector;
	}

	public VectorTermSpace add(VectorTermSpace b) {
		Map<String, Double> result = new HashMap<String, Double>(vector);
		for (String s : b.vector.keySet()) {
			Double d = result.get(s);
			if (d == null)
				d = 0.0;
			d+=b.vector.get(s);
			result.put(s, d);
		}
		return new VectorTermSpace(result);

	}

	public void divideBy(Double n) {
		for (String s : vector.keySet()) {
			vector.put(s, vector.get(s)/n);
		}
	}

	public Double getDistanceFromVector(VectorTermSpace v) {
		Double distance = 0.0;
		for (String s :  this.vector.keySet()) {
			Double x1 = this.vector.get(s);
			Double x2;
			if (v.vector.containsKey(s)) {
				x2 = v.vector.get(s);
				distance += Math.pow(x1-x2,2);
			} else {
				distance += Math.pow(x1,2);

			}
		}

		for (String onlyInSecondOne : v.vector.keySet()) {
			if (this.vector.containsKey(onlyInSecondOne) == false) {
				Double x1 = v.vector.get(onlyInSecondOne);
				distance += Math.pow(x1,2);
			}
		}
		
		return Math.sqrt(distance);

	}

	@Override
	public boolean equals(Object o){
		if (o instanceof VectorTermSpace){
			VectorTermSpace v = (VectorTermSpace)o;
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
