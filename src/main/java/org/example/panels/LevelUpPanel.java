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
    private Image levelupchar;
    private Image starImage;
    private JTextField congratulation;
    private JLabel levelLabel, upLabel, starLabel;
    private Thread sound;
    private boolean isSoundPlaying = false; // 오디오 재생 상태 추적
    private Player mp3Player; // MP3 재생을 위한 Player

    public LevelUpPanel() {
        setLayout(null);
        setBackground(Color.decode("#B0BABA"));
        Font levelupFont = new Font("Neo둥근모", Font.BOLD, 60);

        try {
            levelupchar = ImageIO.read(
                    new File("src/main/java/org/example/img/character/main_char_left.png"));
            starImage = ImageIO.read(new File("src/main/java/org/example/img/star/star.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        congratulation = new JTextField("Congratulation!");

        congratulation.setFont(levelupFont);
        congratulation.setForeground(Color.black);
        congratulation.setEditable(false);
        congratulation.setOpaque(false); // 배경색 투명하게 만들기
        congratulation.setBorder(null); // 텍스트 필드 테두리 삭제
        congratulation.setBounds(450, 280, 500, 60);

        //levelLabel 텍스트
        levelLabel = new JLabel("LEVEL ");
        levelLabel.setFont(levelupFont);
        levelLabel.setForeground(Color.black);
        levelLabel.setBounds(450, 390, 200, 60);
        add(levelLabel);

        add(congratulation);
        add(levelLabel);

        //star 이미지 삽입
        Image scaledStarImage = starImage.getScaledInstance(147, 120, Image.SCALE_SMOOTH);
        starLabel = new JLabel(new ImageIcon(scaledStarImage));
        starLabel.setBounds(575, 360, 147, 120);
        add(starLabel);

        upLabel = new JLabel(" UP");
        upLabel.setFont(levelupFont);
        upLabel.setForeground(Color.black);
        upLabel.setBounds(650, 390, 100, 60);
        add(upLabel);


        if(GameManager.currentCycleCount == GameManager.maxCycleCount - 1) {
            levelLabel.setVisible(false);
            upLabel.setVisible(false);
            starLabel.setVisible(false);
            JTextField lastBonus = new JTextField("LAST BONUS!");
            lastBonus.setFont(levelupFont);
            lastBonus.setForeground(Color.black);
            lastBonus.setEditable(false);
            lastBonus.setOpaque(false); // 배경색 투명하게 만들기
            lastBonus.setBorder(null); // 텍스트 필드 테두리 삭제
            levelLabel.setBounds(450, 390, 200, 60);
            add(lastBonus);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(levelupchar, 130, 220, 210,300, null);
    }

    public void playLevelUpPanelSound() {
        if (!isSoundPlaying) {
            isSoundPlaying = true;
            sound = new Thread(() -> {
                try (FileInputStream fis = new FileInputStream("src/main/java/org/example/audio/MP_Ta Da.mp3")) {
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
}