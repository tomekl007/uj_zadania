package zad10;
/**
 * Interfejs obslugi systemu wyswietlaczy alfanumerycznych.
 * @author oramus
 *
 */
public interface DisplaySystemInterface {
	/**
	 * Rejestruje wyswietlacz w systemie. Wyswietlacz uzyskuje unikalny identyfikator.
	 * @param rows liczba wierszy tekstu
	 * @return unikalny identyfikator wyswietlacza w systemie.
	 */
	public int registerDisplay( int rows );
	
	/**
	 * Tworzy grupe wyswietlaczy. Grupie nadawany jest unikalny identyfikator.
	 * @return unikalny identyfikator grupy
	 */
	public int createGroup();
	
	/**
	 * Dodaje wyswietlacz do grupy.
	 * @param displayID identyfikator wyswietlacza
	 * @param groupID identyfikator grupy, do ktorej wyswietlacza jest dodawany
	 * @return true - operacja zakonczona sukcesem, false - operacja nie mogla zostac wykonana ze wzgledu na
	 * dostarczenie blednych danych
	 */
	public boolean addDisplayToGroup( int displayID, int groupID );
	
	/**
	 * Usuwa grupe z systemu.
	 * @param groupID identyfikator usuwanej grupy
	 * @return true - operacja zakonczona sukcesem, false - blad na skutek blednych danych wejsciowych
	 */
	public boolean removeGroup( int groupID );
	
	/**
	 * Odlacza wyswietlacz od systemu
	 * @param displayID identyfikator wyswietlacza odlaczanego od systemu
	 * @return true - operacja zakonczona sukcesem, false - blad na skutek blednych danych wejsciowych
	 */
	public boolean deregisterDisplay( int displayID );

	/**
	 * Przesyla jedna linike tekstu do wybranego wyswietlacza.
	 * @param displayID identyfikator wyswietlacza 
	 * @param message przekazana wiadomosc
	 * @return true - operacja zakonczona sukcesem, false - blad na skutek blednych danych wejsciowych
	 */
	public boolean toDisplay( int displayID, String message );

	/**
	 * Przesyla jedna linijke tekstu do wszystkich wyswietlaczy w grupie 
	 * @param groupID identyfikator grupy
	 * @param message przekazana wiadomosc
	 * @return true - operacja zakonczona sukcesem, false - blad na skutek blednych danych wejsciowych
	 */
	public boolean toGroup( int groupID, String message );
	
	/**
	 * Zwraca wiadomosci przeslane do wyswietlacza o danym identyfikatorze. Tablica 
	 * nie moze byc dluzsza od ilosci wierszy wyswietlacza. Wiadomosci
	 * ulozone sa w tablicy wynikowej w kolejnosci ich naplywania: od najstarszych (u gory)
	 * do najnowszych (na dole).
	 * @param displayID - identyfikator wyswietlacza
	 * @return - tablica zawierajace wiadomosci przeslane do wswietlacza z uwzglednieniem
	 * ilosci wierszy tekstu, jakie moze on wyswietlac. null - dla blednego identyfikatora lub
	 * braku wiadomosci.
	 */
	public String[] get( int displayID );
}
