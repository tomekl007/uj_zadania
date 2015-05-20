package prir.zad2;

public interface MultithreadTaskManagementInterface {
	/**
	 * Ustala maksymalną liczbe watkow, ktore maja byc uzywane do przetwarzania naplywajacych
	 * zadan. Liczba watkow moze ulec zmianie w trakcie pracy systemu. System
	 * dostosowuje sie do aktualnego limitu mozliwie najszybciej (wzrost liczby
	 * dostepnych watkow przy dostepnych do wykonania zadaniach realizowany
	 * jest praktycznie natychmiastowo, ograniczenie liczby watkow realizowane
	 * jest w miare konczenia sie zadan realizowanych w chwili wykonania
	 * metody).
	 * 
	 * @param cores - liczba watkow do uzycia.
	 */
	public void setNumberOfAvailableThreads(int threads);
	
	/**
	 * Metoda ustala maksymalna liczbe watkow jakie wolno uzyc do prztwarzania
	 * zadan na danym poziomie uprzejmosci. Zadania o poziomach
	 * uprzejmosci wyzszych niz tu podane maja byc wykonywane 
	 * z uzyciem maksymalnie jednego watku. Limit moze zostac podany jednokrotnie
	 * przed nadeslaniem do systemu pierwszego zadania.
	 * 
	 * @param threadsLimit - limit ilosci watkow do uzycia na danym poziomie 
	 * uprzejmosci zadania.
	 */
	public void setMaximumThreadsPerNiceLevel(int[] threadsLimit);
	
	/**
	 * Metoda pozwala zglosic zadanie do wykonania.
	 * 
	 * @param ti - referencja do obiekt reprezentujacego zadanie do wykonania.
	 */
	public void newTask(TaskInterface ti);
}
