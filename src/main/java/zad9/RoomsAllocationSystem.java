package zad9;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;

/**
 * Created by tomasz.lelek on 20/02/15.
 */
class RoomsAllocationSystem implements RoomsAllocationSystemInterface {
    
   
   public static class RoomSchedule{
       private final int day;
       private final int hourFrom;
       private final int hourTo;

       public RoomSchedule(int day, int hourFrom, int hourTo){
           this.day = day;
           this.hourFrom = hourFrom;
           this.hourTo = hourTo;
       }
       public boolean collideWith(RoomSchedule scheduleToBook) {
           if(scheduleToBook.day != day) return false;
           
           List<Integer> a= new LinkedList<Integer>();
           List<Integer> b= new LinkedList<Integer>();
           for (int i = hourFrom; i <= hourTo; i++) {
               a.add(i);
           }

           for (int i = scheduleToBook.hourFrom; i <= scheduleToBook.hourTo; i++) {
               b.add(i);
           }
           return intersect(a, b).size() > 1;

           
       }

       private List<Integer> intersect(List<Integer> A, List<Integer> B) {
           List<Integer> rtnList = new LinkedList<Integer>();
           for(Integer dto : A) {
               if(B.contains(dto)) {
                   rtnList.add(dto);
               }
           }
           return rtnList;
       }

       @Override
       public String toString() {
           return "RoomSchedule{" +
                   "day=" + day +
                   ", hourFrom=" + hourFrom +
                   ", hourTo=" + hourTo +
                   '}';
       }
   }
    
    class Room{
        Integer roomRank;
        private final String id;
        private final RoomType roomType;
        private final int numberOfSeats;
        Map<String, Object> attributes = new LinkedHashMap<String, Object>();
        List<RoomSchedule> roomSchedules = new LinkedList<RoomSchedule>();

        @Override
        public String toString() {
            return "Room{" +
                    "id='" + id + '\'' +
                    ", roomType=" + roomType +
                    ", numberOfSeats=" + numberOfSeats +
                    ", attributes=" + attributes +
                    ", roomSchedules=" + roomSchedules +
                    '}';
        }

        public Room(String id, RoomType roomType, int numberOfSeats ){


            this.id = id;
            this.roomType = roomType;
            this.numberOfSeats = numberOfSeats;
        }

        public boolean hasRequiredAttributes(Map<String, Boolean> requiredAttributes) {
            for(String key : requiredAttributes.keySet()) {
                Object value = attributes.get(key);
                System.out.println("key, value : " + key + ", " + value);
                boolean v = false;
                if (value != null){
                    v = (boolean)value;
                }
                System.out.println("key, value : " + key + ", " + v);
                if(!attributes.containsKey(key) || !v) return false;
            }
            return true;
        }
    }
    Map<String, Room> rooms = new LinkedHashMap<String, Room>();
    Map<String, AttributeType> attributes = new LinkedHashMap<String, AttributeType>();
    
    @Override
    public void addAttribute(String name, AttributeType at) {
        attributes.put(name, at);
    }

    @Override
    public void addRoom(String id, RoomType rt, int ns) {
        rooms.put(id, new Room(id, rt, ns));
    }

    @Override
    public Map<String, Object> getRoomAttributes(String id) {
        return rooms.get(id).attributes;
    }

    @Override
    public void addAttributeToRoom(String name, String id, Integer value) {
        rooms.get(id).attributes.put(name, value);

    }

    @Override
    public void addAttributeToRoom(String name, String id, String value) {
        rooms.get(id).attributes.put(name, value);

    }

    @Override
    public void addAttributeToRoom(String name, String id, Boolean value) {
        rooms.get(id).attributes.put(name, value);
    }

    @Override
    public boolean allocateRoom(String id, Schedule sch) {
        return scheduleAction(id, sch);

    }

    private boolean scheduleAction(String id, Schedule sch) {
        for (int i = 0; i < sch.numberOfUses; i++) {
            RoomSchedule scheduleToBook = new RoomSchedule(sch.firstDay + i*7, sch.beginHour, sch.beginHour + sch.hours);
            if(scheduleIsAvailable(rooms.get(id).roomSchedules, scheduleToBook)) {
                rooms.get(id).roomSchedules.add(scheduleToBook);
            }else {
                return false;
            }
        }
        return true;
    }


    private boolean roomsAreAvailable(String id, Schedule sch) {
        for (int i = 0; i < sch.numberOfUses; i++) {
            RoomSchedule scheduleToBook = new RoomSchedule(sch.firstDay + i*7, sch.beginHour, sch.beginHour + sch.hours);
            if(!scheduleIsAvailable(rooms.get(id).roomSchedules, scheduleToBook)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean scheduleIsAvailable(List<RoomSchedule> roomSchedules, RoomSchedule scheduleToBook) {
        for( RoomSchedule roomSchedule : roomSchedules){
            if( roomSchedule.collideWith(scheduleToBook) ){
                return false;
            }
        }
        return true;
    }
    
    @Override
    public List<String> getRooms(RoomType rt, Schedule sch, int seatsLimit,
                                 Map<String, Boolean> requiredAttributes, 
                                 Map<String, Integer> attributesLimit,
                                 Map<String, Integer> attributesWeight) {
        
        List<Room> roomsAvailable = new LinkedList<Room>();
        for( Room room : rooms.values()){
            boolean didRoomAvailable = roomsAreAvailable(room.id, sch);
            System.out.println("didRoomAvailable ? " + didRoomAvailable + " for : " + room + ", schedule : " +sch );
            
            if ( room.roomType.equals(rt) 
                    && room.numberOfSeats >= seatsLimit 
                    && didRoomAvailable 
                    && room.hasRequiredAttributes(requiredAttributes)){
                roomsAvailable.add(room);
            }
        }
        System.out.println(roomsAvailable);
       
        
        return sortByAttributeWeight(roomsAvailable, attributesLimit, attributesWeight);
        
        

    }


    class Attribute{
        private final String name;
        private final Integer value;
        private Integer limit = 0;


        @Override
        public String toString() {
            return "Attribute{" +
                    "name='" + name + '\'' +
                    ", value=" + value +
                    ", limit=" + limit +
                    '}';
        }

        public Attribute(String name, Integer value,Integer limit){

            this.name = name;
            this.value = value;
            if(limit != null){
                this.limit = limit;
            }
        }

    }
    
    
    //W przypadku atrybutów o wartości liczbowek
    // mnożymy punktację atrybutu przez różnicę
    // jego wartości dla danej sali i limitu
    // (o ile był on podany, jeśli nie to zakładamy, że wynosi on zero).

    //Przykład: Limit liczbowy uzyty do przeszukiwania sal
    // okreslał minimalną liczbę wymian powietrza na 5. Do porządkowania
    // wyników przesłana została mapa zawierająca: "zielone sciany" -> 20, 
    // "sala nasloneczniona" -> 30, "liczba wymian powietrza na godzine" -> 10.
    // Mamy nastepujace podlegające sortowaniu sale:
    
    private List<String> sortByAttributeWeight(List<Room> roomsAvailable, Map<String, Integer> attributesLimit, Map<String, Integer> attributesWeight) {
        List<String> result = new LinkedList<String>();
        List<Attribute> attributes = new LinkedList<Attribute>();
        for( Map.Entry<String, Integer> weight : attributesWeight.entrySet()){
            Integer limit = attributesLimit.get(weight.getKey());
            attributes.add(new Attribute(weight.getKey(), weight.getValue(), limit));
        }

        for(Room room :roomsAvailable){
            Integer roomRank = 0;
            for( Attribute attribute : attributes){
                Object value = room.attributes.get(attribute.name);
                if(value instanceof Boolean){
                    roomRank += attribute.value;
                }else if(value instanceof Integer){
                    roomRank += ((Integer)value) * (attribute.value - attribute.limit);
                }
            }
            room.roomRank = roomRank;
        }
        
        roomsAvailable.sort(new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return o1.roomRank - o2.roomRank;
            }
        });
        
        for(Room room : roomsAvailable){
            result.add(room.id);
        }
        

        System.out.println("attributes : " + attributes);
        return result;
    }
}
