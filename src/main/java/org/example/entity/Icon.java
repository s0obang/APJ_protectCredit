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
  public static int speed;
  private int scoreEffect;
  private static int speedLevel = 1;
  private IconType iconType; // 아이콘 타입을 저장할 필드 추가

  // 아이템 타입 열거형
  public enum IconType {
    F, TEXTBOOK, APLUS, COFFEE, greenF, redA, BOOK
  }
  // 스폰 관리를 위한 정적 변수들
  private static final long SPAWN_CYCLE = 8000; // 8초 주기
  private static final int ITEMS_PER_CYCLE = 18; // 주기당 총 아이템 수
  private static long cycleStartTime = System.currentTimeMillis();
  private static int currentCycleSpawns = 0;
  private static Map<IconType, Integer> remainingSpawns = new HashMap<>();

  static {
    resetSpawnCounts();
  }

  // 스폰 카운트 초기화
  private static void resetSpawnCounts() {
    remainingSpawns.clear();
    remainingSpawns.put(IconType.F, 3);        // F는 주기당 2번
    remainingSpawns.put(IconType.TEXTBOOK, 3); // 교과서는 주기당 2번
    remainingSpawns.put(IconType.greenF, 3);
    remainingSpawns.put(IconType.BOOK, 3);
    remainingSpawns.put(IconType.APLUS, 2);    // A+는 주기당 2번
    remainingSpawns.put(IconType.COFFEE, 2);   // 커피는 주기당 2번
    remainingSpawns.put(IconType.redA, 2);
  }
  public Icon(int x, int y, int width, int height) {
    super(x, y, width, height);
    this.x = random.nextInt(1080 - 20);
    this.y = random.nextInt(180);  //아이콘이 화면 위에서 떨어지도록 초기 위치 설정
    this.speed = random.nextInt(3) + 2;  //아이콘의 속도를 2~4 사이에서 랜덤으로 설정

    // 아이콘 타입 선택 및 이미지 로드
    this.iconType = selectNextIconType();
    String iconPath = getIconPath(this.iconType);

    try {
      iconimg = ImageIO.read(new File(iconPath));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    // 점수 효과 설정
    if (iconType == IconType.APLUS || iconType == IconType.COFFEE || iconType == IconType.redA) {
      scoreEffect = 1;
    } else {
      scoreEffect = -1;
    }
  }

  private String getIconPath(IconType type) {
    switch (type) {
      case F: return "src/main/java/org/example/img/gradeItem/F.png";
      case TEXTBOOK: return "src/main/java/org/example/img/gradeItem/textBook.png";
      case APLUS: return "src/main/java/org/example/img/gradeItem/A+.png";
      case COFFEE: return "src/main/java/org/example/img/gradeItem/Coffee.png";
      case redA: return "src/main/java/org/example/img/gradeItem/redA+.png";
      case BOOK: return "src/main/java/org/example/img/gradeItem/book.png";
      case greenF: return "src/main/java/org/example/img/gradeItem/greenF.png";
      default: throw new IllegalStateException("Invalid icon type");
    }
  }

  // 다음 아이콘 타입을 선택하는 메서드
  private static IconType selectNextIconType() {
    // 남은 스폰이 있는 아이콘 타입들만 선택
    List<IconType> availableTypes = new ArrayList<>();
    for (Map.Entry<IconType, Integer> entry : remainingSpawns.entrySet()) {
      if (entry.getValue() > 0) {
        availableTypes.add(entry.getKey());
      }
    }

    if (availableTypes.isEmpty()) {
      resetSpawnCounts();
      return selectNextIconType();
    }

    // 랜덤하게 하나 선택
    IconType selectedType = availableTypes.get(random.nextInt(availableTypes.size()));
    remainingSpawns.put(selectedType, remainingSpawns.get(selectedType) - 1);
    return selectedType;
  }

  // 새로운 아이콘 생성 및 리스트에 추가
  public static void createAndAddIcon(int width, int height) {
    long currentTime = System.currentTimeMillis();

  // 주기가 끝났는지 체크
    if (currentTime - cycleStartTime >= SPAWN_CYCLE) {
      cycleStartTime = currentTime;
      currentCycleSpawns = 0;
      resetSpawnCounts();
    }

    // 주기당 아이템 수 체크
    //if (currentCycleSpawns < ITEMS_PER_CYCLE) {
      Icon icon = new Icon(width, height, 40, 40) {
        @Override
        public void update() {
        }
      };
      iconList.add(icon);
      currentCycleSpawns++;
   // }
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
    speed += (int) Math.pow(speedLevel, 2); // 속도 증가
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
    int iconSize = (iconType == IconType.BOOK) ? 60 :
            (iconType == IconType.COFFEE || iconType == IconType.TEXTBOOK) ? 40 : 30;

    // 각 아이콘 타입에 따른 크기 및 위치 조정
    if (iconType == IconType.BOOK) {
      width = 60;
      height = 60;
      g.drawImage(iconimg, x - 15, y - 15, iconSize, iconSize, null);
    } else if (iconType == IconType.COFFEE || iconType == IconType.TEXTBOOK) {
      width = 40;
      height = 40;
      g.drawImage(iconimg, x - 5, y - 5, iconSize, iconSize, null);
    } else {
      width = 30;
      height = 30;
      g.drawImage(iconimg, x, y, iconSize, iconSize, null);
    }
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
