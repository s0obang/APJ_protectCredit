package org.example.panels;

import static org.example.Manager.GameManager.bonusPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BeforeBonusPanel extends JPanel {

  private JTextField bonustime;
  private Image backgroundImage, coinImage;
  private JLabel leftcoinLabel, rightcoinLabel;

  public BeforeBonusPanel() {
    setLayout(null);

    try {
      backgroundImage = ImageIO.read(
          Objects.requireNonNull(getClass().getResourceAsStream(
              "/img/backgrounds/etcback.jpg")));
      coinImage = ImageIO.read(Objects.requireNonNull(
          getClass().getResourceAsStream("/img/coin/coin.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    bonustime = new JTextField("!BONUS TIME!");

    Font bonusFont = new Font("Galmuri11 Regular", Font.BOLD, 50);
    bonustime.setFont(bonusFont);
    bonustime.setForeground(Color.black);
    bonustime.setEditable(false);
    bonustime.setOpaque(false);
    bonustime.setBorder(null);
    bonustime.setBounds(338, 300, 500, 80);

    add(bonustime);

    Image scaledCoinImage = coinImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    leftcoinLabel = new JLabel(new ImageIcon(scaledCoinImage));
    leftcoinLabel.setBounds(293, 310, 50, 50);
    rightcoinLabel = new JLabel(new ImageIcon(scaledCoinImage));
    rightcoinLabel.setBounds(700, 310, 50, 50);
    add(leftcoinLabel);
    add(rightcoinLabel);

  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);

    if (visible) {
      bonusPanel.playbonusPanelSound();
    } else {
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    } else {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(0, 0, getWidth(), getHeight());
    }
  }
}
