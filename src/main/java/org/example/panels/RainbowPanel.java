package org.example.panels;

import javax.swing.*;
import java.awt.*;
import javazoom.jl.player.Player;
import java.io.FileInputStream;

import static org.example.Manager.GameManager.bonusPanel;

public class RainbowPanel extends JPanel {
    private JTextField bonustime;
    private Timer colorTimer, displayTimer;
    private int colorIndex = 0; // 무지개 색상 배열의 인덱스
    private final Color[] rainbowColors; // 무지개 색상 배열

    public RainbowPanel() {
        setLayout(null);
        bonustime = new JTextField("BONUS TIME!!");

        Font bonusFont = new Font("Neo둥근모", Font.BOLD, 60);
        bonustime.setFont(bonusFont);
        bonustime.setForeground(Color.black);
        bonustime.setEditable(false);
        bonustime.setOpaque(false); // 배경색 투명하게 만들기
        bonustime.setBorder(null); // 텍스트 필드 테두리 삭제
        bonustime.setBounds(320, 300, 500, 80);

        add(bonustime);


        // 무지개 색 배열 설정
        rainbowColors = new Color[]{
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, new Color(128, 0, 128) // 보라색
        };

        if(colorTimer != null) colorTimer.stop();
        // 색상을 일정 시간마다 변경하는 타이머 설정
        colorTimer = new Timer(300, e -> {
            colorIndex = (colorIndex + 1) % rainbowColors.length; // 인덱스 순환
            repaint(); // 색상 변경 후 다시 그리기
        });
        // 타이머 시작
        colorTimer.setRepeats(true);
        colorTimer.start();

    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            // 패널이 보이면 타이머 시작
            colorTimer.start();
            bonusPanel.playbonusPanelSound();
        } else {
            // 패널이 보이지 않으면 타이머 중지
            colorTimer.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 패널을 무지개 색으로 채우기
        g.setColor(rainbowColors[colorIndex]);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
