package org.example.panels;

import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BackPanel extends JPanel {
    private Image backgroundImage, levelupchar, bookImage;
    private JLabel LetLabel, BackLabel, bookLabel;
    public void BackPanel() {
        setLayout(null);
        // 배경 이미지 로드
        try {
            backgroundImage = ImageIO.read(
                    new File("src/main/java/org/example/img/backgrounds/etcback.jpg"));
            levelupchar = ImageIO.read(
                    new File("src/main/java/org/example/img/character/main_char_left.png"));
            bookImage = ImageIO.read(
                    new File("src/main/java/org/example/img/gradeItem/textBook.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Font levelupFont = new Font("Neo둥근모", Font.BOLD, 60);

        LetLabel = new JLabel("Let's");
        LetLabel.setFont(levelupFont);
        LetLabel.setForeground(Color.black);
        LetLabel.setBounds(200, 280, 500, 60);

        BackLabel = new JLabel("Back!");
        BackLabel.setFont(levelupFont);
        BackLabel.setForeground(Color.black);
        BackLabel.setBounds(200, 390, 200, 60);

        add(LetLabel);
        add(BackLabel);

        Image scaledStarImage = bookImage.getScaledInstance(147, 120, Image.SCALE_SMOOTH);
        bookLabel = new JLabel(new ImageIcon(scaledStarImage));
        bookLabel.setBounds(250, 250, 147, 120);
        add(bookLabel);
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
        g.drawImage(levelupchar, 270, 220, 210,300, null);
    }
}

