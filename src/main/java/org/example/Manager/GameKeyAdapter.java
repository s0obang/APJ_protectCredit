package org.example.Manager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.example.entity.Player;

public class GameKeyAdapter extends KeyAdapter {

  private final Player player;

  public GameKeyAdapter(Player player) {
    this.player = player;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int dx = 0, dy = 0;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP -> dy = -1;
      case KeyEvent.VK_DOWN -> dy = 1;
      case KeyEvent.VK_LEFT -> dx = -1;
      case KeyEvent.VK_RIGHT -> dx = 1;
    }
    player.move(dx, dy);
  }
}
