/**
 * 
 */
package info.mathieusavard.domain;


/**
 * @author jeremiemartinez
 *
 */
public class WeightedDocument extends GenericDocument{
	
	private VectorTermSpace vector;
	
	public WeightedDocument(int id, String title) {
		super(id, title);
	}

	public VectorTermSpace getVector() {
		return vector;
	}

	public void setVector(VectorTermSpace vector) {
		this.vector = vector;
	}
	
	
}
