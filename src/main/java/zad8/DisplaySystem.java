package zad8;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DisplaySystem implements DisplaySystemInterfaceExt {
    Map<Integer, Display> displays = new LinkedHashMap<Integer, Display>();
    Map<Integer, Group> groupOfDisplays = new LinkedHashMap<Integer, Group>();
    private static final AtomicInteger displaysCounter = new AtomicInteger();
    private static final AtomicInteger groupCounter = new AtomicInteger();

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
        if (validateDisplays(displayID)) {
            return false;
        }
        displays.get(displayID).addMessage(message);
        return true;
    }

    @Override
    public boolean toGroup(int groupID, String message) {
        if (validateGroupOfDisplays(groupID)) {
            return false;
        }
        Set<Integer> alreadyDisplayed = new HashSet<Integer>();
        
        Group group = groupOfDisplays.get(groupID);
        displayForGroup(message, alreadyDisplayed, group);
        
        loopGroup(group, alreadyDisplayed, message);
        
        return true;
    }

    private void loopGroup(Group group, Set<Integer> alreadyDisplayed, String message) {
        if( group.groups.size() == 0 ) return;
        
        for(Group g : group.groups){
            loopGroup(g, alreadyDisplayed, message);
            displayForGroup(message, alreadyDisplayed, g);
        }
    }

    private void displayForGroup(String message, Set<Integer> alreadyDisplayed, Group group) {
        for ( Display display : group.displays) {
            if(!alreadyDisplayed.contains(display.id)) {
                display.addMessage(message);
                alreadyDisplayed.add(display.id);
            }
        }
    }

    @Override
    public String[] get(int displayID) {
        if (validateDisplays(displayID)) {
            return null;
        }
        List<String> messagesToDisplay = displays.get(displayID).messagesToDisplay;
        int size = messagesToDisplay.size();

        if (size > displays.get(displayID).rows) {
            List<String> subList = messagesToDisplay.subList(size - displays.get(displayID).rows, size);
            return subList.toArray(new String[subList.size()]);
        }
        return messagesToDisplay.toArray(new String[messagesToDisplay.size()]);
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
        for(Display display : displays.values()){
            display.addMessage(messageToAll);
        }
    }

    class Display {
        int id;
        int rows;
        List<String> messagesToDisplay;

        public Display(int rows, int id) {
            this.rows = rows;
            this.id = id;
            this.messagesToDisplay = new ArrayList<String>();
        }

        public void addMessage(String message) {
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
