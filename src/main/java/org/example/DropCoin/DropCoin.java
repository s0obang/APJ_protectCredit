package org.example.DropCoin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DropCoin extends JFrame {
    DropCoin() {
        setTitle("포인트 등장");
        setSize(1000, 800);

        class Coin {
            int x, y;
            final int speed;  // 각 동전의 위치와 속도를 저장
            final BufferedImage image;

            public Coin(BufferedImage image, int x, int y) {
                this.image = image;
                this.x = x;
                this.y = y;
                this.speed = 5;  // 속도를 고정된 값으로 설정
            }

            public void fall() {
                y += speed;  // y 좌표를 속도만큼 증가시켜 아래로 이동
                if (y > 800) {  // 화면 아래로 넘어가면 다시 위로 이동
                    y = -25; // 새로운 y 위치를 프레임 위로 설정
                    x = new Random().nextInt(1000 - 25); // 새로운 x 위치 설정
                }
            }
        }

        class CoinPanel extends JPanel {
            final ArrayList<Coin> coins = new ArrayList<>(); //coin 이미지를 여러 개 만들기 위해
            final Random random = new Random();
            BufferedImage coinImage;
            final Timer timer;

            public CoinPanel() {
                try {
                    coinImage = ImageIO.read(new File("src/main/java/org/example/img/coin/coin.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 초기 동전 생성
                for (int i = 0; i < 5; i++) {  // 동전 4개 생성
                    int x = random.nextInt(1000 - 25);
                    int y = random.nextInt(280);
                    coins.add(new Coin(coinImage, x, y)); // 속도는 고정된 값으로 사용
                }


                // 타이머를 사용하여 각 동전의 위치 업데이트
                timer = new Timer(30, e -> {
                    for (Coin coin : coins) {
                        coin.fall();  // 각 동전의 위치를 아래로 이동
                    }
                    repaint(); // 화면을 다시 그려서 변경된 위치 반영
                });
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 모든 동전을 현재 위치에 그림
                for (Coin coin : coins) {
                    g.drawImage(coin.image, coin.x, coin.y, 25, 25, this);
                }
            }
        }

        add(new CoinPanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new DropCoin();
    }
}

