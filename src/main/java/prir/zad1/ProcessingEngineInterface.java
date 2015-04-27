package prir.zad1;

/**
 * Interfejs obiektu przetwarzajacego zdarzenia.
 *
 */
public interface ProcessingEngineInterface {
	/**
	 * Metoda testuje, czy odbiorca jest zainteresowany przetworzeniem zdarzenia.
	 * Dla jednego obiektu metoda moze byc wykona tylko z jednego watku jednoczesnie.
	 * 
	 * @param ei - testowane zdarzenie
	 * @return true - zdarzenie jest wazne dla odbiorcy, false - zdarzenie nieciekawe
	 */
	boolean isItImportant(EventInterface ei);
	
	/**
	 * Metoda przetwarza zdarzenie.
	 * Dla jednego obiektu metoda moze byc wykona tylko z jednego watku jednoczesnie.
	 *
	 * @param ei - zdarzenie do przetworzenia.
	 * 
	 */
	void processEvent(EventInterface ei);
}
