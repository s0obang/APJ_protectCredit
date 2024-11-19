package org.example.panels;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.example.Manager.*;
import org.example.entity.Blanket;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.example.Manager.GameInitializer;
import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.Manager.GameTimer;
import org.example.Manager.IconManager;
import org.example.Manager.ProfessorManager;
import org.example.entity.Coin;
import org.example.entity.Icon;
import org.example.entity.Player;
import org.example.object.CoinCrash;
import org.example.object.IconCrash;
import org.example.Manager.PointsManager;


public class GamePanel extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName()); // 강한 로그 사용
  private PointsManager pointsManager;
  private Blanket blanket;
  public CoinCrash coinCrash;
  public Player player; //이거 메인캐릭터임^^
  private IconCrash iconCrash;
  private Timer timer;
  private IconManager iconManager;
  private BufferedImage backgroundImage;
  private JTextField curpointText; // GamePanel의 JTextField
  private boolean blanketDisplayed; // Blanket 표시 여부
  private ProfessorManager professorManager;

  public GamePanel(PointsManager pointsManager) {
    this.pointsManager = pointsManager; // PointsManager 객체 생성
    blanketDisplayed = false;
    blanket = new Blanket();

    // 배경 이미지 로드
    try {
      backgroundImage = ImageIO.read(
          new File("src/main/java/org/example/img/backgrounds/backgroundReal.jpg"));
    } catch (IOException e) {
      LOGGER.severe("Failed to load background image: " + e.getMessage());
    }

    // Player 객체 생성 (초기 위치와 크기 설정)
    player = new Player(500, 500, 100, 100);

    // 텍스트 필드 초기화
    curpointText = createPointsTextField();
    setLayout(null);
    this.add(curpointText);

    // Crash 객체 생성 -> 충돌 감지에 저장
    coinCrash = new CoinCrash(this, null, pointsManager);
    iconCrash = new IconCrash(this, player);
    iconCrash.addEntity(player);
    coinCrash.addEntity(player);
    iconManager = new IconManager();

    // 플레이어 방향키로 이동
    setFocusable(true);
    addKeyListener(new GameKeyAdapter(player));
    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    // F키 이벤트 추가
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (blanket.getCount() > 0) {
          if (e.getKeyCode() == KeyEvent.VK_F && !blanket.isPressedF) {
            // F키가 눌리면 Blanket 카운트 감소하고 Player 이미지 변경
            blanket.decrementCount(); // blanket의 카운트 감소
            blanket.isPressedF = true;
            player.changeImage(); // player 이미지 변경

            // 기존 타이머가 있으면 취소
            if (blanket.timer != null) {
              blanket.timer.stop(); // 중첩 방지
            }

            // 새로운 타이머를 설정
            blanket.timer = new Timer(5000, ev -> {
              player.changeOriginImage(); // 원래 이미지로 복원
              blanket.isPressedF = false; // 상태 초기화
              blanket.timer = null; // 타이머 초기화
            });
            blanket.timer.setRepeats(false); // 한 번만 실행
            blanket.timer.start();
          }
        }
      }
    });


    professorManager = new ProfessorManager(
        this.getPlayer(),
        () -> {
          System.out.println("교수님 충돌");//로깅용임
          this.getPlayer().setMovable(false);
          new Timer(5000, ev -> {
            this.getPlayer().setMovable(true);
            System.out.println("복원~");//로깅용 22
          }).start();
        }
    );

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);
    GameInitializer.initializeEntities(coinCrash, iconCrash);
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

  public void updateCurpointText() {
    curpointText.setText(pointsManager.getPoints() + "만 원");

    // Blanket 표시 여부 업데이트
    if (pointsManager.getPoints() >= 1) {
      if (!blanketDisplayed && pointsManager.getPoints() % 2 ==0) {
        blanket.doubleincrementCount();
        blanketDisplayed = true; // Blanket을 표시 상태로 전환
      } else if (pointsManager.getPoints() % 10 == 0) {
        blanket.incrementCount(); // 10 단위 추가 시 카운터 증가
      }
    }

    repaint(); // 패널 다시 그리기
  }

  public void startGame() {
    timer = new GameTimer(iconManager, coinCrash, iconCrash, professorManager, this);
    this.repaint();
    player.x = 500;
    player.y = 500;
    // 교수님 주기적 등장 시작
    professorManager.start(5000); //5초마다 등장
    timer.start();
  }

  public void stopGame() {
    if (timer != null) {
      timer.stop();
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void changeImage() {
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // 배경 이미지 그리기
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    } else {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(0, 0, getWidth(), getHeight());
    }

    player.draw(g);
    for (Icon icon : Icon.iconList) {
      icon.draw(g);
    }
    for (Coin coin : Coin.arraycoin) {
      coin.draw(g);
    }

    // 좌측 상단에 띄울 코인 이미지 그리기
    if (CoinCrash.getCoinImage() != null) {
      g.drawImage(CoinCrash.getCoinImage(), 50, 90, 30, 30, null);
    }
    professorManager.draw(g);

    // Blanket 이미지와 카운터 그리기
    if (blanketDisplayed) {
      g.drawImage(blanket.itemblanket, 165, 95, 45, 35, null); // Blanket 이미지
      g.setFont(new Font("Neo둥근모", Font.BOLD, 20));
      g.setColor(Color.BLACK);
      g.drawString("x" + blanket.getCount(), 213, 114); // Blanket 옆에 카운트 텍스트
    }
  }
}