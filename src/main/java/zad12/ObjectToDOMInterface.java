package zad12;
import org.w3c.dom.Document;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

public interface ObjectToDOMInterface {
	/**
	 * Metoda zwraca obiekt reprezentujacy strukture pol i ich wartosc dla przeslanego obiektu.
	 * @param o - analizowany obiekt
	 * @return Obiektowa reprezentacja przeslanego obiektu
	 */
	public Document getDocument( Object o ) throws TransformerException;
}
