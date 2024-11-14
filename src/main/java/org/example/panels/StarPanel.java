package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.object.StarCrash;

import javax.swing.*;
import java.awt.*;

public class StarPanel extends JPanel {
  public final Player starplayer;
  public StarCrash starCrash;
  public static GameManager gameManager;

  public StarPanel(GameManager gameManager) {
    this.gameManager = gameManager;
    this.starplayer = new Player(500, 500, 100, 100);

    // StarCrash 객체 생성: GameManager와 StarPanel을 참조
    this.starCrash = new StarCrash(gameManager, this);

    setFocusable(true);
    addKeyListener(new GameKeyAdapter(starplayer));

    // 스타 위치 업데이트와 충돌 타이머 설정
    Timer timer = new Timer(20, e -> {
      if (GameManager.star != null) {
        GameManager.star.moveTowardsTarget();
        repaint();

        // 충돌 체크 및 후속 처리
        if (starCrash.checkCollision()) {
          starCrash.handleCollision();
        }
      }
    });
    timer.start();

    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);
  }

  public void initializeStar(Star star) {
    GameManager.star = star;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.decode("#B0BABA"));
    g.fillRect(0, 0, getWidth(), getHeight());
    starplayer.draw(g);

    if (GameManager.star != null && GameManager.star.isVisible()) {
      GameManager.star.draw(g);  // Star 객체의 visible 속성에 따라 그리기
    }
  }
}
