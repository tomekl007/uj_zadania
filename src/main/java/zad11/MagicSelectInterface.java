package zad11;

import java.util.List;

/**
 * Interfejs budowania zapytan SQL, w ktorych sekcja WHERE generowana
 * jest na podstawie skladnikow dostarczanych w Odwrotnej Notacji Polskiej.
 * {@linkplain http://pl.wikipedia.org/wiki/Odwrotna_notacja_polska }
 * <br>
 * Lista zawierajaca:
 * <pre>
 * [ imie, "Ala", EQUALS ] [ imie, "Alicja", EQUALS ] [ null, null, OR ] [ null, null, NOT ]
 * </pre>
 * odpowiada sekcji WHERE:
 * <pre>
 * not ( ( imie = "Ala" ) or ( imie = "Alicja ) )
 * </pre>
 * 
 * @author oramus
 *
 */
public interface MagicSelectInterface {

	/**
	 * Interfejs opisu operacji.
	 * @author oramus
	 *
	 */
	public interface OPInterface {
		/**
		 * Zwraca informacje czy operacja jest typu logicznego.
		 * @return true - operacja logiczna, false - inna
		 */
		public boolean isLogical();

		/**
		 * Ciag znakow reprezentujacy dana operacje w zapytaniu SQL.
		 * 
		 * @return 
		 */
		public String getSQL();
		
		/**
		 * Zwraca liczbe argumentow koniecznych do wykonania operacji.
		 * 
		 * @return liczba wymaganych argumentow.
		 */
		public int getRequiredArgs();
	}
	
	/**
	 * Typ wyliczeniowy dozwolonych operacji
	 * @author oramus
	 *
	 */
	public enum OP implements OPInterface {
		NOT(true, "NOT", 1), OR(true, "OR"), AND(true, "AND"), EQUALS(false,
				"="), LIKE(false, "like"), GREATER( false, ">" ), SMALLER( false, "<");

		OP(boolean logic, String str) {
			this.logic = logic;
			sqlRepresetation = str;
		}

		OP(boolean logic, String str, int requiredArguments) {
			this.logic = logic;
			sqlRepresetation = str;
			this.requiredArguments = requiredArguments;
		}

		private boolean logic;
		private String sqlRepresetation;
		private int requiredArguments = 2;

		public boolean isLogical() {
			return logic;
		};

		public String getSQL() {
			return sqlRepresetation;
		}

		public int getRequiredArgs() {
			return requiredArguments;
		}
	}

	/**
	 * Klasa reprezentuje jeden skladnik zapytania. Jesli operacja
	 * (operation) jest typu logicznego, pozostale pola moga
	 * zawierac null.
	 * 
	 * @author oramus
	 *
	 */
	public class Item {
		final public String attribute;
		final public String value;
		final public OP operation;

		public Item(String a, String v, OP o) {
			attribute = a;
			value = v;
			operation = o;
		}
	}

	/**
	 * Metoda zwraca ciag znakow z zapytaniem SELECT do relacyjnej bazy danych.
	 * Zapytanie typowo ma postac:
	 * <pre>
	 * SELECT Lista,Atrybutow,Rozdzielonych,Przecinkami FROM tableName WHERE warunek;
	 * </pre>
	 * gdzie warunek jest tworzony na podstawie listy jego skladnikow zapisanych
	 * w Odwrotnej Notacji Polskiej. 
	 * @param tableName - nazwa tabeli
	 * @param attributes - lista atrybutow, jesli null to zamiast list w zapytaniu SELECT powinien pojawic sie znak gwiazdki (SELECT * FROM itd.)
	 * @param whereInRPN - skladniki warunku, jesli null to sekcja WHERE nie wystepuje i zapytanie konczy sie nazwa tabeli.
	 * @throws IllegalArgumentException blad w argumentach wywolania metody
	 * @return Ciag znakow reprezentujacy poprawne zapytanie SELECT wygenerowane na podstawie dostarczonych argumentow. 
	 */
	String select(String tableName, List<String> attributes,
                  List<Item> whereInRPN) throws IllegalArgumentException;
}
