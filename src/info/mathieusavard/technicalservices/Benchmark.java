package info.mathieusavard.technicalservices;
import java.util.TreeMap;


public class Benchmark {
	
	private TreeMap<String, BenchmarkRow> store = new TreeMap<String, BenchmarkRow>();

	public void startTimer(String name) {
		create(name);
		store.get(name).start();
	}
	
	public void stopTimer(String name) {
		create(name);
		store.get(name).stop();
	}
	
	public void reportOnTimer(String name) {
		BenchmarkRow row = store.get(name);
		System.out.println(row.getName() + " is running for " + row.getDuration() + " ms");
	}
	
	private void create(String name) {
		if (store.containsKey(name) == false)
			store.put(name, new BenchmarkRow(name));
	}

	public void reportOnAllTimer() {
		for (String name : store.keySet())
			reportOnTimer(name);
	}
	
	
}
