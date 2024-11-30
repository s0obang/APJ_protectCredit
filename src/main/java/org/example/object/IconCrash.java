package org.example.object;

import static org.example.Manager.GameManager.beforeBonusPanel;
import static org.example.Manager.GameManager.bonusPanel;
import static org.example.Manager.GameManager.starPanel;
import static org.example.panels.StarPanel.gameManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javazoom.jl.player.Player;
import org.example.entity.Entity;
import org.example.entity.GamePlayer;
import org.example.entity.GameResult;
import org.example.entity.Icon;
import org.example.panels.GamePanel;


public class IconCrash {

  private static Player mp3Player;
  private static InputStream fis;
  private final GamePanel gamePanel;
  private final ArrayList<Entity> entities;
  private GamePlayer gamePlayer;
  private JTextField gradeText;
  private JLabel gradeImageLabel;

  public IconCrash(GamePanel gamePanel, GamePlayer gamePlayer) {
    entities = new ArrayList<>();
    this.gamePanel = gamePanel;
    this.gamePlayer = gamePlayer;

    // 학점 표시 텍스트, 이미지 레이블
    createGradeTextField();
    createGradeImageLabel();
  }

  private void createGradeTextField() {
    gradeText = new JTextField("학점 : 4.5점");
    Font gradeFont = new Font("Galmuri11 Regular", Font.BOLD, 15);
    gradeText.setFont(gradeFont);
    gradeText.setForeground(Color.black);
    gradeText.setEditable(false);
    gradeText.setOpaque(false);
    gradeText.setBorder(null);

    gradeText.setBounds(50, 20, 150, 30);
    gamePanel.setLayout(null);
    gamePanel.add(gradeText);
  }


  private void playGradeUpSound() {
    new Thread(() -> {
      try {
        fis = getClass().getResourceAsStream("/audio/gradeUp.mp3");
        mp3Player = new Player(Objects.requireNonNull(fis));
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
        fis = getClass().getResourceAsStream("/audio/gradeDown.mp3");
        mp3Player = new Player(Objects.requireNonNull(fis));
        mp3Player.play();
      } catch (Exception e) {
        System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
        e.printStackTrace();
      }
    }).start();
  }

  private void createGradeImageLabel() {
    gradeImageLabel = new JLabel();
    gradeImageLabel.setBounds(50, 50, 200, 30);
    gamePanel.add(gradeImageLabel);
    updateGradeImage(4.5);
  }

  public void addEntity(Entity entity) {
    entities.add(entity);
  }

  public void clearEntities() {
    entities.clear();
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

  //학점 증감시 소리 재생되게 함
  public void updateGPA(GamePlayer gamePlayer, Icon icon) {
    double currentGPA = gamePlayer.getGPA();
    int scoreEffect = icon.getScoreEffect();

    if (scoreEffect == 1 && currentGPA <= 4.5) {
      playGradeUpSound();
      if (currentGPA < 4.5) {
        gamePlayer.setGPA(currentGPA + 0.5);
      }
    } else if (scoreEffect == -1 && currentGPA > 0) {
      gamePlayer.setGPA(currentGPA - 0.5);
      playGradeDownSound();
    }

    double updatedGPA = gamePlayer.getGPA();
    updateGradeText(updatedGPA);
    updateGradeImage(updatedGPA);

    icon.resetPosition();

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
    if (gradeText != null) {
      gradeText.setText(String.format("학점 : %.1f점", GPA));
    }
  }


  public void updateGradeImage(double GPA) {
    String imagePath = String.format("/img/gradeItem/%.1f.png", GPA);
    try {
      java.net.URL imageUrl = getClass().getResource(imagePath);
      if (imageUrl == null) {
        throw new RuntimeException("이미지 파일을 찾을 수 없습니다: " + imagePath);
      }

      ImageIcon gradeIcon = new ImageIcon(imageUrl);

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