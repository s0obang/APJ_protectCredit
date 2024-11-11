package org.example.object;
import org.example.entity.Coin1;
import org.example.entity.Icon;
import org.example.entity.Entity;
import org.example.entity.Player;
import org.example.panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.File;


public class IconCrash {
    private final GamePanel gamePanel;
    private final ArrayList<Entity> entities;
    private Player player;
    private JTextField gradeText;
    private JLabel gradeImageLabel;

    public IconCrash(GamePanel gamePanel, Player player) {
        entities = new ArrayList<>();
        this.gamePanel = gamePanel;
        this.player = player;

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

        gradeText.setBounds(50, 50, 150, 30);
        gamePanel.setLayout(null);
        gamePanel.add(gradeText);
    }

    private void createGradeImageLabel() {
        gradeImageLabel = new JLabel();
        gradeImageLabel.setBounds(50, 90, 200, 30);  // bar 이미지의 위치와 크기에 맞게 조정
        gamePanel.add(gradeImageLabel);
        updateGradeImage(4.5); // 초기 학점 이미지를 설정
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                if (e1.getBounds().intersects(e2.getBounds())) {
                    if (e1 instanceof Player && e2 instanceof Icon) {
                        updateGPA((Player) e1, (Icon) e2);
                    } else if (e1 instanceof Icon && e2 instanceof Player) {
                        updateGPA((Player) e2, (Icon) e1);
                    }
                }
            }
        }
    }

    private void updateGPA(Player player, Icon icon) {
        double currentGPA = player.getGPA();
        int scoreEffect = icon.getScoreEffect();

        if (scoreEffect == 1 && currentGPA < 4.5) {
            player.setGPA(currentGPA + 0.5);
        } else if (scoreEffect == -1 && currentGPA > 0) {
            player.setGPA(currentGPA - 0.5);
        }


        // 학점 텍스트 필드와 이미지 레이블 업데이트
        double updatedGPA = player.getGPA();
        updateGradeText(updatedGPA);
        updateGradeImage(updatedGPA);
        // 아이콘 초기 위치로 리셋
        icon.resetPosition();
    }

    private void updateGradeText(double GPA) {
        if (gradeText != null) { // 필드가 null인 경우를 체크
            gradeText.setText(String.format("학점 : %.1f점", GPA));
        }
    }

     private void updateGradeImage(double GPA) {
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