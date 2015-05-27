package prir.zad2;

import java.util.concurrent.atomic.AtomicInteger;


public class PMO_Task implements TaskInterface {

	private long executionTime;
	private int niceLevel;
	private Object toWakeUp;
	private long timeToStart;
	private int id;
	private static AtomicInteger ai = new AtomicInteger();
	
	public PMO_Task( int niceLevel, long executionTime, Object toWakeUp ) {
		this.executionTime = executionTime;
		this.niceLevel = niceLevel;
		this.toWakeUp = toWakeUp;
		timeToStart = System.currentTimeMillis();
		id = ai.incrementAndGet();
	}
	
	@Override
	public int getNiceLevel() {
		return niceLevel;
	}

	@Override
	public void execute() {
		timeToStart = System.currentTimeMillis() - timeToStart;
		int tasks = PMO_TaskHelper.workingTasks.incrementAndGet();
		int tasksOnThisNiceLevel = PMO_TaskHelper.tasksOnNiceLevel.incrementAndGet( niceLevel ); 

		PMO_TaskHelper.testMaxTasks( tasks );
		PMO_TaskHelper.setMaxTasks( tasksOnThisNiceLevel, niceLevel); 

		PMO_TaskHelper.tasksDone.incrementAndGet( niceLevel );

		PMO_SOUT.println( "Zadanie " + id + " na poziomie " + niceLevel + " rozpoczeto po " + timeToStart + " msec oczekiwania" );		
		PMO_TaskHelper.timeToStartTask.addAndGet( niceLevel, timeToStart );
		
		long finish = System.currentTimeMillis() + executionTime;
		do {
			PMO_TaskHelper.sleep( 10 );
//			PMO_SOUT.println( "Zadanie pracuje " + id );
		} while ( System.currentTimeMillis() < finish );
		
		PMO_TaskHelper.workingTasks.decrementAndGet();
		PMO_TaskHelper.tasksOnNiceLevel.decrementAndGet( niceLevel );
		PMO_SOUT.println( "Zadanie " + id + " na poziomie " + niceLevel + " zakonczono" );		
		
		// budzenie Main
		synchronized ( toWakeUp ) {
			toWakeUp.notifyAll();
		}
	}
	
	@Override
	public String toString() {
		return "Zadanie id = " + id + " nice level = " + niceLevel ;
	}

}
