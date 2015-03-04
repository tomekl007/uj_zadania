package zad10;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class DisplaySystem implements DisplayTimeExt {
    private final int WITHOUT_TIMEOUT = Integer.MAX_VALUE;
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    Map<Integer, Display> displays = new LinkedHashMap<Integer, Display>();
    Map<Integer, Group> groupOfDisplays = new LinkedHashMap<Integer, Group>();
    private static final AtomicInteger displaysCounter = new AtomicInteger();
    private static final AtomicInteger groupCounter = new AtomicInteger();

    public DisplaySystem(){
        executor.scheduleAtFixedRate(new CountDownMessages(),
                0, 1, TimeUnit.MILLISECONDS);
        
    }
    
    class CountDownMessages extends TimerTask{
        @Override
        public void run() {
            displays.values()
                    .forEach(display -> display.messagesToDisplay
                            .forEach(message -> message.holdTime--));
        }
    }
    
    @Override
    public int registerDisplay(int rows) {
        int id = displaysCounter.getAndIncrement();
        displays.put(id, new Display(rows, id));
        return id;
    }

    @Override
    public int createGroup() {
        int id = groupCounter.incrementAndGet();
        groupOfDisplays.put(id, new Group(id));
        return id;
    }

    @Override
    public boolean addDisplayToGroup(int displayID, int groupID) {
        if (validateGroupOfDisplays(groupID)) {
            return false;
        }
        if (validateDisplays(displayID)) {
            return false;
        }

        Group group = groupOfDisplays.get(groupID);

        group.add(displays.get(displayID));
        return true;

    }

    private boolean validateDisplays(int displayID) {
        return !displays.containsKey(displayID);
    }

    @Override
    public boolean removeGroup(int groupID) {
        if (validateGroupOfDisplays(groupID)) {
            return false;
        }
        groupOfDisplays.remove(groupID);
        return true;
    }

    private boolean validateGroupOfDisplays(int groupID) {
        return !groupOfDisplays.containsKey(groupID);
    }

    @Override
    public boolean deregisterDisplay(int displayID) {
        if (validateDisplays(displayID)) {
            return false;
        }
        displays.remove(displayID);
        return true;
    }

    @Override
    public boolean toDisplay(int displayID, String message) {
       return toDisplay(displayID, message, WITHOUT_TIMEOUT);
    }

    @Override
    public boolean toGroup(int groupID, String message) {
      return toGroup(groupID, message, WITHOUT_TIMEOUT);
    }

    private void loopGroup(Group group, Set<Integer> alreadyDisplayed, String message, long holdTime) {
        if( group.groups.size() == 0 ) return;

        for(Group g : group.groups){
            loopGroup(g, alreadyDisplayed, message, holdTime);
            displayForGroup(message, alreadyDisplayed, g, holdTime);
        }
    }

    private void displayForGroup(String message, Set<Integer> alreadyDisplayed, Group group, long holdTime) {
        for ( Display display : group.displays) {
            if(!alreadyDisplayed.contains(display.id)) {
                display.addMessage(new Message(message, holdTime));
                alreadyDisplayed.add(display.id);
            }
        }
    }

    @Override
    public String[] get(int displayID) {
        if (validateDisplays(displayID)) {
            return null;
        }
        List<Message> messagesToDisplay = displays
                .get(displayID)
                .messagesToDisplay
                .stream()
                .filter(message -> message.holdTime > 0)
                .collect(Collectors.toList());
        
        int size = messagesToDisplay.size();

        if (size > displays.get(displayID).rows) {
            List<String> subList = messagesToDisplay.subList(size - displays.get(displayID).rows, size)
                    .stream()
                    .map(m -> m.message)
                    .collect(Collectors.toList());
            
            return subList.toArray(new String[subList.size()]);
        }
        return messagesToDisplay
                .stream()
                .map(m -> m.message)
                .collect(Collectors.toList())
                .toArray(new String[messagesToDisplay.size()]);
    }

    @Override
    public boolean addGroupToGroup(int sourceGroupID, int destinationGroupID) {
        if (validateGroupOfDisplays(sourceGroupID)) {
            return false;
        }
        if (validateGroupOfDisplays(destinationGroupID)) {
            return false;
        }
        groupOfDisplays.get(sourceGroupID).add(groupOfDisplays.get(destinationGroupID));
        return true;
    }

    @Override
    public boolean removeGroupFromGroup(int groupToRemoveID, int removeFromGroupID) {
        if (validateGroupOfDisplays(groupToRemoveID)) {
            return false;
        }
        if (validateGroupOfDisplays(removeFromGroupID)) {
            return false;
        }
        groupOfDisplays.get(removeFromGroupID).remove(groupOfDisplays.get(groupToRemoveID));
        return true;
    }

    @Override
    public void broadcast(String messageToAll) {
       broadcast(messageToAll, WITHOUT_TIMEOUT);
    }

    @Override
    public boolean toDisplay(int displayID, String message, long holdTime) {
        if (validateDisplays(displayID)) {
            return false;
        }
        displays.get(displayID).addMessage(new Message(message, holdTime));
        return true;
    }

    @Override
    public boolean toGroup(int groupID, String message, long holdTime) {
        if (validateGroupOfDisplays(groupID)) {
            return false;
        }
        Set<Integer> alreadyDisplayed = new HashSet<Integer>();

        Group group = groupOfDisplays.get(groupID);
        displayForGroup(message, alreadyDisplayed, group, holdTime);

        loopGroup(group, alreadyDisplayed, message, holdTime);

        return true;
    }

    @Override
    public void broadcast(String messageToAll, long holdTime) {
        for(Display display : displays.values()){
            display.addMessage(new Message(messageToAll, holdTime));
        }
    }
    
    class Message{
        String message;
        long holdTime;

        public Message(String message, long holdTime) {
            this.message = message;
            this.holdTime = holdTime;
        }

        public Message(String message) {
            this.message = message;
            holdTime = -1L;
        }


        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    ", holdTime=" + holdTime +
                    '}';
        }
    }

    class Display {
        int id;
        int rows;
        List<Message> messagesToDisplay;

        public Display(int rows, int id) {
            this.rows = rows;
            this.id = id;
            this.messagesToDisplay = new ArrayList<Message>();
        }

        public void addMessage(Message message) {
            messagesToDisplay.add(message);
        }

    }

    class Group {
        int id;
        List<Display> displays;
        List<Group> groups;
        public Group(int id){
            this.id = id;
            this.displays = new ArrayList<Display>();
            this.groups = new ArrayList<Group>();

        }

        public void add(Display display) {
            this.displays.add(display);
        }

        public void add(Group group) {
            this.groups.add(group);
        }

        public void remove(Group group) {
            groups.remove(group);
        }
    }
}
