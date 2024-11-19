package org.example.Manager;

import java.awt.Graphics;
import javax.swing.Timer;
import org.example.entity.Player;
import org.example.entity.Professor;

public class ProfessorManager {

  private Professor professor;
  private Timer professorTimer;
  private Player player; // 플레이어와의 상호작용 처리
  private Runnable onCollision; // 충돌 시 동작 실행

  public ProfessorManager(Player player, Runnable onCollision) {
    this.player = player;
    this.onCollision = onCollision;

    // 교수님 객체 초기화
    this.professor = new Professor(100, 100, 90, 90); // 초기 위치와 크기 설정
  }

  public void start(int interval) {
    professorTimer = new Timer(interval, e -> {
      if (!professor.isVisible()) {
        professor.setVisible(true);
        System.out.println("교수님 등장!");
        moveProfessorRandomly();

        // 교수님이 일정 시간 후 사라짐
        new Timer(10000, ev -> {
          professor.setVisible(false);
          System.out.println("교수님 퇴장!");
        }).start();
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
      professor.move(player.getX(), player.getY());
      if (professor.checkCollision(player.getX(), player.getY(), player.getWidth(),
          player.getHeight())) {
        if (!professor.isCollided()) {
          professor.setCollided(true);
          //professor.setWidth(120);
          System.out.println("충돌");

          // 사용자 컨트롤 제한
          player.setMovable(false);

          // 제한 시간이 끝나면 사용자 컨트롤 복원 및 교수님 제거
          new Timer(5000, ev -> {
            professor.setVisible(false);
            professor.setCollided(false);
            player.setMovable(true);
            //professor.setWidth(90);
            System.out.println("충돌 종료");
          }).start();
        }
      }
    }
  }

  public void draw(Graphics g) {
    professor.draw(g);
  }

  private void moveProfessorRandomly() {
    // 추가적으로 교수님이 랜덤으로 이동하거나 더 복잡한 동작을 추가 가능
  }
}