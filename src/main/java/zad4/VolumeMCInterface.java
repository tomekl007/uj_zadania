package zad4;
public interface VolumeMCInterface {
	/**
	 * Ustawia ksztalt obiektu do oszacowania objetosci.
	 * 
	 * @param s3d - Obiekt zgodny z {@link Shape3DInterface}
	 */
	void setShape3D( Shape3DInterface s3d );
	
	/**
	 * Szacuje objetosc przekazanego wczesniej ksztaltu. Metoda
	 * zaklada, ze uzytkownik przed jej wykonaniem poprawnie
	 * uzyl metody {@link setShape3D}
	 * 
	 * @param shotsNumber - liczba strzalow oddanych w szescian
	 * @return - oczacowana objetosc
	 */
	double estimateVolume( int shotsNumber );
}
