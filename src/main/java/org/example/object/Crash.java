package org.example.object;

import org.example.entity.Coin1;
import org.example.entity.Entity;
import org.example.entity.Player;
import org.example.panels.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Timer;

public class Crash {
    private final GamePanel gamePanel;
    ArrayList<Entity> entities;
    private JTextField scoreText;
    private Timer activeTimer;
    private JTextField curpointText;

    // 배열 생성
    public Crash(GamePanel gamePanel) {
        entities = new ArrayList<>();
        this.gamePanel = gamePanel;

        // 누적 금액 표시하는 텍스트 필드 생성
        curpointText = new JTextField("보유 금액 : 0원"); // Initial points display
        Font scoreFont = new Font("Neo둥근모", Font.BOLD, 15);
        curpointText.setFont(scoreFont);
        curpointText.setForeground(Color.black);
        curpointText.setEditable(false);
        curpointText.setOpaque(false); // 배경색 투명하게 만들기
        curpointText.setBorder(null); // 텍스트 필드 테두리 삭제

        curpointText.setBounds(50, 20, 150, 30); // Top-left corner
        gamePanel.setLayout(null);
        gamePanel.add(curpointText); // 게임 패널에 추가
    }

    //배열에 entity 저장
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    // 만 원 추가 표시
    public void pointText(int points) {
        // 타이머가 실행 중인 경우 새로 생성하지 않고 종료
        if (activeTimer != null && activeTimer.isRunning()) {
            return;
        }

        scoreText = new JTextField("+10000");
        Font labelFont = new Font("Neo둥근모", Font.PLAIN, 15);
        scoreText.setFont(labelFont);
        scoreText.setForeground(Color.decode("#5E5E5E"));
        scoreText.setEditable(false);
        scoreText.setOpaque(false); // 배경을 투명하게 설정
        scoreText.setBorder(null); // 텍스트 상자 테두리 지우기

        // 위치와 크기 설정
        scoreText.setBounds(50, 40, 200, 30); // 위치와 크기 설정
        scoreText.setVisible(true);

        // 패널에 추가하고 다시 그리기 요청
        gamePanel.setLayout(null); // 레이아웃 매니저 설정
        gamePanel.add(scoreText);
        gamePanel.add(curpointText);
        gamePanel.repaint();

        // 일정 시간 후 텍스트 필드 숨김
        activeTimer = new Timer(300, e -> {
            gamePanel.remove(scoreText);
            scoreText.setVisible(false);
            gamePanel.repaint();
            activeTimer = null; // 타이머 종료 후 null로 설정
        });
        activeTimer.setRepeats(false);
        activeTimer.start();

    }

    // 누적 금액 패널에 띄우는 함수
    private void showcurpoints(int points) {
        curpointText.setText("보유 금액 : " + points + "만 원");
    }

    // 충돌 관련
    public void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                // 충돌 감지 및 처리
                if (e1.getBounds().intersects(e2.getBounds())) {
                    if (e1 instanceof Player && e2 instanceof Coin1) {
                        (e1).upPoint(1); // 플레이어 점수 증가
                        pointText(e1.getPoints()); // +10000 글자 패널에 띄우기
                        showcurpoints(e1.getPoints()); // 누적 금액 패널에 갱신
                        ((Coin1) e2).resetPosition();// 코인을 초기 위치로 리셋
                    } 
                    // e1이 Coin1이고 e2가 Player일 경우
                    else if (e1 instanceof Coin1 && e2 instanceof Player) {
                        (e2).upPoint(1); // 플레이어 점수 증가
                        pointText(e2.getPoints());
                        showcurpoints(e1.getPoints());
                        ((Coin1) e1).resetPosition(); // 코인을 초기 위치로 리셋
                    }
                }
                }
            }
        }
}

