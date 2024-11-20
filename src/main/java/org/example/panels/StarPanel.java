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

    // starplayer 위치 초기화 (500, 500)으로 설정
    initializeStarPlayer();

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

  // starplayer 위치만 초기화하는 메서드
  private void initializeStarPlayer() {
    if (starplayer == null) {
      starplayer = new Player(500, 500, 100, 100); // 위치 초기화
    } else {
      // 이미 존재하는 starplayer가 있으면 위치만 초기화
      starplayer.setX(500);
      starplayer.setY(500);
    }
  }

  // 패널이 보일 때마다 starplayer 위치 초기화
  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    if (visible) {
      initializeStarPlayer(); // StarPanel이 보일 때마다 위치 초기화
    }
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
