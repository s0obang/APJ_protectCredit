package org.example.panels;

import org.example.Manager.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LevelUpPanel extends JPanel {
    private Image levelupchar;
    private Image starImage;
    private JTextField congratulation;
    private JLabel levelLabel, upLabel, starLabel;

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
        g.drawImage(levelupchar, 130, 220, 300,300, null);
    }
}