package org.example.Manager;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.example.entity.Icon;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.object.StarCrash;
import org.example.object.UserStatus;
import org.example.panels.*;

public class GameManager extends JFrame {
  private int currentCycleCount = 0;
  private final int maxCycleCount = 4;

  private static CardLayout cardLayout;
  private static JPanel mainPanel;
  private DatabaseManager dbManager;
  private LoginManager loginManager;
  private static GamePanel gamePanel;
  private static StarPanel starPanel;
  private static LevelUpPanel levelupPanel;
  public static BonusPanel bonusPanel;
  public static RainbowPanel rainbowPanel;
  public static Star star;
  public StarCrash starCrash;

  private Timer timer;
  private Timer levelUpTimer;
  private Timer starTimer;
  private Timer bonusTimer;
  private Timer returnToGameTimer;
  private Timer noCollisionTimer;

  private UserStatus userStatus;


  public GameManager(UserStatus status) {
    this.userStatus = status;
    FontManager.loadFonts();
    dbManager = DatabaseManager.getInstance();
    loginManager = new LoginManager(dbManager);

    setTitle("학점을 지켜라!");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setSize(1080, 768);

    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);
    //이부분 수정했어엽 민선아
    gamePanel = new GamePanel();
    levelupPanel = new LevelUpPanel();
    bonusPanel = new BonusPanel();
    starPanel = new StarPanel(this);
    starCrash = new StarCrash(this, starPanel);
    rainbowPanel = new RainbowPanel();
    // 각 화면을 패널로 추가
    mainPanel.add(new StartPanel(this, loginManager), "start");
    mainPanel.add(gamePanel, "game");
    mainPanel.add(starPanel, "star");
    mainPanel.add(levelupPanel, "levelup");
    mainPanel.add(bonusPanel, "bonus");
    mainPanel.add(rainbowPanel, "rainbow");

    add(mainPanel);
    setVisible(true);

    // 창 닫을 때 로그아웃
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        loginManager.logout();
        System.out.println("로그아웃 후 애플리케이션 종료.");
      }
    });
  }

  public void startGameCycle() {
    currentCycleCount = 0;
    startLevelUpPhase();
  }

  //마지막에는 endGameCycle()로 이동
  private void startLevelUpPhase() {
    if (currentCycleCount >= maxCycleCount) {
      endGameCycle();
      return;
    }

    // levelUpTimer 설정: 3초 후 levelup 패널로 전환 -> 한 학년 당 게임의 시간
    if (levelUpTimer != null) levelUpTimer.stop();
    levelUpTimer = new Timer(3000, e -> {
      switchToPanelWithDelay("levelup", 0);
      startStarPhase(); // levelup 패널로 전환 후 star 패널로 진행
    });
    levelUpTimer.setRepeats(false);
    levelUpTimer.start();
  }

  //star 패널로 이동 -> 레벨업 패널이 올라와있는 시간
  public void startStarPhase() {
    if (starTimer != null) starTimer.stop();
    starTimer = new Timer(3000, e -> {
      switchToPanelWithDelay("star", 0);

      // Star 객체 초기화
      star = new Star(0, 0, 60, 50);
      starPanel.initializeStar(star);

      // StarPanel 생성 시 StarCrash 객체를 전달
      starPanel = new StarPanel( this);

      // 충돌 체크
      if (starPanel.starCrash.checkCollision()) {
        if (currentCycleCount == maxCycleCount - 1) {
          startFinalBonusPhase();
        } else {
          startBonusPhase();
        }
      } else {
        startNoCollisionPhase();
      }
    });
    starTimer.setRepeats(false);
    starTimer.start();
  }


  //일반적으로 충돌할 경우 -> 보너스 패널로 이동
  private void startBonusPhase() {
    if (bonusTimer != null) bonusTimer.stop();
    bonusTimer = new Timer(1000, e -> {
      switchToPanelWithDelay("bonus", 0);
      starCrash.handleCollision();
      bonusPanel = new BonusPanel();

      // 10초 후 보너스 패널에서 게임 패널로 복귀
      if (returnToGameTimer != null) returnToGameTimer.stop();
      returnToGameTimer = new Timer(10000, e2 -> {
        switchToPanelWithDelay("game", 0);
        //gamePanel.player = new Player(500, 500, 100, 100);
        currentCycleCount++;
        startLevelUpPhase(); // 다음 사이클 시작
      });
      returnToGameTimer.setRepeats(false);
      returnToGameTimer.start();
    });
    bonusTimer.setRepeats(false);
    bonusTimer.start();
  }

  //마지막에 충돌할 경우
  private void startFinalBonusPhase() {
    if (bonusTimer != null) bonusTimer.stop();
    bonusTimer = new Timer(1000, e -> {
      switchToPanelWithDelay("bonus", 0);
      starCrash.handleCollision();

      // 마지막 사이클 -> 보너스 패널 10초 후 엔딩으로 이동
      if (returnToGameTimer != null) returnToGameTimer.stop();
      returnToGameTimer = new Timer(5000, e2 -> endGameCycle());
      returnToGameTimer.setRepeats(false);
      returnToGameTimer.start();
    });
    bonusTimer.setRepeats(false);
    bonusTimer.start();
  }

  //충돌이 없는 경우 (보너스 실패) -> 다음 학년으로 넘어가기
  private void startNoCollisionPhase() {
    if (noCollisionTimer != null) noCollisionTimer.stop();
    //스타와 함께 있었던 시간
    noCollisionTimer = new Timer(5000, e -> {
      starCrash.handleCollision();
      switchToPanelWithDelay("game", 0);
      currentCycleCount++;
      startLevelUpPhase(); // 다음 사이클 시작
    });
    noCollisionTimer.setRepeats(false);
    noCollisionTimer.start();
  }

  //이건 엔드 패널로 이동시키는 거 추가하면 될 듯
  private void endGameCycle() {
    // 게임 종료 처리
    switchToPanelWithDelay("game", 30000);
    if (timer != null) timer.stop();
    timer = new Timer(3000, e -> showEndScreen(true));
    timer.setRepeats(false);
    timer.start();
  }



  public static void switchToPanelWithDelay(String nextPanelName, int delayMillis) {
    Timer timer = new Timer(delayMillis, e -> {
      if (nextPanelName.equals("levelup") || (nextPanelName.equals("star")) || nextPanelName.equals("bonus")
              || nextPanelName.equals("end")) {
        gamePanel.stopGame();// 게임 일시정지
      } else if (nextPanelName.equals("game")) {
        gamePanel.startGame(); // 게임 재시작
        // 아이콘 속도 레벨 증가
        for (Icon icon : Icon.iconList) {
          icon.increaseSpeedLevel();
        }
      }
      cardLayout.show(mainPanel, nextPanelName);
    });
    timer.setRepeats(false); // 한 번만 실행되게 함
    timer.start();
  }

 //이 부분이 핵심인데요~ manager에서 처음부터 약간 몇초 몇초를 설계해야할거같아요
  public void startGameSequence() {
    showScreen("game");
    startGameCycle();
//    switchToPanelWithDelay("fever", 30000);
//    switchToPanelWithDelay("game", 40000);
//    // 아이콘 속도 레벨 증가
//    for (Icon icon : Icon.iconList) {
//      icon.increaseSpeedLevel();
//    }
  }


  // 화면 전환 메서드
  public void showScreen(String screenName) {
    cardLayout.show(mainPanel, screenName);
  }

  // 게임 종료 패널은 따로(boolean 값 필요!!)
  public void showEndScreen(boolean isGameOver) {
    mainPanel.add(new EndPanel(this, isGameOver), "end");
    showScreen("end");
    // 아이콘 속도 레벨 리셋
    for (Icon icon : Icon.iconList) {
      icon.resetSpeedLevel();
    }
  }

  public static GamePanel getGamePanel() {
    return gamePanel;
  }

  // getPanel 메서드 추가 -> 주영이 쓰는 패널에서 필요함.
  public static JPanel getPanel(String panelName) {
    // mainPanel에 등록된 패널을 이름에 맞게 반환
    switch (panelName) {
      case "game":
        return gamePanel;
      case "star":
        return starPanel;
      case "levelup":
        return levelupPanel;
      case "bonus":
        return bonusPanel;
      default:
        return null;  // 잘못된 이름이 들어오면 null 반환
    }
  }

}

