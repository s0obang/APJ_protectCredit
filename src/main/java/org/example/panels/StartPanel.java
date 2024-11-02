package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Manager.GameManager;

public class StartPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel; // 패널들을 담을 메인 패널

    public StartPanel(GameManager manager) {
        // CardLayout과 cardPanel 설정
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 여러 패널 생성 및 버튼 추가
        JPanel panel1 = createPanel("Welcome to the Game!", Color.RED, "Go to Panel 2", "panel2");
        JPanel panel2 = createPanel("Get Ready!", Color.BLUE, "Go to Panel 3", "panel3");
        JPanel panel3 = createPanel("Press Start to Begin", Color.GREEN, "Go to Panel 1", "panel1");

        // CardLayout에 패널 추가
        cardPanel.add(panel1, "panel1");
        cardPanel.add(panel2, "panel2");
        cardPanel.add(panel3, "panel3");

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> manager.showScreen("game")); // GameScreen으로 이동
        panel3.add(startButton, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
    }

    // 패널 생성 메서드
    private JPanel createPanel(String labelText, Color bgColor, String buttonText, String targetPanelName) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        JButton button = new JButton(buttonText);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, targetPanelName); // 버튼 클릭 시 지정된 패널로 전환
            }
        });
        panel.add(button, BorderLayout.SOUTH);

        return panel;
    }

    //어떻게 넘어가는지 감잡으슈 이해했으면 지워도 됨

}
