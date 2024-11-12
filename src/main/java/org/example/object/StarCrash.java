package org.example.object;

import org.example.Manager.GameManager;
import org.example.entity.Player;
import org.example.entity.Star;

import java.awt.*;

public class StarCrash {

    private GameManager gm;
    private Player player;
    private Star star;

    public StarCrash(GameManager gm, Player player, Star star) {
        this.gm = gm;
        this.player = player;
        this.star = star;
    }

    // player와 star가 충돌하는지 확인하는 메서드
    public void checkCollision() {

        // 두 객체의 bounding box를 사용하여 겹침을 확인합니다.
        Rectangle playerBounds = player.getBounds();
        Rectangle starBounds = star.getBounds();

        // 두 사각형이 겹치는지 확인
        if (playerBounds.intersects(starBounds)) {
            handleCollision();
        }
    }

    // 충돌 발생 시 실행할 행동 정의
    private void handleCollision() {
        gm.switchToPanelWithDelay("bonus", 500);
        star.setVisible(false); // star가 보이지 않게 설정
    }
}
