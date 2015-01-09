package zad5;

public class HeavyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6367266041276304112L;
	private final int w;
	
	public HeavyException( int weight ) {
		w = weight;
	}
	
	public int getWeight() {
		return w;
	}
}
