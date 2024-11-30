package org.example.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javazoom.jl.player.Player;

public class LevelUpPanel extends JPanel {

  private Image levelupchar, starImage, backgroundImage;
  private JLabel levelLabel, upLabel, starLabel, congratulation;
  private Thread sound;
  private boolean isSoundPlaying = false; // 오디오 재생 상태 추적
  private Player mp3Player; // MP3 재생을 위한 Player

  public LevelUpPanel() {
    setLayout(null);
    // 배경 이미지 로드
    try {
      backgroundImage = ImageIO.read(
          Objects.requireNonNull(getClass().getResourceAsStream("/img/backgrounds/etcback.jpg")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Font levelupFont = new Font("Galmuri11 Regular", Font.BOLD, 45);

    try {
      levelupchar = ImageIO.read(
          Objects.requireNonNull(
              getClass().getResourceAsStream("/img/character/main_char_left.png")));
      starImage = ImageIO.read(
          Objects.requireNonNull(getClass().getResourceAsStream("/img/star/star.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    congratulation = new JLabel("CONGRATULATION!");
    congratulation.setFont(levelupFont);
    congratulation.setForeground(Color.black);
    congratulation.setBounds(480, 280, 550, 60);

    //levelLabel 텍스트
    levelLabel = new JLabel("GRADE ");
    levelLabel.setFont(levelupFont);
    levelLabel.setForeground(Color.black);
    levelLabel.setBounds(480, 390, 200, 60);
    add(levelLabel);

    add(congratulation);

    //star 이미지 삽입
    Image scaledStarImage = starImage.getScaledInstance(147, 120, Image.SCALE_SMOOTH);
    starLabel = new JLabel(new ImageIcon(scaledStarImage));
    starLabel.setBounds(610, 360, 147, 120);
    add(starLabel);

    upLabel = new JLabel("  UP");
    upLabel.setFont(levelupFont);
    upLabel.setForeground(Color.black);
    upLabel.setBounds(690, 390, 120, 60);
    add(upLabel);
  }

  public void playLevelUpPanelSound() {
    if (!isSoundPlaying) {
      isSoundPlaying = true;
      sound = new Thread(() -> {
        try (InputStream fis = getClass().getResourceAsStream(
            "/audio/levelUpPanel.mp3")) {
          mp3Player = new Player(Objects.requireNonNull(fis));
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

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    } else {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(0, 0, getWidth(), getHeight());
    }
    g.drawImage(levelupchar, 130, 165, 273, 390, null);
  }
}