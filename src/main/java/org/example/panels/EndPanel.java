package org.example.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javazoom.jl.player.Player;
import org.example.Manager.BackgroundPanel;
import org.example.Manager.DatabaseManager;
import org.example.Manager.GameManager;
import org.example.Manager.LoginManager;
import org.example.entity.GameResult;

public class EndPanel extends JPanel {

  public Player mp3Player;
  public Thread sound;
  private DatabaseManager dbManager = DatabaseManager.getInstance();
  private CardLayout cardLayout;
  private JPanel cardPanel; // 패널들을 담을 메인 패널
  private JPanel graduationCardPanel; // graduationPanel의 내부 CardLayout 패널
  private GameResult gameResult;
  private JTextField score;
  private JTextField ranking;
  private GameManager manager;
  private boolean isSoundPlaying;

  public EndPanel(GameManager manager) {
    this.manager = manager;
    cardLayout = new CardLayout();
    cardPanel = new JPanel(cardLayout);

    JPanel ranking = ranking();

    cardPanel.add(graduationPanel(ranking), "graduation");
    cardPanel.add(gameOverPanel(), "gameOver");
    cardPanel.add(ranking, "ranking");

    setLayout(new BorderLayout());
    add(cardPanel, BorderLayout.CENTER);

  }

  private JPanel gameOverPanel() {
    JPanel panel = new BackgroundPanel("/img/backgrounds/gameOver.png");
    panel.setLayout(null);

    ImageIcon buttonIcon1 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/historyButton.png")));
    Image img = buttonIcon1.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
    buttonIcon1 = new ImageIcon(img);
    JButton homeButton = new JButton(buttonIcon1);
    homeButton.setPreferredSize(new Dimension(75, 75));
    homeButton.setBorderPainted(false);
    homeButton.setFocusPainted(false);
    homeButton.setContentAreaFilled(false);
    homeButton.setBounds(310, 578, 75, 75);

    ImageIcon buttonIcon2 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/playButton.png")));
    Image img2 = buttonIcon2.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
    buttonIcon2 = new ImageIcon(img2);
    JButton playAgainButton = new JButton(buttonIcon2);
    playAgainButton.setPreferredSize(new Dimension(75, 75));
    playAgainButton.setBorderPainted(false);
    playAgainButton.setFocusPainted(false);
    playAgainButton.setContentAreaFilled(false);
    playAgainButton.setBounds(745, 575, 75, 75);

    // 홈화면으로 이동
    homeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        manager.showScreen("start");
        manager.resetGame();
        manager.restartStartMusic();
      }
    });

    // 게임 다시 시작
    playAgainButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        manager.startGameSequence();// 아이콘 떨어짐 동작 시작
        manager.getGamePanel().startGame(); // 아이콘 떨어짐 동작 시작
      }
    });

    panel.add(homeButton);
    panel.add(playAgainButton);

    return panel;
  }

  private JPanel graduationPanel(JPanel rankingPanel) {
    Font labelFont = new Font("Galmuri11 Regular", Font.PLAIN, 34);

    graduationCardPanel = new JPanel(new CardLayout());
    JPanel g1 = new BackgroundPanel("/img/backgrounds/graduation_1.png");
    JPanel g2 = new BackgroundPanel("/img/backgrounds/graduation_2.png");
    g2.setLayout(null);

    score = new JTextField(15);

    score.setFont(labelFont);
    score.setBounds(485, 340, 180, 60);
    score.setOpaque(false);
    score.setEditable(false);
    score.setFocusable(false);
    score.setBorder(BorderFactory.createEmptyBorder());

    ImageIcon buttonIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/historyButton.png")));
    Image img = buttonIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img);
    JButton nextButton = new JButton(buttonIcon);
    nextButton.setPreferredSize(new Dimension(75, 75));
    nextButton.setBounds(515, 600, 75, 75);
    nextButton.setBorderPainted(false);
    nextButton.setFocusPainted(false);
    nextButton.setContentAreaFilled(false);

    manager.resetGame();

    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "ranking");
        getRanking(rankingPanel);
      }
    });

    g2.add(score);
    g2.add(nextButton);
    graduationCardPanel.add(g1, "g1");
    graduationCardPanel.add(g2, "g2");

    return graduationCardPanel;
  }

  public void showResultSuccess() {
    CardLayout cl = (CardLayout) graduationCardPanel.getLayout(); // graduationCardPanel 가져옴
    cl.show(graduationCardPanel, "g1");

    // 점수 띄우기
    score.setText(gameResult.getPoints() + "");
    // 점수 저장
    dbManager.saveScore(gameResult, LoginManager.getLoggedInUser());

    // 축하 화면에서 1.5초 후에 점수 띄우는 화면으로 전환
    Timer timer = new Timer(1500, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cl.show(graduationCardPanel, "g2");
      }
    });
    timer.setRepeats(false);
    timer.start();
  }

  private JPanel ranking() {
    JPanel panel = new BackgroundPanel("/img/backgrounds/ranking.png");
    panel.setLayout(null);

    // 홈 버튼 설정
    ImageIcon buttonIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/historyButton.png")));
    Image img = buttonIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img);
    JButton homeButton = new JButton(buttonIcon);
    homeButton.setPreferredSize(new Dimension(75, 75));
    homeButton.setBounds(515, 600, 75, 75);
    homeButton.setBorderPainted(false);
    homeButton.setFocusPainted(false);
    homeButton.setContentAreaFilled(false);

    homeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        mp3Player.close();
        manager.showScreen("start");
        manager.restartStartMusic();
      }
    });

    panel.add(homeButton);

    return panel;
  }

  private void getRanking(JPanel panel) {
    Font labelFont = new Font("Galmuri11 Regular", Font.PLAIN, 25);
    Font scoreFont = new Font("Galmuri11 Regular", Font.BOLD, 33);

    // 랭킹 가져오기
    java.util.List<Map<String, String>> ranking = dbManager.getRanking();
    Map<String, String> first = ranking.get(0);

    JLabel score = new JLabel(first.get("points"));
    JLabel nickname = new JLabel(first.get("nickname"));

    score.setBounds(500, 130, 170, 40);
    score.setFont(scoreFont);
    score.setForeground(Color.BLACK);
    score.setHorizontalAlignment(SwingConstants.CENTER);
    nickname.setBounds(710, 130, 130, 40);
    nickname.setFont(labelFont);
    nickname.setForeground(Color.DARK_GRAY);

    String[] columns = {"", ""};
    Object[][] dataLeft = new Object[5][2];
    Object[][] dataRight = new Object[5][2];

    // 2등부터 6등까지
    for (int i = 1; i < Math.min(ranking.size(), 6); i++) {
      Map<String, String> record = ranking.get(i);
      dataLeft[i - 1][0] = record.get("points");
      dataLeft[i - 1][1] = record.get("nickname");
    }

    // 7등부터 11등까지
    for (int i = 6; i < Math.min(ranking.size(), 11); i++) {
      Map<String, String> record = ranking.get(i);
      dataRight[i - 6][0] = record.get("points");
      dataRight[i - 6][1] = record.get("nickname");
    }

    DefaultTableModel modelLeft = new DefaultTableModel(dataLeft, columns) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    DefaultTableModel modelRight = new DefaultTableModel(dataRight, columns) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    JTable recordsTableLeft = new JTable(modelLeft);
    recordsTableLeft.setFont(labelFont);
    recordsTableLeft.setBorder(null);
    recordsTableLeft.setOpaque(false);
    recordsTableLeft.setShowGrid(false);
    recordsTableLeft.setRowHeight(45);
    recordsTableLeft.setRowSelectionAllowed(false);
    recordsTableLeft.setColumnSelectionAllowed(false);
    recordsTableLeft.setCellSelectionEnabled(false);
    recordsTableLeft.setFocusable(false);

    JTable recordsTableRight = new JTable(modelRight);
    recordsTableRight.setFont(labelFont);
    recordsTableRight.setBorder(null);
    recordsTableRight.setOpaque(false);
    recordsTableRight.setShowGrid(false);
    recordsTableRight.setRowHeight(45);
    recordsTableRight.setRowSelectionAllowed(false);
    recordsTableRight.setColumnSelectionAllowed(false);
    recordsTableRight.setCellSelectionEnabled(false);
    recordsTableRight.setFocusable(false);

    // 셀 렌더러
    DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
            row, column);

        comp.setBackground(Color.decode("#ADB7C4"));

        if (column == 0) {
          comp.setForeground(Color.BLACK);
          setHorizontalAlignment(SwingConstants.CENTER);
        } else {
          comp.setForeground(Color.DARK_GRAY);
          setHorizontalAlignment(SwingConstants.LEFT);
        }

        return comp;
      }
    };

    for (int i = 0; i < recordsTableLeft.getColumnCount(); i++) {
      recordsTableLeft.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
    }
    for (int i = 0; i < recordsTableRight.getColumnCount(); i++) {
      recordsTableRight.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
    }

    recordsTableLeft.getColumnModel().getColumn(0).setPreferredWidth(180);  // Points 컬럼
    recordsTableLeft.getColumnModel().getColumn(1).setPreferredWidth(180);  // Nickname 컬럼

    recordsTableRight.getColumnModel().getColumn(0).setPreferredWidth(180);  // Points 컬럼
    recordsTableRight.getColumnModel().getColumn(1).setPreferredWidth(180);  // Nickname 컬럼

    recordsTableLeft.setBounds(230, 220, 360, 225);
    recordsTableRight.setBounds(550, 220, 360, 225);

    panel.add(recordsTableLeft);
    panel.add(recordsTableRight);

    recordsTableLeft.revalidate();
    recordsTableLeft.repaint();
    recordsTableRight.revalidate();
    recordsTableRight.repaint();

    panel.add(score);
    panel.add(nickname);
  }

  public void playGameOverMusic() {
    if (!isSoundPlaying) {
      isSoundPlaying = true;
      sound = new Thread(() -> {
        try {
          // 기존 mp3Player를 닫기 전에 반드시 종료 처리
          if (mp3Player != null) {
            mp3Player.close();  // 기존 재생 중인 노래 종료
          }
          try (InputStream fis = getClass().getResourceAsStream(
              "/audio/gameover.mp3")) {
            mp3Player = new Player(Objects.requireNonNull(fis));
            mp3Player.play(); // MP3 파일 재생
            isSoundPlaying = false; // 오디오 재생 완료
          } catch (Exception e) {
            System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
            isSoundPlaying = false; // 오류 발생 시 상태 변경
          }
        } catch (Exception e) {
          System.err.println("음악 재생에 실패했습니다: " + e.getMessage());
        }
      });
      sound.start(); // 비동기적으로 오디오 시작
    }
  }

  public void playGraduationMusic() {
    System.out.println("intro 시작"); // 지우면 안 됨
    if (!isSoundPlaying) {
      System.out.println("짜잔");

      isSoundPlaying = true;
      sound = new Thread(() -> {
        try {
          // 기존 mp3Player를 닫기 전에 반드시 종료 처리
          if (mp3Player != null) {
            mp3Player.close();  // 기존 재생 중인 노래 종료
            System.out.println("노래종료");

          }
          try (InputStream fis = getClass().getResourceAsStream(
              "/audio/Graduation.mp3")) {
            mp3Player = new Player(Objects.requireNonNull(fis));
            mp3Player.play(); // MP3 파일 재생
            isSoundPlaying = false; // 오디오 재생 완료
            System.out.println("노래재생");

          } catch (Exception e) {
            System.err.println("오디오 파일 재생 중 오류 발생: " + e.getMessage());
            isSoundPlaying = false; // 오류 발생 시 상태 변경
          }
        } catch (Exception e) {
          System.err.println("음악 재생에 실패했습니다: " + e.getMessage());
        }
      });
      sound.start(); // 비동기적으로 오디오 시작
    }
  }

  // 게임 결과에 따라 화면 다르게 띄움
  public void showEndPanel(GameResult result) {
    this.gameResult = result;
    if (gameResult.isGraduated()) {
      cardLayout.show(cardPanel, "graduation");
      showResultSuccess();
      playGraduationMusic();
    } else {
      cardLayout.show(cardPanel, "gameOver");
      playGameOverMusic();

    }
  }


}
