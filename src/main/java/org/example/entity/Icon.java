package org.example.entity;

import org.example.Manager.GameManager;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

public abstract class Icon extends Entity {

  public static ArrayList<Icon> iconList = new ArrayList<>();
  private final BufferedImage iconimg;
  private static final Random random = new Random();
  private int x;
  private int y;
  private static int speed;
  private int scoreEffect;
  private static int speedLevel = 1;

  // 아이템 타입 열거형
  public enum IconType {
    F, TEXTBOOK, APLUS, COFFEE
  }

  private static final Map<IconType, Integer> SPAWN_COUNTS = new HashMap<>();
  private static final Map<IconType, String> ICON_PATHS = new HashMap<>();
  private static final long SPAWN_INTERVAL = 300000; // 300초마다 스폰 사이클 실행
  private static long lastSpawnTime = 0;
  private static int currentIndex = 0;

  static {
    // 각 아이템 타입별 10초당 출현 횟수 설정
    SPAWN_COUNTS.put(IconType.F, 6);        // F는 30초당 4번
    SPAWN_COUNTS.put(IconType.TEXTBOOK, 6); // 교과서는 30초당 4번
    SPAWN_COUNTS.put(IconType.APLUS, 5);    // A+는 30초당 3번
    SPAWN_COUNTS.put(IconType.COFFEE, 5);   // 커피는 30초당 3번

    // 각 아이템 타입별 이미지 경로 설정
    ICON_PATHS.put(IconType.F, "src/main/java/org/example/img/gradeItem/F.png");
    ICON_PATHS.put(IconType.TEXTBOOK, "src/main/java/org/example/img/gradeItem/textBook.png");
    ICON_PATHS.put(IconType.APLUS, "src/main/java/org/example/img/gradeItem/A+.png");
    ICON_PATHS.put(IconType.COFFEE, "src/main/java/org/example/img/gradeItem/Coffee.png");
  }

  // 아이템 타입을 순서대로 가져오는 리스트 생성
  private static List<IconType> createSpawnSequence() {
    List<IconType> sequence = new ArrayList<>();
    for (Map.Entry<IconType, Integer> entry : SPAWN_COUNTS.entrySet()) {
      for (int i = 0; i < entry.getValue(); i++) {
        sequence.add(entry.getKey());
      }
    }
    Collections.shuffle(sequence); // 순서를 섞어서 랜덤성 부여
    return sequence;
  }

  private static List<IconType> spawnSequence = createSpawnSequence();

  public Icon(int x, int y, int width, int height) {
    super(x, y, width, height);
    this.x = random.nextInt(1080 - 20);
    this.y = random.nextInt(180);  //아이콘이 화면 위에서 떨어지도록 초기 위치 설정
    this.speed = random.nextInt(3) + 2;  //아이콘의 속도를 2~4 사이에서 랜덤으로 설정

    // 다음 아이템 타입 선택
    IconType iconType = getNextIconType();
    String iconPath = ICON_PATHS.get(iconType);

    try {
      iconimg = ImageIO.read(new File(iconPath));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    // 점수 효과 설정
    if (iconType == IconType.APLUS || iconType == IconType.COFFEE) {
      scoreEffect = 1;
    } else {
      scoreEffect = -1;
    }
  }

  private static IconType getNextIconType() {
    if (currentIndex >= spawnSequence.size()) {
      spawnSequence = createSpawnSequence();
      currentIndex = 0;
    }
    return spawnSequence.get(currentIndex++);
  }
  // 새로운 아이콘 생성 및 리스트에 추가
  public static void createAndAddIcon(int width, int height) {
    long currentTime = System.currentTimeMillis();

    // SPAWN_INTERVAL(10초)가 지났는지 확인
    if (currentTime - lastSpawnTime >= SPAWN_INTERVAL) {
      // 새로운 스폰 사이클 시작
      spawnSequence = createSpawnSequence();
      currentIndex = 0;
      lastSpawnTime = currentTime;
    }

    Icon icon = new Icon(width, height, 40, 40) {
      @Override
      public void update() {
      }
    };
    iconList.add(icon);
  }
  // 아이콘이 아래로 떨어지는 메서드
  public void fall() {
    y += speed;
    if (y > 720) {  // 패널 아래로 벗어나면
      y = -20;
      x = random.nextInt(1080 - 40);  // 새로운 x 위치
    }
    //System.out.println("Icon position: " + y);

  }

  // 속도 레벨 증가 메서드
  public static void increaseSpeedLevel() {
    speedLevel++;
    speed = random.nextInt(3) + 2 + (int) Math.pow(speedLevel, 2); // 속도 증가
  }

  // 속도 레벨 리셋 메서드
  public void resetSpeedLevel() {
    speedLevel = 1;
    speed = random.nextInt(3) + 2; // 초기 속도로 리셋
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
