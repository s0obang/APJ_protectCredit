package org.example.object;
import org.example.entity.Coin1;
import org.example.entity.Icon;
import org.example.entity.Entity;
import org.example.entity.Player;
import org.example.panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class IconCrash {
    private final GamePanel gamePanel;
    private final ArrayList<Entity> entities;
    private Player player;
    private JTextField gradeText;

    public IconCrash(GamePanel gamePanel, Player player) {
        entities = new ArrayList<>();
        this.gamePanel = gamePanel;
        this.player = player;

        // 학점 표시 텍스트 필드 생성
        createGradeTextField();
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

        // 학점 텍스트 필드 업데이트
        updateGradeText(player.getGPA());

        // 아이콘 초기 위치로 리셋
        icon.resetPosition();
    }

    private void updateGradeText(double GPA) {
        if (gradeText != null) { // 필드가 null인 경우를 체크
            gradeText.setText(String.format("학점 : %.1f점", GPA));
        }
    }
}