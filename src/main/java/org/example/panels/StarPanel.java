package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.entity.Player;
import org.example.entity.Star;

import javax.swing.*;
import java.awt.*;

public class StarPanel extends JPanel {
  private Player starplayer;
  private Star star;
  private Timer timer;

  public StarPanel() {
    starplayer = new Player(500, 500, 100, 100);
    star = new Star(300, 300, 60, 50);

    setFocusable(true);
    addKeyListener(new GameKeyAdapter(starplayer));

    // Timer to update star position and repaint panel
    timer = new Timer(30, e -> {
      star.moveTowardsTarget();
      repaint();
    });
    timer.start();

    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.decode("#B0BABA"));
    g.fillRect(0, 0, getWidth(), getHeight());

    // Draw player and star
    starplayer.draw(g);
    star.draw(g);
  }
}

