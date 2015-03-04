package zad10;

/**
 * Interfejs systemu wyswietlaczy z obsluga wiadomosci, ktore
 * ulegaja przedawnieniu.
 * 
 * @author oramus
 *
 */
public interface DisplayTimeExt extends DisplaySystemInterfaceExt {
	/**
	 * Przesyla jedna linike tekstu do wybranego wyswietlacza.
	 * @param displayID identyfikator wyswietlacza 
	 * @param message przekazana wiadomosc
	 * @param holdTime czas przetrzymania wiadomosci na wyswietlaczu w msec.
	 * @return true - operacja zakonczona sukcesem, false - blad na skutek blednych danych wejsciowych
	 */
	public boolean toDisplay( int displayID, String message, long holdTime );
	
	/**
	 * Przesyla jedna linijke tekstu do wszystkich wyswietlaczy w grupie 
	 * @param groupID identyfikator grupy
	 * @param message przekazana wiadomosc
	 * @param holdTime czas przetrzymania wiadomosci na wyswietlaczu w msec.
	 * @return true - operacja zakonczona sukcesem, false - blad na skutek blednych danych wejsciowych
	 */
	public boolean toGroup( int groupID, String message, long holdTime );

	/**
	 * Wysyla wiadomosc do wszystkich zarejestrowanych w systemie wyswietlaczy.
	 * 
	 * @param messageToAll - wiadomosc, ktora ma pojawic sie na wszystkich wyswietlaczach.
	 * @param holdTime czas przetrzymania wiadomosci na wyswietlaczu w msec.
	 */
	public void broadcast( String messageToAll, long holdTime  );
}
