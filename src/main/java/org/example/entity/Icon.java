package org.example.entity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class Icon extends Entity {
    private final Image iconimg;  // 아이콘 이미지
    private final Random random = new Random();
    private int x, y, speed;
    public static ArrayList<Icon> iconList = new ArrayList<>(); // 아이콘 리스트

    public Icon(int x, int y, int width, int height) {
        super(x, y,width, height);
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

    // 아이콘을 그리는 메서드
    public void draw(Graphics g) {
        int iconSize = 20;
        g.drawImage(iconimg, x, y, iconSize, iconSize, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);  // 아이콘 충돌 영역 정의
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Icon icon : iconList) {
            int iconSize = 20;
            g.drawImage(iconimg, icon.x, icon.y, iconSize, iconSize, null);
        }

    }

    // 새로운 아이콘 생성 및 리스트에 추가
    public static void createAndAddIcon(int width, int height) {
        Icon icon = new Icon(width, height, 20, 20){
            @Override
            public void update() {}
        }; // 익명 서브클래스로 인스턴스 생성
        iconList.add(icon);
    }

    public void resetPosition() {
        // 아이콘 초기 위치로 리셋
        this.y = -20; // 화면 위쪽에서 다시 떨어지도록
        this.x = random.nextInt(1080 - 40); // 무작위 x 위치
    }
}
