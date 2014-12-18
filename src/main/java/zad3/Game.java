package zad3;

/**
 * @author Tomasz Lelek
 * @since 2014-12-18
 */
class Game implements GameInterface {
  private LifeRulesInterface lri;
  private boolean[][] state;

  public Game(){}

  @Override
  public void setRules(LifeRulesInterface lri) {

    this.lri = lri;
  }

  @Override
  public void setCurrentState(boolean[][] state) {

    this.state = state;
  }

  @Override
  public boolean[][] getCurrentState() {
    return state;
  }

  @Override
  public void nextState() throws RulesNotSetException {
    if(lri == null){
      throw new RulesNotSetException();
    }

    boolean[][] newState = new boolean[state.length][state[0].length];

    for (int i = 1; i < state.length -1; i++) {
      for (int j = 1; j < state[0].length -1; j++) {
          int numberOfLiveNeighbours = countNumberOfLiveNeighbours(state, i, j);
          newState[i][j] = lri.isOn(state[i][j], numberOfLiveNeighbours);
      }
    }

    state = newState;
  }

  private int countNumberOfLiveNeighbours(boolean[][] state, int i, int j) {
    int numberOfLive = 0;
    if ( state[i - 1][ j]) numberOfLive++;
    if ( state[i + 1][ j]) numberOfLive++;
    if ( state[i][ j + 1]) numberOfLive++;
    if ( state[i][ j - 1]) numberOfLive++;
    if ( state[i - 1][ j - 1]) numberOfLive++;
    if ( state[i + 1][ j + 1]) numberOfLive++;
    if ( state[i - 1][ j + 1]) numberOfLive++;
    if ( state[i + 1][ j - 1]) numberOfLive++;
    return numberOfLive;
  }
}
