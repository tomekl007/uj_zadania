package prir.zad1;

public class Event implements PMO_EventInterface {

	final int id;
	final long time;
	
	public Event( int id, long time ) {
		this.time = time;
		this.id = id;
	}
	
	@Override
	public int getEventID() {
		return id;
	}

	@Override
	public long getProcessingTime() {
		return time;
	}

}
