package zad5;

import java.util.List;


public interface StrangeSorterInterface {
	
	/**
	 * Ustawia referencje do obiektu zgodnego z 
	 * interfejsem {@linkplain StrangeInterface}.
	 * 
	 * @param si - referencja do obiektu implementujacego StrangeInterface.
	 */
	public void setStrangeInterface( StrangeInterface si );
	
	/**
	 * Zwraca posortowana rosnaco liste wyjatkow typu wyjatku przeslanego jako wzor. 
	 * Zwracane wyjatki pochodza z wywolywania metody get obiektu zgodnego
	 * z {@linkplain StrangeInterface}. Jesli przeslany wyjatek
	 * nie jest znanego typu (czyli nie jest albo typu {@linkplain HeavyException}
	 * lub {@linkplain TransparentException} metoda zwraca liste
	 * zawierajaca wszystkie odebrane wyjatki w kolejnosci ich uzyskania. 
	 * 
	 * @param example - przykladowy wyjatek typu, ktory ma zostac po posortowaniu zwrocony
	 * @return - posortowana rosnaco lista wyjatkow.
	 */
	public List< Exception > getExceptions( Exception example );
}
