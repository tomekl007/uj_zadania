import java.util.HashMap;

/**
 * @author Tomasz Lelek
 * @since 2014-12-07
 */
public class BartekPercolation {
  public BartekPercolation() {
    this.startRow = false;
    this.endRow = false;
  }

  private boolean startRow;
  private boolean endRow;
  public static final HashMap<String, Integer[]> movement = new HashMap<String, Integer[]>();

  static {
    movement.put("top", new Integer[]{-1, 0});
    movement.put("bottom", new Integer[]{1, 0});
    movement.put("left", new Integer[]{0, -1});
    movement.put("right", new Integer[]{0, 1});
    movement.put("rightTop", new Integer[]{-1, 1});
    movement.put("rightBottom", new Integer[]{1, 1});
    movement.put("leftTop", new Integer[]{-1, -1});
    movement.put("LeftBottom", new Integer[]{1, -1});
  }

  /*getters/setters*/
  public void setStartRow(boolean value) {
    this.startRow = value;
  }

  public void setEndRow(boolean value) {
    this.endRow = value;
  }

  public boolean getStartRow() {
    return this.startRow;
  }

  public boolean getEndRow() {
    return this.endRow;
  }

  /*table invert when array of row is larger than array*/
  public boolean[][] tableInvert(boolean[][] array) {
    int newWidth = array.length;
    int newHeight = array[0].length;
    boolean[][] result = new boolean[newHeight][newWidth];
    for (int i = 0; i < newHeight; ++i) {
      for (int j = 0; j < newWidth; ++j) {
        result[i][j] = array[newWidth - j - 1][i];
      }
    }
    return result;
  }

  /*create visited array with false values*/
  public boolean[][] createVisitedArray(boolean[][] array) {
    boolean[][] vis = new boolean[array.length][array[0].length];
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[0].length; j++) {
        vis[i][j] = false;
      }
    }
    return vis;
  }

  public void DFS4(boolean[][] array, int row, int col, boolean[][] visited, int neighbors) {
        /*move to the right*/

    visited[row][col] = true;
    if (!array[0][col]) {
      this.startRow = true;
    }
    if (row == array.length - 1) {
      this.endRow = true;
    }
//              System.out.println("row: "+ row +" col: "+col);

    if (neighbors == 4) {

            /*move to the top*/
      if (isRoute(array, row + movement.get("top")[0], col + movement.get("top")[1], visited, neighbors)) {
        DFS4(array, row + movement.get("top")[0], col + movement.get("top")[1], visited, neighbors);
      }


            /*move to the bottom*/
      if (isRoute(array, row + movement.get("bottom")[0], col + movement.get("bottom")[1], visited, neighbors)) {
        DFS4(array, row + movement.get("bottom")[0], col + movement.get("bottom")[1], visited, neighbors);
      }

            /*move to the left*/

      if (isRoute(array, row + movement.get("left")[0], col + movement.get("left")[1], visited, neighbors)) {
        DFS4(array, row + movement.get("left")[0], col + movement.get("left")[1], visited, neighbors);
      }

            /*move to the right*/
      if (isRoute(array, row + movement.get("right")[0], col + movement.get("right")[1], visited, neighbors)) {
        DFS4(array, row + movement.get("right")[0], col + movement.get("right")[1], visited, neighbors);
      }

    }


  }


    /*check the way*/

  public boolean isRoute(boolean[][] array, int row, int col, boolean visited[][], int neighbors) {

/*check is row and col out of boundary*/

    try {
      if (row >= 0 && row < array.length && col >= 0 && col <= array[row].length &&
        array[row][col] == false && visited[row][col] == false) {
        return true;
      } else {

        return false;
      }

    } catch (ArrayIndexOutOfBoundsException e) {

    }

    return true;
  }

  public static boolean neighbors4(boolean[][] table) {

    boolean[][] visited;
    BartekPercolation n4 = new BartekPercolation();
    if (table.length < table[0].length) {
      table = n4.tableInvert(table);
    }
    visited = n4.createVisitedArray(table);


    for (int i = 0; i < table.length; i++) {
      for (int j = 0; j < table[i].length; j++) {
        if (table[i][j] == false) {

          n4.DFS4(table, i, j, visited, 4);

        }
      }


      return n4.getStartRow() && n4.getEndRow();

    }
    return (n4.getStartRow() && n4.getEndRow());
  }
}