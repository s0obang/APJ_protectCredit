package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.entity.GamePlayer;
import org.example.entity.Star;
import org.example.object.StarCrash;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import javax.swing.*;
import java.awt.*;

public class StarPanel extends JPanel {
  public GamePlayer starplayer;
  public StarCrash starCrash;
  public static GameManager gameManager;
  private Timer timer;
  private boolean isTimerRunning = false; // Timer 상태를 추적하는 플래그
  private Thread sound;
  private boolean isSoundPlaying = false; // 오디오 재생 상태 추적
  private Player mp3Player; // MP3 재생을 위한 Player 객체

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

  // MP3 파일 재생 메서드
  public void playstarPanelSound() {
    if (!isSoundPlaying) {
      isSoundPlaying = true;
      sound = new Thread(() -> {
        try (FileInputStream fis = new FileInputStream("src/main/java/org/example/audio/starpanel.mp3")) {
          mp3Player = new Player(fis);
          mp3Player.play(); // MP3 파일 재생
          isSoundPlaying = false; // 오디오 재생 완료
        } catch (Exception e) {
          System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
          isSoundPlaying = false; // 오류 발생 시 상태 변경
        }
      });
      sound.start(); // 비동기적으로 오디오 시작
    }
  }

  // Timer 시작 메서드
  public void startTimer() {
    timer.start();
  }

  // Timer 중지 메서드
  public void stopTimer() {
    timer.stop();
  }

  // starplayer 위치만 초기화하는 메서드
  private void initializeStarPlayer() {
    if (starplayer == null) {
      starplayer = new GamePlayer(500, 500, 50, 80); // 위치 초기화
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
    g.setColor(Color.decode("#B0BABA"));
    g.fillRect(0, 0, getWidth(), getHeight());
    starplayer.draw(g);

    if (GameManager.star != null && GameManager.star.isVisible()) {
      GameManager.star.draw(g);  // Star 객체의 visible 속성에 따라 그리기
    }
  }
}
