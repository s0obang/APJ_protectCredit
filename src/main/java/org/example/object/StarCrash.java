package org.example.object;

import org.example.Manager.GameManager;
import org.example.entity.Player;
import org.example.entity.Star;
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

    public void checkCollision() {
        // Player와 Star의 경계값 가져오기
        Rectangle playerBounds = player.getBounds();
        Rectangle starBounds = GameManager.star.getBounds();

        // 두 객체의 경계값이 겹치는지 확인
        if (playerBounds.intersects(starBounds)) {
            handleCollision();  // 충돌 발생 시 처리
        }
    }

    private void handleCollision() {
        // Star 이미지 숨기기
        GameManager.star.setVisible(false);  // Star를 숨김

        // BonusPanel로 전환 (색상 처리와 관련된 작업은 StarPanel에서 하도록 함)
        GameManager.switchToPanelWithDelay("bonus", 500);  // BonusPanel로 전환

        // StarPanel에서 bonusColor()를 호출하도록 해야 합니다.
        // 이 부분은 StarPanel에서 호출할 수 있도록 구조를 바꿔야 합니다.
        StarPanel starPanel = (StarPanel) GameManager.getPanel("star");
        if (starPanel != null) {
            starPanel.handleBonusColor();  // StarPanel에서 bonusColor() 호출
        }
    }
}
