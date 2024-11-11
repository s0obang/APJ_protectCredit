package org.example.panels;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class FeverPanel extends JPanel {

  public FeverPanel() {
    setBackground(Color.RED); // 임시로 배경색을 붉은색으로 설정하여 피버타임 시각적 강조
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.YELLOW);
    g.drawString("Fever Time!", getWidth() / 2 - 30, getHeight() / 2);
  }
}