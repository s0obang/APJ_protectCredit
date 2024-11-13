package org.example.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LevelUpPanel extends JPanel {
    private Image levelupchar;
    private Image starImage;
    private JTextField congratulation;
    private JLabel levelLabel;

    public LevelUpPanel() {
        setLayout(null);
        setBackground(Color.decode("#B0BABA"));

        congratulation = new JTextField("Congratulation!");

        Font levelupFont = new Font("Neo둥근모", Font.BOLD, 60);
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

        add(congratulation);
        add(levelLabel);

        try {
            levelupchar = ImageIO.read(
                    new File("src/main/java/org/example/img/character/main_char_left.png"));
            starImage = ImageIO.read(new File("src/main/java/org/example/img/star/star.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //star 이미지 삽입
        Image scaledStarImage = starImage.getScaledInstance(147, 120, Image.SCALE_SMOOTH);
        JLabel starLabel = new JLabel(new ImageIcon(scaledStarImage));
        starLabel.setBounds(575, 360, 147, 120);
        add(starLabel);

        JLabel upLabel = new JLabel(" UP");
        upLabel.setFont(levelupFont);
        upLabel.setForeground(Color.black);
        upLabel.setBounds(650, 390, 100, 60);
        add(upLabel);

        levelLabel.setBounds(450, 390, 200, 60);
        add(levelLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(levelupchar, 130, 220, 300,300, null);
    }
}