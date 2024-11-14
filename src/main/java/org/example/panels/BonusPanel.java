package org.example.panels;

import org.example.entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BonusPanel extends JPanel {
    private Player bonusplayer;
    private final Image largecoin, medicoin, smallcoin;


    int centerX = 1080 / 2 - 15; // 패널 중앙 X 좌표
    int centerY = 720 / 2 - 15; // 패널 중앙 Y 좌표

    public BonusPanel() {
        // 화면 구성 초기화
        setLayout(null); // 절대 레이아웃 사용
        bonusplayer = new Player(500, 500, 100, 100);

        try {
            largecoin = ImageIO.read(new File("src/main/java/org/example/img/coin/coin.png"));
            medicoin = ImageIO.read(new File("src/main/java/org/example/img/coin/medicoin.png"));
            smallcoin = ImageIO.read(new File("src/main/java/org/example/img/coin/smallcoin.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.decode("#B0BABA"));
        g.fillRect(0, 0, getWidth(), getHeight());
        // 보너스 플레이어 및 코인 그리기
        bonusplayer.draw(g);

        g.drawImage(largecoin, centerX, centerY + 90, 30, 30, this);
        g.drawImage(largecoin, centerX, centerY + 130, 30, 30, this);
        g.drawImage(largecoin, centerX + 60, centerY + 75, 30, 30, this);
        g.drawImage(largecoin, centerX - 60, centerY + 75, 30, 30, this);
        g.drawImage(largecoin, centerX + 60, centerY + 115, 30, 30, this);
        g.drawImage(largecoin, centerX - 60, centerY + 115, 30, 30, this);
        g.drawImage(largecoin, centerX - 30, centerY + 150, 30, 30, this);
        g.drawImage(largecoin, centerX + 30, centerY + 150, 30, 30, this);
        g.drawImage(largecoin, centerX, centerY + 170, 30, 30, this);
        g.drawImage(medicoin, centerX - 30, centerY + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 30, centerY + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 90, centerY + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 90, centerY + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 90, centerY + 95, 30, 30, this);
        g.drawImage(medicoin, centerX - 30, centerY + 100, 30, 30, this);
        g.drawImage(medicoin, centerX + 30, centerY + 100, 30, 30, this);
        g.drawImage(medicoin, centerX - 90, centerY + 95, 30, 30, this);
        g.drawImage(medicoin, centerX + 90, centerY + 130, 30, 30, this);
        g.drawImage(medicoin, centerX - 90, centerY + 130, 30, 30, this);
        g.drawImage(medicoin, centerX + 60, centerY + 160, 30, 30, this);
        g.drawImage(medicoin, centerX - 60, centerY + 160, 30, 30, this);
        g.drawImage(medicoin, centerX - 30, centerY + 190, 30, 30, this);
        g.drawImage(medicoin, centerX + 30, centerY + 190, 30, 30, this);
        g.drawImage(smallcoin, centerX - 60, centerY + 30, 30,30,this);
        g.drawImage(smallcoin, centerX + 60, centerY+ 30, 30,30,this);
        g.drawImage(smallcoin, centerX + 115, centerY + 95, 30,30,this);
        g.drawImage(smallcoin, centerX - 115, centerY + 95, 30,30,this);
        g.drawImage(smallcoin, centerX, centerY + 220, 30,30,this);
        //좌측 상단 하트
        g.drawImage(largecoin, centerX - 220, centerY - 240 + 90, 30, 30, this);
        g.drawImage(largecoin, centerX - 220, centerY - 240 + 130, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 + 60, centerY - 240 + 75, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 - 60, centerY - 240 + 75, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 + 60, centerY - 240 + 115, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 - 60, centerY - 240 + 115, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 - 30, centerY - 240 + 150, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 + 30, centerY - 240 + 150, 30, 30, this);
        g.drawImage(largecoin, centerX - 220, centerY - 240 + 170, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 30, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 30, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 90, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 90, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 90, centerY - 240 + 95, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 30, centerY - 240 + 100, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 30, centerY - 240 + 100, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 90, centerY - 240 + 95, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 90, centerY - 240 + 130, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 90, centerY - 240 + 130, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 60, centerY - 240 + 160, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 60, centerY - 240 + 160, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 30, centerY - 240 + 190, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 30, centerY - 240 + 190, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220 - 60, centerY - 240 + 30, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220 + 60, centerY - 240 + 30, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220 + 115, centerY - 240 + 95, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220 - 115, centerY - 240 + 95, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220, centerY - 240 + 220, 30, 30, this);
        //우측 상단 하트
        g.drawImage(largecoin, centerX + 220, centerY - 240 + 90, 30, 30, this);
        g.drawImage(largecoin, centerX + 220, centerY - 240 + 130, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 + 60, centerY - 240 + 75, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 - 60, centerY - 240 + 75, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 + 60, centerY - 240 + 115, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 - 60, centerY - 240 + 115, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 - 30, centerY - 240 + 150, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 + 30, centerY - 240 + 150, 30, 30, this);
        g.drawImage(largecoin, centerX + 220, centerY - 240 + 170, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 30, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 30, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 90, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 90, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 90, centerY - 240 + 95, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 30, centerY - 240 + 100, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 30, centerY - 240 + 100, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 90, centerY - 240 + 95, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 90, centerY - 240 + 130, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 90, centerY - 240 + 130, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 60, centerY - 240 + 160, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 60, centerY - 240 + 160, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 30, centerY - 240 + 190, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 30, centerY - 240 + 190, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220 - 60, centerY - 240 + 30, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220 + 60, centerY - 240 + 30, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220 + 115, centerY - 240 + 95, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220 - 115, centerY - 240 + 95, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220, centerY - 240 + 220, 30, 30, this);

    }

}