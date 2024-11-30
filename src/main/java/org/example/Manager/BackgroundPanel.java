package org.example.Manager;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {

  private Image backgroundImage;

  public BackgroundPanel(String imagePath) {
    try {

      java.net.URL imageUrl = getClass().getResource(imagePath);
      if (imageUrl == null) {
        throw new IllegalArgumentException("이미지 없음: " + imagePath);
      }
      this.backgroundImage = new ImageIcon(imageUrl).getImage();
    } catch (Exception e) {
      throw new RuntimeException("배경 이미지 로드 중 오류발생: " + imagePath, e);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
  }
}
