package prir.zad1;

public interface ManagementInterface {
	/**
	 * Metoda rejestruje w serwisie odbiorce zdarzen. Po zakonczeniu procesu
	 * rejestracji nowe zdarzenia maja juz docierac do odbiorcy.
	 * @param pei - referencja do odbiorcy zdarzen
	 */
	public void registerProcessingEngine(ProcessingEngineInterface pei);
	
	/**
	 * Metoda pozwala odlaczyc odbiorce od systemu. Z chwila zakonczenia
	 * pracy metody zdarzenia maja juz do odbiorcy nie docierac.
	 * W przypadku zgloszenia odbiorcy, ktory wczesniej nie zostal zarejestrowany
	 * takie wykonanie jest ignornorowane.
	 * 
	 * @param pei - referencja do odlaczanego od systemu odbiorcy zdarzen
	 */
	public void deregisterProcessingEngine(ProcessingEngineInterface pei);
	
	/**
	 * Metoda pozwala na dostarczenie do systemu nowego zdarzenia.
	 * 
	 * @param ei - zdarzenie do przetworzenia przez system.
	 */
	public void newEvent(EventInterface ei);
}
