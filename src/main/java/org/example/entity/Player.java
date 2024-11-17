package org.example.entity;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {

  private double points = 0;
  private int speed;
  private Image characterImage;
  private Image characterImageLeft;
  private Image characterImageRight;
  private boolean facingRight = true;
  private int boundaryWidth;  // 경계 너비
  private int boundaryHeight;
  private double GPA; // 학점
  private int dx = 0, dy =0;

  public Player(int x, int y, int width, int height) {
    super(x, y, width, height);
    this.speed = 10;
    this.boundaryWidth = 1080;
    this.boundaryHeight = 720;
    this.GPA = 4.5; //초기 학점 4.5

    try {
      characterImageLeft = ImageIO.read(
          new File("src/main/java/org/example/img/character/main_char_left.png"));
      characterImageRight = ImageIO.read(
          new File("src/main/java/org/example/img/character/main_char_right.png"));
      characterImage = characterImageRight; // 초기 이미지는 오른쪽보게함
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void move(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
    int newX = x + dx * speed;
    int newY = y + dy * speed;
    // 화면 안 벗어나게 제한 거는거
    if (newX >= 0 && newX + width <= boundaryWidth) {
      x = newX;
    }
    if (newY >= 0 && newY + height <= boundaryHeight) {
      y = newY;
    }
    //이동 방향에 따라 이미지 변경
    if (dx < 0) {
      characterImage = characterImageLeft;
      facingRight = false;
    } else if (dx > 0) {
      characterImage = characterImageRight;
      facingRight = true;
    }
  }



  public double getGPA() {
    return GPA;
  }

  public void setGPA(double newGPA) {
    // 최대 4.5, 최소 0점 유지
    this.GPA = Math.max(0, Math.min(4.5, newGPA));
  }

  @Override
  public void upPoint(double amount) {
    this.points += amount;
  }

  @Override
  public int getPoints() {
    return (int)points;  // double을 int로 변환하여 반환
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
  @Override
  public Rectangle getBounds() {
    System.out.println("Player bounds: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
    return new Rectangle(x, y, width, height);
  }

}
