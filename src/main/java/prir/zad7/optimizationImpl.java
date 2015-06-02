//package prir.zad7;

import org.omg.CORBA.IntHolder;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tomasz.lelek on 02/06/15.
 */
public class optimizationImpl extends optimizationPOA {
    AtomicInteger clientId = new AtomicInteger();
    Map<Short, ClientData> clientDataMap = new TreeMap<>();
    public optimizationImpl(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new CountAvailability(clientDataMap), 0, 1, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void register(short ip, int timeout, IntHolder id) {
        System.out.println("register srever : " + ip + " with timeout : " + timeout);
        int idG = clientId.incrementAndGet();
        id.value = idG;
        clientDataMap.put(ip, new ClientData(idG, timeout));
    }

    @Override
    public void hello(int id) {
        System.out.println("hello for id : "+ id);
        Set<Map.Entry<Short, ClientData>> entries = clientDataMap.entrySet();
        for(Map.Entry<Short, ClientData> entry : entries){
            if(entry.getValue().id == id){
                entry.getValue().refreshCounter();
            }
        }

    }

    @Override
    public void best_range(rangeHolder r) {//tod test it properly
        short startRange=-1;
        short endRange=255;
        int lengthOfRange;
        Set<Map.Entry<Short, ClientData>> entries = clientDataMap.entrySet();
        
        short index = 0;
        for(Map.Entry<Short, ClientData> entry : entries){
            if(entry.getValue().working && startRange != -1){
                endRange = index;
            }else if(entry.getValue().working){
                startRange = index;
                endRange = index;
            }else if(! entry.getValue().working) {
                startRange = index;
                endRange = index;
            }
            
            index++;
        }
        r.value = new range(startRange, endRange);
    }
}

class ClientData{
    @Override
    public String toString() {
        return "ClientData{" +
                "id=" + id +
                ", timeout=" + timeout +
                ", working=" + working +
                ", counter=" + counter +
                '}';
    }

//    private final short ip;
    private final int timeout;
    boolean working;
    AtomicInteger counter;
    public final int id;

    public ClientData(int id, int timeout) {

//        this.ip = ip;
        this.timeout = timeout;
        this.working = true;
        this.counter = new AtomicInteger(timeout);
        this.id = id;
    }

    public void refreshCounter() {
        counter = new AtomicInteger(timeout);
    }
}

class CountAvailability implements Runnable{

    private Map<Short, ClientData> clientDataMap;

    public CountAvailability(Map<Short, ClientData> clientDataMap) {

        this.clientDataMap = clientDataMap;
    }

    @Override
    public void run() {
        Set<Map.Entry<Short, ClientData>> entries = clientDataMap.entrySet();
        for(Map.Entry<Short, ClientData> entry : entries){
            if(entry.getValue().counter.get() == 0){
                entry.getValue().working = false;
            }else {
                entry.getValue().counter.decrementAndGet();
                System.out.println("decrement for : "+ entry);
            }
        }
    }
}

