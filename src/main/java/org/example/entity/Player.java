package org.example.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {

  private int speed;
  private Image characterImage;
  private Image characterImageLeft;
  private Image characterImageRight;
  private boolean facingRight = true;
  private int boundaryWidth;  // 경계 너비
  private int boundaryHeight;

  public Player(int x, int y, int width, int height) {
    super(x, y, width, height);
    this.speed = 15;
    this.boundaryWidth = 1080;
    this.boundaryHeight = 720;

    try {
      characterImageLeft = ImageIO.read(
          new File("src/main/java/org/example/img/character/main_char_left.png"));
      characterImageRight = ImageIO.read(
          new File("src/main/java/org/example/img/character/main_char_right.png"));
      characterImage = characterImageRight; // 초기 이미지는 오른쪽을 보는 이미지로 설정
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void move(int dx, int dy) {
    int newX = x + dx * speed;
    int newY = y + dy * speed;

    // 경계를 벗어나지 않도록 위치 제한
    if (newX >= 0 && newX + width <= boundaryWidth) {
      x = newX;
    }
    if (newY >= 0 && newY + height <= boundaryHeight) {
      y = newY;
    }
    // 이동 방향에 따라 이미지 변경
    if (dx < 0) {
      characterImage = characterImageLeft;
      facingRight = false;
    } else if (dx > 0) {
      characterImage = characterImageRight;
      facingRight = true;
    }
  }

  @Override
  public void update() {
    // 플레이어의 상태 업데이트 로직 (예: 이동 로직)
  }

  @Override
  public void draw(Graphics g) {
    if (characterImage != null) {
      g.drawImage(characterImage, x, y, width, height, null);
    } else {
      g.setColor(Color.BLUE);
      g.fillRect(x, y, width, height);
    }
  }
}
