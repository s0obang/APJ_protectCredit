package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.entity.Coin;
import org.example.entity.Player;
import org.example.object.CoinCrash;

import javax.swing.*;
import java.awt.*;

public class BonusPanel extends JPanel {
    public GamePanel gamePanel;
    public CoinCrash coinCrash;
    public Player bonusplayer;
    public boolean isCoinsInitialized = false; // 코인 초기화 여부 플래그
    private Timer timer;

    private static Image coinimg;

    public BonusPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        coinCrash = new CoinCrash(gamePanel,this);
        // 화면 구성 초기화
        setLayout(null); // 절대 레이아웃 사용
        setPreferredSize(new Dimension(1080, 720)); // 패널 크기 설정
        bonusplayer = new Player(500, 100, 100, 100);

        //플레이어 방향키로 이동하느느거!!!
        setFocusable(true);
        addKeyListener(new GameKeyAdapter(bonusplayer));
        addHierarchyListener(e -> {
            if (isShowing()) {
                Coin.resetBonusCoins(); // 패널이 표시될 때마다 코인 초기화
                isCoinsInitialized = true; // 초기화 플래그 설정
                requestFocusInWindow();
            }
        });

        setOpaque(true);

        // 타이머 설정
        timer = new Timer(30, e -> {
            coinCrash.checkBonusCollisions();  // CoinCrash의 checkBonusCollisions 호출
            repaint(); // 화면 갱신 -> player 움직임
        });
        timer.start(); // Start the timer
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.decode("#B0BABA"));
        g.fillRect(0, 0, getWidth(), getHeight());
        // 보너스 플레이어 및 코인 그리기
        bonusplayer.draw(g);

        // 각 코인들을 화면에 그리기
        for (Coin coin : Coin.largeCoins) {
            coin.draw(g);
        }
        for (Coin coin : Coin.mediCoins) {
            coin.draw(g);
        }
        for (Coin coin : Coin.smallCoins) {
            coin.draw(g);
        }
    }

}