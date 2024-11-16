package org.example.Manager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.example.entity.Player;
import org.example.panels.StarPanel;

public class GameKeyAdapter extends KeyAdapter {

  private final Player player;

  public GameKeyAdapter(Player player) {
    this.player = player;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int dx = 0, dy = 0;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP -> dy = -2;
      case KeyEvent.VK_DOWN -> dy = 2;
      case KeyEvent.VK_LEFT -> dx = -2;
      case KeyEvent.VK_RIGHT -> dx = 2;
    }
    player.move(dx, dy);
  }


}
