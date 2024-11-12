package org.example.entity;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Star extends Entity {
    public Image starImage;
    private int targetX, targetY;
    private final int MOVE_SPEED = 7;
    private Random random;
    private boolean visible = true; // 추가된 속성

    public Star(int initialX, int initialY, int width, int height) {
        super(initialX, initialY, width, height);
        random = new Random();

        // Load star image
        try {
            starImage = ImageIO.read(new File("src/main/java/org/example/img/star/star.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setNewTargetPosition();
    }

    @Override
    public void update() {

    }

    public void setNewTargetPosition() {
        if (getBounds().width > 0 && getBounds().height > 0) {
            targetX = random.nextInt(1080 - getBounds().width);
            targetY = random.nextInt(720 - getBounds().height);
        }
    }

    public void moveTowardsTarget() {
        int dx = targetX - x;
        int dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // If close to target, set new target
        if (distance < MOVE_SPEED) {
            x = targetX;
            y = targetY;
            setNewTargetPosition();
        } else {
            x += (int) (MOVE_SPEED * dx / distance);
            y += (int) (MOVE_SPEED * dy / distance);
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void draw(Graphics g) {
        if (visible) {
            // 이미지 그리기
            g.drawImage(starImage, x, y, width, height, null);
        }
    }
}
