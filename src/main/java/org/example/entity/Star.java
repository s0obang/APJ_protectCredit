package org.example.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;
import org.example.Manager.GameManager;

public class Star extends Entity {

  private final int DEFAULT_MOVE_SPEED = 4; // 기본 속도
  public Image starImage;
  private int targetX, targetY;
  private double MOVE_SPEED = 4;
  private Random random;
  private boolean visible = true;


  public Star(int initialX, int initialY, int width, int height) {
    super(initialX, initialY, width, height);
    random = new Random();

    try {
      starImage = ImageIO.read(Objects.requireNonNull(
          getClass().getResourceAsStream("/img/star/star.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    setVisible(true);
    setNewTargetPosition();
  }

  @Override
  public void update() {

  }

  // star 이미지가 이동할 랜덤 좌표 정하는 메서드
  public void setNewTargetPosition() {
    if (getBounds().width > 0 && getBounds().height > 0) {
      targetX = random.nextInt(1080 - getBounds().width);
      targetY = random.nextInt(720 - getBounds().height);
    }
  }

  // 속도 초기화 메서드
  public void resetSpeed() {
    this.MOVE_SPEED = DEFAULT_MOVE_SPEED;
  }

  public void upSpeed() {
    this.MOVE_SPEED += GameManager.currentCycleCount;
  }

  //star가 움직이는 메서드
  public void moveTowardsTarget() {
    int dx = targetX - x;
    int dy = targetY - y;
    double distance = Math.sqrt(dx * dx + dy * dy);

    // 목표 지점에 가까워지면 다른 랜덤한 위치를 목표 위치로 재설정함
    if (distance < MOVE_SPEED) {
      x = targetX;
      y = targetY;
      setNewTargetPosition();
    } else {
      x += (int) (MOVE_SPEED * dx / distance);
      y += (int) (MOVE_SPEED * dy / distance);
    }
  }

  @Override
  public void draw(Graphics g) {
    if (visible) {
      // 이미지 그리기
      g.drawImage(starImage, x, y, width, height, null);
    }
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(x, y, width, height);
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

}
