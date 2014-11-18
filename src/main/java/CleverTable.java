/**
 * @author Tomasz Lelek
 * @since 2014-11-18
 */
public class CleverTable extends CleverTableA {
    private boolean[][] live;
    private int[][] age;

    @Override
    public void set(int col, int row, boolean value) {
      if ( isOutOfBounds(row,col)){
          return;
      }
      if(!value){
          live[row][col] = false;
          age[row][col] = 0;
      }

      if(value){
          live[row][col] = true;
      }
    }

    @Override
    public boolean get(int col, int row) {
        if ( isOutOfBounds(row, col)){
           return false;
        }
        return live[row][col];
    }

    private boolean isOutOfBounds(int row, int col) {
        int size = getSize();
        return row >= size || row < 0 || col >= size || col < 0;     //todo why is true ? write unit test
    }

    @Override
    public int getAge(int col, int row) {
        if (isOutOfBounds(row, col)){
            return Integer.MIN_VALUE;
        }
        return age[row][col];
    }

    @Override
    public void setSize(int size) {
        live = new boolean[size][size];
        age = new int[size][size];
    }

    @Override
    public int getSize() {
        return live.length;
    }

    @Override
    public void grow() {
        int newSize = getSize() + 2;
        boolean[][] newLive = new boolean[newSize][newSize];
        int[][] newAge = new int[newSize][newSize];
        for (int i = 1; i < newSize - 1; i++) {
            for (int j = 1; j < newSize - 1; j++) {
                newLive[i][j] = live[i-1][j-1];
                newAge[i][j] = age[i-1][j-1];
            }
        }
        live = newLive;
        age = newAge;
    }

    @Override
    public void nextGeneration() {
        for (int i = 0; i < live.length; i++) {
            for (int j = 0; j < live.length; j++) {
                if(live[i][j]){
                    age[i][j] ++;
                }
            }
        }

    }
}
