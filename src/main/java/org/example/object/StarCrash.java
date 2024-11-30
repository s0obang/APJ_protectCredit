package org.example.object;

import org.example.Manager.GameManager;
import org.example.panels.StarPanel;

public class StarCrash {

  private final GameManager gameManager;
  public boolean isCollision = false;
  public double collisionDistance = 0;
  public double distance = 0;
  private StarPanel starPanel;


  public StarCrash(GameManager gameManager, StarPanel starPanel) {
    this.gameManager = gameManager;
    this.starPanel = starPanel;
  }


  public boolean checkCollision() {

    if (starPanel != null && starPanel.starplayer != null) {
      // 충돌 검사 전에 player의 위치를 업데이트
      int dx = 0, dy = 0;
      starPanel.starplayer.move(dx, dy);  // 위치 갱신

      int playerCenterX = starPanel.starplayer.getX() + starPanel.starplayer.getBounds().width / 2;
      int playerCenterY = starPanel.starplayer.getY() + starPanel.starplayer.getBounds().height / 2;
      int starCenterX = GameManager.star.getX() + GameManager.star.getBounds().width / 2;
      int starCenterY = GameManager.star.getY() + GameManager.star.getBounds().height / 2;

      distance = Math.sqrt(
          Math.pow(playerCenterX - starCenterX, 2) + Math.pow(playerCenterY - starCenterY, 2));
      collisionDistance =
          starPanel.starplayer.getBounds().width / 2 + GameManager.star.getBounds().width / 2;
    }
    // 거리가 충돌 범위보다 작거나 같으면 충돌로 판단
    return distance <= collisionDistance;
  }


  public void handleCollision() {
    gameManager.star.setVisible(false);
  }


  public GameManager getGameManager() {
    return gameManager;
  }

  public void setStarPanel(StarPanel starPanel) {
    this.starPanel = starPanel;
  }
}