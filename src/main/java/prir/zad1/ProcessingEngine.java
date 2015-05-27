package prir.zad1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessingEngine implements ProcessingEngineInterface {
	
	private String name;
	
	private AtomicInteger concurrentTestForImportant;
	private AtomicInteger concurrentTestForEvent;
	private List<Integer> proceessedIDs;
	
	// flagi bledow
	private AtomicBoolean importantInMoreThenOneThread;
	private AtomicBoolean processInMoreThenOneThread;
	private AtomicBoolean wrongProcessingOrder;
	private DecisionInterface di;
	private AtomicBoolean deregistered = new AtomicBoolean( false );
	private AtomicBoolean deregisteredError = new AtomicBoolean( false );
	
	
	// w pewnych warunkach test moze zawiesc (za malo zdarzen/wywolan)
	public AtomicBoolean concurrentImportantAndProcessWork;
	public boolean testBlocked = false;
	
	private int mustHaveID; // ten event musi zostac przetworzony
	private int forbiddenID; // tego event-u nie moze byc
	private int lastID; // id ostatniego zadania
	private Object wakeUpObject; // obiekt do obudzenia
	private int eventsToProcess;
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public void setMustHaveID( int id ) {
		mustHaveID = id;
	}
	
	public void setForbiddedID( int id ) {
		forbiddenID = id;
	}
	
	public void setDeregistered() {
		deregistered.set( true );
	}
	
	public void setRegistered() {
		deregistered.set( false );
	}

	public void setEventsToProcess( int i ) {
		eventsToProcess = i;
	}
	
	public void setLastID( int id, Object wakeUP ) {
		lastID = id;
		wakeUpObject = wakeUP;
	}
		
	@Override
	public boolean isItImportant(EventInterface ei) {

		if  ( ! deregistered.compareAndSet( false, false ) ) {
			PMO_SOUT.printlnErr( "Wykonano isItImportant na odrejestrowanym obiekcie" );
			deregisteredError.set( true );
		}
		
		if ( ei instanceof PMO_EventInterface ) {
			PMO_SOUT.println( "PE " + name + " zapytany o waznosc " + ((PMO_EventInterface)ei).getEventID() );			
		}
		
		
		if ( concurrentTestForImportant.compareAndSet( 0, 1 ) == false ) {
			importantInMoreThenOneThread.set( true );
			PMO_SOUT.printlnErr( "isItImportant wywolany z wiecej niz jednego watku");
			Start.criticalError.set( true );
		}
		
		if ( concurrentTestForEvent.get() == 1 ) {
			concurrentImportantAndProcessWork.set( true );
		}
		
		try {			
			if ( ei instanceof PMO_EventInterface ) {
				return di.decide( (PMO_EventInterface)ei);
			} else {
				return false; // to nie jest moj event - nie jestem nim zainteresowany
			}
		} finally {
			concurrentTestForImportant.set(0);
			if ( ei instanceof PMO_EventInterface ) {
				PMO_SOUT.println( "PE " + name + " zaraz odpowie o waznosc " + ((PMO_EventInterface)ei).getEventID() );				
			}
		}
	}
	
	public ProcessingEngine( String name, DecisionInterface di ) {
		this.name = name;
		concurrentTestForEvent = new AtomicInteger(0);
		concurrentTestForImportant = new AtomicInteger(0);
		importantInMoreThenOneThread = new AtomicBoolean( false );
		processInMoreThenOneThread = new AtomicBoolean( false );
		wrongProcessingOrder = new AtomicBoolean( false );
		proceessedIDs = Collections.synchronizedList( new ArrayList<Integer>() );
		concurrentImportantAndProcessWork = new AtomicBoolean( false );
		this.di = di;
	}
	
	@Override
	public void processEvent(EventInterface ei) {
// test rownoczesnego wywolania metody

		if  ( ! deregistered.compareAndSet( false, false ) ) {
			PMO_SOUT.println( "Wykonano processEvent na odrejestrowanym obiekcie" );
			deregisteredError.set( true );
		}		
		
		PMO_EventInterface pei = null;
		
		if ( ei instanceof PMO_EventInterface ) {
			pei = (PMO_EventInterface)ei;
		} else {
			return;
		}
		
		PMO_SOUT.println( "PE " + name + " przetwarza " + pei.getEventID() );
		if ( concurrentTestForEvent.compareAndSet( 0, 1 ) == false ) {
			processInMoreThenOneThread.set( true );
			PMO_SOUT.printlnErr( "processEvent wywolany z wiecej niz jednego watku");
			Start.criticalError.set( true );
		}

// test poprawnej kolejnosci		
		if ( ( proceessedIDs.size() > 0 ) && ( proceessedIDs.get( proceessedIDs.size() - 1 ) > pei.getEventID() ) ) {
			PMO_SOUT.printlnErr( "Zadania z pozniejszym ID juz byly...");
			wrongProcessingOrder.set( true );
			Start.criticalError.set( true );
		}

// test powtorzenia id		
		if ( proceessedIDs.contains( pei.getEventID() )) {
			PMO_SOUT.printlnErr( "To zadanie bylo juz wczesniej przetwarzane");
			wrongProcessingOrder.set( true );
			Start.criticalError.set( true );
		} else {
			proceessedIDs.add( pei.getEventID() );
		}

// zajmowanie CPU niczym		
		try {	
			long waitUntilMsec = System.currentTimeMillis() + pei.getProcessingTime();
			do {
				// tu nic sie nie dzieje...
			} while( System.currentTimeMillis() < waitUntilMsec );
		} finally {
			PMO_SOUT.println( "PE " + name + " juz konczy przetwarzanie zadania " + pei.getEventID() );
			concurrentTestForEvent.set(0);
			
			if ( lastID == pei.getEventID() ) { // wykonano wlasnie ostatnie zadanie

				testIfOK(); // sprawdzamy czy wszystko zadzialalo zgodnie z planem
				
				synchronized ( wakeUpObject ) {
					wakeUpObject.notifyAll(); // budzenie kolejnego testu
				}
			}
			
		}
	}
	
	public boolean testIfOK() {
		boolean result = true;
		
		if ( mustHaveID > 0 ) {
			if ( proceessedIDs.contains( mustHaveID ) ) {
				System.out.println( "Obowiazkowe zadanie przetworzono - dobrze !!!");
			} else {
				PMO_SOUT.printlnErr( "Obowiazkowe zadanie nie zostalo przetworzone - to nie dobrze !!!");
				Start.criticalError.set( true );
				result = false;
			}
		}
		
		if ( ( forbiddenID > 0 ) && ( proceessedIDs.contains( forbiddenID ) ) ) {
			System.out.println( "Zabronione zadanie przetworzono - to nie dobrze !!!");
			result = false;
		}
		
		if ( importantInMoreThenOneThread.get() ) {
			PMO_SOUT.printlnErr( "Metode important wywolano z wiecej niz jednego watku - zle!!!");
			Start.criticalError.set( true );
			result = false;
		}
		if ( processInMoreThenOneThread.get() ) {
			PMO_SOUT.printlnErr( "Metode process wywolano z wiecej niz jednego watku - zle!!!");
			Start.criticalError.set( true );
			result = false;			
		}
		if ( wrongProcessingOrder.get() ) {
			PMO_SOUT.printlnErr( "Zadanie powielono lub zadania wykonano w zlej kolejnosci");
			Start.criticalError.set( true );
			result = false;
		}
		if ( ! concurrentImportantAndProcessWork.get() ) {
			PMO_SOUT.printlnErr( "Nie udalo sie znalezc ani jednego przypadku gdy isImportant i processEvent pracowaly razem");
			Start.criticalError.set( true );
			result = false;
		}
		
		if ( ! testBlocked ) // nie zawsze ten test bedzie wiarygodny
			if ( eventsToProcess != proceessedIDs.size() ) {
				PMO_SOUT.printlnErr( "Nie wykonano wszystkich zadan" );
				Start.criticalError.set( true );
				result = false;
			}
		
		if ( deregisteredError.get() ) {
			PMO_SOUT.println( "Blad w odrejestrowaniu odbiorcy. Odbiorca odrejestrowany, a zdarzenia docieraja");
			result = false;
		}
		
		if ( result ) {
			VoteHelper.increment(); // przyznajemy punkt
		}
		
		return result;
	}
	
}