package zad5;

import java.util.*;

/**
 * Created by tomasz.lelek on 07/02/15.
 */
public class StrangeSorterBartek implements StrangeSorterInterface {
    final protected List exceptions = new ArrayList();

    public StrangeSorterBartek() {
    }

    @Override
    public void setStrangeInterface(StrangeInterface si) {
        while (true) {
            try {
                Object o = si.get();
                if (o != null) {
                    break;
                }
            } catch (HeavyException e) {
                exceptions.add(e);
            } catch (TransparentException e) {
                exceptions.add(e);
            }
        }
    }

    @Override
    public List<Exception> getExceptions(Exception example) {
        if( example instanceof HeavyException){
            Collections.sort(exceptions, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ascendingOrderSort(o1, o2);
                }
            });
            List result = new LinkedList();
            for(Object e : exceptions){
                if(e instanceof HeavyException)
                    result.add(e) ;
            }
            return result;
        }else if(example instanceof TransparentException){
            Collections.sort(exceptions, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return ascendingOrderSort(o1, o2);
                }
            });
            List result = new LinkedList();
            for(Object e : exceptions){
                if(e instanceof TransparentException)
                    result.add(e) ;
            }
            return result;

        } else{
            List result = new LinkedList();
            for(Object e : exceptions){
                result.add(e) ;
            }
            return result;
        }

    }

    private int ascendingOrderSort(Object o1, Object o2) {
        if( o1 instanceof TransparentException && o2 instanceof TransparentException){
            return ((TransparentException)o1).getTransparency() - ((TransparentException)o2).getTransparency();
        }
        else if( o1 instanceof HeavyException && o2 instanceof HeavyException){
            return ((HeavyException)o1).getWeight() - ((HeavyException)o2).getWeight();
        }
        else if( o1 instanceof HeavyException && o2 instanceof TransparentException){
            return ((HeavyException)o1).getWeight() - ((TransparentException)o2).getTransparency();
        }
        else if( o1 instanceof TransparentException && o2 instanceof HeavyException){
            return ((TransparentException)o1).getTransparency() - ((HeavyException)o2).getWeight();
        }
        return 0;
    }


}

