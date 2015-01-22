package zad6;

/**
 * Interfejs pozwala zarejestrowac i odrejestrowac wykonawcow prac
 * realizowanych w wielu nastepujacych po sobie etapach. Po zakonczeniu
 * realizacji wszystkich etapow mozliwe jest ponowne rozpoczecie prac
 * od najwczesniejszego z etapu.
 *  
 * @author oramus
 *
 */
public interface MultiStageExecutorInterface {
	/**
	 * Rejestruje wykonawce, ktorego prace nalezy wykonac w okreslonym etapie.
	 * @param stage - numer etapu, w ktorym wykonawce nalezy poprosic o wykonanie pracy
	 * @param twi - obiekt wykonawcy pracy
	 */
	void register(int stage, OneStageWorkInterface twi);
	
	/**
	 * Odrejestrowuje wykonawce ze wszystkich etapow, w jakich mial wykonywac prace.
	 * @param twi - wykonawca do usuniecia
	 */
	void deregister(OneStageWorkInterface twi);
	
	/**
	 * Odrejestrowuje wykonawce z okreslonego etapu.
	 * @param stage - etap, z ktorego nalezy usunac wykonawce 
	 * @param twi - wykonawca do usuniecia
	 */
	void deregister(int stage, OneStageWorkInterface twi);
	
	/**
	 * Zwraca najwiekszy numer etapu, ktory zostal do tej pory uzyty.
	 * @return - najwiekszy uzyty numer etapu.
	 */
	int getNumerOfStages();
	
	/**
	 * Uruchamia wykonanie pracy dla wszystkich wykonawcow i wszystkich etapow.
	 * Zarejestrowane obiekty wywolywane sa tym później im większy jest numer etapu.
         * Kolejnosc wywolywania obiektow zarejestrowanych w ramach tego samego etapu jest dowolna.
	 */
	void execute();
}
