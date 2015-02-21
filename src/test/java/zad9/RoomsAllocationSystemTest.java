package zad9;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RoomsAllocationSystemTest {

    private RoomsAllocationSystem instance;

    @Test
    public void testAddAttributesToRoom() throws Exception {
        // setup
        this.instance = new RoomsAllocationSystem();
        this.instance.addRoom("id", RoomsAllocationSystemInterface.RoomType.OTHER, 10);
        this.instance.addAttributeToRoom("a1", "id", 1);
        this.instance.addAttributeToRoom("a2", "id", "2");
        this.instance.addAttributeToRoom("a3", "id", true);

        // execute
        final Map<String, Object> attributes = this.instance.getRoomAttributes("id");

        // verify
        assertNotNull(attributes);
        assertEquals(3, attributes.size());
        assertEquals(1, attributes.get("a1"));
        assertEquals("2", attributes.get("a2"));
        assertEquals(true, attributes.get("a3"));
    }

    @Test
    public void testRoomAllocation() {
        // setup
        this.instance = new RoomsAllocationSystem();
        this.instance.addRoom("id", RoomsAllocationSystemInterface.RoomType.OTHER, 10);
        this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(3, 10, 2, 3)); // 3, 10, 17: 10-12

        // execute & verify
        assertFalse(this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(17, 11, 2, 2))); // 17, 24: 11-13
        assertFalse(this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(10, 10, 1, 2))); // 10, 17: 10-11
        assertFalse(this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(10, 11, 1, 2))); // 10, 17: 11-12
        assertFalse(this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(3, 11, 1, 2))); // 3, 10: 11-12

        assertTrue(this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(5, 10, 2, 3))); // 5, 12, 19: 10-12
        assertTrue(this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(3, 12, 2, 3))); // 3, 10, 17: 12-14
        assertTrue(this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(3, 8, 2, 3))); // 3, 10, 17: 8-10

        assertFalse(this.instance.allocateRoom("id", new RoomsAllocationSystemInterface.Schedule(3, 8, 2, 3))); // 3, 10, 17: 8-10
    }

    @Test
    public void testGetRoomsTypeAndSeatsLimitAndScheduleAndAttributes() {
        // setup
        final int MINIMUM_SEATS = 10;
        final String CORRECT_ROOM_1 = "correct room";
        final String CORRECT_ROOM_2 = "correct room 2";
        this.instance = new RoomsAllocationSystem();
        this.instance.addAttribute("a1", RoomsAllocationSystemInterface.AttributeType.BOOLEAN);
        this.instance.addAttribute("a2", RoomsAllocationSystemInterface.AttributeType.INTEGER);
        this.instance.addRoom(CORRECT_ROOM_1, RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 10); // yes
        this.instance.addRoom(CORRECT_ROOM_2, RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 12); // yes
        this.instance.addRoom("too few seats", RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 8);  // no: too few seats
        this.instance.addRoom("wrong type", RoomsAllocationSystemInterface.RoomType.OTHER, 100); // no: wrong type
        this.instance.addRoom("overlapping schedule", RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 100);
        this.instance.allocateRoom("overlapping schedule", new RoomsAllocationSystemInterface.Schedule(1, 1, 1, 1)); // no: overlapping schedule
        this.instance.addRoom("overlapping schedule 2", RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 150);
        this.instance.allocateRoom("overlapping schedule 2", new RoomsAllocationSystemInterface.Schedule(8, 4, 1, 1)); // no: overlapping schedule
        this.instance.allocateRoom(CORRECT_ROOM_2, new RoomsAllocationSystemInterface.Schedule(2, 2, 2, 2)); // yes: ok schedule
        this.instance.addRoom("attribute a1 should be true", RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 150);
        this.instance.addAttributeToRoom("a1", "attribute a1 should be true", false); // no: attribute should be true
        this.instance.addRoom("attribute a1 missing", RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 150);
        this.instance.addRoom("attribute a2 missing", RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 150);
        this.instance.addRoom("attribute a2 too low", RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB, 150);
        this.instance.addAttributeToRoom("a2", "attribute a2 too low", 4);

        this.instance.addAttributeToRoom("a1", CORRECT_ROOM_1, true);
        this.instance.addAttributeToRoom("a2", CORRECT_ROOM_1, 5);
        this.instance.addAttributeToRoom("a1", CORRECT_ROOM_2, true);
        this.instance.addAttributeToRoom("a2", CORRECT_ROOM_2, 100);

        final Map<String, Boolean> requiredAttributes = new HashMap<>();
        requiredAttributes.put("a1", true);
        final Map<String, Integer> attributesLimit = new HashMap<>();
        attributesLimit.put("a2", 5);

        // execute
        final List<String> rooms = this.instance.getRooms(
                RoomsAllocationSystemInterface.RoomType.COMPUTER_LAB,
                new RoomsAllocationSystemInterface.Schedule(1, 1, 8, 10),
                MINIMUM_SEATS,
                requiredAttributes,
                attributesLimit,
                Collections.<String, Integer>emptyMap()
        );

        // verify
        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertTrue(rooms.contains(CORRECT_ROOM_1));
        assertTrue(rooms.contains(CORRECT_ROOM_2));
    }

    @Test
    public void testGetRoomsOrder() {
        // setup
        final String ATTR_LICZBA_WYMIAN = "liczba wymian";
        final String ATTR_ZIELONE_SCIANY = "zielone sciany";
        final String ATTR_SALA_NASLONECZNIONA = "sala nasloneczniona";
        final String ROOM_A = "A-1-01";
        final String ROOM_B = "B-2-01";

        this.instance = new RoomsAllocationSystem();

        this.instance.addAttribute(ATTR_LICZBA_WYMIAN, RoomsAllocationSystemInterface.AttributeType.INTEGER);
        this.instance.addAttribute(ATTR_ZIELONE_SCIANY, RoomsAllocationSystemInterface.AttributeType.BOOLEAN);
        this.instance.addAttribute(ATTR_SALA_NASLONECZNIONA, RoomsAllocationSystemInterface.AttributeType.BOOLEAN);

        this.instance.addRoom(ROOM_A, RoomsAllocationSystemInterface.RoomType.OTHER, 0);
        this.instance.addRoom(ROOM_B, RoomsAllocationSystemInterface.RoomType.OTHER, 0);

        this.instance.addAttributeToRoom(ATTR_ZIELONE_SCIANY, ROOM_A, true);
        this.instance.addAttributeToRoom(ATTR_SALA_NASLONECZNIONA, ROOM_A, false);
        this.instance.addAttributeToRoom(ATTR_LICZBA_WYMIAN, ROOM_A, 7);

        this.instance.addAttributeToRoom(ATTR_ZIELONE_SCIANY, ROOM_B, true);
        this.instance.addAttributeToRoom(ATTR_SALA_NASLONECZNIONA, ROOM_B, true);
        this.instance.addAttributeToRoom(ATTR_LICZBA_WYMIAN, ROOM_B, 5);

        final Map<String, Integer> weights = new HashMap<>();
        weights.put(ATTR_ZIELONE_SCIANY, 20);
        weights.put(ATTR_SALA_NASLONECZNIONA, 30);
        weights.put(ATTR_LICZBA_WYMIAN, 10);
        final Map<String, Integer> limits = new HashMap<>();
        limits.put(ATTR_LICZBA_WYMIAN, 5);

        // execute
        final List<String> rooms = this.instance.getRooms(
                RoomsAllocationSystemInterface.RoomType.OTHER,
                new RoomsAllocationSystemInterface.Schedule(0, 0, 0, 0),
                -100,
                Collections.<String, Boolean>emptyMap(),
                limits,
                weights
        );

        // verify
        assertEquals(ROOM_B, rooms.get(0));
        assertEquals(ROOM_A, rooms.get(1));
    }
    
    
    @Test
    public void shouldCollideWith(){
        RoomsAllocationSystem.RoomSchedule firstSchedule = new RoomsAllocationSystem.RoomSchedule(1, 2, 5);
        RoomsAllocationSystem.RoomSchedule secondSchedule = new RoomsAllocationSystem.RoomSchedule(1, 4, 5);
        //then
        boolean result = firstSchedule.collideWith(secondSchedule);
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(secondSchedule.collideWith(firstSchedule)).isTrue();

    }

    @Test
    public void shouldNotCollideWith(){
        RoomsAllocationSystem.RoomSchedule secondSchedule = new RoomsAllocationSystem.RoomSchedule(1, 5, 6);
        RoomsAllocationSystem.RoomSchedule firstSchedule = new RoomsAllocationSystem.RoomSchedule(1, 2, 5);
        //then
        boolean result = firstSchedule.collideWith(secondSchedule);
        Assertions.assertThat(result).isFalse();
        Assertions.assertThat(secondSchedule.collideWith(firstSchedule)).isFalse();

    }
}