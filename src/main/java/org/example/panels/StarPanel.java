package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.object.StarCrash;

import javax.swing.*;
import java.awt.*;

public class StarPanel extends JPanel {
  public Player starplayer;
  public StarCrash starCrash;
  public static GameManager gameManager;
  private Timer timer;


  public StarPanel(GameManager gameManager) {
    StarPanel.gameManager = gameManager;

    // StarCrash 객체 생성: GameManager와 StarPanel을 참조
    this.starCrash = new StarCrash(gameManager, this);

    starplayer = new Player(500, 200, 100, 100);

    setFocusable(true);
    addKeyListener(new GameKeyAdapter(starplayer));
    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    // 스타 위치 업데이트와 충돌 타이머 설정
    if(timer != null) timer.stop();
    timer = new Timer(20, e -> {
      if (GameManager.star != null) {
        GameManager.star.moveTowardsTarget();
        repaint();

        if(starCrash.isCollision) starCrash.handleCollision();
      }
    });
    timer.start();

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);
  }

  // 스타 초기화 메서드
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
