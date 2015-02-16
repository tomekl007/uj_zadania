package zad8;

/**
 * Rozszerzony interfejs oblslugi systemu wyswietlaczy
 * 
 * @author oramus
 *
 */
public interface DisplaySystemInterfaceExt extends DisplaySystemInterface {
    /**
	 * Dodaje grupe wyswietlaczy do innej grupy wyswietlaczy.
	 * 
	 * @param sourceGroupID - identyfikator grupy dodawanej
	 * @param destinationGroupID - identyfikator grupy, do ktorej inna jest dodawana
	 * @return <tt>true</tt> - dane wejsciowe sa poprawne i operacja zakonczyla sie sukcesem,
	 * <br><tt>false</tt> - operacja nie mogla zostac przeprowadzona ze wzgledu na blad
	 * w danych wejsciowych.
	 */
	public boolean addGroupToGroup( int sourceGroupID, int destinationGroupID );
	
	/**
	 * Usuwa grupe z grupy.
	 * 
	 * @param groupToRemoveID - identyfikator grupy do usuniecia
	 * @param removeFromGroupID - identyfikator grupy, z ktorej inna jest usuwana
	 * @return - <tt>true</tt> - operacja zakonczona sukcesem, 
	 * <br><tt>false</tt> - operacji nie mozna bylo wykonac, bo dostarczone dane sa bledne.
	 */
	public boolean removeGroupFromGroup( int groupToRemoveID, int removeFromGroupID );
	
	
	/**
	 * Wysyla wiadomosc do wszystkich zarejestrowanych w systemie wyswietlaczy.
	 * 
	 * @param messageToAll - wiadomosc, ktora ma pojawic sie na wszystkich wyswietlaczach.
	 */
	public void broadcast( String messageToAll );
	
}