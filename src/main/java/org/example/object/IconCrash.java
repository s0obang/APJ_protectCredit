package org.example.object;
import org.example.entity.GameResult;
import org.example.entity.Icon;
import org.example.entity.Entity;
import org.example.entity.GamePlayer;
import org.example.panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.example.Manager.GameManager.*;
import static org.example.panels.StarPanel.gameManager;

import javazoom.jl.player.Player;
import java.io.FileInputStream;


public class IconCrash {
    private final GamePanel gamePanel;
    private final ArrayList<Entity> entities;
    private GamePlayer gamePlayer;
    private JTextField gradeText;
    private JLabel gradeImageLabel;
    private static Player mp3Player;
    private static FileInputStream fis;

    public IconCrash(GamePanel gamePanel, GamePlayer gamePlayer) {
        entities = new ArrayList<>();
        this.gamePanel = gamePanel;
        this.gamePlayer = gamePlayer;

        // 학점 표시 텍스트 필드와 이미지 레이블 생성
        createGradeTextField();
        createGradeImageLabel();
    }

    private void createGradeTextField() {
        gradeText = new JTextField("학점 : 4.5점");
        Font gradeFont = new Font("Neo둥근모", Font.BOLD, 15);
        gradeText.setFont(gradeFont);
        gradeText.setForeground(Color.black);
        gradeText.setEditable(false);
        gradeText.setOpaque(false);
        gradeText.setBorder(null);

        gradeText.setBounds(50, 20, 150, 30);
        gamePanel.setLayout(null);
        gamePanel.add(gradeText);
    }

    // 오디오 재생 메서드 추가
    private void playGradeUpSound() {
        new Thread(() -> {
            try {
                fis = new FileInputStream("src/main/java/org/example/audio/gradeUp.mp3");
                mp3Player = new Player(fis);
                mp3Player.play();
            } catch (Exception e) {
                System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void playGradeDownSound() {
        new Thread(() -> {
            try {
                fis = new FileInputStream("src/main/java/org/example/audio/gradeDown.mp3");
                mp3Player = new Player(fis);
                mp3Player.play();
            } catch (Exception e) {
                System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void createGradeImageLabel() {
        gradeImageLabel = new JLabel();
        gradeImageLabel.setBounds(50, 50, 200, 30);  // bar 이미지의 위치와 크기에 맞게 조정
        gamePanel.add(gradeImageLabel);
        updateGradeImage(4.5); // 초기 학점 이미지를 설정
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void clearEntities() {
        entities.clear(); // 기존 엔티티 모두 제거
    }

    public void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                if (e1.getBounds().intersects(e2.getBounds())) {
                    if (e1 instanceof GamePlayer && e2 instanceof Icon) {
                        updateGPA((GamePlayer) e1, (Icon) e2);
                    } else if (e1 instanceof Icon && e2 instanceof GamePlayer) {
                        updateGPA((GamePlayer) e2, (Icon) e1);
                    }
                }
            }
        }
    }

    public void updateGPA(GamePlayer gamePlayer, Icon icon) {
        double currentGPA = gamePlayer.getGPA();
        int scoreEffect = icon.getScoreEffect();

        if (scoreEffect == 1 && currentGPA <= 4.5) {
            playGradeUpSound(); // GPA 증가 시 sound 재생
            if (currentGPA < 4.5) gamePlayer.setGPA(currentGPA + 0.5);
        } else if (scoreEffect == -1 && currentGPA > 0) {
            gamePlayer.setGPA(currentGPA - 0.5);
            playGradeDownSound(); // GPA 감소 시 sound 재생
        }


        // 학점 텍스트 필드와 이미지 레이블 업데이트
        double updatedGPA = gamePlayer.getGPA();
        updateGradeText(updatedGPA);
        updateGradeImage(updatedGPA);
        // 아이콘 초기 위치로 리셋
        icon.resetPosition();
        // 유저 상태 업데이트
        gameManager.getUserStatus().setUserScore(updatedGPA);
        // GPA가 0이 되면 게임 종료
        if (updatedGPA <= 0) {
            GameResult result = new GameResult();
            result.setPoints(gameManager.getPointsManager().getPoints());
            result.setGraduated(false);
            gameManager.showEndScreen(result);
            gamePanel.stopGame();
            starPanel.setVisible(false);
            bonusPanel.setVisible(false);
            beforeBonusPanel.setVisible(false);
            gameManager.getLevelUpTimer().stop();
        }

    }

    public void updateGradeText(double GPA) {
        if (gradeText != null) { // 필드가 null인 경우를 체크
            gradeText.setText(String.format("학점 : %.1f점", GPA));
        }
    }

     public void updateGradeImage(double GPA) {
         String imagePath = String.format("src/main/java/org/example/img/gradeItem/%.1f.png", GPA);
         try {
             ImageIcon gradeIcon = new ImageIcon(imagePath);
             // 이미지 크기 조정
             Image image = gradeIcon.getImage();
             Image resizedImage = image.getScaledInstance(200, 30, Image.SCALE_SMOOTH);
             gradeIcon = new ImageIcon(resizedImage);

             gradeImageLabel.setIcon(gradeIcon);
         } catch (Exception e) {
             System.err.println("이미지를 불러오는데 실패했습니다: " + imagePath);
             e.printStackTrace();
         }
     }
}