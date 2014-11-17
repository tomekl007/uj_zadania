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
    public void shouldReturnThatPointWasAlreadyVisited(){
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
    public void shouldFindRouteWhenEightRouteAvailable(){
        boolean[][] input = { { true, false, false, false, true, false, false, false, false},
                              { false, true, false, false, true, false, false, false, false},
                              { false, true, true, false, false, true, false, false, false},
                              { false, false, false, true, false, true, false, false, true},
                              { false, true, false, true, false, true, false, true, false},
                              { true, false, false, true, false, false, true, false, false}};
        //when
        boolean isARoute = Percolation.neighbors8(input);
        //then
        assertThat(isARoute).isTrue();
    }
}