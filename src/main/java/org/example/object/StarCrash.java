package org.example.object;

import org.example.Manager.GameManager;
import org.example.panels.BonusPanel;
import org.example.panels.StarPanel;

public class StarCrash {
    private final GameManager gameManager;
    private StarPanel starPanel;

    public StarCrash(GameManager gameManager, StarPanel starPanel) {
        this.gameManager = gameManager;
        this.starPanel = starPanel; // StarPanel을 받아서 초기화
    }


    public boolean checkCollision() {
        // starPanel이 null이 아닌지 확인한 후 getBounds() 호출
        if (starPanel != null && starPanel.starplayer != null) {
            return gameManager.star.getBounds().intersects(starPanel.starplayer.getBounds());
        }
        return false;  // starPanel 또는 starplayer가 null이면 충돌 검사하지 않음
    }

    public void handleCollision() {
        // Star 이미지 숨기기
        gameManager.star.setVisible(false);

        // GameManager에서 BonusPanel을 가져와서 bonusColor() 호출
        BonusPanel bonusPanel = (BonusPanel) GameManager.getPanel("bonus");  // "bonus" 패널을 가져옵니다.
        if (bonusPanel != null) {
            bonusPanel.bonusColor();  // bonusColor() 호출
        }
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setStarPanel(StarPanel starPanel) {
        this.starPanel = starPanel;
    }
}