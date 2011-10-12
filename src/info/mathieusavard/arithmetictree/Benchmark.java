//Mathieu Dumais-Savard
//TP4 18 Mars 2009
//Benchmark.java
//Cette classe est utilisée par ArithmeticTree pour calculer le temps requis pour la comparaison de l'arbre.
package info.mathieusavard.arithmetictree;

public class Benchmark {
	private long start;
	private long stop;
	private boolean running;
	
	public long start()
	{	start=System.nanoTime();
		running=true;
		return start;
	}

	public long stop()
	{	running=false;
		stop=System.nanoTime();
		return stop;
	}

	public double getResult()
	{	if (running)
			return -1;
		else
			return Math.pow(10,-9);
	}
}
