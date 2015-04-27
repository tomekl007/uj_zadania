package zad13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class ProximityAlertTestOra {
	private ProximityAlertInterface pai;
	private Map< Integer, MovableObject> i2m;
	
	@Before
	public void init() {
		pai = new ProximityAlert();
		i2m = new HashMap<>();
		pai.setCollisionDistance(0.05);
	}
	
	class MovableObject implements ProximityAlertInterface.ProximityWarningListener {
		private List<Integer> li; // lista przechowujaca informacje o ID obiektow z wywolan proximityWarning
		private List<Integer> liExpected; // oczekiwane zagrozenia
		private ProximityAlertInterface.Position2D position;
		private int myID; 
		private double limit;
		private boolean test;
		
		public MovableObject( double x, double y, double limit ) {
			position = new ProximityAlertInterface.Position2D(x, y);
			myID = pai.registerObject( this, limit);
			moveAndTest(0,0); // powiadamiamy system o naszym polozeniu
			i2m.put( myID, this );
			this.limit = limit;
		}
		
		@Override
		public void proximityWarning(int id) {
			assertNotNull( "Wywolano ostrzezenie, choc polozenie obiektu nie jest znane", li );
			System.out.println( "Tu " + myID + " odebrano ostrzezenie o " +  id );
			if ( li != null ) li.add( id );
		}
		
		public void test() {
			liExpected = new ArrayList<>(); 
			for ( Integer i : i2m.keySet() ) { // sami szukamy zagrozenia
				if ( i!=myID) { 
//					System.out.println( "Odleglosc : " +
//							position.distance( i2m.get( i ).position ) );
					if ( position.distance( i2m.get( i ).position ) < limit ) {
						liExpected.add( i ); // obiekt o ID = i nam zagraza
//						System.out.println( ">"  + i );
					}
				}
			}
			
			test = true;
			
			// sprawdzamy czy faktycznie pojawily sie ostrzezenia
			for ( Integer i : liExpected ) {
				if ( ! li.contains( i )) test = false;
			}
			
			assertTrue( "Powinno zostac zgloszone zagrozenie", test );

		}
		
		// zmieniamy polozenie i dokonujemy testow systemu zgloszenia zagrozen
		public void moveAndTest( double dx, double dy ) {
			move( dx, dy );
			for ( Integer i : i2m.keySet() ) {
				i2m.get( i ).test();
			}			
		}

		// tylko zmieniamy polozenie 
		public void move( double dx, double dy ) {
			position = position.move( dx, dy);
			
			installNewWarningsList();
			
			pai.newPosition( myID, position );			
		}
		
		public void installNewWarningsList() {
			li = new ArrayList<>();
		}
		
		public int getNumberOfReceivedWarnings() {
			return li.size();
		}
	
	}
	
	@Test
	public void test2a() {
		MovableObject mo[] = new MovableObject[2];
		
		mo[0] = new MovableObject( 0, 0, 0.2);
		mo[1] = new MovableObject( 1, 1, 0.2);
		
		System.out.println( "Test 2 obiekty");
		for ( int i = 0; i < 9; i++ ) {
			mo[0].moveAndTest( 0.1, 0.1 );
		}		
	}
	
	@Test
	public void test2b() {
		MovableObject mo[] = new MovableObject[2];
		
		mo[0] = new MovableObject( 0, 0, 0.2);
		mo[1] = new MovableObject( 1, 1, 0.5);
		
		System.out.println( "Test 2 obiekty, rozne limity");
		for ( int i = 0; i < 9; i++ ) {
			mo[0].moveAndTest( 0.1, 0.1 );
		}		
	}

	@Test
	public void test3() {
		MovableObject mo[] = new MovableObject[3];
		
		mo[0] = new MovableObject( 0, 0, 1.2);
		mo[1] = new MovableObject( 2, 0, 1.7);
		mo[2] = new MovableObject( 1, 2, 0.1);
		
		System.out.println( "Test 3 obiekty, rozne limity");
		for ( int i = 0; i < 9; i++ ) {
			mo[2].moveAndTest( 0.0, -0.2 );
		}		
	}

	@Test
	public void test4() {
		MovableObject mo[] = new MovableObject[3];
		
		mo[0] = new MovableObject( 0, 0, 1.2);
		mo[1] = new MovableObject( 2, 0, 1.7);
		mo[2] = new MovableObject( 1, 2, 0.1);
		MovableObject mobj = new MovableObject( 1, 1, 3 );
		pai.deregisterObject( mobj.myID );
		i2m.remove( mobj.myID );
		
		System.out.println( "Test 3 obiekty (chwilowo 4), rozne limity");
		for ( int i = 0; i < 9; i++ ) {
			mo[2].moveAndTest( 0.0, -0.2 );
		}		
	}
	
	@Test
	public void test5() {
		MovableObject mo[] = new MovableObject[3];
		
		mo[0] = new MovableObject( 0, 0, 1);
		mo[1] = new MovableObject( 2, 0, 1);
		mo[2] = new MovableObject( 1, 2, 1);
		
		// 1. doprowadzamy do kolizji obiektu 0 i 1
		
		System.out.println( "Dopowadzamy do kolizji");
		mo[0].move( 1, 0);
		mo[1].move(-1, 0);
		
		mo[0].installNewWarningsList();
		mo[1].installNewWarningsList();
		
		System.out.println( "Do miejsca kolizji dociera inny obiekt");
		mo[2].move( 0, -1.75 );
		
		assertEquals( "Od zniszczonego obiektu dotarlo ostrzezenie, lub ostrzezenia dotyczylo zniszczonego obiektu", 1, mo[0].getNumberOfReceivedWarnings() + 
				mo[1].getNumberOfReceivedWarnings() );
		
		assertEquals( "Obiekt ostrzezono o mozliwosci kolizji ze zniszczonym obiektem", 1, mo[2].getNumberOfReceivedWarnings() );
	}



}
