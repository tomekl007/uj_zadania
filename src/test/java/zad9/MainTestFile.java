package zad9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MainTestFile {
    RoomsAllocationSystemInterface rasi;

    private void addAtributes() {
        rasi.addAttribute("KolorScian",
                RoomsAllocationSystemInterface.AttributeType.STRING);
        rasi.addAttribute("LiczbaWymianPowietrza",
                RoomsAllocationSystemInterface.AttributeType.INTEGER);
        rasi.addAttribute("Klimatyzacja",
                RoomsAllocationSystemInterface.AttributeType.BOOLEAN);
        rasi.addAttribute("LiczbaProjektorow",
                RoomsAllocationSystemInterface.AttributeType.INTEGER);
        rasi.addAttribute("LiczbaTablic",
                RoomsAllocationSystemInterface.AttributeType.INTEGER);
        rasi.addAttribute("Naglosnienie",
                RoomsAllocationSystemInterface.AttributeType.BOOLEAN);
        rasi.addAttribute("Umywalka",
                RoomsAllocationSystemInterface.AttributeType.BOOLEAN);
    }

    private void addRooms() {
        rasi.addRoom("K-I-01",
                RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 10);
        rasi.addRoom("K-I-02",
                RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 15);
        rasi.addRoom("K-I-03",
                RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 20);
        rasi.addRoom("K-I-04",
                RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 25);

        rasi.addRoom("P-I-01", RoomsAllocationSystemInterface.RoomType.OTHER,
                10);
        rasi.addRoom("P-I-02", RoomsAllocationSystemInterface.RoomType.OTHER,
                15);
        rasi.addRoom("P-I-03", RoomsAllocationSystemInterface.RoomType.OTHER,
                20);
        rasi.addRoom("P-I-04", RoomsAllocationSystemInterface.RoomType.OTHER,
                25);
    }

    @Before
    public void init() {
        rasi = new RoomsAllocationSystem();
        addAtributes();
        addRooms();
    }

    private static interface SingleTest {
        boolean test(Map<String, Object> map);
    }

    private SingleTest addAttributeToRoom(final String aName, final String id,
                                          final String value) {
        rasi.addAttributeToRoom(aName, id, value);

        return new SingleTest() {
            @Override
            public boolean test(Map<String, Object> map) {
                return checkIfKeyExists(map, aName)
                        && checkValue(map, aName, value);
            }
        };
    }

    private SingleTest addAttributeToRoom(final String aName, final String id,
                                          final Boolean value) {
        rasi.addAttributeToRoom(aName, id, value);

        return new SingleTest() {
            @Override
            public boolean test(Map<String, Object> map) {
                return checkIfKeyExists(map, aName)
                        && checkValue(map, aName, value);
            }
        };
    }

    private SingleTest addAttributeToRoom(final String aName, final String id,
                                          final Integer value) {
        rasi.addAttributeToRoom(aName, id, value);

        return new SingleTest() {
            @Override
            public boolean test(Map<String, Object> map) {
                return checkIfKeyExists(map, aName)
                        && checkValue(map, aName, value);
            }
        };
    }

    private boolean checkIfKeyExists(Map<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            fail("W zwroconym zestawie atrybutow brak " + key);
        }
        return true;
    }

    private boolean checkValue(Map<String, Object> map, String key, String value) {

        Object o = map.get(key);

        if (!(o instanceof String)) {
            fail("Atrybut " + key + " powinien byc typu String");
        }

        if (!value.equals((String) o)) {
            fail("Atrybut " + key + " powinien miec wartosc " + value);
        }

        return true;
    }

    private boolean checkValue(Map<String, Object> map, String key,
                               Boolean value) {
        Object o = map.get(key);

        if (!(o instanceof Boolean)) {
            fail("Atrybut " + key + " powinien byc typu logicznego");
        }

        if (!value.equals((Boolean) o)) {
            fail("Atrybut " + key + " powinien miec wartosc " + value);
        }

        return true;
    }

    private boolean checkValue(Map<String, Object> map, String key,
                               Integer value) {
        Object o = map.get(key);

        if (!(o instanceof Integer)) {
            fail("Atrybut " + key + " powinien byc typu calkowitoliczbowego");
        }

        if (!value.equals((Integer) o)) {
            fail("Atrybut " + key + " powinien miec wartosc " + value);
        }

        return true;
    }

    private boolean runSingleTests(List<SingleTest> lst, Map<String, Object> res) {
        boolean result = true;
        for (SingleTest st : lst)
            result &= st.test(res);

        return result;
    }

    @Test
    public void simpleAtributesTest() {

        List<SingleTest> lst1 = new ArrayList<>();
        List<SingleTest> lst2 = new ArrayList<>();

        lst1.add(addAttributeToRoom("Klimatyzacja", "K-I-01", true));
        lst1.add(addAttributeToRoom("Umywalka", "K-I-01", false));
        lst1.add(addAttributeToRoom("KolorScian", "K-I-01", "fioletowy"));
        lst1.add(addAttributeToRoom("LiczbaProjektorow", "K-I-01", 2));

        lst2.add(addAttributeToRoom("Naglosnienie", "K-I-02", true));
        lst2.add(addAttributeToRoom("LiczbaProjektorow", "K-I-02", 1));
        lst2.add(addAttributeToRoom("KolorScian", "K-I-02", "pomaranczowy"));

        Map<String, Object> result01 = rasi.getRoomAttributes("K-I-01");
        Map<String, Object> result02 = rasi.getRoomAttributes("K-I-02");

        if (runSingleTests(lst1, result01) & runSingleTests(lst2, result02))
            System.out.println("simpleAtributesTest zaliczony");
    }

    private void addAtributesSet1() {
        List<SingleTest> lst1 = new ArrayList<>();
        List<SingleTest> lst2 = new ArrayList<>();
        List<SingleTest> lst3 = new ArrayList<>();
        List<SingleTest> lst4 = new ArrayList<>();

//		LiczbaWymianPowietrza Klimatyzacja LProjektorow Naglosnienie
// K-I-01			1			T			1				T
// K-I-02			2			T			2				T
// K-I-03			3			T			3				N
// K-I-04			4			N			4				N


        lst1.add(addAttributeToRoom("LiczbaWymianPowietrza", "K-I-01", 1));
        lst2.add(addAttributeToRoom("LiczbaWymianPowietrza", "K-I-02", 2));
        lst3.add(addAttributeToRoom("LiczbaWymianPowietrza", "K-I-03", 3));
        lst4.add(addAttributeToRoom("LiczbaWymianPowietrza", "K-I-04", 4));

        lst1.add(addAttributeToRoom("Klimatyzacja", "K-I-01", true));
        lst2.add(addAttributeToRoom("Klimatyzacja", "K-I-02", true));
        lst3.add(addAttributeToRoom("Klimatyzacja", "K-I-03", true));
        lst4.add(addAttributeToRoom("Klimatyzacja", "K-I-04", false));

        lst1.add(addAttributeToRoom("LiczbaProjektorow", "K-I-01", 1));
        lst2.add(addAttributeToRoom("LiczbaProjektorow", "K-I-02", 2));
        lst3.add(addAttributeToRoom("LiczbaProjektorow", "K-I-03", 3));
        lst4.add(addAttributeToRoom("LiczbaProjektorow", "K-I-04", 4));

        lst1.add(addAttributeToRoom("Naglosnienie", "K-I-01", true));
        lst2.add(addAttributeToRoom("Naglosnienie", "K-I-02", true));
        lst3.add(addAttributeToRoom("Naglosnienie", "K-I-03", false));
        lst4.add(addAttributeToRoom("Naglosnienie", "K-I-04", false));

        Map<String, Object> result01 = rasi.getRoomAttributes("K-I-01");
        Map<String, Object> result02 = rasi.getRoomAttributes("K-I-02");
        Map<String, Object> result03 = rasi.getRoomAttributes("K-I-03");
        Map<String, Object> result04 = rasi.getRoomAttributes("K-I-04");


        if (runSingleTests(lst1, result01) && runSingleTests(lst2, result02) &&
                runSingleTests(lst3, result03) && runSingleTests(lst4, result04))
            System.out.println("atributesTest zaliczony");
    }

    private String a2s(String... sa) {
        String tmp = "[" + sa[0];

        for (int i = 1; i < sa.length; i++) {
            tmp += ", " + sa[i];
        }
        return tmp + "]";
    }

    private boolean checkIfListContains(List<String> ls, String... sa) {
        assertNotNull("Lista z wynikami nie moze byc null", ls);
        System.out.println("Lista    > " + ls);
        System.out.println("Expected > " + a2s(sa));
        for (String s : sa) {
            if (!ls.contains(s)) {
                fail("Lista pomieszczen powinna zawierac " + s);
            }
        }
        assertEquals("Rozmiar propozycji nie jest zgodny z poprawnym rozwiazaniem", sa.length, ls.size());
        return true;
    }

    private boolean checkOrder(List<String> ls, String... sa) {
        checkIfListContains(ls, sa);

        for (int i = 0; i < sa.length; i++) {
            assertEquals("Na pozycji " + (i + 1) + " powinno byc ", sa[i], ls.get(i));
        }

        return true;
    }

    @Test
    public void simpleFailSearchTest() {

        addAtributesSet1();

        RoomsAllocationSystemInterface.Schedule sch = new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 1);

        Map<String, Boolean> required = new HashMap<>();
        required.put("Klimatyzacja", true);

        Map<String, Integer> limits = new HashMap<>();
        limits.put("LiczbaProjektorow", 1);

        Map<String, Integer> weights = new HashMap<>();
        weights.put("LiczbaProjektorow", 1);


        List<String> ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.OTHER, sch,
                30, required, limits, weights);

        assertNotNull("Brak wymaganych pomieszczen -> pusta lista, nie null", ls);
        assertEquals("Brak wymaganych pomieszczen -> pusta lista", 0, ls.size());

        ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.OTHER, sch,
                10, required, limits, weights);

        assertNotNull("Brak wymaganych pomieszczen -> pusta lista, nie null", ls);
        assertEquals("Brak wymaganych pomieszczen -> pusta lista", 0, ls.size());
    }

    @Test
    public void simpleSearchTest() {

        addAtributesSet1();

        RoomsAllocationSystemInterface.Schedule sch = new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 1);

//		LiczbaWymianPowietrza Klimatyzacja LProjektorow Naglosnienie
// K-I-01			1			T			1				T
// K-I-02			2			T			2				T
// K-I-03			3			T			3				N
// K-I-04			4			N			4				N

        Map<String, Boolean> required = new HashMap<>();
        required.put("Klimatyzacja", true);  // zostaja 3 sale

        Map<String, Integer> limits = new HashMap<>();
        limits.put("LiczbaProjektorow", 1); // nadal 3

        Map<String, Integer> weights = new HashMap<>();
        weights.put("LiczbaProjektorow", 1);

        List<String> ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                10, required, limits, weights);

        assertNotNull("Sa pomieszczenia -> lista, nie null", ls);
        assertEquals("Sa odpowienie pomieszczenia", 3, ls.size());
        checkIfListContains(ls, "K-I-03", "K-I-02", "K-I-01");
    }

    @Test
    public void simpleSearchTest2() {

        addAtributesSet1();

        RoomsAllocationSystemInterface.Schedule sch = new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 1);

//		LiczbaWymianPowietrza Klimatyzacja LProjektorow Naglosnienie
// K-I-01			1			T			1				T
// K-I-02			2			T			2				T
// K-I-03			3			T			3				N
// K-I-04			4			N			4				N
        Map<String, Boolean> required = new HashMap<>();
        required.put("Klimatyzacja", true);  // Spelniaja: K-01, K-02, K-03
        required.put("Naglosnienie", false); // Spelniaja: K-03 i K-04
        // lacznie tylko K-03

        Map<String, Integer> limits = new HashMap<>();
        limits.put("LiczbaProjektorow", 1);

        Map<String, Integer> weights = new HashMap<>();
        weights.put("LiczbaProjektorow", 1);

        List<String> ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-03");
    }

    @Test
    public void simpleSearchTest3() {

        addAtributesSet1();

        RoomsAllocationSystemInterface.Schedule sch = new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 1);

//		LiczbaWymianPowietrza Klimatyzacja LProjektorow Naglosnienie
// K-I-01			1			T			1				T
// K-I-02			2			T			2				T
// K-I-03			3			T			3				N
// K-I-04			4			N			4				N
        Map<String, Boolean> required = new HashMap<>();
        required.put("Klimatyzacja", true); // 01, 02 i 03

        Map<String, Integer> limits = new HashMap<>();
        limits.put("LiczbaProjektorow", 1);  // wszystkie
        limits.put("LiczbaWymianPowietrza", 2); // 02, 03, 04

        // lacznie: 02 i 03

        Map<String, Integer> weights = new HashMap<>();
        weights.put("LiczbaProjektorow", 1);

        List<String> ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-03", "K-I-02");

        ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                18, required, limits, weights);
        checkIfListContains(ls, "K-I-03");
    }


    // testy: kolejnosc sortowanie
    // sortowanie przy wadze zlozonej z kilku czynnikow
    // schedule

    @Test
    public void simpleOrderTest() {
        System.out.println("simpleOrderTest");

        addAtributesSet1();

//		LiczbaWymianPowietrza Klimatyzacja LProjektorow Naglosnienie LiczbaTablic
// K-I-01			1			T			1				T			40
// K-I-02			2			T			2				T			1
// K-I-03			3			T			3				N			1
// K-I-04			4			N			4				N			1

        addAttributeToRoom("LiczbaTablic", "K-I-01", 40);
        addAttributeToRoom("LiczbaTablic", "K-I-02", 1);
        addAttributeToRoom("LiczbaTablic", "K-I-03", 1);
        addAttributeToRoom("LiczbaTablic", "K-I-04", 1);

        RoomsAllocationSystemInterface.Schedule sch = new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 1);

        Map<String, Boolean> required = new HashMap<>();
        required.put("Klimatyzacja", true);  // K-I-01,02,03

        Map<String, Integer> limits = new HashMap<>();
        limits.put("LiczbaProjektorow", 1);

        Map<String, Integer> weights = new HashMap<>();
        weights.put("LiczbaProjektorow", 1);
        weights.put("Naglosnienie", 5);

        List<String> ls = null;

        try {
            ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                    10, required, limits, weights);
        } catch (Exception e) {
            System.out.println("Wyjatek po getRooms");
            System.out.println(e);
            fail("Po wykonaniu getRooms w metodzie SimpleOrderTest pojawil sie wyjatek. " +
                    "Sortowanie wg. wag przypisanych liczbieProjektorow i naglosnieniu");
        }

        checkOrder(ls, "K-I-02", "K-I-01", "K-I-03");

        weights.put("LiczbaWymianPowietrza", 50);

        try {
            ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                    10, required, limits, weights);
        } catch (Exception e) {
            System.out.println("Wyjatek po getRooms");
            System.out.println(e);
            fail("Po wykonaniu getRooms w metodzie SimpleOrderTest pojawil sie wyjatek. " +
                    "Sortowanie wg. wag przypisanych liczbieProjektorow, naglosnieniu i liczbie wymian powietrza");
        }

        checkOrder(ls, "K-I-03", "K-I-02", "K-I-01");

        weights.put("LiczbaTablic", 150);

        try {
            ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                    10, required, limits, weights);
        } catch (Exception e) {
            System.out.println("Wyjatek po getRooms");
            System.out.println(e);
            fail("Po wykonaniu getRooms w metodzie SimpleOrderTest pojawil sie wyjatek. " +
                    "Sortowanie wg. wag przypisanych liczbieProjektorow, naglosnieniu, liczbie wymian powietrza" +
                    " i LiczbieTablic");
        }

        checkOrder(ls, "K-I-01", "K-I-03", "K-I-02");
    }


    @Test
    public void searchTestWithSchedule() {
        System.out.println("searchTestWithSchedule");

        addAtributesSet1();
        // dzien nr 1
        RoomsAllocationSystemInterface.Schedule sch = new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 1);

        Map<String, Boolean> required = new HashMap<>();
        required.put("Klimatyzacja", true);

        Map<String, Integer> limits = new HashMap<>();
        limits.put("LiczbaProjektorow", 1);
        limits.put("LiczbaWymianPowietrza", 2);

        Map<String, Integer> weights = new HashMap<>();
        weights.put("LiczbaProjektorow", 1);

        List<String> ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-03", "K-I-02");

        // blokada jednego pomieszczenia
        rasi.allocateRoom("K-I-03", sch);

        ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-02");

        // dzien nr 2
        RoomsAllocationSystemInterface.Schedule sch2 = new RoomsAllocationSystemInterface.Schedule(2, 1, 1, 1);

        ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch2,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-03", "K-I-02");

        // blokada obu pomieszczen na dzien nr 2
        rasi.allocateRoom("K-I-03", sch2);
        rasi.allocateRoom("K-I-02", sch2);

        ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch2,
                10, required, limits, weights);

        assertEquals("Wszystkie pomieszczenia maja przypisane zajecia", 0, ls.size());
    }

    @Test
    public void searchTestWithSchedule2() {
        System.out.println("searchTestWithSchedule2");

        addAtributesSet1();

        RoomsAllocationSystemInterface.Schedule sch = new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 2);
// tydzien po sch
        RoomsAllocationSystemInterface.Schedule sch2 = new RoomsAllocationSystemInterface.Schedule(1 + 7, 1, 1, 1);
// trzy tygodnie po sch
        // uwaga: w terminie II ulegnie zmianie na 2 tygodnie !!!
        RoomsAllocationSystemInterface.Schedule sch3 = new RoomsAllocationSystemInterface.Schedule(1 + 3 * 7, 1, 1, 1);

        Map<String, Boolean> required = new HashMap<>();
        required.put("Klimatyzacja", true);

        Map<String, Integer> limits = new HashMap<>();
        limits.put("LiczbaProjektorow", 1);
        limits.put("LiczbaWymianPowietrza", 2);

        Map<String, Integer> weights = new HashMap<>();
        weights.put("LiczbaProjektorow", 1);

        List<String> ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-03", "K-I-02");

        // blokada jednego pomieszczenia, ale na wiecej niz tydzien
        rasi.allocateRoom("K-I-03", sch);

        ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch2,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-02");  // drugie pomieszczenia nadal zajete

        ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch3,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-03", "K-I-02");  // oba juz wolne
    }

    @Test
    public void searchTestWithSchedule3() {

        System.out.println("searchTestWithSchedule3");

        addAtributesSet1();

        RoomsAllocationSystemInterface.Schedule sch = new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 2);
// tydzien po sch
        RoomsAllocationSystemInterface.Schedule sch2 = new RoomsAllocationSystemInterface.Schedule(1 + 7, 1, 1, 1);

        Map<String, Boolean> required = new HashMap<>();
        required.put("Klimatyzacja", true);

        Map<String, Integer> limits = new HashMap<>();
        limits.put("LiczbaProjektorow", 1);
        limits.put("LiczbaWymianPowietrza", 2);

        Map<String, Integer> weights = new HashMap<>();
        weights.put("LiczbaProjektorow", 1);

        List<String> ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch2,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-03", "K-I-02");

        // blokada jednego pomieszczenia w terminie sch2
        rasi.allocateRoom("K-I-03", sch2);

        ls = rasi.getRooms(RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, sch,
                10, required, limits, weights);
        checkIfListContains(ls, "K-I-02");  // jedno z pomieszczen nie jest dostepne w kazdym z wymaganych terminow

    }
}
