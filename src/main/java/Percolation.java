import java.util.*;

/**
 * @author Tomasz Lelek
 * @since 2014-11-17
 */
class Percolation {

    public static boolean neighbors4(boolean[][] table) {
        return percolates(table, new FourNeighboursSupplier());

    }

    public static boolean neighbors8(boolean[][] table) {
        return percolates(table, new EightNeighboursSupplier());
    }


    private static boolean percolates(boolean[][] table, NeighboursSupplier neighboursSupplier) {
        int height = table.length;
        int width = table[0].length;
        if( width > height ){
             table = rotateArrayRight(table);
        }
        height = table.length;
        width = table[0].length;


        List<Point> openPointsInFirstRow = findFirstOpenPoints(table, width);
        if (thereIsNoOpenPointInFirstRow(openPointsInFirstRow))
            return false;




        for(Point currentPoint : openPointsInFirstRow) {
          Map<Point, Deque<Point>> openPoints = getAllOpenPoints(table, height, width, neighboursSupplier);
          if(recursiveTraverse(currentPoint, openPoints, height))
            return true;

        }
        return false;

    }

  private static List<Point> findFirstOpenPoints(boolean[][] table, int width) {
    List<Point> result = new LinkedList<Point>();
    for (int i = 0; i < 1; i++) {
      for (int j = 0; j < width; j++) {
        if (isOpen(table[i][j])) {
           result.add(new Point(i, j));
        }
      }
    }
    return result;
  }

  private static Map<Point, Deque<Point>> getAllOpenPoints(boolean[][] table, int height, int width, NeighboursSupplier neighboursSupplier) {
        Map<Point, Deque<Point>> openPoints = new LinkedHashMap<Point, Deque<Point>>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (isOpen(table[i][j])) {
                    Deque<Point> openNeighbours = neighboursSupplier.getOpenNeighbours(table, i, j);
                    openPoints.put(new Point(i, j), openNeighbours);
                }
            }
        }
        return openPoints;
    }

    private static boolean recursiveTraverse(Point currentPoint, Map<Point, Deque<Point>> openPoints, int height) {
        return loop(null, currentPoint, openPoints, openPoints.get(currentPoint), height, new LinkedList<Point>());

    }

    private static boolean loop(Point previousPoint, Point currentPoint,
                                Map<Point, Deque<Point>> openPoints,
                                Deque<Point> availablePoints, int height,
                                LinkedList<Point> alreadyVisited) {
        if(previousPoint == null && currentPoint == null && availablePoints == null) {
            return false;
        }
        alreadyVisited.add(currentPoint);

        if (isAtTheEndOfTable(currentPoint, height)) {
            return true;
        } else if (availablePoints.isEmpty()) {
          if(previousPoint == null){
              previousPoint = getPointBefore(currentPoint, alreadyVisited);
          }
            Deque<Point> neighbours = openPoints.get(previousPoint);
            return loop(null, previousPoint, openPoints, neighbours, height, alreadyVisited);
        }

        Point newCurrent = availablePoints.pop();

        while (pointWasAlreadyVisited(alreadyVisited, newCurrent) && !availablePoints.isEmpty()) {
            newCurrent = availablePoints.pop();
        }
        if (pointWasAlreadyVisited(alreadyVisited, newCurrent) && availablePoints.isEmpty()) {
            if(previousPoint == null){
                previousPoint = getPointBefore(currentPoint, alreadyVisited);
            }
            return loop(null, previousPoint, openPoints, openPoints.get(previousPoint), height, alreadyVisited);
        }


        Deque<Point> neighbours = openPoints.get(newCurrent);
        return loop(currentPoint, newCurrent, openPoints, neighbours, height, alreadyVisited);
    }

    private static Point getPointBefore(Point currentPoint, LinkedList<Point> alreadyVisited) {
        for (int i = 0; i < alreadyVisited.size(); i++) {
            if(alreadyVisited.get(i).equals(currentPoint)){
              if(i == 0) return null;
                return alreadyVisited.get(i - 1);
            }
        }
        return null;
    }

    static boolean pointWasAlreadyVisited(List<Point> alreadyVisited, Point newCurrent) {
        return alreadyVisited.contains(newCurrent);
    }

    private static boolean isAtTheEndOfTable(Point currentPoint, int height) {
        return currentPoint.i == height - 1;
    }

    private static boolean thereIsNoOpenPointInFirstRow(List<Point> firstOpen) {
        return firstOpen.isEmpty();
    }

    private static Point findFirstOpenPoint(boolean[][] table, int width) {
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < width; j++) {
                if (isOpen(table[i][j])) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    private static void addToListIfIsInArrayBound(boolean[][] table, int i, int j, List<Point> neighbourPointInArrayBounds) {
        if (isInArrayBound(table, i, j)) {
            neighbourPointInArrayBounds.add(new Point(i, j));
        }
    }

    private static boolean isInArrayBound(boolean[][] table, int i, int j) {
        int height = table.length;
        int width = table[0].length;
        return i >= 0 && j >= 0 && i < height && j < width;
    }

    private static boolean isOpen(boolean b) {
        return !b;
    }

    static boolean[][] rotateArrayRight(boolean[][] input)
    {
        int newWidth = input.length;
        int newHeight = input[0].length;
        boolean[][] result = new boolean[newHeight][newWidth];
        for (int i = 0; i < newHeight; ++i) {
            for (int j = 0; j < newWidth; ++j) {
                result[i][j] = input[newWidth - j - 1][i];
            }
        }
        return result;
    }

    public static class Point {
        int i;
        int j;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "i=" + i +
                    ", j=" + j +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (i != point.i) return false;
            if (j != point.j) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + j;
            return result;
        }
    }

    static interface NeighboursSupplier {
        public Deque<Point> getOpenNeighbours(boolean[][] table, int i, int j);
    }

    static class FourNeighboursSupplier implements NeighboursSupplier {

        @Override
        public Deque<Point> getOpenNeighbours(boolean[][] table, int i, int j) {
            Deque<Point> neighbourOpenPoint = new ArrayDeque<Point>();
            List<Point> neighbourPoint = createNeighbourInArrayBounds(table, i, j);
            for (Point point : neighbourPoint) {
                if (isOpen(table[point.i][point.j])) {
                    neighbourOpenPoint.add(point);
                }
            }
            return neighbourOpenPoint;
        }

        private static List<Point> createNeighbourInArrayBounds(boolean[][] table, int i, int j) {
            List<Point> neighbourPointInArrayBounds = new LinkedList<Point>();
            addDirectNeighbours(table, i, j, neighbourPointInArrayBounds);

            return neighbourPointInArrayBounds;
        }
    }

    static class EightNeighboursSupplier implements NeighboursSupplier {

        @Override
        public Deque<Point> getOpenNeighbours(boolean[][] table, int i, int j) {
            Deque<Point> neighbourOpenPoint = new ArrayDeque<Point>();
            List<Point> neighbourPoint = createNeighbourInArrayBounds(table, i, j);
            for (Point point : neighbourPoint) {
                if (isOpen(table[point.i][point.j])) {
                    neighbourOpenPoint.add(point);
                }
            }
            return neighbourOpenPoint;
        }

        private static List<Point> createNeighbourInArrayBounds(boolean[][] table, int i, int j) {
            List<Point> neighbourPointInArrayBounds = new LinkedList<Point>();
            addDirectNeighbours(table, i, j, neighbourPointInArrayBounds);
            addToListIfIsInArrayBound(table, i - 1, j - 1, neighbourPointInArrayBounds);
            addToListIfIsInArrayBound(table, i + 1, j + 1, neighbourPointInArrayBounds);
            addToListIfIsInArrayBound(table, i - 1, j + 1, neighbourPointInArrayBounds);
            addToListIfIsInArrayBound(table, i + 1, j - 1, neighbourPointInArrayBounds);

            return neighbourPointInArrayBounds;
        }
    }

    private static void addDirectNeighbours(boolean[][] table, int i, int j, List<Point> neighbourPointInArrayBounds) {
        addToListIfIsInArrayBound(table, i - 1, j, neighbourPointInArrayBounds);
        addToListIfIsInArrayBound(table, i + 1, j, neighbourPointInArrayBounds);
        addToListIfIsInArrayBound(table, i, j + 1, neighbourPointInArrayBounds);
        addToListIfIsInArrayBound(table, i, j - 1, neighbourPointInArrayBounds);
    }
}
