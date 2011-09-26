package info.mathieusavard.indexgen;

public class BenchmarkRow {
	
	private String name;
	private long start = 0;
	private long runningFor = 0;

	public BenchmarkRow(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void start() {
		start = System.currentTimeMillis();
	}
	
	public void stop() {
		runningFor += System.currentTimeMillis() - start;
		start=0;
	}

	public long getDuration() {
		// TODO Auto-generated method stub
		return runningFor;
	}
}