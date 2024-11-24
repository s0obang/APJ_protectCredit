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

import lombok.Getter;
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

import static org.example.entity.Coin.random;

@Getter
public class GamePanel extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName()); // 강한 로그 사용
  private PointsManager pointsManager;
  private Blanket blanket;
  public CoinCrash coinCrash;
  public Player player; //이거 메인캐릭터임^^
  private IconCrash iconCrash;
  private Timer timer, countTimer;
  private IconManager iconManager;
  private BufferedImage backgroundImage;
  private JTextField curpointText; // GamePanel의 JTextField
  private ProfessorManager professorManager;
  private JLabel timerLabel; // 타이머 표시용 JLabel 추가
  public int remainingTime = 30; // 남은 시간 (30초)
  private JLabel gradeLabel; // 학년 표시용 JLabel 추가

  public GamePanel(PointsManager pointsManager) {
    this.pointsManager = pointsManager; // PointsManager 객체 생성
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

    // 타이머 표시용 JLabel 초기화
    timerLabel = new JLabel("남은 시간: " + remainingTime + "초", SwingConstants.CENTER);
    timerLabel.setFont(new Font("Neo둥근모", Font.BOLD, 15));
    timerLabel.setBounds(120, 20, 150, 30);
    timerLabel.setForeground(Color.BLACK);
    this.add(timerLabel);

    // 학년 레이블 초기화 (새로 추가)
    gradeLabel = new JLabel("1학년", SwingConstants.CENTER);
    gradeLabel.setFont(new Font("Neo둥근모", Font.BOLD, 15));
    gradeLabel.setBounds(20, 0, 100, 30);
    gradeLabel.setForeground(Color.BLACK);
    this.add(gradeLabel);

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

    // Blanket에 player 객체 설정
    blanket.setPlayer(player);

    // 키 이벤트 추가
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
          blanket.handleFKeyEvent();
        }
      }
    });

    setFocusable(true); // 키 입력 활성화


    professorManager = new ProfessorManager(
        this.getPlayer()
    );

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);
    GameInitializer.initializeCoinEntities(coinCrash);
    GameInitializer.initializeIconEntities(iconCrash);
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

  // 학년 업데이트 메서드 추가
  public void updateGrade(int grade) {
    gradeLabel.setText(grade + "학년");
  }

  public void updateCurpointText() {
    curpointText.setText(pointsManager.getPoints() + "만 원");

    if(pointsManager.getPoints() >= 5 && !pointsManager.getisFirstFive()) {
      pointsManager.isFirstFive = true;
      blanket.incrementCount();
      if(pointsManager.getPoints() % 10 == 0) blanket.incrementCount();
    }
    else if(pointsManager.getPoints() >= 5 && pointsManager.getPoints() % 10 == 0)
    {
      blanket.incrementCount();
    }
      repaint(); // 패널 다시 그리기
  }

  public void startGame() {
    timer = new GameTimer(iconManager, coinCrash, iconCrash, professorManager, this);
    this.repaint();
    player.x = 500;
    player.y = 500;
    // 기존 타이머가 있다면 중지
    if (countTimer != null) {
      countTimer.stop();
    }

    // 타이머 설정
    remainingTime = 30; // 시간 명시적으로 초기화
    timerLabel.setText("남은 시간 : " + remainingTime + "초"); // 레이블 즉시 업데이트

    countTimer = new Timer(1000, e -> {
      if (remainingTime > 0) {
        remainingTime--;
        timerLabel.setText("남은 시간 : " + remainingTime + "초");
      } else {
        ((Timer) e.getSource()).stop();
      }
      repaint();
    });
    countTimer.start();
    // 교수님 주기적 등장 시작
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

  public void coinPosition() {
    // 첫 사이클에서는 coinNumber를 감소시키지 않음
    if (GameManager.currentCycleCount > 0) {
      GameInitializer.coinNumber -= 2; // 두 번째 사이클부터 코인 수 감소
    } else {
      GameInitializer.coinNumber = 7; // 첫 사이클에서는 코인을 7개로 초기화
    }

    Coin.arraycoin.clear(); // 기존 코인 초기화
    coinCrash.clearEntities(); // 기존 충돌 엔티티 제거
    GameInitializer.initializeCoinEntities(coinCrash); // 새로 초기화
    Coin.increaseSpeedLevel();
    coinCrash.addEntity(player);
  }

  public void iconPosition() {
    // 기존 코인 제거
    Icon.iconList.clear();
    iconCrash.clearEntities();
    GameInitializer.initializeIconEntities(iconCrash);
    Icon.increaseSpeedLevel();
    iconCrash.addEntity(player);
  }

  public void reset() {
    // 플레이어 초기 위치와 상태 재설정
    player.x = 500;
    player.y = 500;
    player.setMovable(true); // 이동 가능 상태로 설정

    // 새로 초기화된 코인만 추가
    coinPosition();
    iconPosition();

    pointsManager.isFirstFive = false;

    iconCrash.updateGradeText(4.5);
    iconCrash.updateGradeImage(4.5);

    blanket.resetBlanket();

    // 텍스트 필드 초기화
    updateCurpointText();

    // 타이머 초기화
    if (timer != null) {
      timer.stop();
    }
    timer = new GameTimer(iconManager, coinCrash, iconCrash, professorManager, this);

    updateGrade(1); // 1학년으로 초기화

    // 교수님 상태 초기화
    professorManager.stop();

    // 패널 다시 그리기
    repaint();
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

    for (Icon icon : Icon.iconList) {
      icon.draw(g);
    }
    for (Coin coin : Coin.arraycoin) {
      coin.draw(g);
    }

    player.draw(g);

    // 좌측 상단에 띄울 코인 이미지 그리기
    if (CoinCrash.getCoinImage() != null) {
      g.drawImage(CoinCrash.getCoinImage(), 50, 90, 30, 30, null);
    }
    professorManager.draw(g);

    g.drawImage(blanket.itemblanket, 180, 95, 45, 35, null); // Blanket 이미지
    g.setFont(new Font("Neo둥근모", Font.BOLD, 20));
    g.setColor(Color.BLACK);
    g.drawString("x" + blanket.getCount(), 228, 114); // Blanket 옆에 카운트 텍스트
  }
}