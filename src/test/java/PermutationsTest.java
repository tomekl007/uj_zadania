import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PermutationsTest {

    @Test
    public void shouldFIndAllPermutations(){
        //given
        List<Integer> input = Arrays.asList(1,2,3);
        //when
        Permutations.findAll(input);
    }

}