import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CleverTableTest {

    @Test
    public void endToEndTestOfCleverTable(){
        //given
        CleverTableA cleverTableA = new CleverTable();
        //when
        cleverTableA.setSize(2);

        int sizeOfCleverTable = cleverTableA.getSize();
        assertThat(sizeOfCleverTable).isEqualTo(2);

        boolean getOnInitTable = cleverTableA.get(0, 0);
        assertThat(getOnInitTable).isFalse();
        boolean outOfBoundsValue = cleverTableA.get(100, 100);
        assertThat(outOfBoundsValue).isFalse();

        cleverTableA.set(0,0, true);
        int ageOfInitTrue = cleverTableA.getAge(0, 0);
        assertThat(ageOfInitTrue).isEqualTo(0);
        cleverTableA.nextGeneration();
        int ageOfNextGeneration = cleverTableA.getAge(0, 0);
        assertThat(ageOfNextGeneration).isEqualTo(1);

        cleverTableA.grow();
        int sizeAfterGrow = cleverTableA.getSize();
        assertThat(sizeAfterGrow).isEqualTo(4);

        cleverTableA.nextGeneration();
        int ageAfterGrowAndNextGeneration = cleverTableA.getAge(1, 1);
        assertThat(ageAfterGrowAndNextGeneration).isEqualTo(2);


    }

}