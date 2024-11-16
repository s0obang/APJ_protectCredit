package org.example.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.example.Manager.*;
import org.example.entity.Coin;
import org.example.entity.Icon;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.object.CoinCrash;
import org.example.object.IconCrash;
import org.example.object.StarCrash;
import org.example.object.UserStatus;


public class GamePanel extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName()); // 강한 로그 사용
  public CoinCrash coincrash;
  public Player player; //이거 메인캐릭터임^^
  private BufferedImage coinImage;
  private IconCrash iconCrash;
  private Timer timer;
  private IconManager iconManager;
  private StarPanel starPanel;
  private GameManager gameManager;
  private BufferedImage backgroundImage;

  public GamePanel() {

    // 배경 이미지 로드
    try {
      backgroundImage = ImageIO.read(
          new File("src/main/java/org/example/img/backgrounds/backgroundReal.jpg"));
    } catch (IOException e) {
      LOGGER.severe("Failed to load background image: " + e.getMessage());
    }
    // Player 객체 생성 (초기 위치와 크기 설정)
    player = new Player(500, 500, 100, 100);

    // Crash 객체 생성 -> 충돌 감지에 저장
    coincrash = new CoinCrash(this);
    iconCrash = new IconCrash(this, player);
    iconCrash.addEntity(player);
    coincrash.addEntity(player);
    starPanel = new StarPanel(gameManager);
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
    GameInitializer.initializeEntities(coincrash, iconCrash);
  }

  public void startGame() {
    timer = new GameTimer(iconManager, coincrash, iconCrash, this);
    this.repaint();
    timer.start();
  }

  public void stopGame() {
    if (timer != null) {
      timer.stop();
      //LOGGER.info("Game stopped temporarily.");
    }
  }


  // 패널에 아이콘을 그리는 메서드
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // 배경 이미지 그리기
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    } else {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(0, 0, getWidth(), getHeight());
    }
    player.draw(g);
    for (Icon icon : Icon.iconList) {
      icon.draw(g);
    }
    for (Coin coin : Coin.arraycoin) {
      coin.draw(g);
    }
    // 좌측 상단에 띄울 코인 이미지 그리기
    if (CoinCrash.getCoinImage() != null) {
      g.drawImage(CoinCrash.getCoinImage(), 50, 90, 22, 22, null);
    }

    repaint();
  }
}

