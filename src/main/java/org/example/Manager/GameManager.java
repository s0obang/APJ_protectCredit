package org.example.Manager;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.example.entity.Icon;
import org.example.entity.Star;
import org.example.object.UserStatus;

import org.example.entity.GameResult;
import org.example.panels.EndPanel;
import org.example.panels.GamePanel;
import org.example.panels.StartPanel;
import org.example.panels.StarPanel;
import org.example.panels.LevelUpPanel;
import org.example.panels.BounsPanel;

public class GameManager extends JFrame {
  private int currentCycleCount = 0;
  private final int maxCycleCount = 4;

  private static CardLayout cardLayout;
  private static JPanel mainPanel;
  private DatabaseManager dbManager;
  private LoginManager loginManager;
  private EndPanel endPanel;
  private static GamePanel gamePanel;
  private static StarPanel starPanel;
  private static LevelUpPanel levelupPanel;
  public static BounsPanel bonusPanel;
  public static Star star;

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
    bonusPanel = new BounsPanel(this);
    starPanel = new StarPanel(this);
    endPanel = new EndPanel(this);
    // 각 화면을 패널로 추가
    mainPanel.add(new StartPanel(this), "start");
    mainPanel.add(gamePanel, "game");
    mainPanel.add(starPanel, "star");
    mainPanel.add(levelupPanel, "levelup");
    mainPanel.add(bonusPanel, "bonus");
    mainPanel.add(endPanel, "end");

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

    if (currentCycleCount < maxCycleCount - 1) {// maxCycleCount - 1번까지만 반복
      // 30초 후에 levelupPanel로 전환
      Timer levelUpTimer = new Timer(5000, e -> {
        switchToPanelWithDelay("levelup", 0);

        // levelupPanel이 표시된 후 3초 뒤에 starPanel로 전환
        Timer starTimer = new Timer(5000, event -> {
          switchToPanelWithDelay("star", 0);
          starPanel.initializeStar(star); // StarPanel에 Star 객체 전달
          // StarPanel로 전환 시에 Star 객체를 생성하여 전달
          star = new Star(0, 0, 60, 50); // Star 객체 초기화

        });
        starTimer.setRepeats(false); // 한 번만 실행되도록 설정
        starTimer.start();
      });
      levelUpTimer.setRepeats(false); // 한 번만 실행되도록 설정
      levelUpTimer.start();
      //switchToPanelWithDelay("game", 4000);
      currentCycleCount++;

      Timer timer = new Timer(40000, e -> startGameCycle());
      timer.setRepeats(false);
      timer.start();
    } else {
      // 마지막 사이클: "game"을 보여주고 end screen 표시
      switchToPanelWithDelay("game", 30000); // 최종 game 화면
      // endPanel로 전환
      GameResult result = new GameResult();
      result.setPoints(userStatus.getUserPoints());
      result.setGraduated(userStatus.isGraduated());
      Timer timer = new Timer(30000, e -> showEndScreen(result));
      timer.setRepeats(false);
      timer.start();
    }
  }


  public static void switchToPanelWithDelay(String nextPanelName, int delayMillis) {
    Timer timer = new Timer(delayMillis, e -> {
      if (nextPanelName.equals("levelup") || (nextPanelName.equals("star")) || nextPanelName.equals("bonus")) {
        gamePanel.stopGame();// 게임 일시정지
      }
      else if (nextPanelName.equals("game")) {
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
    gamePanel.startGame();
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

  // 게임 종료 패널은 따로(객체 필요!!)
  public void showEndScreen(GameResult gameResult) {
    endPanel.showEndPanel(gameResult);
    showScreen("end");
    // 아이콘 속도 레벨 리셋
    for (Icon icon : Icon.iconList) {
      icon.resetSpeedLevel();
    }
  }

  public GamePanel getGamePanel() {
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
