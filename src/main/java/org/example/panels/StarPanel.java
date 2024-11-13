package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.object.StarCrash;

import javax.swing.*;
import java.awt.*;

public class StarPanel extends JPanel {
  private Player starplayer;
  private Star star;
  private GameManager gm;
  private StarCrash starCrash;
  private Timer timer;

  public StarPanel(GameManager gm) {
    this.gm = gm;  // GameManager 전달 받기
    starplayer = new Player(500, 500, 100, 100);
    star = new Star(300, 300, 60, 50);

    setFocusable(true);
    addKeyListener(new GameKeyAdapter(starplayer));

    // 스타 위치 업데이트
    timer = new Timer(30, e -> {
      star.moveTowardsTarget();
      checkCollision();  // 충돌 체크 메서드 호출
      repaint();
    });
    timer.start();

    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);

    // StarCrash 객체 생성 시 GameManager 전달
    starCrash = new StarCrash(gm, starplayer, star);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.decode("#B0BABA"));
    g.fillRect(0, 0, getWidth(), getHeight());
    starplayer.draw(g);
    star.draw(g);
  }

  // 충돌 체크 메서드
  private void checkCollision() {
    if (starCrash != null) {
      starCrash.checkCollision();
    }
  }
}
