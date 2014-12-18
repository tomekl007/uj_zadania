import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class BartekCleverTableTest{

  @Test
  public void testSet() throws Exception {
    BartekCleverTable table = new BartekCleverTable();
    table.setSize(2);
    table.set(0, 0, true);
    assertEquals(true, table.get(0, 0));
    assertEquals(0, table.getAge(0, 0));
    table.nextGeneration();
    assertEquals(1, table.getAge(0, 0));
    table.set(0, 0, true);
    assertEquals(1, table.getAge(0, 0));
    table.set(0, 0, false);
    table.set(0, 0, true);
    assertEquals(0, table.getAge(0, 0));
    table.set(Integer.MAX_VALUE, Integer.MAX_VALUE, true);
    assertEquals(false, table.get(Integer.MAX_VALUE, Integer.MAX_VALUE));
    table.set(Integer.MAX_VALUE, Integer.MAX_VALUE, false);
  }

  @Test
  public void testGetAge() throws Exception {
    BartekCleverTable table = new BartekCleverTable();
    table.setSize(2);
    assertEquals(0, table.getAge(0, 0));
    table.set(0, 0, true);
    assertEquals(0, table.getAge(0, 0));
    table.nextGeneration();
    assertEquals(1, table.getAge(0, 0));
    assertEquals(Integer.MIN_VALUE, table.getAge(Integer.MAX_VALUE, Integer.MAX_VALUE));
  }

  @Test
  public void testGrow() throws Exception {
    BartekCleverTable table = new BartekCleverTable();
    table.setSize(1);
    assertEquals(1, table.getSize());

    table.set(0, 0, true);
    assertEquals(true, table.get(0, 0));

    table.set(1, 1, true);
    table.set(1, 0, true);
    table.set(0, 1, true);

    assertEquals(false, table.get(1, 1));
    assertEquals(false, table.get(1, 0));
    assertEquals(false, table.get(0, 1));

    table.grow();
    assertEquals(3, table.getSize());

    assertEquals(false, table.get(0, 0));
    assertEquals(true, table.get(1, 1));
    assertEquals(0, table.getAge(1, 1));

    table.grow();
    assertEquals(5, table.getSize());

    assertEquals(false, table.get(0, 0));
    assertEquals(false, table.get(1, 1));
    assertEquals(true, table.get(2, 2));
  }

  @Test
  public void testNextGeneration() throws Exception {
    BartekCleverTable table = new BartekCleverTable();
    table.setSize(2);

    table.set(0, 0, true);
    table.set(1, 1, true);
    for (int i = 0; i < 10; ++i) {
      assertEquals(i, table.getAge(0, 0));
      assertEquals(i, table.getAge(1, 1));
      assertEquals(0, table.getAge(1, 0));
      assertEquals(0, table.getAge(0, 1));
      assertEquals(Integer.MIN_VALUE, table.getAge(Integer.MAX_VALUE, Integer.MAX_VALUE));
      table.nextGeneration();
    }
  }

  // Test from our website http://zti.if.uj.edu.pl/Piotr.Oramus/dydaktyka/PO.cwiczenia/Zadania/
  @Test
  public void test() throws Exception {
    BartekCleverTable table = new BartekCleverTable();
    table.setSize(2);
    assertEquals(2, table.getSize());
    table.set(0, 1, true);
    assertEquals(true, table.get(0, 1));
    assertEquals(0, table.getAge(0, 1));
    table.grow();
    assertEquals(4, table.getSize());
    assertEquals(true, table.get(1, 2));
    assertEquals(0, table.getAge(1, 2));
    table.nextGeneration();
    assertEquals(true, table.get(1, 2));
    assertEquals(1, table.getAge(1, 2));
    table.set(0, 0, true);
    assertEquals(true, table.get(0, 0));
    assertEquals(0, table.getAge(0, 0));
    table.set(1, 0, true);
    assertEquals(true, table.get(1, 0));
    assertEquals(0, table.getAge(1, 0));
    table.nextGeneration();
    assertEquals(true, table.get(0, 0));
    assertEquals(1, table.getAge(0, 0));
    assertEquals(true, table.get(1, 0));
    assertEquals(1, table.getAge(1, 0));
    assertEquals(true, table.get(1, 2));
    assertEquals(2, table.getAge(1, 2));
    table.set(0, 0, false);
    assertEquals(false, table.get(0, 0));
    assertEquals(0, table.getAge(0, 0));
    table.grow();
    assertEquals(6, table.getSize());
    assertEquals(true, table.get(2, 1));
    assertEquals(1, table.getAge(2, 1));
    assertEquals(true, table.get(2, 3));
    assertEquals(2, table.getAge(2, 3));
  }

  @Test
  @Ignore
  public void testBig() throws Exception {
    BartekCleverTable table = new BartekCleverTable();
    table.setSize(2);

    table.set(0, 0, true);
    table.set(0, 1, true);
    table.set(1, 0, true);
    table.set(1, 1, true);

    assertEquals(true, table.get(0, 0));
    assertEquals(true, table.get(0, 1));
    assertEquals(true, table.get(1, 0));
    assertEquals(true, table.get(1, 1));

    for (short i = 0; i < Short.MAX_VALUE; ++i) {
      table.grow();
      table.nextGeneration();
    }

    assertEquals(true, table.get(Short.MAX_VALUE, Short.MAX_VALUE));
    assertEquals(true, table.get(Short.MAX_VALUE, Short.MAX_VALUE + 1));
    assertEquals(true, table.get(Short.MAX_VALUE + 1, Short.MAX_VALUE));
    assertEquals(true, table.get(Short.MAX_VALUE + 1, Short.MAX_VALUE + 1));

    assertEquals(Short.MAX_VALUE, table.getAge(Short.MAX_VALUE, Short.MAX_VALUE));
    assertEquals(Short.MAX_VALUE, table.getAge(Short.MAX_VALUE, Short.MAX_VALUE + 1));
    assertEquals(Short.MAX_VALUE, table.getAge(Short.MAX_VALUE + 1, Short.MAX_VALUE));
    assertEquals(Short.MAX_VALUE, table.getAge(Short.MAX_VALUE + 1, Short.MAX_VALUE + 1));
  }

  @Test
  public void testBig2() throws Exception {
    BartekCleverTable table = new BartekCleverTable();
    table.setSize(Byte.MAX_VALUE);

    for (byte i = 0; i < table.getSize(); ++i)
      for (byte j = 0; j < table.getSize(); ++j)
        table.set(i, j, true);

    for (byte i = 0; i < table.getSize(); ++i)
      table.nextGeneration();
    table.grow();

    for (short i = 0; i < table.getSize(); ++i) {
      assertEquals("i:" + i, false, table.get(i, 0));
      assertEquals("i:" + i, false, table.get(0, i));
      assertEquals("i:" + i, false, table.get(table.getSize(), i));
      assertEquals("i:" + i, false, table.get(i, table.getSize()));
    }

    for (short i = 1; i < table.getSize() - 1; ++i)
      for (short j = 1; j < table.getSize() - 1; ++j)
        assertEquals("i:" + i + " j:" + j, true, table.get(i, j));
  }

  @Test
  public void endToEndTestOfCleverTable(){
    //given
    CleverTableA cleverTableA = new BartekCleverTable();
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
