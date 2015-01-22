package zad6;

import java.util.*;

/**
 * @author Tomasz Lelek
 * @since 2015-01-22
 */
public class MultiStageExecutor implements MultiStageExecutorInterface {
  protected Map<Integer, List<OneStageWorkInterface>> stages = new LinkedHashMap<Integer, List<OneStageWorkInterface>>();


  @Override
  public void register(int stage, OneStageWorkInterface twi) {
    if(twi != null) {
      boolean contains = stages.containsKey(stage);
      if (contains) {
        List<OneStageWorkInterface> work = stages.get(stage);
        work.add(twi);
      } else {
        List<OneStageWorkInterface> work = new LinkedList<OneStageWorkInterface>();
        work.add(twi);
        stages.put(stage, work);
      }
    }

  }

  @Override
  public void deregister(OneStageWorkInterface twi) {
    if(twi != null ) {
      for (Map.Entry<Integer, List<OneStageWorkInterface>> entry : stages.entrySet()) {
        entry.getValue().remove(twi);
      }
    }
  }

  @Override
  public void deregister(int stage, OneStageWorkInterface twi) {
    if(twi != null ){
      List<OneStageWorkInterface> oneStageWork = stages.get(stage);
      if(oneStageWork!= null) {
        oneStageWork.remove(twi);
      }
    }
  }

  @Override
  public int getNumerOfStages() {
    Integer maxValue = Integer.MIN_VALUE;
    for (Integer i : stages.keySet()) {
      if (maxValue < i) {
        maxValue = i;
      }
    }
    return maxValue;
  }

  @Override
  public void execute() {
    Map<Integer, List<OneStageWorkInterface>> sortedStages = new TreeMap<Integer, List<OneStageWorkInterface>>(stages);
    for(Map.Entry<Integer, List<OneStageWorkInterface>> stage : sortedStages.entrySet()){
      for(OneStageWorkInterface work : stage.getValue()){
        work.work(stage.getKey());
      }
    }
  }
}
