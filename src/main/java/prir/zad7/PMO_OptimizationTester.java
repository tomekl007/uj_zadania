
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.omg.CORBA.IntHolder;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;


public class PMO_OptimizationTester {
	private static optimizationOperations oo;
	private static final int SERVERS = 20;
	private static final short FIRST_IP = 10;
	private static final int MIN_TIMEOUT = 100;
	private static final int ADD_TIMEOUT = 100;
	private static CyclicBarrier cb = new CyclicBarrier( SERVERS );
	private static Set<Integer> ids = Collections.synchronizedSet( new HashSet<Integer>() );
	
	static class Serwer implements Runnable {
		private short myIP;
		private boolean sendHello = true;
		private int myTimeout;
		private boolean ignoreBarriers;
		
		synchronized public void toggleSendHello() {
			sendHello = !sendHello;
		}
		
		public Serwer( short ip, int timeout, boolean ignoreBarriers ) {
			this( ip, timeout );
			this.ignoreBarriers = ignoreBarriers;
		}
		
		public Serwer( short ip, int timeout ) {
			myIP = ip;
			myTimeout = timeout;
			System.out.println( "Serwer o adresie " + ip + " timeout " + timeout );
			ignoreBarriers = false;
		}
		
		@Override
		public void run() {
			if ( ! ignoreBarriers ) {
				try {
					cb.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		
			IntHolder ih = new IntHolder();
			oo.register( myIP, myTimeout, ih ); // rejestracja serwera

			PMO_SOUT.println( "Server o adresie " + myIP + " otrzymal ID " + ih.value );
			ids.add( ih.value );

			// BARIERA - czekamy na rejestracje wszystkich serwerow
			if ( ! ignoreBarriers ) {
				try {
					cb.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				if ( ids.size() != SERVERS ) {
					PMO_SOUT.println( "Identyfikatory nie sa unikalne !!!");
					System.exit( 1 );
				}
			}
			
			boolean send;
			while ( true ) {
				synchronized ( this ) {
					send = sendHello;
				}
				if ( send ) {
//					PMO_SOUT.println( "Hello z serwera o " + myIP + " id : " + ih.value );
					oo.hello( ih.value );
				}
				
				sleep( myTimeout / 3 ); // hello co 1/3 timeout-u
			} 
		}
	}
	
	private static void sleep( long ms ) {
		try {
			Thread.sleep( ms );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Serwer[] createServers() {
		Serwer svs[] = new Serwer[ SERVERS ];
		
		for ( int i = 0; i < SERVERS; i++ ) 
			svs[ i ] = new Serwer( (short)( i + FIRST_IP ), 100 + 100 * ( ( 2 * i ) / SERVERS ) );
		// polowa serwerow ma timeout 100 ms, a pozostale 200 ms
		
		return svs;
	}
	
	private static void startOneServer( Serwer sv ) {
		Thread th = new Thread( sv );
		th.setDaemon( true );
		th.start();		
	}
	
	private static void startSerwers( Serwer[] svs ) {
		for ( int i = 0; i < SERVERS; i++ ) {
			startOneServer( svs[ i ]);
		}
	}
	
	private static boolean test( int expectedMinIP, int expectedMaxIP ) {
		boolean result = true;
		rangeHolder rh = new rangeHolder();
		rh.value = new range();
		PMO_SOUT.println( "Wywolanie best_range.");
		oo.best_range(rh);
		
		if ( rh.value == null ) {
			PMO_SOUT.println( "RangeHolder.value == null");
			result = false;
		} else {
			PMO_SOUT.println( "Wywolanie best_range zwrocilo zakres od " + rh.value.from 
					+ " do " + rh.value.to );
			if ( rh.value.from != expectedMinIP ) {
				PMO_SOUT.println( "Zakres powinien zaczynac sie od " + expectedMinIP +
						", a jest : " + rh.value.from );
				result = false;
			}
			
			if ( rh.value.to != expectedMaxIP ) {
				PMO_SOUT.println( "Zakres powinien konczyc sie na " + expectedMaxIP +
						", a jest : " + rh.value.to );
				result = false;
			}
		}
		return result;
	}
	
	public static void main( String[] argv ) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		
		ORB orb = ORB.init(argv, null);
		org.omg.CORBA.Object namingContextObj = orb
				.resolve_initial_references("NameService");
		NamingContext namingContext = NamingContextHelper
				.narrow(namingContextObj);

		NameComponent[] path = { new NameComponent("Optymalizacja", "Object") };

		org.omg.CORBA.Object envObj = namingContext.resolve(path);
		oo = optimizationHelper.narrow(envObj);

		Serwer svs[] = createServers();
		startSerwers(svs);
	
		//////////////////////////////////
		PMO_SOUT.println( "Serwery wlaczone.............");
		sleep( 3 * ( MIN_TIMEOUT + ADD_TIMEOUT ) );
		
		boolean globalOK = true;

		globalOK &= test( FIRST_IP, FIRST_IP + SERVERS - 1 );

		int halfServers = SERVERS / 2;
		int off = 5;
		// wylaczam czesc serwerow
		for ( int i = 0; i < off; i++ ) {
			svs[ halfServers - i ].toggleSendHello();
		}
		PMO_SOUT.println( "Awaria czesci serwerow.............");
		
		sleep( ( MIN_TIMEOUT + ADD_TIMEOUT ) * 3 );
		
		globalOK &= test( FIRST_IP + halfServers + 1, FIRST_IP + SERVERS - 1 );

		// serwery wracaja do pracy
		for ( int i = 0; i < off; i++ ) {
			svs[ halfServers - i ].toggleSendHello();
		}
		PMO_SOUT.println( "Po uruchomieniu czesci serwerow.....");
		
		sleep( ( MIN_TIMEOUT + ADD_TIMEOUT ) * 3 );
		
		globalOK &= test( FIRST_IP, FIRST_IP + SERVERS - 1 );
	
		
		PMO_SOUT.println( "Wylaczamy jeden z serwerow i czekamy ponad timeout...");
		svs[ 0 ].toggleSendHello();
		sleep( ( MIN_TIMEOUT + ADD_TIMEOUT ) * 4 );
		globalOK &= test( FIRST_IP+1, FIRST_IP + SERVERS - 1 );		
		
		// tworzymy serwer z dlugim TIMEOUT-em
		Serwer newOne = new Serwer( FIRST_IP, 9 * MIN_TIMEOUT, true ); // hello co 3x MIN_TIMEOUT
		startOneServer( newOne );
		PMO_SOUT.println( "Dodajemy jeden serwer..............");
		sleep( 10 * MIN_TIMEOUT );
		globalOK &= test( FIRST_IP, FIRST_IP + SERVERS - 1 );		// juz powinien byc
		
		PMO_SOUT.println( "Blokujemy przesylanie komunikatow z jednego z serwerow");
		newOne.toggleSendHello(); // ostatni komunikat w najgorszym razie byl 3x MIN_TIMEOUT temu
		sleep( MIN_TIMEOUT * 4 ); // Czyli max po 7x MIN_TIMEOUT serwer nadal powinien byc taktowany jako dzialajacy
		PMO_SOUT.println( "Test wykonujemy gdy nie minal timeout");
		globalOK &= test( FIRST_IP, FIRST_IP + SERVERS - 1 );		
		
		if ( globalOK ) {
			PMO_SOUT.println( "Testy zakonczone SUKCESEM !!!");
		} else {
			PMO_SOUT.println( "W trakcie testow doszlo do bledu!");
		}
	}
	
}
