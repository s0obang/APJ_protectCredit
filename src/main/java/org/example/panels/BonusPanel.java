package org.example.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BonusPanel extends JPanel {
    private JPanel screenbonus;
    private Timer colorTimer;
    private boolean isColorTimerRunning = false;

    public BonusPanel() {
        // 화면 구성 초기화
        this.setLayout(new BorderLayout());

        // screenbonus 패널 초기화
        screenbonus = new JPanel();
        screenbonus.setPreferredSize(new Dimension(400, 300));  // 화면 크기 설정
        this.add(screenbonus, BorderLayout.CENTER);

        // colorTimer 초기화
        colorTimer = new Timer(300, new ActionListener() {
            private int colorIndex = 0;
            private Color[] rainbowColors = {
                    Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                    Color.CYAN, Color.BLUE, new Color(128, 0, 128) // 보라색
            };

            @Override
            public void actionPerformed(ActionEvent e) {
                // 배경 색상을 순차적으로 변경
                screenbonus.setBackground(rainbowColors[colorIndex]);
                colorIndex = (colorIndex + 1) % rainbowColors.length;
            }
        });
    }

    // bonusColor 메서드 - 무지개 색상을 적용하고 3초 후에 화면을 전환
    public void bonusColor() {
        // 이미 실행 중인 경우 타이머 초기화
        if (isColorTimerRunning) {
            colorTimer.stop();
        }

        // screenbonus 패널을 화면에 추가하고 보이도록 설정
        screenbonus.setVisible(true);
        this.revalidate();
        this.repaint();
        isColorTimerRunning = true;

        // 무지개 색상 변경 시작
        colorTimer.start();

        // 3초 후에 screenbonus를 숨기고 색상 타이머를 중지
        Timer transitionTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorTimer.stop(); // 색상 전환 타이머 중지
                isColorTimerRunning = false;

                // 화면에서 screenbonus 패널 숨기기
                screenbonus.setVisible(false);
                revalidate();
                repaint();
            }
        });
        transitionTimer.setRepeats(false); // 1회만 실행
        transitionTimer.start();
    }
}