package zad9;

import java.util.List;
import java.util.Map;

/**
 * Interfejs systemu przydzialu sal.
 * 
 * @author oramus
 *
 */
public interface RoomsAllocationSystemInterface {
	public static enum AttributeType {
		STRING, INTEGER, BOOLEAN
	}
	
	public static enum RoomType {
		COMPUTER_LAB, OTHER
	}
	
	/**
	 * Dodaje atrubut do systemu
	 * @param name - unikalna nazwa atrybutu (dziala jako klucz)
	 * @param at - typ atrybutu
	 */
	void addAttribute(String name, AttributeType at);
	
	/**
	 * Dodaje pokoj (pomieszczenie) do systemu
	 * @param id - identyfikator w systemie (np. A-1-04)
	 * @param rt - typ pomieszczenia
	 * @param ns - liczba siedzen
	 */
	void addRoom(String id, RoomType rt, int ns);
	
	/**
	 * Pobiera atrybuty i ich wartosci zapamietane dla pomieszczenia.
	 * 
	 * @param id - identyfikator pomieszczenia
	 * @return atrybuty i ich wartosci dla wskazanego pomieszczenia. 
	 * Ciag znakow (klucz) ma zawierac nazwe atrybutu. 
	 */
	Map< String, Object > getRoomAttributes(String id);
	
	/**
	 * Dodaje atrybut z wartoscia calkowitoliczbowa do pomieszczenia
	 * @param name - nazwa atrybutu
	 * @param id - identyfikator pomieszczenia
	 * @param value - wartosc
	 */
	void addAttributeToRoom(String name, String id, Integer value);

	/**
	 * Dodaje atrybut z wartoscia opisowa do pomieszczenia
	 * @param name - nazwa atrybutu
	 * @param id - identyfikator pomieszczenia
	 * @param value - wartosc
	 */
	void addAttributeToRoom(String name, String id, String value);
	/**
	 * Dodaje atrybut z wartoscia logiczna do pomieszczenia
	 * @param name - nazwa atrybutu
	 * @param id - identyfikator pomieszczenia
	 * @param value - wartosc
	 */
	void addAttributeToRoom(String name, String id, Boolean value);

	/**
	 * Harmonogram uzycia sali. Dla uproszczenia sale rezerwowane sa w obrebie jednego
	 * roku akademickiego. Sale rezerwowane sa na pelne godziny. Suma <tt>beginHour</tt> i
	 * <tt>hours</tt> nie przekracza 24. 
	 * <ul>
	 * <li><tt>firstDay</tt> - pierwszy dzien, w ktorym sala jest potrzebna. Dni liczymy od poczatku
	 * roku akademickiego. Wartosci od 0 do 355.</li>
	 * <li><tt>beginHour</tt> - godzina rozpoczecia uzycia sali. Od 0 do 23.</li>
	 * <li><tt>hours</tt> - liczba godzin, na ktore sala jest rezerwowana.</li>
	 * <li><tt>numerOfUses</tt> - liczba powtorzen zdarzenia, dla ktorego sala jest rezerwowana.</li>
	 * </ul>
	 */
	public class Schedule {
		final public int firstDay;
		final public int beginHour;
		final public int hours;
		final public int numberOfUses;// Powtórzenia występują co tydzień.

        @Override
        public String toString() {
            return "Schedule{" +
                    "firstDay=" + firstDay +
                    ", beginHour=" + beginHour +
                    ", hours=" + hours +
                    ", numberOfUses=" + numberOfUses +
                    '}';
        }

        public Schedule( int firstDay, int beginHour, int hours, int numerOfUses ) {
			this.firstDay = firstDay;
			this.beginHour = beginHour;
			this.hours = hours;
			this.numberOfUses = numerOfUses;
		}
	}
	
	/**
	 * Rezerwuje sale wg. okreslonego harmonogramu. 
	 * 
	 * @param id - identyfikator pomieszczenia
	 * @param sch - harmonogram
	 * @return <tt>true</tt> - operacja zakonczona sukcesem, sala zostala zarezerwowana
	 * <br><tt>false</tt> - sali nie udalo sie zarezerwowac 
	 */
	boolean allocateRoom(String id, Schedule sch);
	
	/**
	 * Zwraca liste pomieszczen spelniajacych wskazane warunki. Jesli liczba pomieszczen
	 * spelniajacych warunki jest wieksza od 0, lista jest sortowana wg. nadeslanych wag.
	 * 
	 * @param rt - typ wymaganego pomieszczeia
	 * @param sch - harmonogram zajec, na potrzebe ktorych pomieszcznie jest poszukiwane
	 * @param seatsLimit - wymagana liczba miejsc
	 * @param requiredAttributes - wymagane atrubuty logiczne. Wymagana jest pelna zgodnosc ze
	 * wszystkimi pozycjami.
	 * @param attributesLimit - wymagane wartosci minimalne atrybutow calkowitoliczbowych.
	 * @param attributesWeight - punktowe wagi atrybutow, wg. ktorych lista pomieszczen
	 * spelniajacych poprzednie wymagania jest sortowana
	 * @return posortowana lista pomieszczen lub pusta lista (empty), gdy brak propozycji.
	 */
	List< String > getRooms(RoomType rt, Schedule sch,
                            int seatsLimit,
                            Map<String, Boolean> requiredAttributes,
                            Map<String, Integer> attributesLimit,
                            Map<String, Integer> attributesWeight);
}
