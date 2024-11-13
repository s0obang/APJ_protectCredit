package org.example.panels;

import org.example.Manager.GameManager;
import org.example.entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BounsPanel extends JPanel {
    private GameManager gm;
    private Image largecoin, medicoin, smallcoin;
    private Player bonusplayer;

    public BounsPanel() {
        //보너스 타임! 이라는 패널을 3초 간 띄우고 사라지게 한 후 코인 먹기
        
        bonusplayer = new Player(540, 600, 100, 100);

        //코인들과 충돌함수를 CoinCrash에서 어떻게 해결할 수 있을지 지피티한테 물어보기
        //player의 위치와 points를 그대로 가져오는 방법도 지피티한테 물어보기 근데 이건
        //참조변수를 써서 가져오면 될 것 같기도 함

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
        setBackground(Color.decode("#B0BABA"));
        // 하트 형태를 만들기 위해 코인 이미지를 배치
        int centerX = 1080 / 2 - 15; // 패널 중앙 X 좌표
        int centerY = 720 / 2 - 15; // 패널 중앙 Y 좌표

        // 중앙 하트
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

