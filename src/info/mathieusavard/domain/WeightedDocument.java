/**
 * 
 */
package info.mathieusavard.domain;


/**
 * @author jeremiemartinez
 *
 */
public class WeightedDocument extends GenericDocument{
	
	private TFIDFVector vector;
	
	public WeightedDocument(int id, String title) {
		super(id, title);
	}

	public TFIDFVector getVector() {
		return vector;
	}

	public void setVector(TFIDFVector vector) {
		this.vector = vector;
	}
	
	
}
