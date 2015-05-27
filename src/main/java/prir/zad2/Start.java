package prir.zad2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class Start {

	private static final AtomicInteger vote = new AtomicInteger();
	private static final Object wakeUp = new Object();
	private static final Object wakeUpMain = new Object();
	private static final AtomicBoolean criticalError = new AtomicBoolean( false );
	
	private static int[] calcTasksPerLevel( int[] levels ) {
		int max = levels[0];
		for ( int i = 1; i < levels.length; i++ )
			if ( max < levels[ i ] ) max = levels[ i ];
		
		int[] tasks = new int[ max+1 ];
		for ( int i = 0; i < max+1; i++ ) {
			tasks[i] = 0;
		}
		
		for ( int i = 0; i < levels.length; i++ )
			tasks[ levels[ i ] ] ++;
		
		return tasks;
	}
	
	private static void printTestStart( String name ) {
		PMO_SOUT.println( "****************************************************");
		PMO_SOUT.println( "*** TEST START " + name );
		PMO_SOUT.println( "****************************************************");
	}
	
	private static void insertTasks( MultithreadTaskManagementInterface mtmi, 
			int[] niceLevels, long delay, long executionTime ) {
		PMO_Task task;
		for ( int i = 0; i < niceLevels.length; i++ ) {
			task = new PMO_Task( niceLevels[i], executionTime, wakeUp);
			mtmi.newTask( task );
			PMO_SOUT.println( "Wyslano zadanie " + task );
			PMO_TaskHelper.sleep( delay );
		}
	}
	
	// Dwa poziomy uprzejmosci, tyle samo zadan kazdego z typow,
	// dwa watki po jednym dla kazdego poziomu -> czas oczekiwania
	// na wykonanie powinien byc podobny dla obu poziomow
	private static class Test1 implements Runnable {
		@Override
		public void run() {
			Start.printTestStart( " - 1 - " );
			MultithreadTaskManagementInterface mtmi = new MultithreadTaskManagement();

			mtmi.setNumberOfAvailableThreads( 2 );
			PMO_TaskHelper.maxTasksLimit.set( 2 );
			
			int[] threadsPerLevel = new int[] { 1, 1 };
			mtmi.setMaximumThreadsPerNiceLevel( threadsPerLevel );
			int[] taskLevels = new int[] { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
			insertTasks( mtmi, taskLevels, 10, 700 );
			
			int[] tasksPerLevel = calcTasksPerLevel(  taskLevels );
			
			Start.wait( taskLevels.length, 800L );
			
			boolean result = ! PMO_TaskHelper.maxTasksLimitExceeded.get();
			result &= PMO_TaskHelper.tasksOnNiceLevelLimitTest( threadsPerLevel, 1 );
			result &= PMO_TaskHelper.allTasksExecutedTest( tasksPerLevel );
			PMO_TaskHelper.calcAvgTimeToStartTask( tasksPerLevel );
			PMO_TaskHelper.showAvgTimeToStart( 2 );
			
			if ( Math.abs(  PMO_TaskHelper.timeToStartTask.get(0) - PMO_TaskHelper.timeToStartTask.get(1) ) >
			PMO_TaskHelper.timeToStartTask.get( 0 ) / 10 ) {
				PMO_SOUT.printlnErr( "Rozbieznosc pomiedzy czasami oczekiwania na wykonanie zbyt duza - !!! BLAD KRYTYCZNY !!!");
				criticalError.set( true );
				result = false;
			}
			
			if ( result ) {
				Start.vote.incrementAndGet();
			} else {
				criticalError.set( true );
			}
			
			synchronized ( Start.wakeUpMain ) {
				Start.wakeUpMain.notifyAll();
			}
		}
	}
		
	// Zadania o 3 poziomach uprzejmosci, ostatni nie jest podany w tablicy limitow.
	// 3 watki - podobnie jak w tescie 1 zadania powinny tyle samo srednio czekac
	// na wykonanie
	private static class Test2 implements Runnable {
		@Override
		public void run() {
			Start.printTestStart( " - 2 - " );
			MultithreadTaskManagementInterface mtmi = new MultithreadTaskManagement();

			mtmi.setNumberOfAvailableThreads( 3 );
			PMO_TaskHelper.maxTasksLimit.set( 3 );
			
			int[] threadsPerLevel = new int[] { 1, 1 };
			mtmi.setMaximumThreadsPerNiceLevel( threadsPerLevel );
			int[] taskLevels = new int[] { 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2 };
			insertTasks( mtmi, taskLevels, 10, 500 );
			
			int[] tasksPerLevel = calcTasksPerLevel(  taskLevels );
			
			Start.wait( taskLevels.length, 600L );
			
			boolean result = ! PMO_TaskHelper.maxTasksLimitExceeded.get();
			result &= PMO_TaskHelper.tasksOnNiceLevelLimitTest( threadsPerLevel, 2 );
			result &= PMO_TaskHelper.allTasksExecutedTest( tasksPerLevel );
			PMO_TaskHelper.calcAvgTimeToStartTask( tasksPerLevel );
			PMO_TaskHelper.showAvgTimeToStart( 3 );
			
			if ( Math.abs(  PMO_TaskHelper.timeToStartTask.get(0) - PMO_TaskHelper.timeToStartTask.get(2) ) >
			PMO_TaskHelper.timeToStartTask.get( 1 ) / 10 ) {
				PMO_SOUT.printlnErr( "Rozbieznosc pomiedzy czasami oczekiwania na wykonanie zbyt duza - !!! BLAD KRYTYCZNY !!!");
				result = false;
			}
			
			if ( result ) {
				Start.vote.incrementAndGet();
			} else {
				criticalError.set( true );
			}
 			
			synchronized ( Start.wakeUpMain ) {
				Start.wakeUpMain.notifyAll();
			}
		}
	}

	// zadania o 3 poziomach, tylko dwa watki, zadania o nawyzszym pozimie
	// uprzejmosci powinny czekac najdluzej - system ma preferowac te
	// o poziomach nizszych
	private static class Test3A implements Runnable {
		@Override
		public void run() {
			Start.printTestStart( " - 3A - " );

			MultithreadTaskManagementInterface mtmi = new MultithreadTaskManagement();

			mtmi.setNumberOfAvailableThreads( 2 ); // 2 watki, wiec preferencja wg. nice level
			PMO_TaskHelper.maxTasksLimit.set( 2 );
			
			int[] threadsPerLevel = new int[] { 1, 1 };
			mtmi.setMaximumThreadsPerNiceLevel( threadsPerLevel );
			int[] taskLevels = new int[] { 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2 };
			insertTasks( mtmi, taskLevels, 10, 500 );
			
			int[] tasksPerLevel = calcTasksPerLevel(  taskLevels );
			
			Start.wait( taskLevels.length, 600L );
			
			boolean result = ! PMO_TaskHelper.maxTasksLimitExceeded.get();
			result &= PMO_TaskHelper.tasksOnNiceLevelLimitTest( threadsPerLevel, 2 );
			result &= PMO_TaskHelper.allTasksExecutedTest( tasksPerLevel );
			PMO_TaskHelper.calcAvgTimeToStartTask( tasksPerLevel );
			PMO_TaskHelper.showAvgTimeToStart( 3 );
			
			if ( ( 3 * PMO_TaskHelper.timeToStartTask.get(2) / 2 ) <  PMO_TaskHelper.timeToStartTask.get(0) ) {
				PMO_SOUT.printlnErr( "Czas oczekiwania na wykonanie zadania o nice 2 powinen byc znacznie dluzszy od 0 - BLAD KRYTYCZNY");
				result = false;
			}
			
			if ( result ) {
				Start.vote.incrementAndGet();
			} else {
				criticalError.set( true );
			}
			
			synchronized ( Start.wakeUpMain ) {
				Start.wakeUpMain.notifyAll();
			}
		}
	}

	// Domiana testu 3A. Tym razem preferencja wymuszona poprzez pozwolenie na prace
	// 2 zadan poziomu 0 jednoczesnie
	private static class Test3B implements Runnable {
		@Override
		public void run() {
			Start.printTestStart( " - 3B - " );

			MultithreadTaskManagementInterface mtmi = new MultithreadTaskManagement();

			mtmi.setNumberOfAvailableThreads( 3 ); // 3 watki, 2 dla zadan level 0
			PMO_TaskHelper.maxTasksLimit.set( 3 );
			
			int[] threadsPerLevel = new int[] { 2, 1 };
			mtmi.setMaximumThreadsPerNiceLevel( threadsPerLevel );
			int[] taskLevels = new int[] { 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2 };
			insertTasks( mtmi, taskLevels, 10, 500 );
			
			int[] tasksPerLevel = calcTasksPerLevel(  taskLevels );
			
			Start.wait( taskLevels.length, 600L );
			
			boolean result = ! PMO_TaskHelper.maxTasksLimitExceeded.get();
			result &= PMO_TaskHelper.tasksOnNiceLevelLimitTest( threadsPerLevel, 2 );
			result &= PMO_TaskHelper.allTasksExecutedTest( tasksPerLevel );
			PMO_TaskHelper.calcAvgTimeToStartTask( tasksPerLevel );
			PMO_TaskHelper.showAvgTimeToStart( 3 );
			
			if ( ( 3 * PMO_TaskHelper.timeToStartTask.get(2) / 2 ) <  PMO_TaskHelper.timeToStartTask.get(0) ) {
				PMO_SOUT.printlnErr( "Czas oczekiwania na wykonanie zadania o nice 2 powinen byc znacznie dluzszy od 0 - BLAD KRYTYCZNY");
				result = false;
			}

			if ( ( 3 * PMO_TaskHelper.timeToStartTask.get(1) / 2 ) <  PMO_TaskHelper.timeToStartTask.get(0) ) {
				PMO_SOUT.printlnErr( "Czas oczekiwania na wykonanie zadania o nice 1 powinen byc znacznie dluzszy od 0 - BLAD KRYTYCZNY");
				result = false;
			}
			
			if ( result ) {
				Start.vote.incrementAndGet();
			} else {
				criticalError.set( true );
			}
			
			synchronized ( Start.wakeUpMain ) {
				Start.wakeUpMain.notifyAll();
			}
		}
	}

	// Duzo zadan, duzo watkow - nic nie moze zginac.
	private static class Test4 implements Runnable {
		@Override
		public void run() {
			Start.printTestStart( " - 4 - " );
			MultithreadTaskManagementInterface mtmi = new MultithreadTaskManagement();

			mtmi.setNumberOfAvailableThreads( 10 ); 
			PMO_TaskHelper.maxTasksLimit.set( 10 );
			
			int[] threadsPerLevel = new int[] { 2, 2, 2, 2 };
			mtmi.setMaximumThreadsPerNiceLevel( threadsPerLevel );
			int[] taskLevels = new int[] { 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 0, 1, 2, 3 };
			insertTasks( mtmi, taskLevels, 10, 200 );
			
			int[] tasksPerLevel = calcTasksPerLevel(  taskLevels );
			
			Start.wait( taskLevels.length, 300L );
			
			boolean result = ! PMO_TaskHelper.maxTasksLimitExceeded.get();
			result &= PMO_TaskHelper.tasksOnNiceLevelLimitTest( threadsPerLevel, 5 );
			result &= PMO_TaskHelper.allTasksExecutedTest( tasksPerLevel );
			
			if ( result ) {
				Start.vote.incrementAndGet();
			} else {
				criticalError.set( true );
			}
			
			synchronized ( Start.wakeUpMain ) {
				Start.wakeUpMain.notifyAll();
			}
		}
	}

	// Duzo zadan, duzo watkow - nic nie moze zginac. Jeszcze wiecej zadan i do tego
	// wprowadzanych wspolbieznie
	private static class Test4B implements Runnable {
		@Override
		public void run() {
			Start.printTestStart( " - 4B - " );

			final MultithreadTaskManagementInterface mtmi = new MultithreadTaskManagement();

			mtmi.setNumberOfAvailableThreads( 10 ); 
			PMO_TaskHelper.maxTasksLimit.set( 10 );
			
			int[] threadsPerLevel = new int[] { 2, 2, 2, 2 };
			mtmi.setMaximumThreadsPerNiceLevel( threadsPerLevel );
			final int[] taskLevels = new int[] { 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 5, 0, 1, 2, 3, 4, 0, 1, 2, 3  };
			
			final int insertThreads = 6;
			final CyclicBarrier barrier = new CyclicBarrier( insertThreads );
			
			class Local implements Runnable {
				@Override
				public void run() {
					try {
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
					insertTasks( mtmi, taskLevels, 0, 200 );
				}
			}
			
			Thread[] th = new Thread[ insertThreads ];
			for ( int i = 0; i < insertThreads; i++ ) {
				th[ i ] = new Thread( new Local() );
			}
			for ( int i = 0; i < insertThreads; i++ )
				th[ i ].start();
						
			int[] tasksPerLevel = calcTasksPerLevel(  taskLevels );
			for ( int i = 0 ; i < tasksPerLevel.length; i++ ) {
				tasksPerLevel[ i ] *= insertThreads;
			}
			
			Start.wait( taskLevels.length * insertThreads, 250L );
			
			boolean result = ! PMO_TaskHelper.maxTasksLimitExceeded.get();
			result &= PMO_TaskHelper.tasksOnNiceLevelLimitTest( threadsPerLevel, 5 );
			result &= PMO_TaskHelper.allTasksExecutedTest( tasksPerLevel );
			
			if ( result ) {
				Start.vote.incrementAndGet();
			} else {
				criticalError.set( true );
			}
			
			synchronized ( Start.wakeUpMain ) {
				Start.wakeUpMain.notifyAll();
			}
		}
	}
	
	private static class Test5 implements Runnable {
		@Override
		public void run() {
			Start.printTestStart( " - 5 - " );

			MultithreadTaskManagementInterface mtmi = new MultithreadTaskManagement();

			mtmi.setNumberOfAvailableThreads( 4 ); // 4 watki
			PMO_TaskHelper.maxTasksLimit.set( 4 );
			
			int[] threadsPerLevel = new int[] { 3, 2, 1 };
			mtmi.setMaximumThreadsPerNiceLevel( threadsPerLevel );
			int[] taskLevels = new int[] { 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 1, 2, 0, 0, 1, 2 };
			insertTasks( mtmi, taskLevels, 10, 700 );
			
			int[] tasksPerLevel = calcTasksPerLevel(  taskLevels );
			
			PMO_TaskHelper.sleep( 1000 );
			PMO_SOUT.println( "_________ zmiana limitu z 4 na 2 watki _________");
			mtmi.setNumberOfAvailableThreads( 2 ); // zmniejszany limit o polowe 
			PMO_TaskHelper.maxTasksLimit.set( 2 );
			
			PMO_TaskHelper.sleep( 700 * taskLevels.length );
			
			boolean result = ! PMO_TaskHelper.maxTasksLimitExceeded.get();
			result &= PMO_TaskHelper.tasksOnNiceLevelLimitTest( threadsPerLevel, 2 );
			result &= PMO_TaskHelper.allTasksExecutedTest( tasksPerLevel );
			
			if ( result ) {
				Start.vote.incrementAndGet();
			}
			
			synchronized ( Start.wakeUpMain ) {
				Start.wakeUpMain.notifyAll();
			}
		}
	}
	
	
	private static void startTest( Runnable t ) {
		PMO_TaskHelper.clear();
		Thread th = new Thread( t );
		th.setDaemon( true );
		th.start();
	}
	
	private static void wait( int times ) {
		wait( times, Long.MAX_VALUE ); // niby sie obudzi, ale trzeba by dlugo poczekac...
	}

	private static void wait( int times, long timeLimit ) {
		synchronized ( wakeUp ) {
			try {
				for ( int i = 0; i < times; i++ )
   	 			   wakeUp.wait( timeLimit );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	private static void waitMain() {
		synchronized ( wakeUpMain ) {
			try {
				wakeUpMain.wait( 15000 ); // max 15 sekund na test
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
//                 _       
// _ __ ___   __ _(_)_ __  
//| '_ ` _ \ / _` | | '_ \ 
//| | | | | | (_| | | | | |
//|_| |_| |_|\__,_|_|_| |_|
//
		
	public static void main(String[] args) {
		// ////////////////////////////////////////////////////
		startTest(new Test1());
		waitMain();
		PMO_SOUT.println("Tu MASTER - po test 1, score = " + vote.get() );

		startTest(new Test2());
		waitMain();
		PMO_SOUT.println("Tu MASTER - po test 2, score = " + vote.get() );
	
		startTest(new Test3A());
		waitMain();
		PMO_SOUT.println("Tu MASTER - po test 3A, score = " + vote.get() );

		startTest(new Test3B());
		waitMain();
		PMO_SOUT.println("Tu MASTER - po test 3B, score = " + vote.get() );

		startTest(new Test4());
		waitMain();
		PMO_SOUT.println("Tu MASTER - po test 4, score = " + vote.get() );

		startTest(new Test4B());
		waitMain();
		PMO_SOUT.println("Tu MASTER - po test 4B, score = " + vote.get() );

		startTest(new Test5());
		waitMain();
		PMO_SOUT.println("Tu MASTER - po test 5, score = " + vote.get() );
		
		PMO_SOUT.println( "********************************************" );
		if ( criticalError.get() ) {
			PMO_SOUT.printlnErr( "**  !!!! Wykryto blad krytyczny !!!!" );
		}
		PMO_SOUT.println("**  WYNIK KONCOWY score = " + vote.get() + " na 7 pkt." );
		PMO_SOUT.println( "********************************************" );
		System.exit( 1 );
	}

}
