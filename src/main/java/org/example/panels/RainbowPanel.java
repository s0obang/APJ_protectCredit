package org.example.panels;

import javax.swing.*;
import java.awt.*;

public class RainbowPanel extends JPanel {
    private JPanel screenbonus;
    private JTextField bonustime;
    private Timer colorTimer, bonusTimer;
    private int colorIndex = 0; // 무지개 색상 배열의 인덱스
    private final Color[] rainbowColors; // 무지개 색상 배열
    private boolean showBonus = false;  // 보너스를 나타낼지 여부
    private boolean isColorTimerRunning = false;

    public RainbowPanel() {
        bonustime = new JTextField("Congratulation!");

        Font bonusFont = new Font("Neo둥근모", Font.BOLD, 40);
        bonustime.setFont(bonusFont);
        bonustime.setForeground(Color.black);
        bonustime.setEditable(false);
        bonustime.setOpaque(false); // 배경색 투명하게 만들기
        bonustime.setBorder(null); // 텍스트 필드 테두리 삭제
        bonustime.setBounds(1080/2, 720/2, 500, 60);

        // screenbonus 패널 초기화
        screenbonus = new JPanel();
        screenbonus.setBackground(Color.RED);
        screenbonus.setPreferredSize(new Dimension(1080, 720));  // 화면 크기 설정
        screenbonus.setVisible(false);
        screenbonus.add(bonustime);
        this.add(screenbonus);

        // 무지개 색 배열 설정
        rainbowColors = new Color[]{
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, new Color(128, 0, 128) // 보라색
        };

        // Timer를 사용해 일정 시간마다 색상 변경
        colorTimer = new Timer(300, e -> {
            screenbonus.setForeground(rainbowColors[colorIndex]); // 현재 색상으로 배경 설정
            colorIndex = (colorIndex + 1) % rainbowColors.length; // 다음 색상으로 인덱스 증가 및 순환
        });

        // 타이머 설정
        bonusTimer = new Timer(3000, e -> {
            screenbonus.setVisible(false);// 3초 후 보너스를 숨김
            repaint();  // 화면 갱신
        });
    }

    // bonusColor() 함수
    public void bonusColor() {
        showBonus = true;  // 보너스를 표시하도록 설정
        bonusTimer.start();  // 타이머 시작
        repaint();  // 즉시 화면 갱신
    }
}
