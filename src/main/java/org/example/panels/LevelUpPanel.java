package org.example.panels;

import javazoom.jl.player.Player;
import org.example.Manager.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
                    new File("src/main/java/org/example/img/backgrounds/etcback.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font levelupFont = new Font("Neo둥근모", Font.BOLD, 60);

        try {
            levelupchar = ImageIO.read(
                    new File("src/main/java/org/example/img/character/main_char_left.png"));
            starImage = ImageIO.read(new File("src/main/java/org/example/img/star/star.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        congratulation = new JLabel("CONGRATULATION!");
        congratulation.setFont(levelupFont);
        congratulation.setForeground(Color.black);
        congratulation.setBounds(480, 280, 500, 60);

        //levelLabel 텍스트
        levelLabel = new JLabel("LEVEL ");
        levelLabel.setFont(levelupFont);
        levelLabel.setForeground(Color.black);
        levelLabel.setBounds(480, 390, 200, 60);
        add(levelLabel);

        add(congratulation);

        //star 이미지 삽입
        Image scaledStarImage = starImage.getScaledInstance(147, 120, Image.SCALE_SMOOTH);
        starLabel = new JLabel(new ImageIcon(scaledStarImage));
        starLabel.setBounds(605, 360, 147, 120);
        add(starLabel);

        upLabel = new JLabel(" UP");
        upLabel.setFont(levelupFont);
        upLabel.setForeground(Color.black);
        upLabel.setBounds(680, 390, 100, 60);
        add(upLabel);
    }

    public void playLevelUpPanelSound() {
        if (!isSoundPlaying) {
            isSoundPlaying = true;
            sound = new Thread(() -> {
                try (FileInputStream fis = new FileInputStream("src/main/java/org/example/audio/levelUpPanel.mp3")) {
                    mp3Player = new Player(fis);
                    mp3Player.play(); // MP3 파일 재생
                    isSoundPlaying = false; // 오디오 재생 완료
                } catch (Exception e) {
                    System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
                    isSoundPlaying = false; // 오류 발생 시 상태 변경
                }
            });
            sound.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.drawImage(levelupchar, 130, 165, 273,390, null);
    }
}