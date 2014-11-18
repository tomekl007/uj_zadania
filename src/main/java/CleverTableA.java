/**
 * Abstrakcyjna klasa przechowująca dane logiczne wraz z licznikiem czasu
 * (liczby pokolen) jaki uplynal od ustawienia w danej komorce 
 * prawdy logicznej.
 * 
 * @author oramus
 * @version 0.91
 */
public abstract class CleverTableA {
	/**
	 * Ustawia wartosc logiczna komorki o podanej pozycji. Ustawienie
	 * false resetuje do zera licznik czasu zycia tej komorki. 
	 * Ponowne ustawienie true dla komorki, ktora juz taka wartosc
	 * przechowuje, niczego nie zmienia. Operacje na komorkach o
	 * blednych adresach sa ignorowane. 
	 * @param col - kolumna, w ktorej znajduje sie komorka, ktorej wartosc ma byc ustawiona
	 * @param row - wiersz, w ktorym znajduje sie komorka, ktorej wartosc ma byc ustawiona
	 * @param value - wartosc do zapisania w komorce
	 */
	abstract public void set( int col, int row, boolean value );
	
	/**
	 * Zwraca wartosc logiczna komorki o podanym adresie. Operacja odczytu stanu
	 * komorki o blednym adresie zwraca zawsze false.
	 * @param col - kolumna, w ktorej znajduje sie komorka, ktorej wartosc ma byc odczytana
	 * @param row - wiersz, w ktorym znajduje sie komorka, ktorej wartosc ma byc odczytana
	 * @return - zawartosc komorki o podanym adresie
	 */
	abstract public boolean get( int col, int row );
	
	/** Zwraca wiek komorki. Wiek rozny od zera maja wylacznie komorki z wpisana
	 * wartoscia logiczna true. Dla komorek zawierajacych false zawsze zero. Wiek
	 * komorki to liczba wykonan metody nextGeneration od chwili gdy komorka
	 * jest ustawiona na true. Ustawienia komorki na false resetuje licznik wieku.
	 * W przypadku podania blednych wspolrzednych komorki zwraca Integer,MIN_VALUE
	 *
	 * @param col - kolumna, w ktorej znajduje sie komorka, ktorej wiek ma byc odczytany
	 * @param row -  wiersz, w ktorym znajduje sie komorka, ktorej wiek ma byc odczytany
	 * @return - wiek komorki lub Integer.MIN_VALUE
	 * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/Integer.html#MIN_VALUE">Integer.MIN_VALUE</a>
	 */
	abstract public int getAge( int col, int row );
	
	/** 
	 * Ustawia rozmiar tablicy do przechowywania danych. Tablica jest kwadratowa. 
	 * Wszystkie pola po wykonaniu tej metody ustatawione sa na false
	 * @param size - rozmiar tablicy
	 */
	abstract public void setSize( int size );
	
	/**
	 * Zwraca aktualny rozmiar tablicy
	 * @return - aktualny rozmiar tablicy
	 */
	abstract public int getSize();
	
	/** 
	 * Zwieksza rozmiar tablicy o 2. Poprzednie dane otaczane sa marginesem o rozmiarze
	 * (grubosci) jednej komorki. Stan komorek i ich wiek jest przenoszony do nowej,
	 * wiekszej tablicy.
	 */
	abstract public void grow();
	
	/**
	 * Zwieksza o jeden wiek wszyskich komorek ustawionych na true. 
	 */
	abstract public void nextGeneration();
}
