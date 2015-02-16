package zad5;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StrangeSorterTest {

  @Test
  public void shouldHaveTwoExceptions() {
    //given
    StrangeInterface strangeInterface = createStrangeInterface();
      StrangeSorterBartek strangeSorter = new StrangeSorterBartek();
    //when
    strangeSorter.setStrangeInterface(strangeInterface);
    //then
    assertThat(strangeSorter.exceptions.size()).isEqualTo(2);
  }

  @Test
  public void shouldReturnUnsortedExceptions() {
    //given
    StrangeInterface strangeInterface = createStrangeInterface();
      StrangeSorterBartek strangeSorter = new StrangeSorterBartek();
    //when
    strangeSorter.setStrangeInterface(strangeInterface);
    //then
    List<Exception> exceptions = strangeSorter.getExceptions(new Exception());
    assertThat(exceptions.size()).isEqualTo(2);
    assertThat(exceptions.get(0).getClass()).isEqualTo(HeavyException.class);
    assertThat(exceptions.get(1).getClass()).isEqualTo(TransparentException.class);
  }

  @Test
  public void shouldReturnTwoHeavySortedExceptions() {
    //given
    StrangeInterface strangeInterface = createStrangeInterfaceTwoHeavy();
      StrangeSorterBartek strangeSorter = new StrangeSorterBartek();
    //when
    strangeSorter.setStrangeInterface(strangeInterface);
    //then
    List<Exception> exceptions = strangeSorter.getExceptions(new HeavyException(1));
    assertThat(exceptions.size()).isEqualTo(2);
    assertThat(exceptions.get(0).getClass()).isEqualTo(HeavyException.class);
    assertThat(exceptions.get(1).getClass()).isEqualTo(HeavyException.class);
    assertThat(((HeavyException) exceptions.get(0)).getWeight()).isEqualTo(1);
    assertThat(((HeavyException) exceptions.get(1)).getWeight()).isEqualTo(2);
  }

  @Test
  public void shouldReturnThreeSortedTransparentExceptions() {
    //given
    StrangeInterface strangeInterface = createStrangeInterfaceThreeTransparentException();
      StrangeSorterBartek strangeSorter = new StrangeSorterBartek();
    //when
    strangeSorter.setStrangeInterface(strangeInterface);
    //then
    List<Exception> exceptions = strangeSorter.getExceptions(new TransparentException(1));
    assertThat(exceptions.size()).isEqualTo(3);
    assertThat(((TransparentException) exceptions.get(0)).getTransparency()).isEqualTo(1);
    assertThat(((TransparentException) exceptions.get(1)).getTransparency()).isEqualTo(3);
    assertThat(((TransparentException) exceptions.get(2)).getTransparency()).isEqualTo(33);
  }

  private StrangeInterface createStrangeInterfaceTwoHeavy() {
    return new StrangeInterface() {
      int counter = 0;

      @Override
      public Object get() throws HeavyException, TransparentException {
        if (counter == 0) {
          counter++;
          throw new HeavyException(2);
        } else if (counter == 1) {
          counter++;
          throw new HeavyException(1);
        } else {
          return new Object();
        }
      }
    };
  }

  private StrangeInterface createStrangeInterfaceThreeTransparentException() {
    return new StrangeInterface() {
      int counter = 0;

      @Override
      public Object get() throws HeavyException, TransparentException {
        if (counter == 0) {
          counter++;
          throw new TransparentException(3);
        } else if (counter == 1) {
          counter++;
          throw new TransparentException(1);
        } else if (counter == 2) {
          counter++;
          throw new TransparentException(33);
        } else if (counter == 3 ){
          counter++;
          throw new HeavyException(1);
        }else {
          return new Object();
        }
      }
    };
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
        } else {
          return new Object();
        }
      }
    };
  }

}