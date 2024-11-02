package panels;

import javax.swing.*;

import Manager.GameManager;

import java.awt.*;

public class GamePanel extends JPanel {
    public GamePanel(GameManager manager) {

        setLayout(new BorderLayout());
        JButton endButton = new JButton("End Game (Game Over)");
        endButton.addActionListener(e -> manager.showEndScreen(false)); // 게임 오버로 종료 화면으로 이동
        JButton successButton = new JButton("End Game (Success)");
        successButton.addActionListener(e -> manager.showEndScreen(true)); // 성공으로 종료 화면으로 이동
        add(endButton, BorderLayout.WEST);
        add(successButton, BorderLayout.EAST);

        //어떻게 넘어가는지 감잡으슈 이해했으면 지워도 됨
        //게임 종료 화면으로 전환 시 boolean 값으로 게임 오버인지 졸업인지 알려줘야함
        //true -> 졸업, false -> 실패


    }
}
