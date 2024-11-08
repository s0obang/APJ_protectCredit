package org.example.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.example.Manager.GameInitializer;
import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.Manager.GameTimer;
import org.example.Manager.IconManager;
import org.example.entity.Coin1;
import org.example.entity.Icon;
import org.example.entity.Player;
import org.example.object.Crash;
import org.example.object.IconCrash;


public class GamePanel extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName()); // 강한 로그 사용
  private Player player; //이거 메인캐릭터임^^
  private BufferedImage coinImage;
  private Crash crash;
  private IconCrash iconCrash;
  private Timer timer;
  private IconManager iconManager;

  public GamePanel(GameManager manager) {

    setLayout(new BorderLayout());
    JButton endButton = new JButton("End Game (Game Over)");
    endButton.addActionListener(e -> manager.showEndScreen(false)); // 게임 오버로 종료 화면으로 이동
    JButton successButton = new JButton("End Game (Success)");
    successButton.addActionListener(e -> manager.showEndScreen(true)); // 성공으로 종료 화면으로 이동

    // Player 객체 생성 (초기 위치와 크기 설정)
    player = new Player(500, 500, 70, 70);

    // Crash 객체 생성 -> 충돌 감지에 저장
    crash = new Crash(this);
    iconCrash = new IconCrash(this, player);
    iconCrash.addEntity(player);
    crash.addEntity(player);
    iconManager = new IconManager();

    //플레이어 방향키로 이동하느느거!!!
    setFocusable(true);
    addKeyListener(new GameKeyAdapter(player));
    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);
    GameInitializer.initializeEntities(crash, iconCrash);
  }

  public void startGame() {
    timer = new GameTimer(iconManager, crash, iconCrash, this);
    timer.start();
    
  }

  // 패널에 아이콘을 그리는 메서드
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(0, 0, getWidth(), getHeight());
    player.draw(g);
    for (Icon icon : Icon.iconList) {
      icon.draw(g);
    }
    for (Coin1 coin : Coin1.arraycoin) {
      coin.draw(g);
    }
  }

}
