package zad5;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StrangeSorterTest {

  @Test
  public void shouldHaveTwoExceptions(){
    //given
    StrangeInterface strangeInterface = createStrangeInterface();
    StrangeSorter strangeSorter = new StrangeSorter();
    //when
    strangeSorter.setStrangeInterface(strangeInterface);
    //then
    assertThat(strangeSorter.exceptions.size()).isEqualTo(2);
  }

  @Test
  public void shouldReturnSortedExceptions(){
    //given
    StrangeInterface strangeInterface = createStrangeInterface();
    StrangeSorter strangeSorter = new StrangeSorter();
    //when
    strangeSorter.setStrangeInterface(strangeInterface);
    //then
    List<Exception> exceptions = strangeSorter.getExceptions(new Exception());    //todo why pass that arg
    assertThat(exceptions.size()).isEqualTo(2);
    assertThat(exceptions.get(0).getClass()).isEqualTo(TransparentException.class);
    assertThat(exceptions.get(1).getClass()).isEqualTo(HeavyException.class);
  }



  private StrangeInterface createStrangeInterface() {
    return new StrangeInterface() {
      int counter = 0;
      @Override
      public Object get() throws HeavyException, TransparentException {
        if (counter == 0) {
          counter++;
          throw new HeavyException(2);
        } else if (counter == 1) {
          counter++;
          throw new TransparentException(1);
        } else{
          return new Object();
        }
      }
    };
  }

}