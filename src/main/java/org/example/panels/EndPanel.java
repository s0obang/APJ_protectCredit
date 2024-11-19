package org.example.panels;

import javax.swing.*;

import lombok.Setter;
import org.example.Manager.*;
import org.example.entity.Blanket;
import org.example.entity.GameResult;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel; // 패널들을 담을 메인 패널
    private JPanel graduationCardPanel; // graduationPanel의 내부 CardLayout 패널
    private GameResult gameResult;
    private PointsManager pointsManager;
    private Blanket blanket;
    private JTextField score;
    private JTextField ranking;
    private GameManager manager;

    public EndPanel(GameManager manager) {
        this.manager = manager;
        pointsManager = new PointsManager();
        blanket = new Blanket();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(graduationPanel(), "graduation");
        cardPanel.add(gameOverPanel(), "gameOver");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

    }
    private JPanel gameOverPanel() {
        JPanel panel = new BackgroundPanel("src/main/java/org/example/img/backgrounds/gameOver.png");
        panel.setLayout(null);

        ImageIcon buttonIcon1 = new ImageIcon("src/main/java/org/example/img/intro/historyButton.png");
        Image img = buttonIcon1.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        buttonIcon1 = new ImageIcon(img);
        JButton homeButton = new JButton(buttonIcon1);
        homeButton.setPreferredSize(new Dimension(75, 75));
        homeButton.setBorderPainted(false);
        homeButton.setFocusPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setBounds(310, 578, 75, 75);

        ImageIcon buttonIcon2 = new ImageIcon("src/main/java/org/example/img/intro/playButton.png");
        Image img2 = buttonIcon2.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        buttonIcon2 = new ImageIcon(img2);
        JButton playAgainButton = new JButton(buttonIcon2);
        playAgainButton.setPreferredSize(new Dimension(75, 75));
        playAgainButton.setBorderPainted(false);
        playAgainButton.setFocusPainted(false);
        playAgainButton.setContentAreaFilled(false);
        playAgainButton.setBounds(745, 575, 75, 75);

        // 홈화면으로 이동
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showScreen("start");
            }
        });

        // 게임 다시 시작
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pointsManager.resetPoints(); //코인 초기화
                blanket.resetBlanket(); //이불 초기화
                manager.startGameSequence();// 아이콘 떨어짐 동작 시작
                manager.getGamePanel().startGame(); // 아이콘 떨어짐 동작 시작
            }
        });

        panel.add(homeButton);
        panel.add(playAgainButton);


        return panel;
    }

    private JPanel graduationPanel() {
        Font labelFont = new Font("Neo둥근모", Font.PLAIN, 34);

        graduationCardPanel = new JPanel(new CardLayout());
        JPanel g1 = new BackgroundPanel("src/main/java/org/example/img/backgrounds/graduation_1.png");
        JPanel g2 = new BackgroundPanel("src/main/java/org/example/img/backgrounds/graduation_2.png");
        g2.setLayout(null);

        score = new JTextField(15);
        ranking = new JTextField(15);

        score.setFont(labelFont);
        score.setBounds(485, 255, 180, 60);
        score.setOpaque(false);
        score.setEditable(false);
        score.setFocusable(false);
        score.setBorder(BorderFactory.createEmptyBorder());
        ranking.setFont(labelFont);
        ranking.setBounds(485, 364, 140, 60);
        ranking.setOpaque(false);
        ranking.setEditable(false);
        ranking.setFocusable(false);
        ranking.setBorder(BorderFactory.createEmptyBorder());

        ImageIcon buttonIcon = new ImageIcon("src/main/java/org/example/img/intro/historyButton.png");
        Image img = buttonIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        buttonIcon = new ImageIcon(img);
        JButton nextButton = new JButton(buttonIcon);
        nextButton.setPreferredSize(new Dimension(75, 75));
        nextButton.setBounds(515, 600, 75, 75);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.setContentAreaFilled(false);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.showScreen("start"); // 일단 메인으로 넘김 history not yet...
            }
        });


        g2.add(score);
        g2.add(ranking);
        g2.add(nextButton);
        graduationCardPanel.add(g1, "g1");
        graduationCardPanel.add(g2, "g2");

        return graduationCardPanel;
    }

    public void showResultSuccess() {
        CardLayout cl = (CardLayout) graduationCardPanel.getLayout(); // graduationCardPanel 가져옴
        cl.show(graduationCardPanel, "g1");

        // 점수 띄우기
        score.setText(gameResult.getPoints()+"");
        // 랭킹 띄우기 (not yet)

        // 3초 후에 점수 띄우는 화면으로 전환
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(graduationCardPanel, "g2");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // 게임 결과에 따라 화면 다르게 띄움
    public void showEndPanel(GameResult result) {
        this.gameResult = result;
        if (gameResult.isGraduated()) {
            cardLayout.show(cardPanel, "graduation");
            showResultSuccess();
        }
        else {
            cardLayout.show(cardPanel, "gameOver");
        }
    }


}
