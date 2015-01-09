package zad5;

public interface StrangeInterface {
	/**
	 * Metoda, ktora nalezy wywolywac az do chwili uzyskania 
	 * referencji do obiektu. Orzymane wyjatki
	 * nalezy zapamietac do dalszych celow.
	 * @return - referencja do jakiegos obiektu
	 * @throws HeavyException 
	 * @throws TransparentException
	 */
	public Object get() throws HeavyException, TransparentException;
}
