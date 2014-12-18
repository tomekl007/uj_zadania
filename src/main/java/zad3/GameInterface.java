
package zad3;
/**
 * Interfejs silnika gry w zycie
 * @author oramus
 *
 */
public interface GameInterface {
	
	/**
	 * Wyjatek uzywany do sygnalizacji braku wykonania metody setRules przed nextState
	 * @author oramus
	 */
	public class RulesNotSetException extends Exception{
		private static final long serialVersionUID = 4358826696210911429L;} 
	
	/**
	 * Ustawia reguly gry za pomoca obiektu implementujacego interfejs {@linkplain LifeRulesInterface}
	 * @param lri - obiekt reprezentujacy reguly gry
	 * @see LifeRulesInterface
	 */
	void setRules( LifeRulesInterface lri );
	
	/**
	 * Ustawia aktualny stan komorek. true - komorka zyje, false - komorka martwa
	 * @param state - tablica zawierajaca stan komorek
	 */
	void setCurrentState( boolean[][] state );
	
	/**
	 * Pobranie aktualnego stanu komorek.
	 * @return - tablica zawierajaca aktualny stan komorek
	 */
	boolean[][] getCurrentState();
	
	/**
	 * Wylicza nowy stan komorek.
	 * @throws RulesNotSetException - sygnalizacja proby przetwarzania stanu komorek przy nieustalonych regulach gry 
	 */
	void nextState() throws RulesNotSetException;
}
