package org.example.object;

import org.example.entity.Coin;
import org.example.entity.Entity;
import org.example.entity.Player;
import org.example.panels.BonusPanel;
import org.example.panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CoinCrash {
    private final GamePanel gamePanel;
    private BonusPanel bonusPanel;
    ArrayList<Entity> entities;
    private static BufferedImage coinimg;

    // 배열 생성
    public CoinCrash(GamePanel gamePanel, BonusPanel bonusPanel) {
        entities = new ArrayList<>();
        this.gamePanel = gamePanel;
        this.bonusPanel = bonusPanel;

    }

    //배열에 entity 저장
    public void addEntity(Entity entity) {
        entities.add(entity);
        if (entity instanceof Coin) {
            this.coinimg = (BufferedImage) ((Coin) entity).coinimg;
        }
    }

    // 누적 금액 패널에 띄우는 함수
    private void showCurPoints(int points) {
        gamePanel.curpointText.setText(points + "만 원");
        gamePanel.repaint();
    }


    // 충돌 관련
    public void checkCollisions() {
        System.out.println("Checking collisions...");
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                // 충돌 감지 및 처리
                if (e1.getBounds().intersects(e2.getBounds())) {
                    if (e1 instanceof Player && e2 instanceof Coin) {
                        System.out.println("Player collided with Coin");
                        (e1).upPoint(1); // 플레이어 점수 증가
                        showCurPoints(e1.getPoints()); // 누적 금액 패널에 갱신
                        ((Coin) e2).resetPosition();// 코인을 초기 위치로 리셋

                    }
                    // e1이 Coin1이고 e2가 Player일 경우
                    else if (e1 instanceof Coin && e2 instanceof Player) {
                        (e2).upPoint(1); // 플레이어 점수 증가
                        showCurPoints(e2.getPoints());
                        ((Coin) e1).resetPosition(); // 코인을 초기 위치로 리셋
                    }
                }
            }
        }
    }

    // 충돌 검사 메서드
    public void checkBonusCollisions() {
        if(!bonusPanel.isCoinsInitialized) Coin.resetBonusCoins();

        // Player와 LargeCoin, MediCoin, SmallCoin 간의 충돌 확인 후 해당 코인 제거
        Coin.largeCoins.removeIf(coin -> bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds()));
        Coin.mediCoins.removeIf(coin -> bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds()));
        Coin.smallCoins.removeIf(coin -> bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds()));
    }


    // Coin 이미지 반환 메서드
    public static BufferedImage getCoinImage() {
        return coinimg;
    }
}



