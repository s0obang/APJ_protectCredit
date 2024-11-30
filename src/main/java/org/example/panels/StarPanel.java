package org.example.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import javazoom.jl.player.Player;
import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.entity.GamePlayer;
import org.example.entity.Star;
import org.example.object.StarCrash;

public class StarPanel extends JPanel {

  public static GameManager gameManager;
  public GamePlayer starplayer;
  public StarCrash starCrash;
  private Timer timer;
  private boolean isTimerRunning = false;
  private Thread sound;
  private boolean isSoundPlaying = false;
  private Player mp3Player;
  private Image backgroundImage;

  public StarPanel(GameManager gameManager) {
    StarPanel.gameManager = gameManager;

    try {
      backgroundImage = ImageIO.read(
          Objects.requireNonNull(getClass().getResourceAsStream(
              "/img/backgrounds/etcback.jpg")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.starCrash = new StarCrash(gameManager, this);
    initializeStarPlayer();
    setFocusable(true);
    addKeyListener(new GameKeyAdapter(starplayer));
    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    // Timer 초기화 (생성 단계에서 중복 방지)
    if (timer == null) {
      timer = new Timer(20, e -> {
        if (GameManager.star != null) {
          GameManager.star.moveTowardsTarget();
          repaint();

          if (starCrash.isCollision) {
            starCrash.handleCollision();
          }
        }
      });
    }

    timer.start();
    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);
  }

  public void playstarPanelSound() {
    if (!isSoundPlaying) {
      isSoundPlaying = true;
      sound = new Thread(() -> {
        try (InputStream fis = getClass().getResourceAsStream("/audio/starpanel.mp3")) {
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


  public void startTimer() {
    timer.start();
  }


  public void stopTimer() {
    timer.stop();
  }

  // starplayer 위치만 초기화하는 메서드
  private void initializeStarPlayer() {
    if (starplayer == null) {
      starplayer = new GamePlayer(500, 500, 60, 90); // 위치 초기화
    } else {
      // 이미 존재하는 starplayer가 있으면 위치만 초기화
      starplayer.setX(500);
      starplayer.setY(500);
    }
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    if (visible) {
      startTimer();
      initializeStarPlayer();
      playstarPanelSound();
    } else {
      stopTimer();

      // setVisible이 false일 때 sound 스레드 멈추기
      if (sound != null && sound.isAlive()) {
        sound.interrupt();  // 스레드 멈추기
        isSoundPlaying = false; // 오디오 재생 상태 초기화
        if (mp3Player != null) {
          mp3Player.close(); // 오디오 파일 종료
        }
      }
    }
  }

  // 스타 초기화 메서드
  public void initializeStar(Star star) {
    GameManager.star = star;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    } else {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(0, 0, getWidth(), getHeight());
    }
    starplayer.draw(g);

    if (GameManager.star != null && GameManager.star.isVisible()) {
      GameManager.star.draw(g);
    }
  }
}
