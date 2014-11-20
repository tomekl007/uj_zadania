import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

import static java.util.Collections.swap;

/**
 * @author Tomasz Lelek
 * @since 2014-11-18
 */
public class Permutations {

    public static void findAll(List<Integer> input){
      permute(input, input.size());
    }

  private static void permute(List<Integer> list, int n) {
    System.out.println("-> " + list + " n = " + n);
    if (n == 1) {
      System.out.println(list);
      return;
    }
    for (int i = 0; i < n; i++) {
      System.out.println("preswap"+ i + " <-> " + (n-1));
      swap(list, i, n-1);
      permute(list, n-1);
      System.out.println("postswap " + i + " <-> " + (n-1));
      swap(list, i, n-1);
    }
  }

  RecursiveAction recursiveAction = new RecursiveAction() {
    @Override
    protected void compute() {

    }
  };
  RecursiveTask<Integer> recursiveTask = new RecursiveTask<Integer>() {
    @Override
    protected Integer compute() {
      return null;
    }
  };
}
