package org.example.Manager;

import javax.swing.Timer;
import org.example.object.Crash;
import org.example.object.IconCrash;
import org.example.panels.GamePanel;


public class GameTimer extends Timer {

  public GameTimer(IconManager iconManager, Crash crash, IconCrash iconCrash, GamePanel panel) {
    super(30, e -> {
      iconManager.updateIcons();
      iconManager.updateCoins();
      crash.checkCollisions();
      iconCrash.checkCollisions();
      panel.repaint();
    });
  }
}