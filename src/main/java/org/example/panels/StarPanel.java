package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.object.StarCrash;
import org.example.panels.BounsPanel;

import javax.swing.*;
import java.awt.*;

public class StarPanel extends JPanel {
  private final Player starplayer;
  private final Star star;
  public StarCrash starCrash;
  public GameManager gameManager;

  public StarPanel(GameManager ignoredGameManager) {
    starplayer = new Player(500, 500, 100, 100);
    star = new Star(300, 300, 60, 50);

    setFocusable(true);
    addKeyListener(new GameKeyAdapter(starplayer));

    // 스타 위치 업데이트와 충돌 타이머 설정
    Timer timer = new Timer(30, e -> {
      star.moveTowardsTarget();
      repaint();
      checkCollision();  // 충돌을 체크하는 메서드 호출
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
    starCrash = new StarCrash(this.gameManager, starplayer, star);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.decode("#B0BABA"));
    g.fillRect(0, 0, getWidth(), getHeight());
    starplayer.draw(g);
    star.draw(g);  // 여기서 Star 객체의 visible 속성에 따라 그려짐
  }

  // 충돌 체크 메서드
  private void checkCollision() {
    starCrash.checkCollision();  // 충돌 발생 시 처리
  }

  // 충돌 후 bonusColor() 호출
  public void handleBonusColor() {
    // BonusPanel 객체 가져오기
    BounsPanel bp = (BounsPanel) GameManager.getPanel("bonus");
    if (bp != null) {
      bp.bonusColor();  // Bonus 색상 처리
    }
  }
}
