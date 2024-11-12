package org.example.panels;

import org.example.Manager.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BounsPanel extends JPanel {
    private GameManager gm;
    private Image largecoin, medicoin, smallcoin;

    public BounsPanel() {
        setBackground(Color.decode("#B0BABA"));

        try {
            largecoin = ImageIO.read(new File("src/main/java/org/example/img/coin/coin.png"));
            medicoin = ImageIO.read(new File("src/main/java/org/example/img/coin/medicoin.png"));
            smallcoin = ImageIO.read(new File("src/main/java/org/example/img/coin/smallcoin.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        //시간 지나면 게임 패널로 돌아가는 메서드 지피티한테 물어보기
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 하트 형태를 만들기 위해 코인 이미지를 배치
        int centerX = 1080 / 2 - 15; // 패널 중앙 X 좌표
        int centerY = 720 / 2 - 15; // 패널 중앙 Y 좌표

        // 하트 모양을 구성하는 코인 배치
        // 예시로, 7개의 코인 이미지로 하트 모양을 만들어봄
        // 아래와 같은 좌표 배치로 하트 모양을 만들 수 있습니다.

        // 중앙 상단 하트
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

