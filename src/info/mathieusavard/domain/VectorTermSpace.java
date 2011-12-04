package info.mathieusavard.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class VectorTermSpace {

	private Map<Integer, Double> vector;


	public VectorTermSpace() {
		vector = new TreeMap<Integer, Double>();
	}

	private VectorTermSpace(Map<Integer, Double> vector) {
		this.vector = vector;
	}

	public Map<Integer, Double> getVector() {
		return vector;
	}

	public void setVector(Map<Integer, Double> vector) {
		this.vector = vector;
	}

	public VectorTermSpace add(VectorTermSpace b) {
		Map<Integer, Double> result = new HashMap<Integer, Double>(vector);
		for (Integer i : b.vector.keySet()) {
			Double d = this.vector.get(i);
			if (d == null)
				d = 0.0;
			d+=b.vector.get(i);
			result.put(i, d);
		}
		return new VectorTermSpace(result);

	}

	public void divideBy(Double n) {
		for (Integer i : vector.keySet()) {
			vector.put(i, vector.get(i)/n);
		}
	}

	public Double getDistanceFromVector(VectorTermSpace v) {
		Double distance = 0.0;
		for (Integer i :  this.vector.keySet()) {
			Double x1 = this.vector.get(i);
			if (v.vector.containsKey(i)) {
				Double x2 = v.vector.get(i);
				distance += Math.pow(x1-x2,2);
			} else {
				distance += Math.pow(x1,2);

			}
		}

		for (Integer onlyInSecondOne : v.vector.keySet()) {
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
				for (Integer i : vector.keySet()){
					for (Integer i2 : v.getVector().keySet()){
						if (!i.equals(i2) || v.getVector().get(i2)!=this.vector.get(i)){
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
