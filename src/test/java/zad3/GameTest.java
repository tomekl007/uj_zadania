package zad3;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {

  @Test
  public void shouldStartGame(){
    boolean[][] table = tableInit();

    GameInterface gi = new Game();
    LifeRulesInterface lri = new LifeRules();

    gi.setRules( lri ); // ustalam reguly gry
    gi.setCurrentState( table ); // i stan gry

    try {
      gi.nextState(); // zlecam zmiane stanu komorek
    } catch (GameInterface.RulesNotSetException e) {
      e.printStackTrace();
    }

    // odbieram wynik
    table = gi.getCurrentState();

    // sprawdzam wynik
    assertThat(table[1][1]).isTrue();
    assertThat(table[1][2]).isTrue();
    assertThat(table[2][1]).isTrue();
    assertThat(table[2][2]).isTrue();
  }


  @Test
  public void shouldStartGameAlwaysFalse(){
    boolean[][] table = tableInit();

    GameInterface gi = new Game();
    LifeRulesInterface lri = new LifeRulesAlwaysFalse();

    gi.setRules( lri ); // ustalam reguly gry
    gi.setCurrentState( table ); // i stan gry

    try {
      gi.nextState(); // zlecam zmiane stanu komorek
    } catch (GameInterface.RulesNotSetException e) {
      e.printStackTrace();
    }

    // odbieram wynik
    table = gi.getCurrentState();

    // sprawdzam wynik
    assertThat(table[1][1]).isFalse();
    assertThat(table[1][2]).isFalse();
    assertThat(table[2][1]).isFalse();
    assertThat(table[2][2]).isFalse();
  }

  private boolean[][] tableInit() {
    boolean[][] table = new boolean[4][4];
    table[1][1] = true;
    table[1][2] = true;
    table[2][1] = true;
    table[2][2] = true;
    return table;
  }

  private class LifeRulesAlwaysFalse implements LifeRulesInterface {
    @Override
    public boolean isOn(boolean state, int numberOfLiveNeighbors) {
      return false;
    }
  }
}