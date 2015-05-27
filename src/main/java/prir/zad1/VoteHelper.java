package prir.zad1;

import java.util.concurrent.atomic.AtomicInteger;


public class VoteHelper {
	private static AtomicInteger score = new AtomicInteger(0);
	
	public static void increment() {
		score.incrementAndGet();
	}
	
	public static int getScore() {
		return score.get();
	}
	
	
}
