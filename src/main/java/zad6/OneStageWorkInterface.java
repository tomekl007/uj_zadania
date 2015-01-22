package zad6;

/**
 * Interfejs wykonawcy fragmentu pracy. 
 * 
 * @author oramus
 *
 */
public interface OneStageWorkInterface {
	
	/**
	 * Zleca wykonanie pracy przypadającej na okreslony jej etap.
	 * @param stage - numer etapu, dla ktorego nalezy wykonac prace.
	 */
	void work(int stage);
}
