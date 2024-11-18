package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.Manager.PointsManager;
import org.example.entity.Coin;
import org.example.entity.Player;
import org.example.object.CoinCrash;

import javax.swing.*;
import java.awt.*;

public class BonusPanel extends JPanel {
    private PointsManager pointsManager; // PointsManager 추가
    private JTextField curpointText; // BonusPanel의 JTextField
    public CoinCrash coinCrash;
    public Player bonusplayer;
    public boolean isCoinsInitialized = false; // 코인 초기화 여부 플래그
    private Timer timer;


    public BonusPanel(PointsManager pointsManager) {
        this.pointsManager = pointsManager;
        coinCrash = new CoinCrash(null,this, pointsManager);
        // 텍스트 필드 초기화
        curpointText = createPointsTextField();
        setLayout(null);
        this.add(curpointText);

        setPreferredSize(new Dimension(1080, 720)); // 패널 크기 설정
        bonusplayer = new Player(500, 100, 100, 100);

        //플레이어 방향키로 이동하느느거!!!
        setFocusable(true);
        addKeyListener(new GameKeyAdapter(bonusplayer));
        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
            }
        });


        setOpaque(true);

        // 타이머 설정
        timer = new Timer(30, e -> {
            if(!isCoinsInitialized) {
                Coin.resetBonusCoins(); // 패널이 표시될 때마다 코인 초기화
                isCoinsInitialized = true; // 초기화 플래그 설정
                coinCrash.checkBonusCollisions();  // CoinCrash의 checkBonusCollisions 호출
                repaint(); // 화면 갱신 -> player 움직임
            }
            coinCrash.checkBonusCollisions();
            repaint();
        });
        timer.start(); // Start the timer

    }

    private JTextField createPointsTextField() {
        JTextField textField = new JTextField(pointsManager.getPoints() + "만 원");
        textField.setFont(new Font("Neo둥근모", Font.BOLD, 20));
        textField.setForeground(Color.black);
        textField.setEditable(false);
        textField.setOpaque(false);
        textField.setFocusable(false);
        textField.setBorder(null);
        textField.setBounds(85, 93, 150, 30);
        return textField;
    }


    public void updateCurpointText() {
        curpointText.setText(pointsManager.getPoints() + "만 원");
        repaint();
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

        // 좌측 상단에 띄울 코인 이미지 그리기
        if (CoinCrash.getCoinImage() != null) {
            g.drawImage(CoinCrash.getCoinImage(), 50, 90, 30, 30, null);
        }
    }

}