package zad8;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DisplaySystem implements DisplaySystemInterfaceExt {
    Map<Integer, Display> displays = new LinkedHashMap<Integer, Display>();
    Map<Integer, List<Integer>> groupOfDisplays = new LinkedHashMap<Integer, List<Integer>>();
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
        groupOfDisplays.put(id, new LinkedList<Integer>());
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

        List<Integer> displaysForThatGroup = groupOfDisplays.get(groupID);
        if (displaysForThatGroup == null) {
            displaysForThatGroup = new LinkedList<Integer>();
        }
        displaysForThatGroup.add(displayID);
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
        for (Integer displayId : groupOfDisplays.get(groupID)) {
            displays.get(displayId).addMessage(message);
        }
        return true;
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
        return false;
    }

    @Override
    public boolean removeGroupFromGroup(int groupToRemoveID, int removeFromGroupID) {
        return false;
    }

    @Override
    public void broadcast(String messageToAll) {

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
}
