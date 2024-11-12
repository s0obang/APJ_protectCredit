package org.example.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LevelUpPanel extends JPanel {
    private Image levelupchar;

    public LevelUpPanel() {
        setBackground(Color.decode("#B0BABA"));

        try {
            levelupchar = ImageIO.read(
                    new File("src/main/java/org/example/img/character/main_char_left.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(levelupchar, 180, 220, 300,300, null);
        g.setColor(Color.BLACK);
        g.drawString("Congratulation!", 540,220);
    }
}