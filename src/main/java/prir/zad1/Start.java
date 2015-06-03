package prir.zad1;

import java.util.concurrent.atomic.AtomicBoolean;

public class Start {
	private static Object wakeUp = new Object();
	private static ProcessingEngine runTest;
	public static AtomicBoolean criticalError = new AtomicBoolean( false );
	
	private static void sleep( long msec) {
		try {
			Thread.sleep( msec );
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}

// 	  _____         _     _ 
//	 |_   _|__  ___| |_  / |
//	   | |/ _ \/ __| __| | |
//	   | |  __/\__ \ |_  | |
//	   |_|\___||___/\__| |_|
//	                        
		
	// jeden przetwarzajacy kontra 10 zdarzen do przetworzenia
	private static class Test1 implements Runnable {
		ManagementInterface mi = new Management();
		ProcessingEngine pe = new ProcessingEngine( "Jedny", new DecisionInterface() {
						
			@Override
			public boolean decide( PMO_EventInterface ei) {
				sleep( 1000 ); // spimy 1 sekunde
				return true; // tak, wszystko nas interesuje...
			}
		});
				
		@Override
		public void run() {
			PMO_SOUT.println( "Start testu 1");
			long sum = 0L, t0;
			
			PMO_SOUT.println( "Rejestracja odbiorcy zdarzen");
			mi.registerProcessingEngine( pe );
			pe.setLastID( 10, wakeUp );
			pe.setEventsToProcess( 10 );
			
			for ( int i = 1; i < 11; i++ ) {
				PMO_SOUT.println( "Wysylam  zadanie " + i);				
				t0 = System.currentTimeMillis();
				mi.newEvent( new Event( i, 4000 - i * 350 ) ); // czas przetwarzania maleje
				sum += System.currentTimeMillis() - t0;
				PMO_SOUT.println( "Wysylano zadanie " + i );
				sleep( 200 ); // 200ms pomiedzy kolejnymi zdarzeniami
			}
			
			PMO_SOUT.println( "Sredni czas odbioru zadania to " + ( sum / 10.0 ) + " msec");
					
			PMO_SOUT.println( "Koniec testu 1");
		}
	}
	
//	  _____         _     ____  
//	 |_   _|__  ___| |_  |___ \ 
//	   | |/ _ \/ __| __|   __) |
//	   | |  __/\__ \ |_   / __/ 
//	   |_|\___||___/\__| |_____|
//	                            
	
	
	// dwoch przetwarzajacych kontra 10 zdarzen do przetworzenia po 5 dla kazdego
	private static class Test2 implements Runnable {
		ManagementInterface mi = new Management();
		ProcessingEngine peE = new ProcessingEngine( "Even", new DecisionInterface() {
						
			@Override
			public boolean decide(PMO_EventInterface ei) {
				sleep( 1000 ); // spimy 1 sekunde
				return ei.getEventID() % 2 == 0;
			}
		});

		ProcessingEngine peO = new ProcessingEngine( "Odd ", new DecisionInterface() {
			
			@Override
			public boolean decide( PMO_EventInterface ei) {
				sleep( 1000 ); // spimy 1 sekunde
				return ei.getEventID() % 2 == 1;
			}
		});
		
		@Override
		public void run() {
			PMO_SOUT.println( "Start testu 2");
			long sum = 0L, t0;
			
			peE.setLastID( 10, wakeUp );
			peE.setEventsToProcess( 5 );
			peO.setLastID( 9, wakeUp );
			peO.setEventsToProcess( 5 );
			PMO_SOUT.println( "Rejestracja 2 odbiorcow zdarzen, kazdy ma przetworzyc 5 sztuk");
			mi.registerProcessingEngine( peE );
			mi.registerProcessingEngine( peO );
			
			for ( int i = 1; i < 11; i++ ) {
				PMO_SOUT.println( "Wysylam  zadanie " + i);				
				t0 = System.currentTimeMillis();
				mi.newEvent( new Event( i, 3000 - i * 200 ) ); // czas przetwarzania maleje
				sum += System.currentTimeMillis() - t0;
				PMO_SOUT.println( "Wysylano zadanie " + i );
				sleep( 250 ); // 250ms pomiedzy kolejnymi zdarzeniami
			}
			
			PMO_SOUT.println( "Sredni czas odbioru zadania to " + ( sum / 10.0 ) + " msec");
					
			PMO_SOUT.println( "Koniec testu 2");
		}
	}	
	
	
// 	  _____         _     _____ 
//	 |_   _|__  ___| |_  |___ / 
//	   | |/ _ \/ __| __|   |_ \ 
//	   | |  __/\__ \ |_   ___) |
//	   |_|\___||___/\__| |____/ 
//	                            
	
	// dwoch przetwarzajacych kontra 10 zdarzen, przy 6-tym nastepuje odrejestrowanie jednego z nich
	private static class Test3 implements Runnable {
		ManagementInterface mi = new Management();
		ProcessingEngine peE = new ProcessingEngine( "ALL", new DecisionInterface() {
						
			@Override
			public boolean decide(PMO_EventInterface ei) {
				sleep( 1000 ); // spimy 1 sekunde
				return true; // przetwarzamy wszystko
			}
		});

		ProcessingEngine peO = new ProcessingEngine( "ToDeregister ", new DecisionInterface() {
			
			@Override
			public boolean decide( PMO_EventInterface ei) {
				sleep( 1000 ); // spimy 1 sekunde				
				return true;
			}
		});
		
		@Override
		public void run() {
			PMO_SOUT.println( "Start testu 3");
			long sum = 0L, t0;
			
			peE.setLastID( 10, wakeUp );
			peE.setEventsToProcess( 10 );
			peO.setLastID( 11, wakeUp ); // to zdarzenie nie dotrze -> automatyczny test sie nie wykona
			peO.setForbiddedID( 6 ); // to zadanie nie moze dotrzec do odbiorcy
			
			runTest = peO;
			
			PMO_SOUT.println( "Rejestracja 2 odbiorcow zdarzen, drugi zostanie odrejestrowany w trakcie testu");
			mi.registerProcessingEngine( peE );
			mi.registerProcessingEngine( peO );
			
			for ( int i = 1; i < 11; i++ ) {
				if ( i == 6 ) {
					mi.deregisterProcessingEngine( peO ); // odrejestrowanie procesu
					peO.setDeregistered(); // informujemy obiekt, ze zostal juz odrejestrowany
					peO.setName( "DeactivatedProcessingEngine");
					PMO_SOUT.println( ">>>>   Jeden z odbiorcow zostal odlaczony od systemu.");
				}
				PMO_SOUT.println( "Wysylam  zadanie " + i);				
				t0 = System.currentTimeMillis();
				mi.newEvent( new Event( i, 2000 ) ); 
				sum += System.currentTimeMillis() - t0;
				PMO_SOUT.println( "Wysylano zadanie " + i );
				sleep( 200 ); // 200ms pomiedzy kolejnymi zdarzeniami
			}
			
			PMO_SOUT.println( "Sredni czas odbioru zadania to " + ( sum / 10.0 ) + " msec");
					
			PMO_SOUT.println( "Koniec testu 2");
		}
	}	

// 	  _____         _     _  _   
//	 |_   _|__  ___| |_  | || |  
//	   | |/ _ \/ __| __| | || |_ 
//	   | |  __/\__ \ |_  |__   _|
//	   |_|\___||___/\__|    |_|  
//	                             
	
	// dwoch przetwarzajacych kontra 10 zdarzen, przy 6-tym nastepuje zarejestrowanie jednego z nich
	private static class Test4 implements Runnable {
		ManagementInterface mi = new Management();
		ProcessingEngine peE = new ProcessingEngine( "ALL", new DecisionInterface() {
						
			@Override
			public boolean decide(PMO_EventInterface ei) {
				sleep( 1000 ); // spimy 1 sekunde
				return true; // przetwarzamy wszystko
			}
		});

		ProcessingEngine peO = new ProcessingEngine( "ToRegister ", new DecisionInterface() {
			
			@Override
			public boolean decide( PMO_EventInterface ei) {
				sleep( 1000 ); // spimy 1 sekunde				
				return true;
			}
		});
		
		@Override
		public void run() {
			PMO_SOUT.println( "Start testu 4");
			long sum = 0L, t0;
			
			peE.setLastID( 10, wakeUp );
			peE.setEventsToProcess( 10 );
			peO.setLastID( 10, wakeUp ); // to zdarzenie powinno dotrzec -> automatyczny test sie wykona
			peO.setForbiddedID( 1 ); // to zadanie nie moze dotrzec do odbiorcy
			peO.setMustHaveID( 6 ); // to zadanie powinno zostac przetworzone
			peO.setEventsToProcess( 5 );
			
			PMO_SOUT.println( "Rejestracja 2 odbiorcow zdarzen, drugi zostanie zaraz odrejestrowany");
			mi.registerProcessingEngine( peE );
			mi.registerProcessingEngine( peO );
			mi.deregisterProcessingEngine( peO ); // zdarzenia nie moga docierac
			peO.setDeregistered();
			peO.setName( "NonRegisteredProcessingEngine");
			
			for ( int i = 1; i < 11; i++ ) {
				if ( i == 6 ) {
					mi.registerProcessingEngine( peO ); // rejestrowanie procesu
					peO.setRegistered(); // informujemy obiekt, ze zostal juz odrejestrowany
					peO.setName( "RegisteredProcessingEngine");
					PMO_SOUT.println( ">>>>   Odbiorca zostal podlaczony od systemu.");
				}
				PMO_SOUT.println( "Wysylam  zadanie " + i);				
				t0 = System.currentTimeMillis();
				mi.newEvent( new Event( i, 2000 ) ); 
				sum += System.currentTimeMillis() - t0;
				PMO_SOUT.println( "Wysylano zadanie " + i );
				sleep( 200 ); // 200ms pomiedzy kolejnymi zdarzeniami
			}
			
			PMO_SOUT.println( "Sredni czas odbioru zadania to " + ( sum / 10.0 ) + " msec");
					
			PMO_SOUT.println( "Koniec testu 2");
		}
	}	

	
	
	private static void startTest( Runnable t ) {
		PMO_SOUT.println( "_________________________________________" );
		Thread th = new Thread( t );
		th.setDaemon( true );
		th.start();
	}
	
	private static void wait( int times ) {
		synchronized ( wakeUp ) {
			try {
				for ( int i = 0; i < times; i++ )
   	 			   wakeUp.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
//                     _       
//     _ __ ___   __ _(_)_ __  
//    | '_ ` _ \ / _` | | '_ \ 
//    | | | | | | (_| | | | | |
//    |_| |_| |_|\__,_|_|_| |_|
//            
	
	public static void main(String[] args) {
//////////////////////////////////////////////////////
		startTest( new Test1() );
		wait(1);
		PMO_SOUT.println( "Tu MASTER - po test 1");		
		sleep(2000);
		PMO_SOUT.println( ">>>>>>> Wynik : uzyskano " + VoteHelper.getScore() + " punktow na 1" );
		
//////////////////////////////////////////////////////		
		startTest( new Test2() );
		wait(2);
		PMO_SOUT.println( "Tu MASTER - po test 2");
		sleep(2000);
		PMO_SOUT.println( ">>>>>>> Wynik : uzyskano " + VoteHelper.getScore() + " punktow na 3" );
		
//////////////////////////////////////////////////////
		startTest( new Test3() );
		wait(1);
		PMO_SOUT.println( "Tu MASTER - po test 3");
		sleep(2000);
		PMO_SOUT.println( ">>>>>>> Wynik : uzyskano " + VoteHelper.getScore() + " punktow na 4" );

		if ( runTest != null ) {
			PMO_SOUT.println( "Reczne uruchomienie testu");
			runTest.concurrentImportantAndProcessWork.set( true ); // recznie wymuszamy poprawnosc testu
			runTest.testBlocked = true; // blokada testu, ktory moze zawiesc
			runTest.testIfOK();
		}
		PMO_SOUT.println( ">>>>>>> Wynik : uzyskano " + VoteHelper.getScore() + " punktow na 5" );
		sleep(2000);

//////////////////////////////////////////////////////
		startTest( new Test4() );
		wait(2);
		PMO_SOUT.println( "Tu MASTER - po test 4");
		sleep(2000);
		
//////////////////////////////////////////////////////
		PMO_SOUT.println( "Tu MASTER - KONIEC TESTOW");
		PMO_SOUT.println( ">>>>>>> >>>>>>> Wynik koncowy: uzyskano " + VoteHelper.getScore() + " punktow na 7 mozliwych" );
		
		if ( criticalError.get() ) {
			PMO_SOUT.printlnErr( "Wykryto blad krytyczny!!!" );
		}
		
		System.exit( 0 );
	}
}
