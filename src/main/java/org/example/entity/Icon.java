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

public abstract class Icon extends Entity {

  public static ArrayList<Icon> iconList = new ArrayList<>(); // 아이콘 리스트
  private final BufferedImage iconimg;  // 아이콘 이미지
  private final Random random = new Random();
  private int x, y, speed;
  private int scoreEffect;  // 점수 증가 또는 감소 효과 (1이면 증가, -1이면 감소)

  public Icon(int x, int y, int width, int height) {
    super(x, y, width, height);
    this.x = random.nextInt(1080 - 20);
    this.y = random.nextInt(180);  // 아이콘이 화면 위에서 떨어지도록 초기 위치 설정
    this.speed = random.nextInt(3) + 2;  // 아이콘의 속도를 2~4 사이에서 랜덤으로 설정

    // 아이콘 이미지 로드
    String[] iconPaths = {
        "src/main/java/org/example/img/gradeItem/F.png",
        "src/main/java/org/example/img/gradeItem/A+.png",
        "src/main/java/org/example/img/gradeItem/Coffee.png",
        "src/main/java/org/example/img/gradeItem/textBook.png"
    };
    String iconPath = iconPaths[random.nextInt(iconPaths.length)];

    try {
      iconimg = ImageIO.read(new File(iconPath));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    // 점수 효과 설정 (이미지에 따라 다르게 처리)
    if (iconPath.contains("A+") || iconPath.contains("Coffee")) {
      scoreEffect = 1;  // A+, Coffee 아이콘은 점수 증가
    } else if (iconPath.contains("F") || iconPath.contains("textBook")) {
      scoreEffect = -1;  // F, textBook 아이콘은 점수 감소
    }
  }

  // 새로운 아이콘 생성 및 리스트에 추가
  public static void createAndAddIcon(int width, int height) {
    Icon icon = new Icon(width, height, 30, 30) {
      @Override
      public void update() {
      }
    }; // 익명 서브클래스로 인스턴스 생성
    iconList.add(icon);
  }

  // 아이콘이 아래로 떨어지는 메서드
  public void fall() {
    y += speed;
    if (y > 720) {  // 패널 아래로 벗어나면
      y = -20;
      x = random.nextInt(1080 - 40);  // 새로운 x 위치
    }
    System.out.println("Icon position: " + y);

  }

  // 아이콘의 점수 효과를 반환하는 메서드 (1은 점수 증가, -1은 점수 감소)
  public int getScoreEffect() {
    return scoreEffect;
  }

  // 아이콘을 그리는 메서드
  public void draw(Graphics g) {
    int iconSize = 30;
    g.drawImage(iconimg, x, y, iconSize, iconSize, null);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(x, y, width, height);  // 아이콘 충돌 영역 정의
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (Icon icon : iconList) {
      int iconSize = 30;
      g.drawImage(iconimg, icon.x, icon.y, iconSize, iconSize, null);
    }

  }

  public void resetPosition() {
    // 아이콘 초기 위치로 리셋
    this.y = -20; // 화면 위쪽에서 다시 떨어지도록
    this.x = random.nextInt(1080 - 40); // 무작위 x 위치
  }
}
