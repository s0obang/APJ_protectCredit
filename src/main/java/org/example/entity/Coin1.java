package org.example.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class Coin1 extends Entity {
    private final Image coinimg;
    private int boundaryWidth;  // 경계 너비
    private int boundaryHeight;
    private final Random random = new Random();
    int x, y, speed;
    public static ArrayList<Coin1> arraycoin = new ArrayList<>();

    public Coin1(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.speed = 7;
        this.x = random.nextInt(1080 - 20);
        this.y = random.nextInt(180);

        try {
            coinimg = ImageIO.read(new File("src/main/java/org/example/img/coin/coin.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
    public void fall() {
        y += speed;
        if (y > 720) {
            y = -20;
            x = random.nextInt(1080 - 40);
        }
    }

    public void draw(Graphics g) {
        int coinSize = 20;
        g.drawImage(coinimg, x, y, coinSize, coinSize, null);
    }

    @Override
    public Rectangle getBounds() {
        // 코인의 충돌 영역을 정의
        return new Rectangle(x, y, width, height); // 코인 이미지의 크기에 맞게 수정
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Coin1 coin : arraycoin) {
            int coinSize = 20;
            g.drawImage(coinimg, coin.x, coin.y, coinSize, coinSize, null);
        }

    }

    // 코인 생성 및 배열에 추가

    public static void createAndAddCoin ( int width, int height){
        Coin1 coin = new Coin1(width, height, 20, 20) {
            @Override
            public void update() {}
        }; // 익명 서브클래스로 인스턴스 생성
        arraycoin.add(coin);


    }

    public void resetPosition() {
        // 코인 초기 위치로 리셋
        this.y = -20; // 화면 위쪽에서 다시 떨어지도록
        this.x = random.nextInt(1080 - 40); // 무작위 x 위치
    }

}


