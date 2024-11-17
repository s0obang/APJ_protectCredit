package org.example.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Coin extends Entity {

  public static ArrayList<Coin> arraycoin = new ArrayList<>();
  public Image coinimg;
  private static final Random random = new Random();
  int x, y, speed;

  public Coin( int x, int y, int width, int height, Image coinimg) {
    super(x, y, width, height);
    this.speed = 7;
    this.x = x;
    this.y = y;
    this.coinimg = coinimg;

  }

  public static Image loadImage(String imageName) {
    try {
      return ImageIO.read(new File("src/main/java/org/example/img/coin/" + imageName));
    } catch (IOException ex) {
      throw new RuntimeException("이미지 로드 실패: " + imageName, ex);
    }
  }

  // 속도 레벨 증가 메서드
  public void increaseSpeedLevel() {
    speed++;
  }

  // 속도 레벨 리셋 메서드
  public void resetSpeedLevel() {
    speed = 7;
  }

  public static void createAndAddCoin(int x, int y, int width, int height) {
    Coin coin = new Coin( random.nextInt(x - 60), random.nextInt(y),width, height, Coin.loadImage("coin.png")) {
      @Override
      public void update() {
      }
    }; // 익명 서브클래스로 인스턴스 생성
    arraycoin.add(coin);


  }

  public void fall() {
    y += speed;
    if (y > 720) {
      y = -20;
      x = random.nextInt(1080 - 60);
    }
  }

  public void draw(Graphics g) {
    int coinSize = 30;
    g.drawImage(coinimg, x, y, coinSize, coinSize, null);
  }

  @Override
  public Rectangle getBounds() {
    // 코인의 충돌 영역을 정의
    return new Rectangle(x, y, width, height); // 코인 이미지의 크기에 맞게 수정
  }


  @Override
  public void update() {

  }

  // 코인 생성 및 배열에 추가

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (Coin coin : arraycoin) {
      int coinSize = 30;
      g.drawImage(coinimg, coin.x, coin.y, coinSize, coinSize, null);
    }

  }

  public void resetPosition() {
    // 코인 초기 위치로 리셋
    this.y = -20; // 화면 위쪽에서 다시 떨어지도록
    this.x = random.nextInt(1080 - 40); // 무작위 x 위치
  }

}




