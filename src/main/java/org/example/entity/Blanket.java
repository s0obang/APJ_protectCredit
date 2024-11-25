package org.example.entity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javazoom.jl.player.Player;
import java.io.FileInputStream;


public class Blanket {
    public Image blanket, itemblanket;
    public Timer timer;
    private int count; // Blanket이 표시된 횟수
    public boolean isPressedF = false;
    private GamePlayer gamePlayer; // GamePanel의 player를 참조


    public Blanket() {
        try {
            blanket = ImageIO.read(new File("src/main/java/org/example/img/blanket/blanket.png"));
            itemblanket = ImageIO.read(new File("src/main/java/org/example/img/blanket/itemblanket.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        count = 0; // 초기값 0
    }

    // Player 객체를 설정하는 메서드
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

    // MP3 파일 재생 메서드
    public void playBlanketSound() {
        new Thread(() -> { // 별도의 스레드에서 재생
            try (FileInputStream fis = new FileInputStream("src/main/java/org/example/audio/blanket.mp3")) {
                Player mp3Player = new Player(fis);
                mp3Player.play(); // MP3 파일 재생
            } catch (Exception e) {
                System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
            }
        }).start();
    }

    // F키 이벤트 처리 메서드
    public void handleFKeyEvent() {
        // Blanket 효과음 재생
        playBlanketSound();
        if (gamePlayer == null) {
            throw new IllegalStateException("Player 객체가 설정되지 않았습니다.");
        }

        if (getCount() > 0 && !isPressedF && gamePlayer.isMovable()) {
            decrementCount(); // Blanket의 카운트 감소
            isPressedF = true;
            gamePlayer.changeImage(); // Player 이미지 변경

            // 기존 타이머가 있으면 취소
            if (timer != null) {
                timer.stop(); // 중첩 방지
            }

            // 새로운 타이머를 설정
            timer = new Timer(5000, ev -> {
                gamePlayer.changeOriginImage(); // 원래 이미지로 복원
                gamePlayer.setMovable(true);
                isPressedF = false; // 상태 초기화
                timer = null; // 타이머 초기화
            });
            timer.setRepeats(false); // 한 번만 실행
            timer.start();
        }
    }

}
