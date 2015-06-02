//package prir.zad7;

import org.omg.CORBA.IntHolder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tomasz.lelek on 02/06/15.
 */
public class optimizationImpl extends optimizationPOA {
    AtomicInteger clientId = new AtomicInteger();
    Map<Integer, ClientData> clientDataMap = new LinkedHashMap<>();
    
    @Override
    public void register(short ip, int timeout, IntHolder id) {
        System.out.println("register srever : " + ip + " with timeout : " + timeout);
        int idG = clientId.incrementAndGet();
        id.value = idG;
        clientDataMap.put(idG, new ClientData(ip, timeout));
    }

    @Override
    public void hello(int id) {

    }

    @Override
    public void best_range(rangeHolder r) {

    }
}

class ClientData{


    private final short ip;
    private final int timeout;

    public ClientData(short ip, int timeout) {

        this.ip = ip;
        this.timeout = timeout;
    }
}
