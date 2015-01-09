package zad5;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tomasz Lelek
 * @since 2015-01-09
 */
public class StrangeSorter implements StrangeSorterInterface {
  private StrangeInterface si;
  final protected  List<ExceptionWithWeight> exceptions = new LinkedList<ExceptionWithWeight>();

  public StrangeSorter() {
  }

  @Override
  public void setStrangeInterface(StrangeInterface si) {

    this.si = si;

    while (true) {
      try {
        Object o = si.get();
        if (o != null) {
          break;
        }
      } catch (HeavyException e) {
        exceptions.add(new ExceptionWithWeight(e, e.getWeight()));
      } catch (TransparentException e) {
        exceptions.add(new ExceptionWithWeight(e, e.getTransparency()));
      }
    }
  }

  @Override
  public List<Exception> getExceptions(Exception example) {
    Collections.sort(exceptions, new Comparator<ExceptionWithWeight>() {
      @Override
      public int compare(ExceptionWithWeight o1, ExceptionWithWeight o2) {
        return ascendingOrderSort(o1, o2);
      }
    });
    List<Exception> result = new LinkedList<Exception>();
    for(ExceptionWithWeight e : exceptions){
      result.add(e.e) ;
    }
    return result;
  }

  private int ascendingOrderSort(ExceptionWithWeight o1, ExceptionWithWeight o2) {
    return o1.weight - o2.weight;
  }

  class ExceptionWithWeight {

    private final Exception e;
    private final int weight;

    public ExceptionWithWeight(Exception e, int weight) {

      this.e = e;
      this.weight = weight;
    }
  }
}
