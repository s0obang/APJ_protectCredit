package org.example.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.Setter;

public class GamePlayer extends Entity {

  private int speed;
  private Image characterImage;
  private Image characterImageLeft;
  private Image characterImageRight;
  private boolean facingRight = true;
  private int boundaryWidth;  // 경계 너비
  private int boundaryHeight;
  private double GPA; // 학점
  private Blanket blanket;
  private boolean blanketActive = false;
  private int dx = 0, dy =0;
  @Setter
  @Getter
  private boolean movable = true;

  public GamePlayer(int x, int y, int width, int height) {
    super(x, y, width, height);
    this.speed = 10;
    this.boundaryWidth = 1080;
    this.boundaryHeight = 720;
    this.GPA = 4.5; //초기 학점 4.5
    blanket = new Blanket();

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

  public void changeImage() {
    characterImage = blanket.blanket;
    this.width = 100;
    this.height = 100;
    blanketActive = true;  // Blanket 활성화 상태로 설정
  }

  public void changeOriginImage() {
    characterImage = characterImageRight;
    this.width = 60;
    this.height = 90;
    blanketActive = false;  // Blanket 비활성화 상태로 설정
  }

  public boolean isBlanketActive() {
    return blanketActive;  // Blanket 상태 반환
  }

  public double getGPA() {
    return GPA;
  }

  public void setGPA(double newGPA) {
    // 최대 4.5, 최소 0점 유지
    this.GPA = Math.max(0, Math.min(4.5, newGPA));
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
    return new Rectangle(x, y, width, height);
  }

}
