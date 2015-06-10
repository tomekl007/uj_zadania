package prir.zad7;

import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by tomasz.lelek on 03/06/15.
 */
public class RangeTest {

    
    @Test
    public void shouldFindRange(){
        //given
        Map<Short, ClientData> clientDataMap = new TreeMap<>();
        clientDataMap.put((short)1, new ClientData(true));
        clientDataMap.put((short)2, new ClientData(true));
        clientDataMap.put((short)3, new ClientData(false));
        clientDataMap.put((short)4, new ClientData(true));
        clientDataMap.put((short)5, new ClientData(true));
        clientDataMap.put((short)6, new ClientData(true));
        
        //when
        range range = new RangeTest().best_range(clientDataMap);
        
        //then
        assertThat(range.bestStartIp).isEqualTo(((short)4));
        assertThat(range.bestEndIp).isEqualTo((short)6);
        

    }


    @Test//todo make that test pass
    public void shouldFindRangeIndexAgnostic(){
        //given
        Map<Short, ClientData> clientDataMap = new TreeMap<>();
        clientDataMap.put((short)11, new ClientData(true));
        clientDataMap.put((short)12, new ClientData(true));
        clientDataMap.put((short)13, new ClientData(false));
        clientDataMap.put((short)14, new ClientData(true));
        clientDataMap.put((short)15, new ClientData(true));
        clientDataMap.put((short)16, new ClientData(true));

        //when
        range range = new RangeTest().best_range(clientDataMap);

        //then
        assertThat(range.bestStartIp).isEqualTo(((short)14));
        assertThat(range.bestEndIp).isEqualTo((short)16);


    }


    @Test
    public void shouldFindRangeNotLinear(){
        //given
        Map<Short, ClientData> clientDataMap = new TreeMap<>();
        clientDataMap.put((short)1, new ClientData(true));
        clientDataMap.put((short)3, new ClientData(true));
        clientDataMap.put((short)4, new ClientData(true));
        clientDataMap.put((short)5, new ClientData(true));

        //when
        range range = new RangeTest().best_range(clientDataMap);
        System.out.println("---> " + range);

        //then
        assertThat(range.bestStartIp).isEqualTo(((short)3));
        assertThat(range.bestEndIp).isEqualTo((short)5);


    }


    @Test
    public void shouldFindRangeAll(){
        //given
        Map<Short, ClientData> clientDataMap = new TreeMap<>();
        clientDataMap.put((short)1, new ClientData(true));
        clientDataMap.put((short)2, new ClientData(true));
        clientDataMap.put((short)3, new ClientData(true));
        clientDataMap.put((short)4, new ClientData(true));
        clientDataMap.put((short)5, new ClientData(true));
        clientDataMap.put((short)6, new ClientData(true));

        //when
        range range = new RangeTest().best_range(clientDataMap);

        //then
        assertThat(range.bestStartIp).isEqualTo(((short)1));
        assertThat(range.bestEndIp).isEqualTo((short)6);


    }


    @Test
    public void shouldFindRangeStart(){
        //given
        Map<Short, ClientData> clientDataMap = new TreeMap<>();
        clientDataMap.put((short)1, new ClientData(true));
        clientDataMap.put((short)2, new ClientData(true));
        clientDataMap.put((short)3, new ClientData(true));
        clientDataMap.put((short)4, new ClientData(false));
        clientDataMap.put((short)5, new ClientData(true));
        clientDataMap.put((short)6, new ClientData(true));

        //when
        range range = new RangeTest().best_range(clientDataMap);

        //then
        assertThat(range.bestStartIp).isEqualTo(((short)1));
        assertThat(range.bestEndIp).isEqualTo((short)3);


    }




    @Test
    public void shouldFindEmpty(){
        //given
        Map<Short, ClientData> clientDataMap = new TreeMap<>();
        clientDataMap.put((short)1, new ClientData(false));
        clientDataMap.put((short)2, new ClientData(false));
        clientDataMap.put((short)3, new ClientData(false));
        clientDataMap.put((short)4, new ClientData(false));
        clientDataMap.put((short)5, new ClientData(false));
        clientDataMap.put((short)6, new ClientData(false));

        //when
        range range = new RangeTest().best_range(clientDataMap);

        //then
        assertThat(range.bestStartIp).isEqualTo(((short)0));
        assertThat(range.bestEndIp).isEqualTo((short)0);


    }


    public range best_range(Map<Short, ClientData> clientDataMap) {//tod test it properly
        //todo 1 3 4 5 -> 3 - 5
        System.out.println("bestRange for : "+ clientDataMap);
        short startRange=0;
        short startIp=0;
        short endRange=0;
        short endIp = 0;
        int lengthOfRange = -1;


        short bestStartIp = 0;
        short bestStartRange = 0;
        short bestEndIp = 0;
        short bestEndRange = 0;
        int bestLengthOfRange = -1;
        Set<Map.Entry<Short, ClientData>> entries = clientDataMap.entrySet();

        short index = 1;
        for(Map.Entry<Short, ClientData> entry : entries){
            if(!entry.getValue().working) {
                if(lengthOfRange > bestLengthOfRange){
                    bestLengthOfRange = lengthOfRange;
                    bestStartRange = startRange;
                    bestEndRange = endRange;
                    bestStartIp = startIp;
                    bestEndIp = endIp;
                }
                startRange = 0;
                endRange = 0;
                startIp = 0;
                endIp = 0;
                lengthOfRange = -1;
            }else if(entry.getValue().working && startRange != 0){
                endIp = entry.getKey();
                endRange = index;
            }else if(entry.getValue().working){
                startIp = entry.getKey();
                endIp = entry.getKey();
                startRange = index;
                endRange = index;
            }
            lengthOfRange = endRange - startRange;

            index++;
        }
        if(lengthOfRange > bestLengthOfRange){
            bestStartRange = startRange;
            bestEndRange = endRange;
            bestStartIp = startIp;
            bestEndIp = endIp;
        }

        range range = new range(bestStartRange, bestEndRange, bestStartIp, bestEndIp);
        System.out.println("--> returns : "+ range);
        return range;
    }




}

class ClientData{
    public ClientData(boolean b) {
        this.working = b;
    }

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
    private final int timeout = 0;
    boolean working;
    AtomicInteger counter;
    public final int id = 0;

    public ClientData(int id, int timeout) {

//        this.ip = ip;
        
        this.working = true;
        this.counter = new AtomicInteger(timeout);
        
    }

    public void refreshCounter() {
        counter = new AtomicInteger(timeout);
    }
}

class range{


    public final short bestStartRange;
    public final short bestEndRange;
    public final short bestStartIp;
    public final short bestEndIp;

    @Override
    public String toString() {
        return "RangeClass{" +
                "bestStartRange=" + bestStartRange +
                ", bestEndRange=" + bestEndRange +
                ", bestStartIp=" + bestStartIp +
                ", bestEndIp=" + bestEndIp +
                '}';
    }

    public range(short bestStartRange, short bestEndRange, short bestStartIp, short bestEndIp) {

        this.bestStartRange = bestStartRange;
        this.bestEndRange = bestEndRange;
        this.bestStartIp = bestStartIp;
        this.bestEndIp = bestEndIp;
    }
}


    