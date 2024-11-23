package org.example.object;

import org.example.Manager.PointsManager;
import org.example.entity.Coin;
import org.example.entity.Entity;
import org.example.entity.Player;
import org.example.panels.BonusPanel;
import org.example.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CoinCrash {
    private PointsManager pointsManager;
    private final GamePanel gamePanel;
    private BonusPanel bonusPanel;
    ArrayList<Entity> entities;
    private static BufferedImage coinimg;

    // 배열 생성
    public CoinCrash(GamePanel gamePanel, BonusPanel bonusPanel, PointsManager pointsManager) {
        entities = new ArrayList<>();
        this.gamePanel = gamePanel;
        this.bonusPanel = bonusPanel;
        this.pointsManager = pointsManager;

    }

    //배열에 entity 저장
    public void addEntity(Entity entity) {
        entities.add(entity);
        if (entity instanceof Coin) {
            this.coinimg = (BufferedImage) ((Coin) entity).coinimg;
        }
    }

    public void clearEntities() {
        entities.clear(); // 기존 엔티티 모두 제거
    }

    // 충돌 처리 메서드
    private void handleCollision(Entity e1, Entity e2, boolean isBonus) {
        if (e1 instanceof Player && e2 instanceof Coin) {
            pointsManager.increasePoints(1);
            if (isBonus) {
                bonusPanel.updateCurpointText();
            } else {
                gamePanel.updateCurpointText();
            }
            ((Coin) e2).resetPosition();
        } else if (e1 instanceof Coin && e2 instanceof Player) {
            pointsManager.increasePoints(1);
            if (isBonus) {
                bonusPanel.updateCurpointText();
            } else {
                gamePanel.updateCurpointText();
            }
            ((Coin) e1).resetPosition();
        }
    }

    // 일반 게임 패널에서의 충돌 처리
    public void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                // 충돌 감지
                if (e1.getBounds().intersects(e2.getBounds())) {
                    handleCollision(e1, e2, false); // Bonus가 아님
                }
            }
        }
    }

    // 보너스 패널에서의 충돌 처리
    public void checkBonusCollisions() {

        if (!bonusPanel.isCoinsInitialized) {
            Coin.resetBonusCoins(); // 코인 초기화
        }

        // LargeCoins 충돌 확인
        Coin.largeCoins.removeIf(coin -> {
            if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
                pointsManager.increasePoints(1);
                bonusPanel.updateCurpointText();
                return true;  // 충돌 후 제거
            }
            return false; // 충돌 없으면 유지
        });

        // MediCoins 충돌 확인
        Coin.mediCoins.removeIf(coin -> {
            if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
                pointsManager.increasePoints(1);
                bonusPanel.updateCurpointText();
                return true;  // 충돌 후 제거
            }
            return false; // 충돌 없으면 유지
        });

        // SmallCoins 충돌 확인
        Coin.smallCoins.removeIf(coin -> {
            if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
                pointsManager.increasePoints(1);
                bonusPanel.updateCurpointText();
                return true;  // 충돌 후 제거
            }
            return false; // 충돌 없으면 유지
        });
    }


    // Coin 이미지 반환 메서드
    public static BufferedImage getCoinImage() {
        return coinimg;
    }
}



