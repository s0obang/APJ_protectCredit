package org.example.Manager;

import javax.swing.*;
import java.awt.*;

//생성자 파라미터로 경로 넣으면 배경 그려주는 JPanel
public class BackgroundPanel extends JPanel{
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 사이즈
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
