package org.example.entity;

import org.example.Manager.PointsManager;
import org.example.panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Blanket {
    public Image blanket, itemblanket;
    private int count; // Blanket이 표시된 횟수

    public Blanket() {
        try {
            blanket = ImageIO.read(new File("src/main/java/org/example/img/blanket/blanket.png"));
            itemblanket = ImageIO.read(new File("src/main/java/org/example/img/blanket/itemblanket.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        count = 0; // 초기값 0
    }

    public void incrementCount() {
        count++;
    }

    public void doubleincrementCount() {
        count += 2;
    }

    public int getCount() {
        return count;
    }

    public void resetBlanket() {
        count = 0;
    }
}