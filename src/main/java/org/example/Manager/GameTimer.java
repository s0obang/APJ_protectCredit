package org.example.Manager;

import javax.swing.Timer;
import org.example.object.CoinCrash;
import org.example.object.IconCrash;
import org.example.panels.GamePanel;


public class GameTimer extends Timer {

  public GameTimer(IconManager iconManager, CoinCrash crash, IconCrash iconCrash, GamePanel panel) {
    super(30, e -> {
      iconManager.updateIcons();
      iconManager.updateCoins();
      crash.checkCollisions();
      iconCrash.checkCollisions();
      panel.repaint();
    });
  }
}