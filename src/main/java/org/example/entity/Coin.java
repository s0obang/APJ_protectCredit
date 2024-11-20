package org.example.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

public class Coin extends Entity {

  public static ArrayList<Coin> arraycoin = new ArrayList<>();
  public Image coinimg;
  private static final Random random = new Random();
  int x, y, speed;
  // 코인 이미지와 좌표 리스트
  public static List<Coin> largeCoins;
  public static List<Coin> mediCoins;
  public static List<Coin> smallCoins;

  public Coin(int x, int y, int width, int height, Image coinimg) {
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
    speed += 1.5;
  }

  // 속도 레벨 리셋 메서드
  public void resetSpeedLevel() {
    speed = 7;
  }

  public static void createAndAddCoin(int x, int y, int width, int height) {
    Coin coin = new Coin(random.nextInt(x - 60), random.nextInt(y-600),width, height, Coin.loadImage("coin.png")) {
      @Override
      public void update() {
      }
    }; // 익명 서브클래스로 인스턴스 생성
    arraycoin.add(coin);


  }

  // largecoin 위치 설정
  public static final List<int[]> largeCoinPositions = List.of(
          new int[]{525, 345 + 90}, new int[]{525, 345 + 130}, new int[]{525 + 60, 345 + 75}, new int[]{525 - 60, 345 + 75}
          , new int[]{525 + 60, 345 + 115}, new int[]{525 - 60, 345 + 115}, new int[]{525 + 30, 345 + 150}, new int[]{525 - 30, 345 + 150}
          , new int[]{525 - 220, 345 - 240 + 90}, new int[]{525 - 220, 345 - 240 + 130}, new int[]{525 - 220 + 60, 345 - 240 + 75}, new int[]{525 - 220 - 60, 345 - 240 + 75}
          , new int[]{525 - 220 - 60, 345 - 240 + 115}, new int[]{525 - 220 + 60, 345 - 240 + 115}, new int[]{525 - 220 + 30, 345 - 240 + 150}, new int[]{525 - 220 - 30, 345 - 240 + 150}
          , new int[]{525 - 220, 345 - 240 + 170}, new int[]{525 + 220, 345 - 240 + 90}, new int[]{525 + 220, 345 - 240 + 130}, new int[]{525 + 220 - 60, 345 - 240 + 75}
          , new int[]{525 + 220 + 60, 345 - 240 + 75}, new int[]{525 + 220 - 60, 345 - 240 + 115}, new int[]{525 + 220 + 60, 345 - 240 + 115}, new int[]{525 + 220 + 30, 345 - 240 + 150}
          , new int[]{525 + 220 - 30, 345 - 240 + 150}, new int[]{525 + 220, 345 - 240 + 170}, new int[]{525, 345 + 170}
  );

  // medicoin 위치 설정
  public static final List<int[]> medicoinPositions = List.of(
          new int[]{525 + 220 - 60, 345 - 240 + 160}, new int[]{525 + 220 + 60, 345 - 240 + 160}, new int[]{525 + 30, 345 + 190}, new int[]{525 - 30, 345 + 190}
          , new int[]{525 + 220 - 90, 345 - 240 + 130}, new int[]{525 + 220 + 90, 345 - 240 + 130}, new int[]{525 + 60, 345 + 160}, new int[]{525 - 60, 345 + 160}
          , new int[]{525 + 220 - 90, 345 - 240 + 95}, new int[]{525 + 220 + 90, 345 - 240 + 95}, new int[]{525 - 90, 345 + 130}, new int[]{525 + 90, 345 + 130}
          , new int[]{525 + 220 + 30, 345 - 240 + 100}, new int[]{525 + 220 - 30, 345 - 240 + 100}, new int[]{525 + 30, 345 + 100}, new int[]{525 - 30, 345 + 100}
          , new int[]{525 + 220 - 90, 345 - 240 + 60}, new int[]{525 + 220 + 90, 345 - 240 + 60}, new int[]{525 - 90, 345 + 95}, new int[]{525 + 90, 345 + 95}
          , new int[]{525 + 220 + 30, 345 - 240 + 60}, new int[]{525 + 220 - 30, 345 - 240 + 60}, new int[]{525 - 90, 345 + 60}, new int[]{525 + 90, 345 + 60}
          , new int[]{525 - 220 + 30, 345 - 240 + 190}, new int[]{525 - 220 - 30, 345 - 240 + 190}, new int[]{525 + 30, 345 + 60}, new int[]{525 - 30, 345 + 60}
          , new int[]{525 - 220 - 60, 345 - 240 + 160}, new int[]{525 - 220 + 60, 345 - 240 + 160}
          , new int[]{525 - 220 - 90, 345 - 240 + 130}, new int[]{525 - 220 + 90, 345 - 240 + 130}
          , new int[]{525 - 220 - 90, 345 - 240 + 95}, new int[]{525 - 220 + 90, 345 - 240 + 95}
          , new int[]{525 - 220 + 30, 345 - 240 + 100}, new int[]{525 - 220 - 30, 345 - 240 + 100}
          , new int[]{525 - 220 - 90, 345 - 240 + 60}, new int[]{525 - 220 + 90, 345 - 240 + 60}
          , new int[]{525 - 220 + 30, 345 - 240 + 60}, new int[]{525 - 220 - 30, 345 - 240 + 60}
          , new int[]{525 + 220 + 30, 345 - 240 + 190}, new int[]{525 + 220 - 30, 345 - 240 + 190}
  );

  // smallcoin 위치 설정
  public static final List<int[]> smallcoinPositions = List.of(
          new int[]{525 + 60, 345+ 30}, new int[]{525 + 115, 345 + 95}, new int[]{525 - 115, 345 + 95}
          , new int[]{525, 345 + 220}, new int[]{525 - 220, 345 - 240 + 220}
          , new int[]{525 - 220 - 60, 345 - 240 + 30}, new int[]{525 - 220 + 60, 345 - 240 + 30}
          , new int[]{525 - 220 + 115, 345 - 240 + 95}, new int[]{525 - 220 - 115, 345 - 240 + 95}
          , new int[]{525 + 220 - 60, 345 - 240 + 30}, new int[]{525 + 220 + 60, 345 - 240 + 30}
          , new int[]{525 + 220 + 115, 345 - 240 + 95}, new int[]{525 + 220 - 115, 345 - 240 + 95}
          , new int[]{525 + 220, 345 - 240 + 220}, new int[]{525 - 60, 345 + 30}
  );

  public static List<Coin> createCoins(List<int[]> positions, String imageName) {
    List<Coin> coins = new ArrayList<>();
    Image coinImage = loadImage(imageName);
    for (int[] pos : positions) {
      coins.add(new Coin(pos[0], pos[1], 30, 30, coinImage));
    }
    return coins;
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

  public static void resetBonusCoins() {
    // 코인 리스트 초기화
    largeCoins = Coin.createCoins(Coin.largeCoinPositions, "coin.png");
    mediCoins = Coin.createCoins(Coin.medicoinPositions, "medicoin.png");
    smallCoins = Coin.createCoins(Coin.smallcoinPositions, "smallcoin.png");
  }

}




