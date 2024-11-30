package org.example.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javazoom.jl.player.Player;
import lombok.Getter;
import lombok.Setter;
import org.example.Manager.GameInitializer;
import org.example.Manager.GameKeyAdapter;
import org.example.Manager.GameManager;
import org.example.Manager.GameTimer;
import org.example.Manager.IconManager;
import org.example.Manager.PointsManager;
import org.example.Manager.ProfessorManager;
import org.example.entity.Blanket;
import org.example.entity.Coin;
import org.example.entity.GamePlayer;
import org.example.entity.Icon;
import org.example.object.CoinCrash;
import org.example.object.IconCrash;

@Getter
@Setter

public class GamePanel extends JPanel {

  private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName()); // 강한 로그 사용
  public CoinCrash coinCrash;
  public GamePlayer gamePlayer; //이거 메인캐릭터임^^
  public int remainingTime = 30; // 남은 시간 (30초)
  private PointsManager pointsManager;
  private Blanket blanket;
  private IconCrash iconCrash;
  private Timer timer, countTimer;
  private IconManager iconManager;
  private BufferedImage backgroundImage;
  private JTextField curpointText;
  private ProfessorManager professorManager;
  private JLabel timerLabel;
  private JLabel gradeLabel;
  private Thread sound;
  private boolean isSoundPlaying = false;
  private Player mp3Player;

  public GamePanel(PointsManager pointsManager) {
    this.pointsManager = pointsManager;
    blanket = new Blanket();

    try {
      backgroundImage = ImageIO.read(
          Objects.requireNonNull(
              getClass().getResourceAsStream("/img/backgrounds/backgroundReal.jpg")));
    } catch (IOException e) {
      LOGGER.severe("Failed to load background image: " + e.getMessage());
    }

    // Player 객체 생성 (초기 위치와 크기 설정)
    gamePlayer = new GamePlayer(500, 500, 60, 90);

    curpointText = createPointsTextField();
    setLayout(null);
    this.add(curpointText);

    timerLabel = new JLabel(remainingTime + " 초", SwingConstants.CENTER);
    timerLabel.setFont(new Font("Galmuri11 Regular", Font.BOLD, 18));
    timerLabel.setBounds(50, 0, 150, 30);
    timerLabel.setForeground(Color.BLACK);
    this.add(timerLabel);

    gradeLabel = new JLabel("1학년", SwingConstants.CENTER);
    gradeLabel.setFont(new Font("Galmuri11 Regular", Font.BOLD, 18));
    gradeLabel.setBounds(20, 0, 100, 30);
    gradeLabel.setForeground(Color.GREEN);
    this.add(gradeLabel);

    coinCrash = new CoinCrash(this, null, pointsManager);
    iconCrash = new IconCrash(this, gamePlayer);
    iconCrash.addEntity(gamePlayer);
    coinCrash.addEntity(gamePlayer);
    iconManager = new IconManager();

    // 플레이어 방향키로 이동
    setFocusable(true);
    addKeyListener(new GameKeyAdapter(gamePlayer));
    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    // Blanket에 player 객체 설정
    blanket.setPlayer(gamePlayer);

    // 키 이벤트 추가
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
          blanket.handleFKeyEvent();
        }
      }
    });

    setFocusable(true);

    professorManager = new ProfessorManager(
        this.getGamePlayer()
    );

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);
    GameInitializer.initializeCoinEntities(coinCrash);
    GameInitializer.initializeIconEntities(iconCrash);
  }

  private JTextField createPointsTextField() {
    JTextField textField = new JTextField(pointsManager.getPoints() + "원");
    textField.setFont(new Font("Galmuri11 Regular", Font.BOLD, 20));
    textField.setForeground(Color.black);
    textField.setEditable(false);
    textField.setOpaque(false);
    textField.setFocusable(false);
    textField.setBorder(null);
    textField.setBounds(85, 93, 150, 30);
    return textField;
  }

  // 학년 업데이트함수
  public void updateGrade(int grade) {
    gradeLabel.setText(grade + "학년");
  }

  public void updateCurpointText() {
    curpointText.setText(pointsManager.getPoints() + "만 원");

    if (pointsManager.getPoints() >= 5 && !pointsManager.getisFirstFive()) {
      pointsManager.isFirstFive = true;
      blanket.incrementCount();
      if (pointsManager.getPoints() % 10 == 0) {
        blanket.incrementCount();
      }
    } else if (pointsManager.getPoints() >= 5 && pointsManager.getPoints() % 10 == 0) {
      blanket.incrementCount();
    }
    repaint();
  }

  public void startGame() {
    timer = new GameTimer(iconManager, coinCrash, iconCrash, professorManager, this);
    this.repaint();
    gamePlayer.x = 500;
    gamePlayer.y = 500;

    if (countTimer != null) {
      countTimer.stop();
    }
    remainingTime = 30;
    timerLabel.setText(remainingTime + " 초");
    countTimer = new Timer(1000, e -> {
      if (remainingTime > 0) {
        remainingTime--;
        timerLabel.setText(remainingTime + " 초");
      } else {
        ((Timer) e.getSource()).stop();
      }
      repaint();
    });
    countTimer.start();
    resumeSound();
    timer.start();
  }

  public void stopGame() {
    if (timer != null) {
      timer.stop();
    }
    professorManager.stop();
    pauseSound();
  }

  public GamePlayer getPlayer() {
    return gamePlayer;
  }

  public void coinPosition() {

    if (GameManager.currentCycleCount > 0) {
      GameInitializer.coinNumber -= 2; // 두 번째 사이클부터 코인 수 감소
    } else {
      GameInitializer.coinNumber = 7; // 첫 사이클에서는 코인을 7개로 초기화
    }

    Coin.arraycoin.clear(); // 기존 코인 초기화
    coinCrash.clearEntities(); // 기존 충돌 엔티티 제거
    GameInitializer.initializeCoinEntities(coinCrash); // 새로 초기화
    Coin.increaseSpeedLevel();
    coinCrash.addEntity(gamePlayer);
  }

  public void iconPosition() {

    Icon.iconList.clear();
    iconCrash.clearEntities();
    GameInitializer.initializeIconEntities(iconCrash);
    Icon.increaseSpeedLevel();
    iconCrash.addEntity(gamePlayer);
  }

  public void reset() {

    gamePlayer.x = 500;
    gamePlayer.y = 500;
    gamePlayer.setMovable(true);

    // 새로 초기화된 코인만 추가
    coinPosition();
    iconPosition();

    pointsManager.isFirstFive = false;

    iconCrash.updateGradeText(4.5);
    iconCrash.updateGradeImage(4.5);

    blanket.resetBlanket();
    updateCurpointText();

    if (timer != null) {
      timer.stop();
    }
    timer = new GameTimer(iconManager, coinCrash, iconCrash, professorManager, this);

    updateGrade(1); // 1학년으로 초기화

    // 교수님 상태 초기화
    professorManager.stop();

    repaint();
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

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

    gamePlayer.draw(g);

    // 좌측 상단에 띄울 코인 이미지
    if (CoinCrash.getCoinImage() != null) {
      g.drawImage(CoinCrash.getCoinImage(), 50, 90, 30, 30, null);
    }
    professorManager.draw(g);

    g.drawImage(blanket.itemblanket, 180, 95, 45, 35, null);
    g.setFont(new Font("Galmuri11 Regular", Font.BOLD, 20));
    g.setColor(Color.BLACK);
    g.drawString("x" + blanket.getCount(), 228, 114); // Blanket 옆에 카운트 텍스트
  }

  public void playGamePanelSound() {
    if (!isSoundPlaying) {
      isSoundPlaying = true;
      sound = new Thread(() -> {
        try (InputStream fis = getClass().getResourceAsStream(
            "/audio/gamePanel.mp3")) {
          mp3Player = new Player(Objects.requireNonNull(fis));
          mp3Player.play();
          isSoundPlaying = false;
        } catch (Exception e) {
          System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
          isSoundPlaying = false;
        }
      });
      sound.start();
    }
  }

  public void pauseSound() {
    if (isSoundPlaying && mp3Player != null) {
      mp3Player.close();
      isSoundPlaying = false;
    }
  }

  // 사운드 재개
  public void resumeSound() {
    if (!isSoundPlaying) {
      playGamePanelSound();
    }
  }
}