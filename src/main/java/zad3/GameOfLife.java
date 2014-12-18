package zad3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Transient;

@SuppressWarnings("serial")
public class GameOfLife extends JPanel {

  private static boolean[][] example = {
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, true, false, false, false, false, true, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, true, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false,},
      {false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false,},
      {false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, true, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, true, false, false, false, false, true, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, true, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, true, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, true, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, true, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, true, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, true, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, true, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
      {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,},
  };

  private GameInterface gi;

  public GameOfLife(boolean[][] grid) {
    LifeRulesInterface lri = new LifeRules();
    this.gi = new Game();
    this.gi.setRules(lri);
    this.gi.setCurrentState(grid);
  }

  public void updateGrid() {
    try {
      gi.nextState();
    } catch (GameInterface.RulesNotSetException e) {
      e.printStackTrace();
    }
  }

  @Override
  @Transient
  public Dimension getPreferredSize() {
    return new Dimension(this.gi.getCurrentState()[0].length * 8, this.gi.getCurrentState().length * 8);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Color gColor = g.getColor();
    boolean[][] grid = this.gi.getCurrentState();
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j]) {
          g.setColor(Color.black);
          g.fillRect(j * 8, i * 8, 8, 8);
        }
      }
    }
    g.setColor(gColor);
  }

  public static void main(String[] args) {
    final GameOfLife c = new GameOfLife(GameOfLife.example);
    JFrame frame = new JFrame();
    frame.getContentPane().add(c);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationByPlatform(true);
    frame.setVisible(true);
    new Timer(300, new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        c.updateGrid();
        c.repaint();
      }
    }).start();
  }
}
