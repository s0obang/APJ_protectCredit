package org.example.Manager;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.example.object.UserStatus;
import org.example.panels.EndPanel;
import org.example.panels.GamePanel;
import org.example.panels.StartPanel;
import org.example.panels.StarPanel;
import org.example.panels.LevelUpPanel;
import org.example.panels.BounsPanel;

public class GameManager extends JFrame {

  private CardLayout cardLayout;
  private JPanel mainPanel;
  private DatabaseManager dbManager;
  private LoginManager loginManager;
  private GamePanel gamePanel;
  private StarPanel starPanel;
  private LevelUpPanel levelupPanel;
  public BounsPanel bonusPanel;

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
    bonusPanel = new BounsPanel();
    starPanel = new StarPanel(this);
    // 각 화면을 패널로 추가
    mainPanel.add(new StartPanel(this, loginManager), "start");
    mainPanel.add(gamePanel, "game");
    mainPanel.add(starPanel, "star");
    mainPanel.add(levelupPanel, "levelup");
    mainPanel.add(bonusPanel, "bonus");

    add(mainPanel);
    setVisible(true);

    // 창 닫을 때 로그아웃
    addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent e) {
        loginManager.logout();
        System.out.println("로그아웃 후 애플리케이션 종료.");
      }
    });
  }

  public void switchToPanelWithDelay(String nextPanelName, int delayMillis) {
    Timer timer = new Timer(delayMillis, e -> {
      if (nextPanelName.equals("fever")) {
        gamePanel.stopGame(); // 게임 일시정지
      } else if (nextPanelName.equals("game")) {
        gamePanel.startGame(); // 게임 재시작
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
    // 30초 후에 levelupPanel로 전환
    Timer levelUpTimer = new Timer(1000, e -> {
      switchToPanelWithDelay("levelup", 0);

      // levelupPanel이 표시된 후 3초 뒤에 starPanel로 전환
      Timer starTimer = new Timer(1000, event -> {
        switchToPanelWithDelay("star", 0);
      });
      starTimer.setRepeats(false); // 한 번만 실행되도록 설정
      starTimer.start();
    });
    levelUpTimer.setRepeats(false); // 한 번만 실행되도록 설정
    levelUpTimer.start();

    switchToPanelWithDelay("game", 40000);
  }

  // 화면 전환 메서드
  public void showScreen(String screenName) {
    cardLayout.show(mainPanel, screenName);
  }

  // 게임 종료 패널은 따로(boolean 값 필요!!)
  public void showEndScreen(boolean isGameOver) {
    mainPanel.add(new EndPanel(this, isGameOver), "end");
    showScreen("end");
  }

  public GamePanel getGamePanel() {
    return gamePanel;
  }

  // getPanel 메서드 추가 -> 주영이 쓰는 패널에서 필요함.
  public JPanel getPanel(String panelName) {
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
