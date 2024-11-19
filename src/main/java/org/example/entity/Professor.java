package org.example.entity;


import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Professor {

  private int x, y, width, height;
  private int speedX, speedY;
  private boolean isVisible;
  private Image profImage;
  private boolean isCollided;
  private Image collidedImage1;


  public Professor(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.speedX = 3;
    this.speedY = 2;
    this.isVisible = false;
    try {
      this.profImage = ImageIO.read(
          new File("src/main/java/org/example/img/character/prof.png"));
      this.collidedImage1 = ImageIO.read(
          new File("src/main/java/org/example/img/character/profEle.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void move(int playerX, int playerY) {
    if (x < playerX) {
      x += speedX;
    } else if (x > playerX) {
      x -= speedX;
    }

    if (y < playerY) {
      y += speedY;
    } else if (y > playerY) {
      y -= speedY;
    }
  }

  public void draw(Graphics g) {
    if (isVisible) {
      if (isVisible) {
        Image currentImage = isCollided ? collidedImage1 : profImage;
        g.drawImage(currentImage, x, y, width, height, null);
      }
    }
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible(boolean visible) {
    isVisible = visible;
  }

  public boolean checkCollision(int playerX, int playerY, int playerWidth, int playerHeight) {
    return x < playerX + playerWidth &&
        x + width > playerX &&
        y < playerY + playerHeight &&
        y + height > playerY;
  }
}

