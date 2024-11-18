package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.entity.Coin;
import org.example.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BonusPanel extends JPanel {
    public Player bonusplayer;
    // 코인 이미지와 좌표 리스트
    public List<Coin> largeCoins;
    public List<Coin> mediCoins;
    public List<Coin> smallCoins;
    private boolean isCoinsInitialized = false; // 코인 초기화 여부 플래그
    private Timer timer;

    private static Image coinimg;

    public BonusPanel() {
        // 화면 구성 초기화
        setLayout(null); // 절대 레이아웃 사용
        setPreferredSize(new Dimension(1080, 720)); // 패널 크기 설정
        bonusplayer = new Player(500, 100, 100, 100);

        //플레이어 방향키로 이동하느느거!!!
        setFocusable(true);
        addKeyListener(new GameKeyAdapter(bonusplayer));
        addHierarchyListener(e -> {
            if (isShowing()) {
                resetCoins(); // 패널이 표시될 때마다 코인 초기화
                isCoinsInitialized = true; // 초기화 플래그 설정
                requestFocusInWindow();
            }
        });

        setOpaque(true);

        // 타이머 설정
        timer = new Timer(30, e -> {
            checkBonusCollisions();  // CoinCrash의 checkBonusCollisions 호출
            repaint(); // 화면 갱신 -> player 움직임
        });
        timer.start(); // Start the timer
    }

    private void resetCoins() {
        // 코인 리스트 초기화
        largeCoins = Coin.createCoins(Coin.largeCoinPositions, "coin.png");
        mediCoins = Coin.createCoins(Coin.medicoinPositions, "medicoin.png");
        smallCoins = Coin.createCoins(Coin.smallcoinPositions, "smallcoin.png");
    }

    // 충돌 검사 메서드
    private void checkBonusCollisions() {
        if(isCoinsInitialized == false) resetCoins();

        // Player와 LargeCoin, MediCoin, SmallCoin 간의 충돌 확인 후 해당 코인 제거
        largeCoins.removeIf(coin -> bonusplayer.getBounds().intersects(coin.getBounds()));
        mediCoins.removeIf(coin -> bonusplayer.getBounds().intersects(coin.getBounds()));
        smallCoins.removeIf(coin -> bonusplayer.getBounds().intersects(coin.getBounds()));
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