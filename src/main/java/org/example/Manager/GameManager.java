package Manager;

import panels.EndPanel;
import panels.GamePanel;
import panels.StartPanel;

import javax.swing.*;
import java.awt.*;

public class GameManager extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public GameManager() {
        setTitle("학점을 지켜라!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 768);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 화면을 패널로 추가
        mainPanel.add(new StartPanel(this), "start");
        mainPanel.add(new GamePanel(this), "game");

        add(mainPanel);
        setVisible(true);
    }

    // 화면 전환 메서드
    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }
    // 게임 종료 패널은 따로(boolean 값 필요)
    public void showEndScreen(boolean isGameOver) {
        mainPanel.add(new EndPanel(this, isGameOver), "end");
        showScreen("end");
    }

}
