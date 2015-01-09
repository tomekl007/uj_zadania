package zad5;

public class TransparentException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7145568360632343443L;
	private final int transparency;
	
	public TransparentException( int transparency ) {
		this.transparency = transparency;
	}
	
	public int getTransparency() {
		return transparency;
	}
}
