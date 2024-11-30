package org.example.panels;

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

public class BackPanel extends JPanel {

  private Image backgroundImage, levelupchar, bookImage;
  private JLabel LetLabel, BackLabel, bookLabel;

  public BackPanel() {
    setLayout(null);

    try {
      backgroundImage = ImageIO.read(
          Objects.requireNonNull(
              getClass().getResourceAsStream("/img/backgrounds/etcback.jpg")));
      levelupchar = ImageIO.read(
          Objects.requireNonNull(getClass().getResourceAsStream(
              "/img/character/main_char_left.png")));
      bookImage = ImageIO.read(
          Objects.requireNonNull(getClass().getResourceAsStream(
              "/img/gradeItem/textBook.png")));

    } catch (IOException e) {
      e.printStackTrace();
    }
    Font levelupFont = new Font("Galmuri11 Regular", Font.BOLD, 60);

    LetLabel = new JLabel("LET'S");
    LetLabel.setFont(levelupFont);
    LetLabel.setForeground(Color.black);
    LetLabel.setBounds(250, 280, 400, 80);

    BackLabel = new JLabel("BACK!");
    BackLabel.setFont(levelupFont);
    BackLabel.setForeground(Color.black);
    BackLabel.setBounds(250, 390, 200, 80);

    add(LetLabel);
    add(BackLabel);

    Image scaledBookImage = bookImage.getScaledInstance(147, 120, Image.SCALE_SMOOTH);
    bookLabel = new JLabel(new ImageIcon(scaledBookImage));
    bookLabel.setBounds(350, 200, 147, 120);
    add(bookLabel);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    } else {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(0, 0, getWidth(), getHeight());
    }
    g.drawImage(levelupchar, 600, 165, 273, 390, null);
  }
}

