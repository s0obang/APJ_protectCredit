package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.entity.Coin;
import org.example.entity.Player;
import org.example.object.CoinCrash;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BonusPanel extends JPanel {
    public Player bonusplayer;

    public List<Coin> largeCoins;
    public List<Coin> mediCoins;
    public List<Coin> smallCoins;
    // 코인 이미지와 좌표 리스트
    private List<int[]> largeCoinPositions; // largecoin 좌표
    private List<int[]> medicoinPositions; // medicoin 좌표
    private List<int[]> smallcoinPositions; // smallcoin 좌표
    private Timer timer;

    int centerX = 1080 / 2 - 15; // 패널 중앙 X 좌표
    int centerY = 720 / 2 - 15; // 패널 중앙 Y 좌표

    public BonusPanel() {
        // 화면 구성 초기화
        setLayout(null); // 절대 레이아웃 사용
        setPreferredSize(new Dimension(1080, 720)); // 패널 크기 설정
        largeCoins = new ArrayList<>();
        mediCoins = new ArrayList<>();
        smallCoins = new ArrayList<>();
        bonusplayer = new Player(500, 500, 100, 100);

        //플레이어 방향키로 이동하느느거!!!
        setFocusable(true);
        addKeyListener(new GameKeyAdapter(bonusplayer));
        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
            }
        });

        setPreferredSize(new Dimension(1080, 720));
        setOpaque(true);

        // largecoin 위치 설정
        largeCoinPositions = List.of(
                new int[]{centerX, centerY + 90}, new int[]{centerX, centerY + 130}, new int[]{centerX + 60, centerY + 75}, new int[]{centerX - 60, centerY + 75}
                , new int[]{centerX + 60, centerY + 115}, new int[]{centerX - 60, centerY + 115}, new int[]{centerX + 30, centerY + 150}, new int[]{centerX - 30, centerY + 150}
                , new int[]{centerX - 220, centerY - 240 + 90}, new int[]{centerX - 220, centerY - 240 + 130}, new int[]{centerX - 220 + 60, centerY - 240 + 75}, new int[]{centerX - 220 - 60, centerY - 240 + 75}
                , new int[]{centerX - 220 - 60, centerY - 240 + 115}, new int[]{centerX - 220 + 60, centerY - 240 + 115}, new int[]{centerX - 220 + 30, centerY - 240 + 150}, new int[]{centerX - 220 - 30, centerY - 240 + 150}
                , new int[]{centerX - 220, centerY - 240 + 170}, new int[]{centerX + 220, centerY - 240 + 90}, new int[]{centerX + 220, centerY - 240 + 130}, new int[]{centerX + 220 - 60, centerY - 240 + 75}
                , new int[]{centerX + 220 + 60, centerY - 240 + 75}, new int[]{centerX + 220 - 60, centerY - 240 + 115}, new int[]{centerX + 220 + 60, centerY - 240 + 115}, new int[]{centerX + 220 + 30, centerY - 240 + 150}
                , new int[]{centerX + 220 - 30, centerY - 240 + 150}, new int[]{centerX + 220, centerY - 240 + 170}, new int[]{centerX, centerY + 170}
        );

        // medicoin 위치 설정
        medicoinPositions = List.of(
                new int[]{centerX + 220 - 60, centerY - 240 + 160}, new int[]{centerX + 220 + 60, centerY - 240 + 160}, new int[]{centerX + 30, centerY + 190}, new int[]{centerX - 30, centerY + 190}
                , new int[]{centerX + 220 - 90, centerY - 240 + 130}, new int[]{centerX + 220 + 90, centerY - 240 + 130}, new int[]{centerX + 60, centerY + 160}, new int[]{centerX - 60, centerY + 160}
                , new int[]{centerX + 220 - 90, centerY - 240 + 95}, new int[]{centerX + 220 + 90, centerY - 240 + 95}, new int[]{centerX - 90, centerY + 130}, new int[]{centerX + 90, centerY + 130}
                , new int[]{centerX + 220 + 30, centerY - 240 + 100}, new int[]{centerX + 220 - 30, centerY - 240 + 100}, new int[]{centerX + 30, centerY + 100}, new int[]{centerX - 30, centerY + 100}
                , new int[]{centerX + 220 - 90, centerY - 240 + 60}, new int[]{centerX + 220 + 90, centerY - 240 + 60}, new int[]{centerX - 90, centerY + 95}, new int[]{centerX + 90, centerY + 95}
                , new int[]{centerX + 220 + 30, centerY - 240 + 60}, new int[]{centerX + 220 - 30, centerY - 240 + 60}, new int[]{centerX - 90, centerY + 60}, new int[]{centerX + 90, centerY + 60}
                , new int[]{centerX - 220 + 30, centerY - 240 + 190}, new int[]{centerX - 220 - 30, centerY - 240 + 190}, new int[]{centerX + 30, centerY + 60}, new int[]{centerX - 30, centerY + 60}
                , new int[]{centerX - 220 - 60, centerY - 240 + 160}, new int[]{centerX - 220 + 60, centerY - 240 + 160}
                , new int[]{centerX - 220 - 90, centerY - 240 + 130}, new int[]{centerX - 220 + 90, centerY - 240 + 130}
                , new int[]{centerX - 220 - 90, centerY - 240 + 95}, new int[]{centerX - 220 + 90, centerY - 240 + 95}
                , new int[]{centerX - 220 + 30, centerY - 240 + 100}, new int[]{centerX - 220 - 30, centerY - 240 + 100}
                , new int[]{centerX - 220 - 90, centerY - 240 + 60}, new int[]{centerX - 220 + 90, centerY - 240 + 60}
                , new int[]{centerX - 220 + 30, centerY - 240 + 60}, new int[]{centerX - 220 - 30, centerY - 240 + 60}
                , new int[]{centerX + 220 + 30, centerY - 240 + 190}, new int[]{centerX + 220 - 30, centerY - 240 + 190}
        );
        // smallcoin 위치 설정
        smallcoinPositions = List.of(
                new int[]{centerX + 60, centerY+ 30}, new int[]{centerX + 115, centerY + 95}, new int[]{centerX - 115, centerY + 95}
                , new int[]{centerX, centerY + 220}, new int[]{centerX - 220, centerY - 240 + 220, 30}
                , new int[]{centerX - 220 - 60, centerY - 240 + 30}, new int[]{centerX - 220 + 60, centerY - 240 + 30}
                , new int[]{centerX - 220 + 115, centerY - 240 + 95}, new int[]{centerX - 220 - 115, centerY - 240 + 95}
                , new int[]{centerX + 220 - 60, centerY - 240 + 30}, new int[]{centerX + 220 + 60, centerY - 240 + 30}
                , new int[]{centerX + 220 + 115, centerY - 240 + 95}, new int[]{centerX + 220 - 115, centerY - 240 + 95}
                , new int[]{centerX + 220, centerY - 240 + 220}, new int[]{centerX - 60, centerY + 30}
        );


        // 코인 엔티티 추가
        addCoins();

        // 타이머 설정
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCollisions();  // CoinCrash의 checkBonusCollisions 호출
                repaint(); // 화면 갱신 -> player 움직임
            }
        });
        timer.start(); // Start the timer
    }

    // 충돌 검사 메서드
    private void checkCollisions() {
        // Player와 LargeCoin, MediCoin, SmallCoin 간의 충돌 확인 후 해당 코인 제거
        largeCoins.removeIf(coin -> bonusplayer.getBounds().intersects(coin.getBounds()));
        mediCoins.removeIf(coin -> bonusplayer.getBounds().intersects(coin.getBounds()));
        smallCoins.removeIf(coin -> bonusplayer.getBounds().intersects(coin.getBounds()));
    }

    // 각 코인 리스트에 맞는 코인 객체 추가
    public void addCoins() {
        largeCoins.clear();
        mediCoins.clear();
        smallCoins.clear();

        // largecoins 추가
        for (int[] pos : largeCoinPositions) {
            Coin largeCoin = new Coin(pos[0], pos[1], 15,15);
            largeCoins.add(largeCoin);
        }

        // medicoin 추가
        for (int[] pos : medicoinPositions) {
            Coin medicCoin = new Coin(pos[0], pos[1], 15,15);
            mediCoins.add(medicCoin);
        }

        // smallcoins 추가
        for (int[] pos : smallcoinPositions) {
            Coin smallCoin = new Coin(pos[0], pos[1], 15,15);
            smallCoins.add(smallCoin);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.decode("#B0BABA"));
        g.fillRect(0, 0, getWidth(), getHeight());
        // 보너스 플레이어 및 코인 그리기
        bonusplayer.draw(g);

        // 각 코인들을 화면에 그리기
        for (Coin coin : largeCoins) {
            coin.draw(g);
        }
        for (Coin coin : mediCoins) {
            coin.draw(g);
        }
        for (Coin coin : smallCoins) {
            coin.draw(g);
        }
    }

}