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
    ArrayList<Entity> entities;
    private JTextField curpointText;
    private static BufferedImage coinimg;

    // 배열 생성
    public CoinCrash(GamePanel gamePanel) {
        entities = new ArrayList<>();
        this.gamePanel = gamePanel;

        // 누적 금액 표시하는 텍스트 필드 생성
        curpointText = new JTextField("0원"); // Initial points display
        Font scoreFont = new Font("Neo둥근모", Font.BOLD, 15);
        curpointText.setFont(scoreFont);
        curpointText.setForeground(Color.black);
        curpointText.setEditable(false);
        curpointText.setOpaque(false); // 배경색 투명하게 만들기
        curpointText.setBorder(null); // 텍스트 필드 테두리 삭제

        curpointText.setBounds(82, 87, 150, 30);
        gamePanel.setLayout(null);
        gamePanel.add(curpointText); // 게임 패널에 추가

        // gamePanel에 CoinCrash 인스턴스 설정
        setCoinCrash();
    }

    // CoinCrash 인스턴스를 GamePanel에 설정하는 메서드
    private void setCoinCrash() {
        gamePanel.coincrash = this; // GamePanel의 coinCrash에 현재 CoinCrash 설정
    }
    //배열에 entity 저장
    public void addEntity(Entity entity) {
        entities.add(entity);
        if (entity instanceof Coin) {
            this.coinimg = (BufferedImage) ((Coin) entity).coinimg;
        }
    }

    // 누적 금액 패널에 띄우는 함수
    private void showcurpoints(int points) {
        curpointText.setText(points + "만 원");
        gamePanel.repaint();
    }

    // 충돌 관련
    public void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                // 충돌 감지 및 처리
                if (e1.getBounds().intersects(e2.getBounds())) {
                    if (e1 instanceof Player && e2 instanceof Coin) {
                        (e1).upPoint(1); // 플레이어 점수 증가
                        showcurpoints(e1.getPoints()); // 누적 금액 패널에 갱신
                        ((Coin) e2).resetPosition();// 코인을 초기 위치로 리셋

                    }
                    // e1이 Coin1이고 e2가 Player일 경우
                    else if (e1 instanceof Coin && e2 instanceof Player) {
                        (e2).upPoint(1); // 플레이어 점수 증가
                        showcurpoints(e1.getPoints());
                        ((Coin) e1).resetPosition(); // 코인을 초기 위치로 리셋
                    }
                }
            }
        }
    }

    // Coin 이미지 반환 메서드
    public static BufferedImage getCoinImage() {
        return coinimg;
    }
}



