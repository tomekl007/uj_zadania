package zad13;

/**
 * Interfejs systemu ostrzegajacego o mozliwosci kolizji.
 * @author oramus
 *
 */
public interface ProximityAlertInterface {
	/**
	 * Niezmiennicza klasa opisujaca polozenie na plaszczyznie.
	 * @author oramus
	 *
	 */
	class Position2D {
		private final double x;
		private final double y;
		
		/**
		 * Konstruktor dwuparametrowy
		 * @param x - wspolrzedna x
		 * @param y - wspolrzedna y
		 */
		public Position2D( double x, double y ) {
			this.x = x;
			this.y = y;
		}
		
		/**
		 * Odleglosc do wskazanego puktu
		 * @param p - polozenie punktu, do ktorego odleglosc ma zostac policzona.
		 * @return - odleglosc
		 */
		public double distance( Position2D p ) {
			double dx = p.x - this.x;
			double dy = p.y - this.y;
			
			return Math.sqrt( dx * dx + dy * dy );
		}
		
		/** 
		 * Generator polozenia po przesunieciu o [dx,dy]
		 * @param dx - zmiana wspolrzednej x
		 * @param dy - zmiana wspolrzednej y
		 * @return
		 */
		public Position2D move( double dx, double dy ) {
			return new Position2D( x + dx,  y + dy);
		}
	}	
	
	/**
	 * Interfejs obiektow oczekujacych na powiadomienie o zagrozeniu
	 * zderzeniem.
	 * @author oramus
	 *
	 */
	interface ProximityWarningListener {
		/**
		 * Komunikat o zagrozeniu ze strony obiektu o podanym ID.
		 * @param id - numer zagrazajacego nam obiektu.
		 */
		void proximityWarning(int id);
	}
	
	/**
	 * Ustala granice odleglosci ponizej ktorej uwaza sie, ze pomiedzy obiektami doszlo do
	 * zderzenia. Metoda musi zostac wykonana przed pierwszym wywolaniem metody newPosition. 
	 * 
	 * @param limit - odleglosc ponizej ktorej uznaje sie, ze obiekty zderzyly sie ze soba.
	 */
	void setCollisionDistance(double limit);
	
	
	/**
	 * Rejestracja obiektu
	 * @param pwl - obiekt, ktory ma zosta powiadomiony o zagrozeniu, null oznacza, że obiekt nie chce
	 * byc informowany o zagrozeniu dla niego, moze natomiast byc zagrozeniem dla innych.
	 * @param limit - limit ponizej ktorego obiekt ma zostac poinformowany o zagrozeniu
	 * @return - unikalny identyfikator obiektu
	 */
	int registerObject(ProximityWarningListener pwl, double limit);
	
	/**
	 * Usuniecie obiektu z systemu
	 * @param id - identyfikator obiektu do usunieciu
	 * @return - informacja o poprawnym usunieciu obiektu (true - ID bylo poprawne i obiekt zostal usuniety,
	 * false - blad danych wejsciowych, bledne id. Takze wtedy, gdy obiekt ulegl zniszczeniu na skutek kolizji. )
	 */
	boolean deregisterObject(int id);
	
	/**
	 * Zmiana polozenia przez obiekt. Wywolanie metody wyzwala testy zagrozenia kolizja.
	 * Jesli podany identyfikator jest bledny (brak obiektu o takim ID w systemie), to przeslane dane nalezy zignorowac.
	 * @param id - identyfikator obiektu, ktorego polozenie uleglo zmianie
	 * @param pos - polozenie 
	 */
	void newPosition(int id, Position2D pos);
}
