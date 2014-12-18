package zad3;
/**
 * Interfejs regol gry w Life.
 * @author oramus
 *
 */
public interface LifeRulesInterface {
	/**
	 * Metoda zwraca informacje czy komorka o przeslanych parametrach wlasnych i otoczenia bedzie
	 * zyc w kolejnym stanie gry.
	 * @param state - stan komorki. true - zyje, false - martwa 
	 * @param numberOfLiveNeighbors - liczba zywych sasiadow, od 0 do 8 
	 * @return - stan komorki w kolejnym stanie gry
	 */
	boolean isOn( boolean state, int numberOfLiveNeighbors  );
}
