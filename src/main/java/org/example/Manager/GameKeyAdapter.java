package org.example.Manager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;

import org.example.entity.Player;

public class GameKeyAdapter extends KeyAdapter {

  private final Player player;
  private Timer timer;

  public GameKeyAdapter(Player player) {
    this.player = player;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (!player.isMovable()) {
      System.out.println("교 수 님 의 따 스 한 훈 화 말 씀 시간 ~ ❤️");
      return;
    }

    if (player.isBlanketActive()) {
      return;
    }

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
