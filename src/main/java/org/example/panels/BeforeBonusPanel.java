package org.example.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.io.IOException;

import static org.example.Manager.GameManager.bonusPanel;

public class BeforeBonusPanel extends JPanel {
    private JTextField bonustime;
    private Image backgroundImage, coinImage;
    private JLabel leftcoinLabel, rightcoinLabel;

    public BeforeBonusPanel() {
        setLayout(null);

        // 배경 이미지 로드
        try {
            backgroundImage = ImageIO.read(
                    new File("src/main/java/org/example/img/backgrounds/etcback.jpg"));
            coinImage = ImageIO.read(new File ("src/main/java/org/example/img/coin/coin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bonustime = new JTextField("!BONUS TIME!");

        Font bonusFont = new Font("Neo둥근모", Font.BOLD, 60);
        bonustime.setFont(bonusFont);
        bonustime.setForeground(Color.black);
        bonustime.setEditable(false);
        bonustime.setOpaque(false); // 배경색 투명하게 만들기
        bonustime.setBorder(null); // 텍스트 필드 테두리 삭제
        bonustime.setBounds(320, 300, 500, 80);

        add(bonustime);

        //star 이미지 삽입
        Image scaledStarImage = coinImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        leftcoinLabel = new JLabel(new ImageIcon(scaledStarImage));
        leftcoinLabel.setBounds(275, 310, 50, 50);
        rightcoinLabel = new JLabel(new ImageIcon(scaledStarImage));
        rightcoinLabel.setBounds(700, 310, 50, 50);
        add(leftcoinLabel);
        add(rightcoinLabel);

    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            bonusPanel.playbonusPanelSound();
        } else {
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
