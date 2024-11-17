package org.example.object;

import org.example.Manager.GameManager;
import org.example.entity.Coin;
import org.example.entity.Entity;
import org.example.entity.Player;
import org.example.panels.BonusPanel;
import org.example.panels.RainbowPanel;
import org.example.panels.StarPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StarCrash {
    private final GameManager gameManager;
    private StarPanel starPanel;
    public boolean isCollision = false;
    public double collisionDistance = 0;
    public double distance = 0;


    public StarCrash(GameManager gameManager, StarPanel starPanel) {
        this.gameManager = gameManager;
        this.starPanel = starPanel; // StarPanel을 받아서 초기화
    }


    public boolean checkCollision() {
        // starPanel이 null이 아닌지 확인한 후 getBounds() 호출
        if (starPanel != null && starPanel.starplayer != null) {
            // 충돌 검사 전에 player의 위치를 업데이트
            int dx = 0, dy = 0;
            starPanel.starplayer.move(dx, dy);  // 위치 갱신

            int playerCenterX = starPanel.starplayer.getX() + starPanel.starplayer.getBounds().width / 2;
            int playerCenterY = starPanel.starplayer.getY() + starPanel.starplayer.getBounds().height / 2;
            int starCenterX = GameManager.star.getX() + GameManager.star.getBounds().width / 2;
            int starCenterY = GameManager.star.getY() + GameManager.star.getBounds().height / 2;

            // 두 객체 간의 거리 계산
            distance = Math.sqrt(Math.pow(playerCenterX - starCenterX, 2) + Math.pow(playerCenterY - starCenterY, 2));

            // 예를 들어, Player와 Star 간의 최소 거리(충돌 발생 범위) 설정
            collisionDistance = starPanel.starplayer.getBounds().width / 2 + GameManager.star.getBounds().width / 2;
        }
        // 거리가 충돌 범위보다 작거나 같으면 충돌로 판단
        return distance <= collisionDistance;
    }


    public void handleCollision() {
        // Star 이미지 숨기기
        gameManager.star.setVisible(false);
    }


    public GameManager getGameManager() {
        return gameManager;
    }

    public void setStarPanel(StarPanel starPanel) {
        this.starPanel = starPanel;
    }
}