package org.example.Manager;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.example.entity.*;
import org.example.object.CoinCrash;
import org.example.entity.Coin;
import org.example.entity.GameResult;
import org.example.entity.Icon;
import org.example.entity.Player;
import org.example.entity.Star;
import org.example.object.StarCrash;
import org.example.object.UserStatus;
import org.example.panels.BonusPanel;
import org.example.panels.EndPanel;
import org.example.panels.GamePanel;
import org.example.panels.LevelUpPanel;
import org.example.panels.RainbowPanel;
import org.example.panels.StarPanel;
import org.example.panels.StartPanel;

public class GameManager extends JFrame {

  public static final int maxCycleCount = 4;
  public static int currentCycleCount = 0;
  public static BonusPanel bonusPanel;
  public static RainbowPanel rainbowPanel;
  public static Star star;
  public static boolean overStarTime = false;
  private static CardLayout cardLayout;
  private static JPanel mainPanel;
  private DatabaseManager dbManager;
  private LoginManager loginManager;
  private EndPanel endPanel;
  private PointsManager pointsManager;
  private static GamePanel gamePanel;
  private static StarPanel starPanel;
  private static LevelUpPanel levelupPanel;
  public CoinCrash coinCrash;
  public StarCrash starCrash;
  public Timer collisionCheckTimer;
  private Timer timer, levelUpTimer, starTimer, rainbowTimer, bonusTimer, returnToGameTimer, noCollisionTimer;
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
    pointsManager = new PointsManager();
    gamePanel = new GamePanel(pointsManager);
    bonusPanel = new BonusPanel(pointsManager);

    //이부분 수정했어엽 민선아
    levelupPanel = new LevelUpPanel();
    coinCrash = new CoinCrash(gamePanel, bonusPanel, pointsManager);
    starPanel = new StarPanel(this);
    starCrash = new StarCrash(this, starPanel);
    rainbowPanel = new RainbowPanel();
    endPanel = new EndPanel(this);
    // 각 화면을 패널로 추가
    mainPanel.add(new StartPanel(this), "start");
    mainPanel.add(gamePanel, "game");
    mainPanel.add(starPanel, "star");
    mainPanel.add(levelupPanel, "levelup");
    mainPanel.add(bonusPanel, "bonus");
    mainPanel.add(endPanel, "end");
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

  public static void switchToPanelWithDelay(String nextPanelName, int delayMillis) {
    System.out.println("Preparing to switch to: " + nextPanelName + " in " + delayMillis + " ms");
    Timer timer = new Timer(delayMillis, e -> {
      System.out.println("Attempting to switch to panel: " + nextPanelName);
      if (nextPanelName.equals("levelup") || (nextPanelName.equals("star")) || nextPanelName.equals(
          "bonus")
          || nextPanelName.equals("end") || nextPanelName.equals("rainbow")) {
        gamePanel.stopGame();// 게임 일시정지
      } else if (nextPanelName.equals("game")) {
        gamePanel.startGame(); // 게임 재시작
        // 아이콘 속도 레벨 증가
        for (Icon icon : Icon.iconList) {
          icon.increaseSpeedLevel();
        }
        // 코인 속도 레벨 증가
        for (Coin coin : Coin.arraycoin) {
          coin.increaseSpeedLevel();
        }
      }
      // 패널 전환
      System.out.println("Switching to: " + nextPanelName);
      cardLayout.show(mainPanel, nextPanelName);
      System.out.println("Successfully switched to: " + nextPanelName);
    });
    timer.setRepeats(false); // 한 번만 실행되게 함
    timer.start();
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

    //30초 뒤에 levelup패널로 전환
    if (levelUpTimer != null) levelUpTimer.stop();
    levelUpTimer = new Timer(10000, e -> {
      switchToPanelWithDelay("levelup", 0);
      startStarPhase(); // levelup 패널로 전환 후 star 패널로 진행
    });
    levelUpTimer.setRepeats(false);
    levelUpTimer.start();
  }

  //3초 뒤 star 패널로 이동
  public void startStarPhase() {
    if (starTimer != null) starTimer.stop();
    starTimer = new Timer(3000, e -> {
      switchToPanelWithDelay("star", 0);

      // Star 객체 초기화
      star = new Star(0, 0, 60, 50);
      starPanel.initializeStar(star);

      // StarPanel 생성 시 StarCrash 객체를 전달
      starPanel = new StarPanel(this);

      // 5초 동안 충돌 체크 반복
      final long startTime = System.currentTimeMillis();
      collisionCheckTimer = new Timer(100, event -> {
        long elapsedTime = System.currentTimeMillis() - startTime;

        // 충돌 체크 및 5초 경과 체크
        if (elapsedTime < 7000) {  // 5초 동안 반복
          starCrash.checkCollision();  // 충돌 체크

          // 충돌이 발생하면 바로 패널로 이동
          if (starCrash.distance < starCrash.collisionDistance) {
            System.out.println("충돌 발생!");
            ((Timer) event.getSource()).stop();  // Timer 종료

            if (currentCycleCount == maxCycleCount - 1) {
              System.out.println("충돌 발생!");
              startFinalBonusPhase();
            } else {
              startBonusPhase();
            }
          }
        } else {
          // 5초가 지나면 Timer를 종료하고 충돌이 없으면 다음 단계로 진행
          ((Timer) event.getSource()).stop();
          starCrash.handleCollision();
          overStarTime = true;
          startNoCollisionPhase();
        }
      });

      collisionCheckTimer.setRepeats(true);  // 100ms 간격으로 반복
      collisionCheckTimer.start();  // 충돌 체크 시작
    });

    starTimer.setRepeats(false);
    starTimer.start();
  }

  //일반적으로 충돌할 경우 -> 보너스 패널로 이동
  private void startBonusPhase() {
    if (rainbowTimer != null) rainbowTimer.stop();
    if (bonusTimer != null) bonusTimer.stop();
    if (returnToGameTimer != null) returnToGameTimer.stop();
    if(bonusPanel.timer != null) bonusPanel.timer.stop();

    bonusPanel.remainingTime = 10;
    bonusPanel.isCoinsInitialized = false;
    bonusPanel.bonusplayer.x = 500;
    bonusPanel.bonusplayer.y = 100;

    rainbowTimer = new Timer(0, e -> {
      switchToPanelWithDelay("rainbow", 0);

    bonusTimer = new Timer(3000, e2 -> {
      switchToPanelWithDelay("bonus", 0);
      bonusPanel.updateTime();
      bonusPanel.updateCurpointText(); // 포인트 동기화
      bonusPanel.timer.start();
      bonusPanel.countTimer.start();

      // 10초 후 보너스 패널에서 게임 패널로 복귀
    returnToGameTimer = new Timer(13000, e3 -> {
      switchToPanelWithDelay("game", 0);
      if(bonusPanel.timer != null) bonusPanel.timer.stop();
      gamePanel.updateCurpointText();
      currentCycleCount++;
      startLevelUpPhase(); // 다음 사이클 시작
  });
      returnToGameTimer.setRepeats(false);
      returnToGameTimer.start();
    });
      bonusTimer.setRepeats(false);
      bonusTimer.start();
    });
    rainbowTimer.setRepeats(false);
    rainbowTimer.start();

  }

  //마지막에 충돌할 경우
  private void startFinalBonusPhase() {
    if(rainbowTimer != null) rainbowTimer.stop();
    if (bonusTimer != null) bonusTimer.stop();
    if (returnToGameTimer != null) returnToGameTimer.stop();

    bonusPanel.remainingTime = 10;
    bonusPanel.isCoinsInitialized = false;

    rainbowTimer = new Timer(0, e1 -> {
      switchToPanelWithDelay("rainbow", 0);

    bonusTimer = new Timer(3000, e -> {
      switchToPanelWithDelay("bonus", 0);
      bonusPanel.updateTime();
      bonusPanel.updateCurpointText();
      bonusPanel.timer.start();
      bonusPanel.countTimer.start();

      // 마지막 사이클 -> 보너스 패널 10초 후 엔딩으로 이동
      returnToGameTimer = new Timer(13000, e2 -> {
        endGameCycle();
        if(bonusPanel.timer != null) bonusPanel.timer.stop();
      }
    );
      returnToGameTimer.setRepeats(false);
      returnToGameTimer.start();
      });
      bonusTimer.setRepeats(false);
      bonusTimer.start();
    });
    rainbowTimer.setRepeats(false);
    rainbowTimer.start();
  }

  //충돌이 없는 경우 (보너스 실패) -> 다음 학년으로 넘어가기
  private void startNoCollisionPhase() {
    if (noCollisionTimer != null) noCollisionTimer.stop();
    if(bonusPanel.timer != null) bonusPanel.timer.stop();
    overStarTime = false;
    //스타와 함께 있었던 시간
    noCollisionTimer = new Timer(2000, e -> {
      switchToPanelWithDelay("game", 0);
      gamePanel.updateCurpointText();
      // GamePanel로 돌아올 때 BonusPanel 포인트를 동기화
      currentCycleCount++;
      startLevelUpPhase(); // 다음 사이클 시작
    });
    noCollisionTimer.setRepeats(false);
    noCollisionTimer.start();
  }

  private void updateUserStatus() {
    // GamePanel로부터 현재 점수 가져오기
    Player player = gamePanel.getPlayer();
    double currentScore = player.getGPA();
    int currentPoints = pointsManager.getPoints();

    // UserStatus 업데이트
    userStatus.setUserGrade(currentCycleCount + 1); // 1학년부터 시작
    userStatus.setUserScore(currentScore);
    userStatus.setUserPoints(currentPoints);

    // 졸업 여부 결정
    if (currentCycleCount >= maxCycleCount - 1) { // 4학년까지 완료
      userStatus.setGraduated(currentScore > 0); // 점수가 0보다 크면 졸업
    } else {
      userStatus.setGraduated(false); // 4학년 이전에는 졸업 불가
    }
  }

  //이건 엔드 패널로 이동시키는 거 추가하면 될 듯
  private void endGameCycle() {
    if (timer != null) timer.stop();
    // 최종 상태 업데이트
    updateUserStatus();
    // endPanel로 전환
    GameResult result = new GameResult();
    result.setPoints(userStatus.getUserPoints());
    result.setGraduated(userStatus.isGraduated());
    timer = new Timer(2000, e -> showEndScreen(result));
    timer.setRepeats(false);
    timer.start();
  }

  public void startGameSequence() {
    showScreen("game");
    startGameCycle();
  }

  // 화면 전환 메서드
  public void showScreen(String screenName) {
    cardLayout.show(mainPanel, screenName);
  }

  // 게임 종료 패널은 따로(객체 필요!!)
  public void showEndScreen(GameResult gameResult) {
    endPanel.showEndPanel(gameResult);
    showScreen("end");
    // 아이콘 속도 레벨 리셋
    for (Icon icon : Icon.iconList) {
      icon.resetSpeedLevel();
    }
    // 코인 속도 리셋
    for (Coin coin : Coin.arraycoin) {
      coin.resetSpeedLevel();
    }
  }

  public static GamePanel getGamePanel() {
    return gamePanel;
  }

}

