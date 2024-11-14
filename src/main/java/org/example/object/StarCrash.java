package org.example.object;

import org.example.Manager.GameManager;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.panels.BounsPanel;
import org.example.panels.StarPanel;

import java.awt.*;

public class StarCrash {
    private final GameManager gameManager;
    private Player player;
    private Star star;

    public StarCrash(GameManager gameManager, Player player, Star star) {
        this.gameManager = gameManager;
        this.player = player;
        this.star = star;
    }


    public boolean checkCollision() {
        // Player와 Star의 경계값 가져오기
        Rectangle playerBounds = player.getBounds();
        Rectangle starBounds = gameManager.star.getBounds();

        // 두 객체의 경계값이 겹치는지 확인
        if (playerBounds.intersects(starBounds)) {
            handleCollision();  // 충돌 발생 시 처리
            return true;  // 충돌이 발생한 경우
        }
        return false;  // 충돌이 발생하지 않으면 false 반환
    }


    public void handleCollision() {
        // Star 이미지 숨기기
        GameManager.star.setVisible(false);  // Star를 숨김
        GameManager.switchToPanelWithDelay("bonus", 0);
    }
}
