package org.example.object;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import javazoom.jl.player.Player;
import org.example.Manager.PointsManager;
import org.example.entity.Coin;
import org.example.entity.Entity;
import org.example.entity.GamePlayer;
import org.example.panels.BonusPanel;
import org.example.panels.GamePanel;

public class CoinCrash {

  private static BufferedImage coinimg;
  private static Player mp3Player;
  private static InputStream fis;
  private final GamePanel gamePanel;
  ArrayList<Entity> entities;
  private PointsManager pointsManager;
  private BonusPanel bonusPanel;


  public CoinCrash(GamePanel gamePanel, BonusPanel bonusPanel, PointsManager pointsManager) {
    entities = new ArrayList<>();
    this.gamePanel = gamePanel;
    this.bonusPanel = bonusPanel;
    this.pointsManager = pointsManager;

    try {

      fis = getClass().getResourceAsStream("/audio/coin.mp3");
      mp3Player = new Player(Objects.requireNonNull(fis));
    } catch (Exception e) {
      System.err.println("오디오 파일 로딩 중 오류 발생: " + e.getMessage());
      e.printStackTrace();
    }
  }


  public static BufferedImage getCoinImage() {
    return coinimg;
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

  private void playCoinSound() {
    new Thread(() -> { // 별도의 스레드에서 재생
      try {
        fis = getClass().getResourceAsStream("/audio/coin.mp3");
        mp3Player = new Player(Objects.requireNonNull(fis));
        mp3Player.play();

      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
      }
    }).start();
  }

  // 충돌 처리 메서드
  private void handleCollision(Entity e1, Entity e2, boolean isBonus) {
    if (e1 instanceof GamePlayer && e2 instanceof Coin) {
      playCoinSound();
      pointsManager.increasePoints(1);
      if (isBonus) {
        bonusPanel.updateCurpointText();
      } else {
        gamePanel.updateCurpointText();
      }
      ((Coin) e2).resetPosition();
    } else if (e1 instanceof Coin && e2 instanceof GamePlayer) {
      playCoinSound();
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
      Coin.resetBonusCoins();
    }

    Coin.largeCoins.removeIf(coin -> {
      if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
        playCoinSound();
        pointsManager.increasePoints(1);
        bonusPanel.updateCurpointText();
        return true;  // 충돌 후 제거
      }
      return false; // 충돌 없으면 유지
    });

    Coin.mediCoins.removeIf(coin -> {
      if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
        playCoinSound();
        pointsManager.increasePoints(1);
        bonusPanel.updateCurpointText();
        return true;
      }
      return false;
    });

    Coin.smallCoins.removeIf(coin -> {
      if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
        playCoinSound();
        pointsManager.increasePoints(1);
        bonusPanel.updateCurpointText();
        return true;
      }
      return false;
    });
  }
}



