import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class PercolationTest {
  @Test
  public void shouldPercolateWhenFourRoutesAvailable() {
    //given
    boolean[][] input = {{true, true, true, true, false, false},
      {false, false, false, false, true, false},
      {false, true, true, false, false, false},
      {false, false, false, true, true, false},
      {false, true, false, false, false, true},
      {true, false, true, false, false, false},
      {false, false, false, true, true, false},
      {false, true, false, false, false, false}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isTrue();
  }

  @Test
  public void shouldReturnThatPointWasAlreadyVisited() {
    //given
    List<Percolation.Point> points = new LinkedList<Percolation.Point>();
    Percolation.Point point = new Percolation.Point(4, 2);
    points.add(point);
    //when
    boolean wasAlreadyVisited = Percolation.pointWasAlreadyVisited(points, new Percolation.Point(4, 2));
    //then
    assertThat(wasAlreadyVisited).isTrue();
  }

  @Test
  public void shouldFindRouteWhenEightRouteAvailable() {
    boolean[][] input = {{true, false, false, false, true, false, false, false, false},
      {false, true, false, false, true, false, false, false, false},
      {false, true, true, false, false, true, false, false, false},
      {false, false, false, true, false, true, false, false, true},
      {false, true, false, true, false, true, false, true, false},
      {true, false, false, true, false, false, true, false, false}};
    //when
    boolean isARoute = Percolation.neighbors8(input);
    //then
    assertThat(isARoute).isTrue();
  }

  @Test
  public void shouldNotFindRouteWhenFourRouteAvailable() {
    boolean[][] input = {{true, false, false, false, true, false, false, false, false},
      {false, true, false, false, true, false, false, false, false},
      {false, true, true, false, false, true, false, false, false},
      {false, false, false, true, false, true, false, false, true},
      {false, true, false, true, false, true, false, true, false},
      {true, false, false, true, false, false, true, false, false}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isFalse();
  }

  @Test
  public void shouldRotateArrayRight() {
    //given
    boolean[][] input = {{true, false, false}, {false, false, true}};
    //when
    boolean[][] result = Percolation.rotateArrayRight(input);
    assertThat(result.length).isEqualTo(3);
    assertThat(result[0].length).isEqualTo(2);
    assertThat(result[2][0]).isTrue();
    assertThat(result[0][1]).isTrue();
  }

  @Test
  public void simpleCase() {
    boolean[][] input = {{true, false, false, false}, {true, false, false, false},
      {true, false, false, false}, {true, false, false, false}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isTrue();
  }

  @Test
  public void simpleCaseFour() {
    boolean[][] input =
      {{true, false, true, false},
      {false, true, false, false},
      {true, false, true, false},
      {true, true, true, false},
      {true, true, true, false}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isTrue();
  }

  @Test
  public void simpleCaseEight() {
    boolean[][] input = {{true, false, true, false}, {false, true, false, false},
      {true, false, true, false}, {true, true, true, false}};
    //when
    boolean isARoute = Percolation.neighbors8(input);
    //then
    assertThat(isARoute).isTrue();
  }

  @Test
  public void notPecolateFour() {
    boolean[][] input = {{false, false, false, false}, {true, true, true, false},
      {true, true, true, false}, {true, true, true, true}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isFalse();
  }

  @Test
  public void onlyFalse() {
    boolean[][] input = {{false, false, false, false}, {false, false, false, false},
      {false, false, false, false}, {false, false, false, false}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isTrue();
  }

  @Test
  public void falseInFirstRow() {
    boolean[][] input = {{false, false, false, false},
      {true, true, true, true}, {true, true, true, true}, {true, true, true, true}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isFalse();
  }

  @Test
  public void onlyTrue() {
    boolean[][] input = {{true, true, true, true}, {true, true, true, true},
      {true, true, true, true}, {true, true, true, true}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isFalse();
  }


  @Test
  public void hugeInputTrue() {
    boolean[][] input = {{false, true, true, true, true, true, true, true, true, true, true, true, true, true},
      {false, false, true, true, true, true, true, true, true, true, true, true, true, true},
      {true, false, false, true, true, true, true, true, true, true, true, true, true, true},
      {true, true, false, true, true, true, true, true, true, true, true, true, true, true},
      {true, true, false, false, true, true, true, true, true, true, true, true, true, true},
      {true, true, true, false, false, false, false, false, false, false, false, false, false, false},
      {true, true, true, false, false, true, true, true, true, true, true, true, true, true},
      {true, true, true, true, false, false, true, true, true, true, true, true, true, true},
      {true, true, true, true, true, false, true, true, true, true, true, true, true, true}};

    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isTrue();
  }


  @Test
  public void oramusExampleShouldPass(){
    boolean[][] input =
   {{true, true, true, true, false, false},
    {false, false, false, false, true, false},
    {false, true, true, false, false, false},
    {false, false, false, true, true, false},
    {false, true, false, false, false, true},
    {true, false, true, false, false, false},
    {false, false, false, true, true, false},
    {false, true, false, false, false, false}};
    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isTrue();

  }



  @Test
  public void hugeInputFalse() {
    boolean[][] input = {{false, true, true, true, true, true, true, true },
      {false, false, true, true, true, true, true, true},
      {true, false, false, true, true, true, true, true},
      {true, true, false, true, true, true, true, true},
      {true, true, false, false, true, true, true, true},
      {true, true, true, false, false, false, false, false},
      {true, true, true, false, false, true, true, true},
      {true, true, true, true, false, false, true, true},
      {true, true, true, true, true, true, true, true }};

    //when
    boolean isARoute = Percolation.neighbors4(input);
    //then
    assertThat(isARoute).isFalse();
  }

  @Test
  public void shouldBeTrue(){
    boolean[][] input = {
      {false, true, false, true, false},
      {false, false, false, false, true},     //todo need to try all entries
      {true, true, false, true, true},
      {false, false, false, false, true},
    };
    //when
    boolean isARouteFor4 = Percolation.neighbors4(input);
    boolean isARouteFor8 = Percolation.neighbors8(input);
    //then
    assertThat(isARouteFor4).isFalse();
    assertThat(isARouteFor8).isTrue();
  }


  @Test
  public void simpleOk2(){
    boolean[][] input =  {
      {true, true},
      {false, false},
    };
    //when
    boolean isARouteFor4 = Percolation.neighbors4(input);

    //then
    assertThat(isARouteFor4).isFalse();
  }

  @Test
  public void shouldRotateArrayAndPass(){
    boolean[][] input =
      {
        {false, false, false, false, false, true},
        {true, false, true, true, false, true},
        {true, false, false, false, false, true},
        {true, true, true, true, false, false},
        {false, false, false, false, true, true},
      };

    //when
    boolean isARouteFor4 = Percolation.neighbors4(input);

    //then
    assertThat(isARouteFor4).isTrue();
  }

  private boolean[][] fixture_BigLab_OK = {
    {true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true,},
    {
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, true, false, true, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true,},

    {
      true, false, true, false, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, true, true, false, true, true, true, true, true, true,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, true, true, true, true, true, true, false, false,}, // last one here is exit
    {
      true, false, true, false, true, false, false, false, false, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      false, false, true, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, true, false, true, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, true, true, true, true, false, true, true, true, false,
      true, false, true, false, true, false, true, true, true, false, true, true,
      true, true, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, true, false, true, false,
      false, false, false, false, false, false, false, false, true, false, false, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      true, false, true, false, true, false, false, false, true,},
    {
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, false, true, false,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true, false, true, false,
      true, false, false, false, false, false, true, false, true, false, true, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, false, false, true, false, false, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      true, false, true, false, false, false, true, false, true, false, false, false,
      false, false, true, false, true, false, true, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, false, true, false, true, false, true, false, true, true,
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, false,
      true, false, true, true, true, false, true, false, true, true, true, true,
      true, true, true, false, true, false, true, false, true,},
    {
      true, false, false, false, false, false, true, false, false, false, true, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      true, false, false, false, true, false, true, false, false, false, true, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, true, false, true,},
    {
      true, false, true, true, true, false, true, false, true, false, true, false,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, false, true, true, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, true, false, true, false,
      true, false, false, false, false, false, true, false, false, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, false, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, false,
      true, true, true, true, true, false, true, false, true, false, true, false,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, false,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true,},
    {
      true, false, false, false, true, false, false, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true, false, true, false,
      true, false, false, false, true, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, false, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, true, false, false, false, true, false,
      false, false, true, false, true, false, false, false, true,},
    {
      true, false, true, true, true, true, true, false, true, false, true, true,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, false, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, true, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      false, false, false, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, true, false, false, false, true, false,
      true, false, false, false, true, false, true, false, true, false, false, false,
      true, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, true, true, true,
      true, true, true, true, true, false, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, false, true, true,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, true, true, true, true, false, true,},
    {
      true, false, false, false, true, false, true, false, false, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      true, false, false, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, true, true, true, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, false, true, false, true, true, true, true, true, false,
      true, false, true, true, true, false, true, false, true,},
    {
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true, false, true, false,
      false, false, false, false, true, false, true, false, false, false, true, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, false,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true,},
    {
      true, false, false, false, true, false, true, false, false, false, true, false,
      false, false, false, false, true, false, false, false, false, false, true, false,
      true, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      true, false, true, false, true, false, false, false, false, false, true, false,
      true, false, true, false, true, false, true, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, true, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, true, true, true, true, true, true, false, true,},
    {
      true, false, true, false, true, false, false, false, true, false, false, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true,},
    {
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, false, true, false,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, true, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true, false, true, false,
      true, false, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, false, true, true, true, false, true, false,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, false, false, true, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      true, false, true, false, false, false, false, false, true, false, false, false,
      false, false, true, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, true, false, false, false,
      true, false, false, false, true, false, true, false, true,},
    {
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, true, true, false,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true,},
    {
      true, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, true, false, true, false, false, false, false, false, true, false,
      false, false, false, false, true, false, false, false, true, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, false, true, false,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, true, true, false, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, false, false, true, false,
      false, false, true, false, false, false, false, false, false, false, true, false,
      true, false, true, false, false, false, true, false, false, false, true, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      true, false, true, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, false, true, true, true, false, true, false, true, true, true, false,
      true, true, true, false, true, false, true, false, true, true, true, true,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, true,
      true, true, true, true, true, false, false, false, true,},
    {
      true, false, false, false, false, false, false, false, true, false, true, false,
      true, false, false, false, true, false, false, false, true, false, true, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, true, false, true, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, true,},
    {
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, false, true,},
    {
      true, false, true, false, false, false, true, false, false, false, false, false,
      false, false, true, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, false, false, true, false,
      false, false, false, false, true, false, true, false, true,},
    {
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, true, true, true, true, false, true, false, true,},
    {
      true, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, false, false, true,}
  };


  private boolean[][] fixture_BigLab_FAIL = {
    {true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true,},
    {
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, true, false, true, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true,},

    {
      true, false, true, false, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, true, true, false, true, true, true, true, true, true,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, true, true, true, true, true, true, false, true}, // last one here is exit
    {
      true, false, true, false, true, false, false, false, false, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      false, false, true, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, true, false, true, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, true, true, true, true, false, true, true, true, false,
      true, false, true, false, true, false, true, true, true, false, true, true,
      true, true, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, true, false, true, false,
      false, false, false, false, false, false, false, false, true, false, false, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      true, false, true, false, true, false, false, false, true,},
    {
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, false, true, false,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true, false, true, false,
      true, false, false, false, false, false, true, false, true, false, true, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, false, false, true, false, false, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      true, false, true, false, false, false, true, false, true, false, false, false,
      false, false, true, false, true, false, true, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, false, true, false, true, false, true, false, true, true,
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, false,
      true, false, true, true, true, false, true, false, true, true, true, true,
      true, true, true, false, true, false, true, false, true,},
    {
      true, false, false, false, false, false, true, false, false, false, true, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      true, false, false, false, true, false, true, false, false, false, true, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, true, false, true,},
    {
      true, false, true, true, true, false, true, false, true, false, true, false,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, false, true, true, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, true, false, true, false,
      true, false, false, false, false, false, true, false, false, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, false, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, false,
      true, true, true, true, true, false, true, false, true, false, true, false,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, false,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true,},
    {
      true, false, false, false, true, false, false, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true, false, true, false,
      true, false, false, false, true, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, false, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, true, false, false, false, true, false,
      false, false, true, false, true, false, false, false, true,},
    {
      true, false, true, true, true, true, true, false, true, false, true, true,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, false, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, true, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      false, false, false, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, true, false, false, false, true, false,
      true, false, false, false, true, false, true, false, true, false, false, false,
      true, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, true, true, true,
      true, true, true, true, true, false, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, false, true, true,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, true, true, true, true, false, true,},
    {
      true, false, false, false, true, false, true, false, false, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      true, false, false, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, true, true, true, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, false, true, false, true, true, true, true, true, false,
      true, false, true, true, true, false, true, false, true,},
    {
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true, false, true, false,
      false, false, false, false, true, false, true, false, false, false, true, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, false,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true,},
    {
      true, false, false, false, true, false, true, false, false, false, true, false,
      false, false, false, false, true, false, false, false, false, false, true, false,
      true, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      true, false, true, false, true, false, false, false, false, false, true, false,
      true, false, true, false, true, false, true, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, true, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, true, true, true, true, true, true, false, true,},
    {
      true, false, true, false, true, false, false, false, true, false, false, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true,},
    {
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, false, true, false,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, true, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true, false, true, false,
      true, false, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, false, true, true, true, false, true, false,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, false, false, true, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      true, false, true, false, false, false, false, false, true, false, false, false,
      false, false, true, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, true, false, false, false,
      true, false, false, false, true, false, true, false, true,},
    {
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, true, true, false,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true,},
    {
      true, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, true, false, true, false, false, false, false, false, true, false,
      false, false, false, false, true, false, false, false, true, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, false, true, false,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, true, true, false, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, false, false, true, false,
      false, false, true, false, false, false, false, false, false, false, true, false,
      true, false, true, false, false, false, true, false, false, false, true, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      true, false, true, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, false, true, true, true, false, true, false, true, true, true, false,
      true, true, true, false, true, false, true, false, true, true, true, true,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, true,
      true, true, true, true, true, false, false, false, true,},
    {
      true, false, false, false, false, false, false, false, true, false, true, false,
      true, false, false, false, true, false, false, false, true, false, true, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, true, false, true, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, true,},
    {
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, false, true,},
    {
      true, false, true, false, false, false, true, false, false, false, false, false,
      false, false, true, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, false, false, true, false,
      false, false, false, false, true, false, true, false, true,},
    {
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, true, true, true, true, false, true, false, true,},
    {
      true, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, false, false, true,}
  };

  private boolean[][][] fixture4_OK = {
    {
      {false},
      {false},
      {false}
    },
    {
      {false, false, false}
    },
    {
      {false, true},
      {false, false},
    },
    {
      {false, true},
      {false, true},
      {false, true},
    },
    {
      {false, false, false, false, true},
      {false, true, false, false, true},
      {false, false, false, false, true},
      {false, false, true, false, false}
    },
    {
      // first test from http://zti.if.uj.edu.pl/Piotr.Oramus/dydaktyka/PO.cwiczenia/Zadania/
      {true, true, true, true, false, false},
      {false, false, false, false, true, false},
      {false, true, true, false, false, false},
      {false, false, false, true, true, false},
      {false, true, false, false, false, true},
      {true, false, true, false, false, false},
      {false, false, false, true, true, false},
      {false, true, false, false, false, false}
    },
    fixture_BigLab_OK
  };

  private boolean[][][] fixture4_FAIL = {
    {
      {true}
    },
    {
      {false, true, false}
    },
    {
      {false},
      {true},
      {false}
    },
    {
      {false, true, true},
      {false, true, true},
    },
    {
      {false, true, false},
      {true, false, false}
    },
    {
      {false, true, false, true, false},
      {false, false, false, false, true},
      {true, true, false, true, true},
      {false, false, false, false, true},
    },
    fixture_BigLab_FAIL
  };

  private boolean[][][] fixture8_OK = {
    {
      {false, true, true, true},
      {false, false, true, true},
      {false, false, false, true},
      {false, true, false, false},
      {true, false, false, true},
      {true, false, true, true},
    },
    {
      {true, false, true},
      {false, true, false}
    },
    {
      {false, true, false},
      {true, false, true}
    },
    {
      {false, true},
      {true, false},
      {false, true}
    },
    {
      {true, false},
      {false, true},
      {true, false}
    },
    {
      {false},
      {false},
      {false}
    },
    {
      {false, false, false}
    },
    {
      {false, true, false, false},
      {true, false, true, false},
      {true, true, true, false},
      {true, false, false, true},
      {false, true, true, true},
      {true, false, false, true},
      {true, true, true, false},
    },
    {
      // second test from zti.if.uj.edu.pl/Piotr.Oramus/dydaktyka/PO.cwiczenia/Zadania/
      {true, false, false, false, true, false, false, false, false},
      {false, true, false, false, true, false, false, false, false},
      {false, true, true, false, false, true, false, false, false},
      {false, false, false, true, false, true, false, false, true},
      {false, true, false, true, false, true, false, true, false},
      {true, false, false, true, false, false, true, false, false}
    },
    fixture_BigLab_OK
  };


  private boolean[][][] fixture8_FAIL = {
    {
      {false, true, false}
    },
    {
      {false},
      {true},
      {false},
    },
    {
      {false, true, true},
      {true, false, true}
    },
    {
      {false, true},
      {true, false},
      {true, true}
    },
    fixture_BigLab_FAIL
  };

  @Test
  public void testNeighbour4() throws Exception {
    for (int i = 0; i < fixture4_OK.length; ++i) {
      System.out.println("for : " + fixture4_OK[i]);
      assertEquals(i + " supposed to pass", true, Percolation.neighbors4(fixture4_OK[i]));
    }
    for (int i = 0; i < fixture4_FAIL.length; ++i)
      assertEquals(i + " supposed to fail", false, Percolation.neighbors4(fixture4_FAIL[i]));
  }

  @Test
  public void testNeighbour8() throws Exception {
    for (int i = 0; i < fixture8_OK.length; ++i)
      assertEquals(i + " supposed to pass", true, Percolation.neighbors8(fixture8_OK[i]));
    for (int i = 0; i < fixture8_FAIL.length; ++i)
      assertEquals(i + " supposed to fail", false, Percolation.neighbors8(fixture8_FAIL[i]));
  }
}



