package zad11;

import java.util.List;

import java.util.*;
import java.util.function.Function;

/**
 * Created by tomasz.lelek on 17/03/15.
 */
public class MagicSelect implements MagicSelectInterface{
    @Override
    public String select(String tableName, List<String> attributes, List<Item> whereInRPN) throws IllegalArgumentException {
        return null;
    }


    /**
     * @author Tomasz Lelek
     * @since 2014-11-03
     */
    public static class ONP {
        static Deque<Integer> stack = new ArrayDeque<>();

        static Map<String, Function<Deque<Integer>, Integer>> functions = new LinkedHashMap<>();

        static {
         /*   functions.put("abs", integers -> Math.abs(integers.removeLast()));
            functions.put("+", (integer) -> integer.removeLast() + integer.removeLast());
            functions.put("-", (integer) -> {
                int arg1 = integer.removeLast();
                int arg2 = integer.removeLast();
                return arg2 - arg1;
            });
            functions.put("/", (integer) -> {
                int arg1 = integer.removeLast();
                int arg2 = integer.removeLast();
                return arg2 / arg1;
            });
            functions.put("*", (integer) -> integer.removeLast() * integer.removeLast());*/
        }


        private static String space;

        public static int countExpression(String input) {
            space = " ";
            String[] splittedInput = input.split(space);
            System.out.println(Arrays.asList(splittedInput));

            for (String i : splittedInput) {
                if (isNumber(i)) {
                    stack.add(Integer.parseInt(i));
                } else if (isOperand(i)) {
                    Integer result = functions.get(i).apply(stack);
                    stack.add(result);
                }
            }

            return stack.removeLast();
        }


        private static boolean isOperand(String c) {
            return functions.keySet().contains(c);
        }

        private static boolean isNumber(String str) {
            return str.matches("-?\\d+(\\.\\d+)?");
        }

    }
    
    interface Function<T, R> {
        R apply(T t);
    }
}
