package org.example.Manager;

import javax.swing.Timer;
import org.example.object.CoinCrash;
import org.example.object.IconCrash;
import org.example.object.StarCrash;
import org.example.panels.GamePanel;
import org.example.panels.StarPanel;


public class GameTimer extends Timer {

  public GameTimer(IconManager iconManager, CoinCrash crash, IconCrash iconCrash, GamePanel panel, StarPanel starPanel) {
    super(30, e -> {
      iconManager.updateIcons();
      iconManager.updateCoins();
      crash.checkCollisions();
      iconCrash.checkCollisions();
      starPanel.starCrash.checkCollision();
      panel.repaint();
    });
  }
}