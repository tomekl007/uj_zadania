import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        boolean[][] input = {{true, false, true, false}, {false, true, false, false},
                {true, false, true, false}, {true, true, true, false}};
        //when
        boolean isARoute = Percolation.neighbors4(input);
        //then
        assertThat(isARoute).isFalse();
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
}