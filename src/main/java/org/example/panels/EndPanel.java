package panels;

import javax.swing.*;

import Manager.GameManager;

import java.awt.*;

public class EndPanel extends JPanel {
    public EndPanel(GameManager manager, boolean isGameOver) {

        setLayout(new BorderLayout());
        JLabel messageLabel;

        //게임 종료 상태에 따른 메시지 표시
        if (!isGameOver) {
            setBackground(Color.RED);
            messageLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        } else {
            setBackground(Color.GREEN);
            messageLabel = new JLabel("Congratulations! You Won!", SwingConstants.CENTER);
        }

        messageLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(messageLabel, BorderLayout.CENTER);

        JButton restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> manager.showScreen("start")); // StartScreen으로 돌아가기
        add(restartButton, BorderLayout.SOUTH);

        //주석으로 어떻게 넘어가는지 감잡으슈 이해했으면 지워도 됨


    }
}
