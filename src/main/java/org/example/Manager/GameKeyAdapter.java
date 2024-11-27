package org.example.Manager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;

import org.example.entity.GamePlayer;

public class GameKeyAdapter extends KeyAdapter {

  private final GamePlayer gamePlayer;
  private Timer timer;

  public GameKeyAdapter(GamePlayer gamePlayer) {
    this.gamePlayer = gamePlayer;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (!gamePlayer.isMovable()) {
      System.out.println("교 수 님 의 따 스 한 훈 화 말 씀 시간 ~ ❤️");
      return;
    }

    if (gamePlayer.isBlanketActive()) {
      return;
    }

    int dx = 0, dy = 0;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP -> dy = -4;
      case KeyEvent.VK_DOWN -> dy = 4;
      case KeyEvent.VK_LEFT -> dx = -4;
      case KeyEvent.VK_RIGHT -> dx = 4;
    }
    gamePlayer.move(dx, dy);
  }


}
