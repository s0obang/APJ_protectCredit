package org.example.object;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.example.Manager.PointsManager;
import org.example.entity.Coin;
import org.example.entity.Entity;
import org.example.entity.GamePlayer;
import org.example.panels.BonusPanel;
import org.example.panels.GamePanel;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CoinCrash {
    private PointsManager pointsManager;
    private final GamePanel gamePanel;
    private BonusPanel bonusPanel;
    ArrayList<Entity> entities;
    private static BufferedImage coinimg;
    private static Player mp3Player; // MP3 플레이어 객체
    private static FileInputStream fis; // MP3 파일 스트림

    // 배열 생성
    public CoinCrash(GamePanel gamePanel, BonusPanel bonusPanel, PointsManager pointsManager) {
        entities = new ArrayList<>();
        this.gamePanel = gamePanel;
        this.bonusPanel = bonusPanel;
        this.pointsManager = pointsManager;

        try {
            // MP3 파일을 한 번만 로드
            fis = new FileInputStream("src/main/java/org/example/audio/coin.mp3");
            mp3Player = new Player(fis);
        } catch (IOException | JavaLayerException e) {
            System.err.println("오디오 파일 로딩 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
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

    // MP3 파일 재생 메서드
    private void playCoinSound() {
        new Thread(() -> { // 별도의 스레드에서 재생
            try {
                fis = new FileInputStream("src/main/java/org/example/audio/coin.mp3"); // 새로운 스트림 생성
                    mp3Player = new Player(fis); // Player 새로 생성
                    mp3Player.play(); // MP3 파일 재생

            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();  // 오류에 대한 추가 정보 출력
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
            Coin.resetBonusCoins(); // 코인 초기화
        }

        // LargeCoins 충돌 확인
        Coin.largeCoins.removeIf(coin -> {
            if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
                playCoinSound();
                pointsManager.increasePoints(1);
                bonusPanel.updateCurpointText();
                return true;  // 충돌 후 제거
            }
            return false; // 충돌 없으면 유지
        });

        // MediCoins 충돌 확인
        Coin.mediCoins.removeIf(coin -> {
            if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
                playCoinSound();
                pointsManager.increasePoints(1);
                bonusPanel.updateCurpointText();
                return true;  // 충돌 후 제거
            }
            return false; // 충돌 없으면 유지
        });

        // SmallCoins 충돌 확인
        Coin.smallCoins.removeIf(coin -> {
            if (bonusPanel.bonusplayer.getBounds().intersects(coin.getBounds())) {
                playCoinSound();
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



