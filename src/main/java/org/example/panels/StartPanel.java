package org.example.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.example.Manager.BackgroundPanel;
import org.example.Manager.GameManager;

public class StartPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel; // 패널들을 담을 메인 패널

    public StartPanel(GameManager manager) {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);


        cardPanel.add(startGamePanel(manager), "start");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel startGamePanel(GameManager manager) {
        JPanel panel = new BackgroundPanel("src/main/java/org/example/img/backgrounds/startBackground.png"); // 배경 패널
        panel.setLayout(null); // bPanel 절대 위치 정하려고

        JPanel bPanel = new JPanel(new GridBagLayout()); // 버튼 감싸는 패널
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        bPanel.setOpaque(false);

        Font labelFont = new Font("Neo둥근모", Font.PLAIN, 26);

        ImageIcon buttonIcon1 = new ImageIcon("src/main/java/org/example/img/intro/historyButton.png");
        Image img = buttonIcon1.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        buttonIcon1 = new ImageIcon(img);
        JButton historyButton = new JButton(buttonIcon1);
        historyButton.setPreferredSize(new Dimension(75, 75));
        historyButton.setBorderPainted(false);
        historyButton.setFocusPainted(false);
        historyButton.setContentAreaFilled(false);

        JLabel historyLabel = new JLabel("history");
        historyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        historyLabel.setFont(labelFont);
        historyLabel.setForeground(Color.decode("#5E5E5E"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        bPanel.add(historyButton, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        bPanel.add(historyLabel, gbc);


        ImageIcon buttonIcon2 = new ImageIcon("src/main/java/org/example/img/intro/playButton.png");
        Image img2 = buttonIcon2.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        buttonIcon2 = new ImageIcon(img2);
        JButton playButton = new JButton(buttonIcon2);
        playButton.setPreferredSize(new Dimension(75, 75));
        playButton.addActionListener(e -> manager.showScreen("game")); // 임의로 파라미터 받아서 GameScreen으로 이동

        playButton.setBorderPainted(false);
        playButton.setFocusPainted(false);
        playButton.setContentAreaFilled(false);

        JLabel playLabel = new JLabel("play!");
        playLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playLabel.setFont(labelFont);
        playLabel.setForeground(Color.decode("#5E5E5E"));

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 120, 0, 0);
        bPanel.add(playButton, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 120, 0, 0);
        bPanel.add(playLabel, gbc);


        bPanel.setBounds(398, 570, 290, 135);

        panel.add(bPanel);

        return panel;
    }


}
