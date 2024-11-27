package org.example.Manager;

import java.awt.Graphics;
import java.io.FileInputStream;
import java.util.Random;
import javax.swing.Timer;
import javazoom.jl.player.Player;
import org.example.entity.GamePlayer;
import org.example.entity.Professor;


public class ProfessorManager {

  private Professor professor;
  private Timer professorTimer;
  private GamePlayer gamePlayer;
  private Thread sound;
  private boolean isSoundPlaying = false;
  private Player mp3Player;
  private Timer byeTimer;
  private String audio;

  public ProfessorManager(GamePlayer player) {
    this.gamePlayer = player;
    this.professor = new Professor(100, 100, 70, 100);
  }

  public void start(int interval) {
    professorTimer = new Timer(interval, e -> {
      if (!professor.isVisible()) {
        professor.setVisible(true);
        System.out.println("교수님 등장!");
        playProfessor(1);
        moveProfessorRandomly();

        byeTimer = new Timer(7000, ev -> {
          professor.setVisible(false);
          System.out.println("교수님 퇴장!");
        });
        byeTimer.start();
        byeTimer.setRepeats(false);
      }
    });
    professorTimer.setRepeats(true);
    professorTimer.start();
  }

  public void stop() {
    if (professorTimer != null) {
      professorTimer.stop();
    }
  }

  public void update() {
    if (professor.isVisible()) {
      if(!professor.isCollided()) professor.move(gamePlayer.getX(), gamePlayer.getY());
      if (professor.checkCollision(gamePlayer.getX(), gamePlayer.getY(), gamePlayer.getWidth(),
          gamePlayer.getHeight())) {
        if (!professor.isCollided()) {
          professorTimer.stop();
          byeTimer.stop();
          professor.setCollided(true);
          professor.setWidth(260);
          professor.setHeight(140);
          System.out.println("충돌");
          playProfessor(2);
          gamePlayer.setMovable(false);

          //5초 딜레이 후에 복원, 교수님 사라지게
          Timer recoverTimer = new Timer(5000, ev -> {
            professor.setVisible(false);
            professor.setCollided(false);
            gamePlayer.setMovable(true);
            professor.setWidth(70);
            professor.setHeight(100);
            professorTimer.restart();
            System.out.println("충돌 종료");
          });
          recoverTimer.start();
          recoverTimer.setRepeats(false);
        }
      }
    }
  }

  public void draw(Graphics g) {
    professor.draw(g);
  }

  private void moveProfessorRandomly() {
    Random random = new Random();
    int corner = random.nextInt(4);
    switch (corner) {
      case 0:
        professor.setX(0);
        professor.setY(0);
        break;
      case 1:
        professor.setX(1024 - professor.getWidth());
        professor.setY(0);
        break;
      case 2:
        professor.setX(0);
        professor.setY(768- professor.getHeight());
        break;
      case 3:
        professor.setX(1024 - professor.getWidth());
        professor.setY(768 - professor.getHeight());
        break;
    }
    System.out.println("교수님 위치: " + professor.getX() + ", " + professor.getY());
  }

  public void playProfessor(int type) {
    if (!isSoundPlaying) {
      if( type == 1) {
        audio = "src/main/java/org/example/audio/prof.mp3";
      }
      else {
        audio = "src/main/java/org/example/audio/profCol.mp3";
      }
        isSoundPlaying = true;
        sound = new Thread(() -> {
          try (FileInputStream fis = new FileInputStream(audio)) {
            mp3Player = new Player(fis);
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
}
