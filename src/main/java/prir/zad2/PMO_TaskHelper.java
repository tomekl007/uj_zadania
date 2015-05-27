package prir.zad2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;

public class PMO_TaskHelper {

	private static int MAX_LEVELS = 64;
	private static int MAX_TASKS = 16000;

	// detekcja ilosci watkow na danym poziomie uprzejmosci
	public static AtomicIntegerArray tasksOnNiceLevel;

	// najwieksza wykryta liczba zadan realizowanych na danym poziomie
	public static AtomicIntegerArray maxTasksOnNiceLevel;

	// calkowita liczba uruchomionych jednoczesnie zadan
	public static AtomicInteger workingTasks;

	// wskaznik przekroczenia calkowitej liczby jednoczesnych zadan
	public static AtomicBoolean maxTasksLimitExceeded;

	// detekcja jednoczesnie dzialajacych zadan
	public static AtomicInteger maxTasksLimit;
	// public static

	// liczba zrealizowanych zadan
	public static AtomicIntegerArray tasksDone;

	// sumaryczny czas oczekiwania na rozpoczenie pracy
	public static AtomicLongArray timeToStartTask;

	{
		clear();
	}

	public static void sleep(long msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Inicjuje obiekty
	 */
	public static void clear() {
		tasksOnNiceLevel = new AtomicIntegerArray(MAX_LEVELS);
		maxTasksOnNiceLevel = new AtomicIntegerArray(MAX_LEVELS);
		tasksDone = new AtomicIntegerArray(MAX_TASKS);
		workingTasks = new AtomicInteger();
		maxTasksLimitExceeded = new AtomicBoolean(false);
		maxTasksLimit = new AtomicInteger();
		timeToStartTask = new AtomicLongArray(MAX_LEVELS);
	}

	/**
	 * Ustawia maksymalna liczbe znalezionych jednoczesnych zadan na danym
	 * poziomie nice
	 * 
	 * @param max
	 *            - liczba zadan
	 * @param niceLevel
	 *            - poziom uprzejmosci
	 */
	public static synchronized void setMaxTasks(int max, int niceLevel) {
		if (max > maxTasksOnNiceLevel.get(niceLevel)) {
			maxTasksOnNiceLevel.set(niceLevel, max);
		}
	}

	/**
	 * Sprawdza czy nie przekroczono liczby jednoczesnych zadan
	 * 
	 * @param tasks
	 *            - liczba aktualnie realizowanych zadan
	 */
	public static synchronized void testMaxTasks(int tasks) {
		if (tasks > maxTasksLimit.get()) {
			PMO_SOUT.printlnErr( "BLAD: przekroczono limit jednoczesnych zadan");
			maxTasksLimitExceeded.set(true);
		}
	}

	public static boolean tasksOnNiceLevelLimitTest(int[] limits,
			int maxNiceLevel) {
		boolean result = true;

		for (int i = 0; i <= maxNiceLevel; i++) {
			if (i < limits.length) {
				if (maxTasksOnNiceLevel.get(i) > limits[i]) {
					PMO_SOUT.printlnErr("BLAD: Za duzo zadan bylo jednoczesnie realizowanych na jednym poziomie; poziom " + i + ""
							+ " oczekiwano " + limits[i] + " jest " + maxTasksOnNiceLevel.get(i));
					result = false;
				}
				if ((limits[i] > 1) && (maxTasksOnNiceLevel.get(i) < 2)) {
					PMO_SOUT.printlnErr("BLAD: Nie wykryto pracy rownoczesnej nad wieloma zadaniami, poziom " + i + ""
							+ " oczekiwano " + limits[i] + " jest " + maxTasksOnNiceLevel.get(i));
					result = false;
				}
			} else {
				if (maxTasksOnNiceLevel.get(i) > 1) {
					PMO_SOUT.printlnErr("BLAD: Zadania dla niepodanych w tablicy limitow powinny byc realizowane po jednym");
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * Sprawdza czy wszystkie zadania zostaly wykonane
	 * 
	 * @param tasksPerLevel
	 *            liczba zadan na dany poziom uprzejmosci
	 * @return wynik testu
	 */
	public static boolean allTasksExecutedTest(int[] tasksPerLevel) {
		boolean result = true;

		for (int i = 0; i < tasksPerLevel.length; i++) {
			if (tasksPerLevel[i] != tasksDone.get(i)) {
				PMO_SOUT.printlnErr("Nie zrealizowano wszystkich zadan; poziom " + i + " jest " + tasksDone.get(i) +
						" powinno " + tasksPerLevel[ i ]);
				result = false;
			}
		}

		return result;
	}

	/**
	 * Przelicza sume czasow oczekiwania na rozpoczecie wykonywania zadania na
	 * srednia
	 * 
	 * @param tasksPerLevel
	 *            liczba zadan na danym poziomie uprzejmosci
	 */
	public static void calcAvgTimeToStartTask(int[] tasksPerLevel) {
		for (int i = 0; i < tasksPerLevel.length; i++)
			if (tasksPerLevel[i] > 0)
				timeToStartTask.set(i, timeToStartTask.get(i)
						/ tasksPerLevel[i]);
	}

	public static void showAvgTimeToStart(int levels) {
		for (int i = 0; i < levels; i++)
			PMO_SOUT.println("Level " + i + " " + timeToStartTask.get(i));
	}

}
