package org.example.panels;

import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.entity.Coin;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.object.CoinCrash;
import org.example.object.StarCrash;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BounsPanel extends JPanel {
    private GameManager gm;
    private final Image largecoin, medicoin, smallcoin;
    private Player bonusplayer;
    private final JPanel screenbonus;
    private final Timer colorTimer;
    private Timer collisionTimer;  // 충돌 체크를 위한 타이머
    private StarPanel starPanel;

    int centerX = 1080 / 2 - 15; // 패널 중앙 X 좌표
    int centerY = 720 / 2 - 15; // 패널 중앙 Y 좌표

    public BounsPanel(GameManager gm) {
        starPanel = new StarPanel(this.gm);
        this.gm = gm;
        setLayout(null); // CardLayout 대신 절대 레이아웃 사용

        // screenbonus 패널 설정
        screenbonus = new JPanel();
        screenbonus.setLayout(null); // 절대 레이아웃 사용
        screenbonus.setBackground(Color.RED);
        screenbonus.setBounds(0, 0, 1080, 740); // 화면 크기와 동일하게 설정

        JLabel screenbonusLabel = new JLabel("BONUS TIME!");
        screenbonusLabel.setFont(new Font("Neo둥근모", Font.BOLD, 30));
        screenbonusLabel.setBounds(450, 360 - 15, 300, 40);
        screenbonus.add(screenbonusLabel);

        //코인들과 충돌함수를 CoinCrash에서 어떻게 해결할 수 있을지 지피티한테 물어보기
        //player의 위치와 points를 그대로 가져오는 방법도 지피티한테 물어보기 근데 이건
        //참조변수를 써서 가져오면 될 것 같기도 함

        try {
            largecoin = ImageIO.read(new File("src/main/java/org/example/img/coin/coin.png"));
            medicoin = ImageIO.read(new File("src/main/java/org/example/img/coin/medicoin.png"));
            smallcoin = ImageIO.read(new File("src/main/java/org/example/img/coin/smallcoin.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // 무지개 색 배열 설정
        Color[] rainbowColors = {
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, new Color(128, 0, 128) // 보라색
        };

        colorTimer = new Timer(300, e -> {
            Color currentColor = screenbonus.getBackground();
            int nextIndex = (Arrays.asList(rainbowColors).indexOf(currentColor) + 1) % rainbowColors.length;
            screenbonus.setBackground(rainbowColors[nextIndex]);
        });


        this.add(screenbonus);// BounsPanel에 screenbonus 추가

        // bonusplayer 생성
        bonusplayer = new Player(540, 600, 100, 100);
        setFocusable(true);

        // GameKeyAdapter를 사용하여 key 이벤트 처리
        addKeyListener(new GameKeyAdapter(bonusplayer));

        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
            }
        });

        setPreferredSize(new Dimension(1080, 720));
        setOpaque(true);

        // Timer로 update 메서드를 주기적으로 호출
        Timer movementTimer = new Timer(16, e -> {
            bonusplayer.update(); // player 객체의 위치 업데이트
            repaint(); // 위치 변경 반영
        });
        movementTimer.start(); // Timer 시작


    }

    public void bonusColor() {
        colorTimer.start();

        // 3초 뒤에 screenbonus를 사라지게 하고 화면 전환
        Timer transitionTimer = new Timer(3000, e -> {
            colorTimer.stop(); // 색상 전환 타이머 중지

            // screenbonus 패널을 삭제
            BounsPanel.this.remove(screenbonus); // 화면에서 삭제
            BounsPanel.this.repaint(); // 화면 갱신
        });
        transitionTimer.setRepeats(false); // 한 번만 실행
        transitionTimer.start();
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.decode("#B0BABA"));
        g.fillRect(0, 0, getWidth(), getHeight());
        bonusplayer.draw(g);
        drawCoin(g);

        // 하트 형태를 만들기 위해 코인 이미지를 배치


    }

    public void drawCoin(Graphics g) {
        g.drawImage(largecoin, centerX, centerY + 90, 30, 30, this);
        g.drawImage(largecoin, centerX, centerY + 130, 30, 30, this);
        g.drawImage(largecoin, centerX + 60, centerY + 75, 30, 30, this);
        g.drawImage(largecoin, centerX - 60, centerY + 75, 30, 30, this);
        g.drawImage(largecoin, centerX + 60, centerY + 115, 30, 30, this);
        g.drawImage(largecoin, centerX - 60, centerY + 115, 30, 30, this);
        g.drawImage(largecoin, centerX - 30, centerY + 150, 30, 30, this);
        g.drawImage(largecoin, centerX + 30, centerY + 150, 30, 30, this);
        g.drawImage(largecoin, centerX, centerY + 170, 30, 30, this);
        g.drawImage(medicoin, centerX - 30, centerY + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 30, centerY + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 90, centerY + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 90, centerY + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 90, centerY + 95, 30, 30, this);
        g.drawImage(medicoin, centerX - 30, centerY + 100, 30, 30, this);
        g.drawImage(medicoin, centerX + 30, centerY + 100, 30, 30, this);
        g.drawImage(medicoin, centerX - 90, centerY + 95, 30, 30, this);
        g.drawImage(medicoin, centerX + 90, centerY + 130, 30, 30, this);
        g.drawImage(medicoin, centerX - 90, centerY + 130, 30, 30, this);
        g.drawImage(medicoin, centerX + 60, centerY + 160, 30, 30, this);
        g.drawImage(medicoin, centerX - 60, centerY + 160, 30, 30, this);
        g.drawImage(medicoin, centerX - 30, centerY + 190, 30, 30, this);
        g.drawImage(medicoin, centerX + 30, centerY + 190, 30, 30, this);
        g.drawImage(smallcoin, centerX - 60, centerY + 30, 30,30,this);
        g.drawImage(smallcoin, centerX + 60, centerY+ 30, 30,30,this);
        g.drawImage(smallcoin, centerX + 115, centerY + 95, 30,30,this);
        g.drawImage(smallcoin, centerX - 115, centerY + 95, 30,30,this);
        g.drawImage(smallcoin, centerX, centerY + 220, 30,30,this);
        //좌측 상단 하트
        g.drawImage(largecoin, centerX - 220, centerY - 240 + 90, 30, 30, this);
        g.drawImage(largecoin, centerX - 220, centerY - 240 + 130, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 + 60, centerY - 240 + 75, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 - 60, centerY - 240 + 75, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 + 60, centerY - 240 + 115, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 - 60, centerY - 240 + 115, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 - 30, centerY - 240 + 150, 30, 30, this);
        g.drawImage(largecoin, centerX - 220 + 30, centerY - 240 + 150, 30, 30, this);
        g.drawImage(largecoin, centerX - 220, centerY - 240 + 170, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 30, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 30, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 90, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 90, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 90, centerY - 240 + 95, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 30, centerY - 240 + 100, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 30, centerY - 240 + 100, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 90, centerY - 240 + 95, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 90, centerY - 240 + 130, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 90, centerY - 240 + 130, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 60, centerY - 240 + 160, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 60, centerY - 240 + 160, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 - 30, centerY - 240 + 190, 30, 30, this);
        g.drawImage(medicoin, centerX - 220 + 30, centerY - 240 + 190, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220 - 60, centerY - 240 + 30, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220 + 60, centerY - 240 + 30, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220 + 115, centerY - 240 + 95, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220 - 115, centerY - 240 + 95, 30, 30, this);
        g.drawImage(smallcoin, centerX - 220, centerY - 240 + 220, 30, 30, this);
        //우측 상단 하트
        g.drawImage(largecoin, centerX + 220, centerY - 240 + 90, 30, 30, this);
        g.drawImage(largecoin, centerX + 220, centerY - 240 + 130, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 + 60, centerY - 240 + 75, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 - 60, centerY - 240 + 75, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 + 60, centerY - 240 + 115, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 - 60, centerY - 240 + 115, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 - 30, centerY - 240 + 150, 30, 30, this);
        g.drawImage(largecoin, centerX + 220 + 30, centerY - 240 + 150, 30, 30, this);
        g.drawImage(largecoin, centerX + 220, centerY - 240 + 170, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 30, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 30, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 90, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 90, centerY - 240 + 60, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 90, centerY - 240 + 95, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 30, centerY - 240 + 100, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 30, centerY - 240 + 100, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 90, centerY - 240 + 95, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 90, centerY - 240 + 130, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 90, centerY - 240 + 130, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 60, centerY - 240 + 160, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 60, centerY - 240 + 160, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 - 30, centerY - 240 + 190, 30, 30, this);
        g.drawImage(medicoin, centerX + 220 + 30, centerY - 240 + 190, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220 - 60, centerY - 240 + 30, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220 + 60, centerY - 240 + 30, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220 + 115, centerY - 240 + 95, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220 - 115, centerY - 240 + 95, 30, 30, this);
        g.drawImage(smallcoin, centerX + 220, centerY - 240 + 220, 30, 30, this);
    }

    // bonusplayer의 이동과 그리기를 업데이트하는 메서드 (필요에 따라 추가)
    public void update() {
    }

    // 보너스 패널 초기화 메서드
    public void initBonus() {
        // 보너스 패널 초기화 로직 추가
        // 필요한 초기화 작업 수행
    }
}

