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

import static org.example.entity.Coin.random;


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
  private ProfessorManager professorManager;

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
    // 교수님 주기적 등장 시작
    professorManager.start(5000); //5초마다 등장
    timer.start();

    // 코인 감소 로직 추가
    if (Coin.arraycoin.size() > 2) { // 최소 코인 수를 유지하려면 이 조건 사용
      if(GameManager.currentCycleCount == 0) return;
      for (int i = 0; i < 2; i++) {
        Coin.arraycoin.remove(Coin.arraycoin.size() - 1);
      }
    }
  }

  public void stopGame() {
    if (timer != null) {
      timer.stop();
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void reset() {
    // 플레이어 초기 위치와 상태 재설정
    player.x = 500;
    player.y = 500;
    player.setMovable(true); // 이동 가능 상태로 설정
    player.setGPA(4.5);  // 예시: GPA를 초기값으로 설정 (4.5)

    // GPA 텍스트 및 이미지 초기화 (IconCrash에서 이를 처리)
    iconCrash.updateGradeText(4.5);  // 초기 GPA 값 설정
    iconCrash.updateGradeImage(4.5);  // 초기 GPA 이미지 설정

    GameInitializer.coinNumber = 7;
    pointsManager.isFirstFive = false;

    for (Coin coin : Coin.arraycoin) {
      coin.setY(random.nextInt(120));
    }

    for (Icon icon : Icon.iconList) {
      icon.setY(random.nextInt(120));
    }


    blanket.resetBlanket();

    // 텍스트 필드 초기화
    updateCurpointText();

    timer = new GameTimer(iconManager, coinCrash, iconCrash, professorManager, this);

    // 교수님 상태 초기화
    professorManager.stop();

    // 패널 다시 그리기
    repaint();
    startGame();
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