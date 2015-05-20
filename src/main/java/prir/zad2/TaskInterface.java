package prir.zad2;

public interface TaskInterface {
	/**
	 * Poziom uprzejmosci zadania - im wiekszy tym znaczenie zadania nizsze.
	 * @return - poziom uprzejmosci zadania - wartosc nieujemna
	 */
	public int getNiceLevel();
	
	/**
	 * Metoda realizuje zadanie.
	 */
	public void execute();
}
