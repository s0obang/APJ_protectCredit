package org.example.panels;

import javazoom.jl.player.Player;
import org.example.Manager.GameKeyAdapter;
import org.example.Manager.PointsManager;
import org.example.entity.Coin;
import org.example.entity.GamePlayer;
import org.example.object.CoinCrash;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javazoom.jl.player.Player;

public class BonusPanel extends JPanel {
    private PointsManager pointsManager; // PointsManager 추가
    private RainbowPanel rainbowPanel;
    private JTextField curpointText; // BonusPanel의 JTextField
    public CoinCrash coinCrash;
    public GamePlayer bonusplayer;
    public boolean isCoinsInitialized = false; // 코인 초기화 여부 플래그
    public Timer timer, countTimer;
    private JLabel timerLabel; // 타이머 표시용 JLabel 추가
    public int remainingTime = 10; // 남은 시간 (10초)
    private boolean isTimerRunning = false;
    public Thread sound;
    public boolean isSoundPlaying = false; // 오디오 재생 상태 추적
    public Player mp3Player; // MP3 재생을 위한 Player 객체
    private Image backgroundImage;


    public BonusPanel(PointsManager pointsManager) {
        this.pointsManager = pointsManager;
        rainbowPanel = new RainbowPanel();
        coinCrash = new CoinCrash(null,this, pointsManager);
        // 텍스트 필드 초기화
        curpointText = createPointsTextField();
        setLayout(null);
        this.add(curpointText);

        // 배경 이미지 로드
        try {
            backgroundImage = ImageIO.read(
                    new File("src/main/java/org/example/img/backgrounds/backFever.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 타이머 표시용 JLabel 초기화
        timerLabel = new JLabel("남은 시간: " + remainingTime + "초", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Neo둥근모", Font.BOLD, 20));
        timerLabel.setBounds(40, 40, 200, 50);
        timerLabel.setForeground(Color.BLACK);
        this.add(timerLabel);

        setPreferredSize(new Dimension(1080, 720)); // 패널 크기 설정
        bonusplayer = new GamePlayer(500, 100, 100, 100);

        //플레이어 방향키로 이동하느느거!!!
        setFocusable(true);
        addKeyListener(new GameKeyAdapter(bonusplayer));
        addHierarchyListener(e -> {
            if (isShowing()) {
                requestFocusInWindow();
            }
        });


        setOpaque(true);

        // 타이머 설정
        countTimer = new Timer(1000, e -> { // 1초마다 실행
            if (remainingTime > 0) {
                remainingTime--; // 남은 시간 감소
                timerLabel.setText("남은 시간 : " + remainingTime + "초"); // 타이머 갱신
            } else {
                ((Timer) e.getSource()).stop(); // 타이머 종료
            }
            repaint();
        });

        // 타이머 설정
        timer = new Timer(30, e -> {
            if(!isCoinsInitialized) {
                Coin.resetBonusCoins(); // 패널이 표시될 때마다 코인 초기화
                isCoinsInitialized = true; // 초기화 플래그 설정
                coinCrash.checkBonusCollisions();  // CoinCrash의 checkBonusCollisions 호출
                repaint(); // 화면 갱신 -> player 움직임
            }
            coinCrash.checkBonusCollisions();
            repaint();
        });
        timer.start(); // Start the timer

    }

    private JTextField createPointsTextField() {
        JTextField textField = new JTextField(pointsManager.getPoints() + "원");
        textField.setFont(new Font("Neo둥근모", Font.BOLD, 20));
        textField.setForeground(Color.black);
        textField.setEditable(false);
        textField.setOpaque(false);
        textField.setFocusable(false);
        textField.setBorder(null);
        textField.setBounds(85, 93, 150, 30);
        return textField;
    }

    // MP3 파일 재생 메서드
    public void playbonusPanelSound() {
        if (!isSoundPlaying) {
            isSoundPlaying = true;
            sound = new Thread(() -> {
                try (FileInputStream fis = new FileInputStream("src/main/java/org/example/audio/bonuspanel.mp3")) {
                    mp3Player = new Player(fis);
                    mp3Player.play(); // MP3 파일 재생
                    isSoundPlaying = false; // 오디오 재생 완료
                } catch (Exception e) {
                    System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
                    isSoundPlaying = false; // 오류 발생 시 상태 변경
                }
            });
            sound.start(); // 비동기적으로 오디오 시작
        }
    }


    public void updateCurpointText() {
        curpointText.setText(pointsManager.getPoints() + "만 원");
        repaint();
    }

    public void updateTime() {
        timerLabel.setText("남은 시간 : " + remainingTime + "초");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            // BonusPanel이 보일 때는 타이머 시작
            startTimer();
        } else {
            // BonusPanel이 사라질 때는 타이머 중지
            stopTimer();
            // setVisible이 false일 때 sound 스레드 멈추기
            if (sound != null && sound.isAlive()) {
                sound.interrupt();  // 스레드 멈추기
                isSoundPlaying = false; // 오디오 재생 상태 초기화
                if (mp3Player != null) {
                    mp3Player.close(); // 오디오 파일 종료
                }
            }
        }
    }

    // 타이머 시작 메서드
    private void startTimer() {
        if (timer != null && !isTimerRunning) {
            timer.start();
            isTimerRunning = true;
        }
    }

    // 타이머 중지 메서드
    private void stopTimer() {
        if (timer != null && isTimerRunning) {
            timer.stop();
            isTimerRunning = false;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        // 보너스 플레이어 및 코인 그리기
        bonusplayer.draw(g);

        // 각 코인들을 화면에 그리기
        for (Coin coin : Coin.largeCoins) {
            coin.draw(g);
        }
        for (Coin coin : Coin.mediCoins) {
            coin.draw(g);
        }
        for (Coin coin : Coin.smallCoins) {
            coin.draw(g);
        }

        // 좌측 상단에 띄울 코인 이미지 그리기
        if (CoinCrash.getCoinImage() != null) {
            g.drawImage(CoinCrash.getCoinImage(), 50, 90, 30, 30, null);
        }
    }

}