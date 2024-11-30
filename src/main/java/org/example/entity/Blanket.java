package org.example.entity;

import java.awt.Image;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class Blanket {

  public Image blanket, itemblanket;
  private Timer timer;
  private int count; // Blanket이 표시된 횟수
  private boolean isPressedF = false;
  private GamePlayer gamePlayer;
  private Player mp3Player;
  private InputStream fis;

  public Blanket() {
    try {

      blanket = ImageIO.read(
          Objects.requireNonNull(getClass().getResourceAsStream("/img/blanket/blanket.png")));
      itemblanket = ImageIO.read(Objects.requireNonNull(
          getClass().getResourceAsStream("/img/blanket/itemblanket.png")));
    } catch (Exception e) {
      throw new RuntimeException("이미지 파일 로드 실패", e);
    }

    count = 0; // 초기값 0
  }


  public void setPlayer(GamePlayer gamePlayer) {
    this.gamePlayer = gamePlayer;
  }

  public void incrementCount() {
    count++;
  }

  public void decrementCount() {
    count--;
  }

  public int getCount() {
    return count;
  }

  public void resetBlanket() {
    count = 0;
  }


  public void playBlanketSound() {
    new Thread(() -> {
      try {
        fis = getClass().getResourceAsStream("/audio/blanket.mp3");
        mp3Player = new Player(Objects.requireNonNull(fis));
        mp3Player.play(); // MP3 파일 재생

      } catch (JavaLayerException e) {
        System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
      }
    }).start();
  }

  // F키 이벤트 처리 메서드
  public void handleFKeyEvent() {
    if (gamePlayer == null) {
      throw new IllegalStateException("Player 객체가 설정되지 않았습니다.");
    }

    if (getCount() > 0 && !isPressedF && gamePlayer.isMovable()) {

      playBlanketSound();
      decrementCount();
      isPressedF = true;
      gamePlayer.changeImage();

      if (timer != null) {
        timer.stop(); // 중첩 방지
      }

      timer = new Timer(5000, ev -> {
        gamePlayer.changeOriginImage(); // 원래 이미지로 복원
        gamePlayer.setMovable(true);
        isPressedF = false;
        timer = null;
      });
      timer.setRepeats(false);
      timer.start();
    }
  }
}
