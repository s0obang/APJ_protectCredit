package org.example.Manager;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.example.panels.EndPanel;
import org.example.panels.GamePanel;
import org.example.panels.StartPanel;

public class GameManager extends JFrame {

  private CardLayout cardLayout;
  private JPanel mainPanel;
  private DatabaseManager dbManager;
  private LoginManager loginManager;
  private GamePanel gamePanel;


  public GameManager() {
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
    gamePanel = new GamePanel(this);
    // 각 화면을 패널로 추가
    mainPanel.add(new StartPanel(this, loginManager), "start");
    mainPanel.add(gamePanel, "game");

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

}
