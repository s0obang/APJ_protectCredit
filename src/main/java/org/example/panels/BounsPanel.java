package org.example.panels;

import javax.swing.*;
import java.awt.*;

public class BounsPanel extends JPanel {
    public BounsPanel() {
        setBackground(Color.decode("#B0BABA"));

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("bonus", 250, 250);
    }
}

