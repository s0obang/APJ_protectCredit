package org.example.entity;

import java.awt.*;

public abstract class Entity {

  protected int points = 0;
  public int x;
  public int y;
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


  // 위치 설정 메서드
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }


  // 위치 가져오기 메서드
  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  // 충돌 판정 메서드 (필요한 경우)
  public boolean isColliding(Entity other) {
    return x < other.x + other.width && x + width > other.x &&
        y < other.y + other.height && y + height > other.y;
  }

  // 각 엔티티가 자신의 상태를 업데이트하는 메서드
  public abstract void update();

  // 각 엔티티가 자신을 그리는 메서드
  public abstract void draw(Graphics g);

    protected void paintComponent(Graphics g) {
    }
}





