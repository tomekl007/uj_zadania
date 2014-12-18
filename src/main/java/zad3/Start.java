package zad3;

public class Start {

	private static final int SIZE = 16; 
	private static boolean table[][] = new boolean[ SIZE ][ SIZE ];
	
	private static void tableInit() {
		// to be implemented...
	}
	
	private static void tableTest() {
		// to be implemented...
	}

	public static void main(String[] args) {
		tableInit();
		
		GameInterface gi = new Game();
		LifeRulesInterface lri = new LifeRules();
		
		gi.setRules( lri ); // ustalam reguly gry
		gi.setCurrentState( table ); // i stan gry
		
		try {
			gi.nextState(); // zlecam zmiane stanu komorek
		} catch (GameInterface.RulesNotSetException e) {
			e.printStackTrace();
		}
		
		// odbieram wynik
		table = gi.getCurrentState();
		
		// sprawdzam wynik
		tableTest();
	}

}
