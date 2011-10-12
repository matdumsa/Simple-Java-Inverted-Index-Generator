package info.mathieusavard.indexgen;


public class Document {

	private String fullPath;
	private int id;
	
	
	public Document(String fullPath, int id) {
		super();
		this.fullPath = fullPath;
		this.id = id;
	}
	
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
