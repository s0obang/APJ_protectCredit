package org.example.entity;

import org.example.Manager.PointsManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Blanket {
    public Image blanket, itemblanket;
    public Timer timer;
    private int count; // Blanket이 표시된 횟수
    public boolean isPressedF = false;
    private PointsManager pointsManager;
    private Player player; // GamePanel의 player를 참조

    public Blanket() {
        try {
            blanket = ImageIO.read(new File("src/main/java/org/example/img/blanket/blanket.png"));
            itemblanket = ImageIO.read(new File("src/main/java/org/example/img/blanket/itemblanket.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        count = 0; // 초기값 0
        pointsManager = new PointsManager();
    }

    // Player 객체를 설정하는 메서드
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void incrementCount() {
        count++;
    }

    public void doubleincrementCount() {
        count += 2;
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

    // F키 이벤트 처리 메서드
    public void handleFKeyEvent() {
        if (player == null) {
            throw new IllegalStateException("Player 객체가 설정되지 않았습니다.");
        }

        if (getCount() > 0 && !isPressedF) {
            decrementCount(); // Blanket의 카운트 감소
            isPressedF = true;
            player.changeImage(); // Player 이미지 변경

            // 기존 타이머가 있으면 취소
            if (timer != null) {
                timer.stop(); // 중첩 방지
            }

            // 새로운 타이머를 설정
            timer = new Timer(5000, ev -> {
                player.changeOriginImage(); // 원래 이미지로 복원
                isPressedF = false; // 상태 초기화
                timer = null; // 타이머 초기화
            });
            timer.setRepeats(false); // 한 번만 실행
            timer.start();
        }
    }

}
