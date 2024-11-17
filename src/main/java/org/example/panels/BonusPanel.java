package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.entity.Coin;
import org.example.entity.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BonusPanel extends JPanel {
    public Player bonusplayer;
    // 코인 이미지와 좌표 리스트
    public List<Coin> largeCoins;
    public List<Coin> mediCoins;
    public List<Coin> smallCoins;
    private List<int[]> largeCoinPositions; // largecoin 좌표
    private List<int[]> medicoinPositions; // medicoin 좌표
    private List<int[]> smallcoinPositions; // smallcoin 좌표
    private Timer timer;

    private static Image coinimg;

    public BonusPanel() {
        // 화면 구성 초기화
        setLayout(null); // 절대 레이아웃 사용
        setPreferredSize(new Dimension(1080, 720)); // 패널 크기 설정
        largeCoins = new ArrayList<>();
        mediCoins = new ArrayList<>();
        smallCoins = new ArrayList<>();
        bonusplayer = new Player(500, 100, 100, 100);

        //플레이어 방향키로 이동하느느거!!!
        setFocusable(true);
        addKeyListener(new GameKeyAdapter(bonusplayer));
        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
            }
        });

        // 패널 크기 설정 후, 반드시 레이아웃을 재계산하도록 재호출
        revalidate();
        repaint();


        setOpaque(true);

        // largecoin 위치 설정
        largeCoinPositions = List.of(
                new int[]{525, 345 + 90}, new int[]{525, 345 + 130}, new int[]{525 + 60, 345 + 75}, new int[]{525 - 60, 345 + 75}
                , new int[]{525 + 60, 345 + 115}, new int[]{525 - 60, 345 + 115}, new int[]{525 + 30, 345 + 150}, new int[]{525 - 30, 345 + 150}
                , new int[]{525 - 220, 345 - 240 + 90}, new int[]{525 - 220, 345 - 240 + 130}, new int[]{525 - 220 + 60, 345 - 240 + 75}, new int[]{525 - 220 - 60, 345 - 240 + 75}
                , new int[]{525 - 220 - 60, 345 - 240 + 115}, new int[]{525 - 220 + 60, 345 - 240 + 115}, new int[]{525 - 220 + 30, 345 - 240 + 150}, new int[]{525 - 220 - 30, 345 - 240 + 150}
                , new int[]{525 - 220, 345 - 240 + 170}, new int[]{525 + 220, 345 - 240 + 90}, new int[]{525 + 220, 345 - 240 + 130}, new int[]{525 + 220 - 60, 345 - 240 + 75}
                , new int[]{525 + 220 + 60, 345 - 240 + 75}, new int[]{525 + 220 - 60, 345 - 240 + 115}, new int[]{525 + 220 + 60, 345 - 240 + 115}, new int[]{525 + 220 + 30, 345 - 240 + 150}
                , new int[]{525 + 220 - 30, 345 - 240 + 150}, new int[]{525 + 220, 345 - 240 + 170}, new int[]{525, 345 + 170}
        );

        // medicoin 위치 설정
        medicoinPositions = List.of(
                new int[]{525 + 220 - 60, 345 - 240 + 160}, new int[]{525 + 220 + 60, 345 - 240 + 160}, new int[]{525 + 30, 345 + 190}, new int[]{525 - 30, 345 + 190}
                , new int[]{525 + 220 - 90, 345 - 240 + 130}, new int[]{525 + 220 + 90, 345 - 240 + 130}, new int[]{525 + 60, 345 + 160}, new int[]{525 - 60, 345 + 160}
                , new int[]{525 + 220 - 90, 345 - 240 + 95}, new int[]{525 + 220 + 90, 345 - 240 + 95}, new int[]{525 - 90, 345 + 130}, new int[]{525 + 90, 345 + 130}
                , new int[]{525 + 220 + 30, 345 - 240 + 100}, new int[]{525 + 220 - 30, 345 - 240 + 100}, new int[]{525 + 30, 345 + 100}, new int[]{525 - 30, 345 + 100}
                , new int[]{525 + 220 - 90, 345 - 240 + 60}, new int[]{525 + 220 + 90, 345 - 240 + 60}, new int[]{525 - 90, 345 + 95}, new int[]{525 + 90, 345 + 95}
                , new int[]{525 + 220 + 30, 345 - 240 + 60}, new int[]{525 + 220 - 30, 345 - 240 + 60}, new int[]{525 - 90, 345 + 60}, new int[]{525 + 90, 345 + 60}
                , new int[]{525 - 220 + 30, 345 - 240 + 190}, new int[]{525 - 220 - 30, 345 - 240 + 190}, new int[]{525 + 30, 345 + 60}, new int[]{525 - 30, 345 + 60}
                , new int[]{525 - 220 - 60, 345 - 240 + 160}, new int[]{525 - 220 + 60, 345 - 240 + 160}
                , new int[]{525 - 220 - 90, 345 - 240 + 130}, new int[]{525 - 220 + 90, 345 - 240 + 130}
                , new int[]{525 - 220 - 90, 345 - 240 + 95}, new int[]{525 - 220 + 90, 345 - 240 + 95}
                , new int[]{525 - 220 + 30, 345 - 240 + 100}, new int[]{525 - 220 - 30, 345 - 240 + 100}
                , new int[]{525 - 220 - 90, 345 - 240 + 60}, new int[]{525 - 220 + 90, 345 - 240 + 60}
                , new int[]{525 - 220 + 30, 345 - 240 + 60}, new int[]{525 - 220 - 30, 345 - 240 + 60}
                , new int[]{525 + 220 + 30, 345 - 240 + 190}, new int[]{525 + 220 - 30, 345 - 240 + 190}
        );
        // smallcoin 위치 설정
        smallcoinPositions = List.of(
                new int[]{525 + 60, 345+ 30}, new int[]{525 + 115, 345 + 95}, new int[]{525 - 115, 345 + 95}
                , new int[]{525, 345 + 220}, new int[]{525 - 220, 345 - 240 + 220}
                , new int[]{525 - 220 - 60, 345 - 240 + 30}, new int[]{525 - 220 + 60, 345 - 240 + 30}
                , new int[]{525 - 220 + 115, 345 - 240 + 95}, new int[]{525 - 220 - 115, 345 - 240 + 95}
                , new int[]{525 + 220 - 60, 345 - 240 + 30}, new int[]{525 + 220 + 60, 345 - 240 + 30}
                , new int[]{525 + 220 + 115, 345 - 240 + 95}, new int[]{525 + 220 - 115, 345 - 240 + 95}
                , new int[]{525 + 220, 345 - 240 + 220}, new int[]{525 - 60, 345 + 30}
        );


        // 코인 엔티티 추가
        addCoins();

        // 타이머 설정
        timer = new Timer(30, e -> {
            checkCollisions();  // CoinCrash의 checkBonusCollisions 호출
            repaint(); // 화면 갱신 -> player 움직임
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
            Coin largeCoin = new Coin(pos[0], pos[1], 30,30, Coin.loadImage("coin.png"));
            largeCoins.add(largeCoin);
        }

        // medicoin 추가
        for (int[] pos : medicoinPositions) {
            Coin medicCoin = new Coin(pos[0], pos[1], 30,30, Coin.loadImage("medicoin.png"));
            mediCoins.add(medicCoin);
        }

        // smallcoins 추가
        for (int[] pos : smallcoinPositions) {
            Coin smallCoin = new Coin(pos[0], pos[1], 30,30, Coin.loadImage("smallcoin.png"));
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