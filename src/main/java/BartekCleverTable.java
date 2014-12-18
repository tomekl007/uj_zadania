/**
 * @author Tomasz Lelek
 * @since 2014-12-07
 */
public class BartekCleverTable extends CleverTableA{
    private int [][] age;
    private boolean [][] table;
    public BartekCleverTable(){
      table = null;
      age = null;
    }

    private boolean arrayBound (int row, int col){
      return row >= getSize() || row < 0 || col >= getSize() || col < 0;
    }
    @Override
    public void set(int col, int row, boolean value) {
      if(!arrayBound(row,col)){
        this.table[row][col] = value;
            /*set age*/
        if(!value){
          this.age[row][col] = 0;
        }
      }
    }



    @Override
    public boolean get(int col, int row) {
      if(!arrayBound(row,col)){

        return this.table[row][col];
      }
      return false;
    }

    @Override
    public int getAge(int col, int row) {
      if(!arrayBound(row,col)){
        return this.age[col][row];
      }
      return Integer.MIN_VALUE;
    }

    @Override
    public void setSize(int size) {
      this.table = new boolean[size][size];
      this.age = new int[size][size];
      for (int i = 0; i < size ; i++){
        for (int j = 0; j < size; j++){
          this.table[i][j] = false;
          this.age[i][j] = 0;
        }
      }
    }

    @Override
    public int getSize() {
      return this.table.length;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void grow() {
      boolean [][] growedTable = fill2dArrayBol(this.table,false);
      int [][] growedAge = fill2dArrayInt(this.age,0);
      for (int i = 0; i < growedTable.length-2 ; i++ ){
        for( int j = 0; j < growedTable[i].length-2; j++){
          growedTable[i+1][j+1] = this.table[i][j];
          growedAge[i+1][j+1] = this.age[i][j];
        }
      }
      this.table = growedTable;
      this.age = growedAge;

    }
    /*fill 2d array with bol values*/
    private boolean [][] fill2dArrayBol(boolean[][] array, boolean value){
      boolean [][] newArr = new boolean[array.length+2][array.length+2];
      for (int i = 0; i < newArr.length ; i++){
        for( int j = 0; j < newArr[i].length ; j++){
          newArr[i][j] = value;
        }
      }
      return newArr;
    }
    /*fill 2d array with int value*/
    private int [][] fill2dArrayInt(int[][] array, int value){
      int [][] newArr = new int[array.length+2][array.length+2];
      for (int i = 0; i < newArr.length ; i++){
        for( int j = 0; j < newArr[i].length ; j++){
          newArr[i][j] = value;
        }
      }
      return newArr;
    }
    @Override
    public void nextGeneration() {
      for(int i = 0 ; i < this.table.length; i++){
        for (int j = 0 ; j < this.table[i].length; j++){
          if(this.table[i][j]){
            this.age[i][j] ++;
          }
        }
      }
    }
  }

