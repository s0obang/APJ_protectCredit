package org.example.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javazoom.jl.player.Player;
import org.example.Manager.GameKeyAdapter;
import org.example.Manager.PointsManager;
import org.example.entity.Coin;
import org.example.entity.GamePlayer;
import org.example.object.CoinCrash;

public class BonusPanel extends JPanel {

  public CoinCrash coinCrash;
  public GamePlayer bonusplayer;
  public boolean isCoinsInitialized = false;
  public Timer timer, countTimer;
  public int remainingTime = 10; // 남은 시간 (10초)
  public Thread sound;
  public boolean isSoundPlaying = false;
  public Player mp3Player;
  private PointsManager pointsManager;
  private BeforeBonusPanel beforeBonusPanel;
  private JTextField curpointText;
  private JLabel timerLabel;
  private boolean isTimerRunning = false;
  private Image backgroundImage;


  public BonusPanel(PointsManager pointsManager) {
    this.pointsManager = pointsManager;
    beforeBonusPanel = new BeforeBonusPanel();
    coinCrash = new CoinCrash(null, this, pointsManager);

    curpointText = createPointsTextField();
    setLayout(null);
    this.add(curpointText);

    try {
      backgroundImage = ImageIO.read(
          Objects.requireNonNull(
              getClass().getResourceAsStream("/img/backgrounds/backFever.jpeg")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    timerLabel = new JLabel("남은 시간: " + remainingTime + "초", SwingConstants.CENTER);
    timerLabel.setFont(new Font("Galmuri11 Regular", Font.BOLD, 20));
    timerLabel.setBounds(40, 40, 200, 50);
    timerLabel.setForeground(Color.BLACK);
    this.add(timerLabel);

    setPreferredSize(new Dimension(1080, 720)); // 패널 크기 설정
    bonusplayer = new GamePlayer(500, 100, 60, 90);

    //플레이어 방향키로 이동하느느거!!!
    setFocusable(true);
    addKeyListener(new GameKeyAdapter(bonusplayer));
    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    setOpaque(true);

    countTimer = new Timer(1000, e -> {
      if (remainingTime > 0) {
        remainingTime--;
        timerLabel.setText("남은 시간 : " + remainingTime + "초");
      } else {
        ((Timer) e.getSource()).stop();
      }
      repaint();
    });

    timer = new Timer(30, e -> {
      if (!isCoinsInitialized) {
        Coin.resetBonusCoins();
        isCoinsInitialized = true;
        coinCrash.checkBonusCollisions();
        repaint();
      }
      coinCrash.checkBonusCollisions();
      repaint();
    });
    timer.start();

  }

  private JTextField createPointsTextField() {
    JTextField textField = new JTextField(pointsManager.getPoints() + "원");
    textField.setFont(new Font("Galmuri11 Regular", Font.BOLD, 20));
    textField.setForeground(Color.black);
    textField.setEditable(false);
    textField.setOpaque(false);
    textField.setFocusable(false);
    textField.setBorder(null);
    textField.setBounds(85, 93, 150, 30);
    return textField;
  }

  public void playbonusPanelSound() {
    if (!isSoundPlaying) {
      isSoundPlaying = true;
      sound = new Thread(() -> {
        try (InputStream fis = getClass().getResourceAsStream(
            "/audio/bonuspanel.mp3")) {
          mp3Player = new Player(Objects.requireNonNull(fis));
          mp3Player.play();
          isSoundPlaying = false;
        } catch (Exception e) {
          System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
          isSoundPlaying = false;
        }
      });
      sound.start();
    }
  }


  public void updateCurpointText() {
    curpointText.setText(pointsManager.getPoints() + "만 원");
    repaint();
  }

  public void updateTime() {
    timerLabel.setText("남은 시간 : " + remainingTime + "초");
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);

    if (visible) {

      startTimer();
    } else {

      stopTimer();
      // setVisible이 false일 때 sound 스레드 멈추기
      if (sound != null && sound.isAlive()) {
        sound.interrupt();
        isSoundPlaying = false;
        if (mp3Player != null) {
          mp3Player.close();
        }
      }
    }
  }


  private void startTimer() {
    if (timer != null && !isTimerRunning) {
      timer.start();
      isTimerRunning = true;
    }
  }

  private void stopTimer() {
    if (timer != null && isTimerRunning) {
      timer.stop();
      isTimerRunning = false;
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    } else {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(0, 0, getWidth(), getHeight());
    }

    bonusplayer.draw(g);

    for (Coin coin : Coin.largeCoins) {
      coin.draw(g);
    }
    for (Coin coin : Coin.mediCoins) {
      coin.draw(g);
    }
    for (Coin coin : Coin.smallCoins) {
      coin.draw(g);
    }

    // 좌측 상단에 띄울 코인 이미지임
    if (CoinCrash.getCoinImage() != null) {
      g.drawImage(CoinCrash.getCoinImage(), 50, 90, 30, 30, null);
    }
  }

}