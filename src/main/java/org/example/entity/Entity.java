package org.example.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity {

  public int x;
  public int y;
  protected int points = 0;
  protected int width;
  protected int height;


  //각 엔티티들이 상속받아서 쓸 수 있는 추상클래스 입니당~~
  public Entity(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

  }


  // 엔티티 경계값 가져오는 메서드
  public Rectangle getBounds() {
    return new Rectangle(x, y, width, height);
  }


  // 위치 가져오기 메서드
  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  // 충돌 판정 메서드
  public boolean isColliding(Entity other) {
    return x < other.x + other.width && x + width > other.x &&
        y < other.y + other.height && y + height > other.y;
  }


  public abstract void update();


  public abstract void draw(Graphics g);

  protected void paintComponent(Graphics g) {
  }
}





