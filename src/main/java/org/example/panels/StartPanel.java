package org.example.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
import org.example.entity.User;


public class StartPanel extends JPanel {

  public Player mp3Player;
  public Thread sound;
  private CardLayout cardLayout;
  private JPanel cardPanel; // 패널들을 담을 메인 패널
  private DatabaseManager dbManager = DatabaseManager.getInstance();
  private LoginManager loginManager;
  private boolean isSoundPlaying;
  private GameManager manager;


  public StartPanel(GameManager manager) {
    this.loginManager = loginManager;
    this.manager = manager;

    cardLayout = new CardLayout();
    cardPanel = new JPanel(cardLayout);

    JPanel history = history();
    cardPanel.add(startGamePanel(history), "main");
    cardPanel.add(selectSign_(), "selectSign_");
    cardPanel.add(signIn(), "signIn");
    cardPanel.add(signUp(), "signUp");
    cardPanel.add(intro(), "intro");
    cardPanel.add(history, "history");
    cardPanel.add(gameRule(), "gameRule");

    setLayout(new BorderLayout());
    add(cardPanel, BorderLayout.CENTER);
  }

  private JPanel startGamePanel(JPanel history) {
    JPanel panel = new BackgroundPanel("/img/backgrounds/startBackground.png"); // 배경 패널
    panel.setLayout(null); // bPanel 절대 위치 정하려고

    playStartMusic();

    JPanel bPanel = new JPanel(new GridBagLayout()); // 버튼 감싸는 패널
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.weighty = 0;
    bPanel.setOpaque(false);

    Font labelFont = new Font("Galmuri11 Regular", Font.PLAIN, 20);

    ImageIcon buttonIcon1 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/historyButton.png")));
    Image img = buttonIcon1.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
    buttonIcon1 = new ImageIcon(img);
    JButton historyButton = new JButton(buttonIcon1);
    historyButton.setPreferredSize(new Dimension(75, 75));
    historyButton.setBorderPainted(false);
    historyButton.setFocusPainted(false);
    historyButton.setContentAreaFilled(false);

    JLabel historyLabel = new JLabel("history");
    historyLabel.setHorizontalAlignment(SwingConstants.CENTER);
    historyLabel.setFont(labelFont);
    historyLabel.setForeground(Color.decode("#5E5E5E"));

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 0, 0);
    bPanel.add(historyButton, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(5, 0, 0, 0);
    bPanel.add(historyLabel, gbc);

    ImageIcon buttonIcon2 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/playButton.png")));
    Image img2 = buttonIcon2.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
    buttonIcon2 = new ImageIcon(img2);
    JButton playButton = new JButton(buttonIcon2);
    playButton.setPreferredSize(new Dimension(75, 75));
    playButton.setBorderPainted(false);
    playButton.setFocusPainted(false);
    playButton.setContentAreaFilled(false);
    playButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // 로그인 되어있으면 바로 게임 시작
        if (LoginManager.getLoggedInUser() != null) {
          cardLayout.show(cardPanel, "intro");
          mp3Player.close();
          playIntroMusic();
        } else {
          cardLayout.show(cardPanel, "selectSign_");
        }

      }
    });
    historyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "history");
        if (LoginManager.getLoggedInUser() != null) {
          getHistory(history);
        }
      }
    });

    JLabel playLabel = new JLabel("play!");
    playLabel.setHorizontalAlignment(SwingConstants.CENTER);
    playLabel.setFont(labelFont);
    playLabel.setForeground(Color.decode("#5E5E5E"));

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 120, 0, 0);
    bPanel.add(playButton, gbc);

    gbc.gridy = 1;
    gbc.insets = new Insets(5, 120, 0, 0);
    bPanel.add(playLabel, gbc);

    bPanel.setBounds(395, 570, 290, 135);

    CharacterPanel characterPanel = new CharacterPanel("/img/intro/mainCharacter.png");
    characterPanel.setBounds(335, 60, 509, 436);
    characterPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        cardLayout.show(cardPanel, "gameRule");
      }
    });

    panel.add(bPanel);
    panel.add(characterPanel);

    // 패널의 계층 상태 변경을 감지하는 이벤트 리스너 추가
    panel.addHierarchyListener(e -> {
      if (e.getID() == HierarchyEvent.HIERARCHY_CHANGED) {
        if (panel.isShowing()) {
          characterPanel.startCharacterMovement();  // 인트로 화면에 돌아왔을 때 이동 시작
        } else {
          characterPanel.stopCharacterMovement();   // 다른 화면으로 갔을 때 이동 멈춤
        }
      }
    });

    return panel;
  }

  private JPanel gameRule() {
    JPanel panel = new JPanel(new CardLayout());
    CardLayout cardLayout = (CardLayout) panel.getLayout();

    JPanel rule1 = new BackgroundPanel("/img/backgrounds/gameRule1.png");
    rule1.setLayout(null);

    JPanel rule2 = new BackgroundPanel("/img/backgrounds/gameRule2.png");
    rule2.setLayout(null);

    // 첫번째 화면 버튼(이벤트를 따로 걸어야해서 .. 4개 만들어야함)
    ImageIcon buttonIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/playButton.png")));
    Image img = buttonIcon.getImage().getScaledInstance(68, 68, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img);

    JButton nextButton = new JButton(buttonIcon);
    nextButton.setPreferredSize(new Dimension(68, 68));
    nextButton.setBounds(626, 608, 68, 68);
    nextButton.setBorderPainted(false);
    nextButton.setFocusPainted(false);
    nextButton.setContentAreaFilled(false);

    ImageIcon buttonIcon2 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/backButton2.png")));
    Image img2 = buttonIcon2.getImage().getScaledInstance(68, 68, Image.SCALE_SMOOTH);
    buttonIcon2 = new ImageIcon(img2);

    JButton backButton = new JButton(buttonIcon2);
    backButton.setPreferredSize(new Dimension(68, 68));
    backButton.setBounds(408, 609, 68, 68);
    backButton.setBorderPainted(false);
    backButton.setFocusPainted(false);
    backButton.setContentAreaFilled(false);

    // 두번째 화면 버튼
    JButton homeButton = new JButton(buttonIcon);
    homeButton.setPreferredSize(new Dimension(68, 68));
    homeButton.setBounds(626, 608, 68, 68);
    homeButton.setBorderPainted(false);
    homeButton.setFocusPainted(false);
    homeButton.setContentAreaFilled(false);

    JButton backButton2 = new JButton(buttonIcon2);
    backButton2.setPreferredSize(new Dimension(68, 68));
    backButton2.setBounds(408, 609, 68, 68);
    backButton2.setBorderPainted(false);
    backButton2.setFocusPainted(false);
    backButton2.setContentAreaFilled(false);

    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(panel, "rule2");
      }
    });

    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        StartPanel.this.cardLayout.show(cardPanel, "main");
      }
    });

    homeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(panel, "rule1");
        StartPanel.this.cardLayout.show(cardPanel, "main");
      }
    });

    backButton2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(panel, "rule1");
      }
    });

    rule1.add(nextButton);
    rule1.add(backButton);
    rule2.add(homeButton);
    rule2.add(backButton2);

    panel.add(rule1, "rule1");
    panel.add(rule2, "rule2");

    return panel;
  }


  private JPanel history() {
    JPanel panel = new BackgroundPanel("/img/backgrounds/history.png");
    panel.setLayout(null);

    ImageIcon buttonIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/backButton2.png")));
    Image img = buttonIcon.getImage().getScaledInstance(73, 73, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img);
    JButton backButton = new JButton(buttonIcon);
    backButton.setPreferredSize(new Dimension(75, 75));
    backButton.setBounds(515, 605, 73, 73);
    backButton.setBorderPainted(false);
    backButton.setFocusPainted(false);
    backButton.setContentAreaFilled(false);

    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "main");
      }
    });

    panel.add(backButton);

    return panel;
  }

  private void getHistory(JPanel panel) {
    Font labelFont = new Font("Galmuri11 Regular", Font.PLAIN, 25);

    List<Map<String, String>> recentRecords = dbManager.getRecentRecords(
        LoginManager.getLoggedInUser());

    String[] columns = {"", ""};
    Object[][] data = new Object[recentRecords.size()][2];

    for (int i = 0; i < recentRecords.size(); i++) {
      Map<String, String> record = recentRecords.get(i);
      data[i][0] = record.get("points");
      data[i][1] = record.get("date");
    }

    DefaultTableModel model = new DefaultTableModel(data, columns) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    JTable recordsTable = new JTable(model);
    recordsTable.setFont(labelFont);
    recordsTable.setOpaque(false);
    recordsTable.setBorder(null);
    recordsTable.setShowGrid(false);
    recordsTable.setRowHeight(45);
    recordsTable.setRowSelectionAllowed(false);
    recordsTable.setColumnSelectionAllowed(false);
    recordsTable.setCellSelectionEnabled(false);
    recordsTable.setFocusable(false);

    DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
            row, column);

        setHorizontalAlignment(SwingConstants.CENTER);
        comp.setBackground(Color.decode("#ADB7C4"));

        if (column == 0) {
          comp.setForeground(Color.BLACK);
        } else {
          comp.setForeground(Color.DARK_GRAY);
        }

        return comp;
      }
    };

    for (int i = 0; i < recordsTable.getColumnCount(); i++) {
      recordsTable.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
    }

    recordsTable.getColumnModel().getColumn(0).setPreferredWidth(200);  // Points 컬럼
    recordsTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Date 컬럼

    recordsTable.setBounds(325, 257, 400, 180);

    panel.add(recordsTable);

    recordsTable.revalidate();
    recordsTable.repaint();

    // 최고점 띄우기
    Font scoreFont = new Font("Galmuri11 Regular", Font.BOLD, 40);

    Map<String, String> highest = dbManager.getHighestScore(LoginManager.getLoggedInUser());
    JLabel score = new JLabel(highest.get("points"));
    JLabel date = new JLabel(highest.get("date"));

    score.setBounds(500, 130, 170, 40);
    score.setFont(scoreFont);
    score.setForeground(Color.BLACK);
    score.setHorizontalAlignment(SwingConstants.CENTER);
    date.setBounds(710, 130, 130, 40);
    date.setFont(labelFont);
    date.setForeground(Color.DARK_GRAY);

    panel.add(score);
    panel.add(date);

  }

  private JPanel selectSign_() {
    JPanel panel = new BackgroundPanel("/img/backgrounds/selectSign_.png");
    panel.setLayout(null);

    ImageIcon buttonIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/backButton.png")));
    Image img1 = buttonIcon.getImage().getScaledInstance(65, 60, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img1);
    JButton backButton = new JButton(buttonIcon);
    backButton.setBorderPainted(false);
    backButton.setFocusPainted(false);
    backButton.setContentAreaFilled(false);
    backButton.setBounds(20, 20, 65, 60);

    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "main");
      }
    });

    ImageIcon buttonIcon1 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/signInButton.png")));
    Image img = buttonIcon1.getImage().getScaledInstance(393, 105, Image.SCALE_SMOOTH);
    buttonIcon1 = new ImageIcon(img);
    JButton signInButton = new JButton(buttonIcon1);
    signInButton.setBorderPainted(false);
    signInButton.setFocusPainted(false);
    signInButton.setContentAreaFilled(false);
    signInButton.setBounds(360, 263, 393, 105);

    signInButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "signIn");
      }
    });

    ImageIcon buttonIcon2 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/signUpButton.png")));
    Image img2 = buttonIcon2.getImage().getScaledInstance(393, 105, Image.SCALE_SMOOTH);
    buttonIcon2 = new ImageIcon(img2);
    JButton signUpButton = new JButton(buttonIcon2);
    signUpButton.setBorderPainted(false);
    signUpButton.setFocusPainted(false);
    signUpButton.setContentAreaFilled(false);
    signUpButton.setBounds(360, 404, 393, 105);

    signUpButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "signUp");
      }
    });

    panel.add(backButton);
    panel.add(signInButton);
    panel.add(signUpButton);

    return panel;
  }

  private JPanel signIn() {
    Font labelFont = new Font("Galmuri11 Regular", Font.PLAIN, 29);

    JPanel panel = new BackgroundPanel("/img/backgrounds/signInBackground.png");
    panel.setLayout(null);

    ImageIcon buttonIcon1 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/backButton.png")));
    Image img1 = buttonIcon1.getImage().getScaledInstance(65, 60, Image.SCALE_SMOOTH);
    buttonIcon1 = new ImageIcon(img1);
    JButton backButton = new JButton(buttonIcon1);
    backButton.setBorderPainted(false);
    backButton.setFocusPainted(false);
    backButton.setContentAreaFilled(false);
    backButton.setBounds(20, 20, 65, 60);

    JTextField nickname = new JTextField(15);
    JPasswordField password = new JPasswordField(15);

    nickname.setFont(labelFont);
    nickname.setBounds(488, 273, 360, 60);
    nickname.setOpaque(false);
    nickname.setBorder(BorderFactory.createEmptyBorder());
    password.setFont(labelFont);
    password.setBounds(488, 402, 360, 60);
    password.setOpaque(false);
    password.setBorder(BorderFactory.createEmptyBorder());

    ImageIcon buttonIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/playButton.png")));
    Image img = buttonIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img);
    JButton playButton = new JButton(buttonIcon);
    playButton.setBorderPainted(false);
    playButton.setFocusPainted(false);
    playButton.setContentAreaFilled(false);
    playButton.setBounds(500, 580, 70, 70);
    playButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String nick = nickname.getText();
        String pass = new String(password.getPassword());

        boolean isSuccess = dbManager.signIn(dbManager, nick, pass);

        if (isSuccess) {
          System.out.println(LoginManager.getLoggedInUser());
          nickname.setText("");
          password.setText("");
          cardLayout.show(cardPanel, "main");
        } else {
          JOptionPane.showMessageDialog(panel, "로그인 실패. 닉네임이나 비밀번호를 확인하세요.", "오류",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    nickname.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          password.requestFocus(); // password 필드로 이동
        }
      }
    });
    password.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          playButton.doClick(); // playButton 클릭
        }
      }
    });

    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "selectSign_");
        nickname.setText("");
        password.setText("");
      }
    });

    panel.add(nickname);
    panel.add(password);
    panel.add(playButton);
    panel.add(backButton);

    return panel;
  }

  private JPanel signUp() {
    Font labelFont = new Font("Galmuri11 Regular", Font.PLAIN, 29);

    JPanel panel = new BackgroundPanel("/img/backgrounds/signUpBackground.png");
    panel.setLayout(null);

    ImageIcon buttonIcon1 = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/backButton.png")));
    Image img1 = buttonIcon1.getImage().getScaledInstance(65, 60, Image.SCALE_SMOOTH);
    buttonIcon1 = new ImageIcon(img1);
    JButton backButton = new JButton(buttonIcon1);
    backButton.setBorderPainted(false);
    backButton.setFocusPainted(false);
    backButton.setContentAreaFilled(false);
    backButton.setBounds(20, 20, 65, 60);

    JTextField nickname = new JTextField(15);
    JPasswordField password = new JPasswordField(15);
    JPasswordField passwordCheck = new JPasswordField(15);

    nickname.setFont(labelFont);
    nickname.setBounds(488, 215, 360, 60);
    nickname.setOpaque(false);
    nickname.setBorder(BorderFactory.createEmptyBorder());
    password.setFont(labelFont);
    password.setBounds(488, 335, 360, 60);
    password.setOpaque(false);
    password.setBorder(BorderFactory.createEmptyBorder());
    passwordCheck.setFont(labelFont);
    passwordCheck.setBounds(488, 458, 360, 60);
    passwordCheck.setOpaque(false);
    passwordCheck.setBorder(BorderFactory.createEmptyBorder());

    ImageIcon buttonIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/playButton.png")));
    Image img = buttonIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img);
    JButton signUpButton = new JButton(buttonIcon);
    signUpButton.setBorderPainted(false);
    signUpButton.setFocusPainted(false);
    signUpButton.setContentAreaFilled(false);
    signUpButton.setBounds(500, 580, 70, 70);

    nickname.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          password.requestFocus(); // password 필드로 이동
        }
      }
    });

    password.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          passwordCheck.requestFocus(); // password 필드로 이동
        }
      }
    });
    passwordCheck.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          signUpButton.doClick(); // playButton 클릭
        }
      }
    });

    signUpButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String nick = nickname.getText();
        String pass = new String(password.getPassword());
        String passCheck = new String(passwordCheck.getPassword());

        if (nick.isEmpty() || pass.isEmpty() || passCheck.isEmpty()) {
          JOptionPane.showMessageDialog(panel, "모든 필드를 채워주세요.", "오류", JOptionPane.ERROR_MESSAGE);
          return;
        }

        if (!pass.equals(passCheck)) {
          JOptionPane.showMessageDialog(panel, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
          return;
        }

        if (nick.length() < 4) {
          JOptionPane.showMessageDialog(panel, "닉네임은 최소 4글자 이상이어야 합니다.", "오류",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        if (pass.length() < 4) {
          JOptionPane.showMessageDialog(panel, "비밀번호는 최소 4글자 이상이어야 합니다.", "오류",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        // 중복 닉네임 체크
        if (dbManager.isNicknameDuplicate(nick)) {
          JOptionPane.showMessageDialog(panel, "이미 존재하는 닉네임입니다. 다른 닉네임을 입력하세요.", "오류",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        User newUser = new User(nick, pass);
        boolean isSuccess = dbManager.signUp(nick, pass);

        if (isSuccess) {
          JOptionPane.showMessageDialog(panel, "회원가입 성공.", "회원가입", JOptionPane.PLAIN_MESSAGE);
          nickname.setText("");
          password.setText("");
          passwordCheck.setText("");
          cardLayout.show(cardPanel, "main");
        } else {
          JOptionPane.showMessageDialog(panel, "회원가입 실패. 다시 시도해주세요.", "오류",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "selectSign_");
        nickname.setText("");
        password.setText("");
        passwordCheck.setText("");
      }
    });

    panel.add(nickname);
    panel.add(password);
    panel.add(passwordCheck);
    panel.add(signUpButton);
    panel.add(backButton);

    return panel;
  }


  private JPanel intro() {
    JPanel panel = new JPanel(new CardLayout());
    CardLayout introCardLayout = (CardLayout) panel.getLayout();

    JPanel intro1 = new BackgroundPanel("/img/backgrounds/intro1.png");
    JPanel intro2 = new BackgroundPanel("/img/backgrounds/intro2.png");
    JPanel intro3 = new BackgroundPanel("/img/backgrounds/intro3.png");

    intro1.setLayout(null);
    intro2.setLayout(null);
    intro3.setLayout(null);

    ImageIcon buttonIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/img/intro/playButton.png")));
    Image img = buttonIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img);

    JButton nextButton1 = new JButton(buttonIcon);
    nextButton1.setBounds(880, 548, 40, 40);
    nextButton1.setBorderPainted(false);
    nextButton1.setFocusPainted(false);
    nextButton1.setContentAreaFilled(false);
    nextButton1.addActionListener(e -> introCardLayout.next(panel));
    intro1.add(nextButton1);

    JButton nextButton2 = new JButton(buttonIcon);
    nextButton2.setBounds(880, 548, 40, 40);
    nextButton2.setBorderPainted(false);
    nextButton2.setFocusPainted(false);
    nextButton2.setContentAreaFilled(false);
    nextButton2.addActionListener(e -> introCardLayout.next(panel));
    intro2.add(nextButton2);

    JButton playButton = new JButton(buttonIcon);
    playButton.setBounds(880, 548, 40, 40);
    playButton.setBorderPainted(false);
    playButton.setFocusPainted(false);
    playButton.setContentAreaFilled(false);
    playButton.addActionListener(e -> {
      cardLayout.show(cardPanel, "main"); // 이거 왜 안 되냐
      introCardLayout.show(panel, "intro1"); //처음으로 돌려놓기

      //manager.getGamePanel().startGame();
      manager.startGameSequence();// 아이콘 떨어짐 동작 시작
      manager.getGamePanel().startGame(); // 아이콘 떨어짐 동작 시작
      mp3Player.close();
    }); // GameScreen으로 이동

    intro3.add(playButton);

    panel.add(intro1, "intro1");
    panel.add(intro2, "intro2");
    panel.add(intro3, "intro3");

    return panel;
  }

  public void playStartMusic() {
    System.out.println("main 시작"); // 지우면 안 됨
    if (!isSoundPlaying) {
      isSoundPlaying = true;
      sound = new Thread(() -> {
        try {
          // 기존 mp3Player를 닫기 전에 반드시 종료 처리
          if (mp3Player != null) {
            mp3Player.close();  // 기존 재생 중인 노래 종료
          }
          try (InputStream fis = getClass().getResourceAsStream("/audio/start.mp3")) {
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

  public void playIntroMusic() {
    System.out.println("intro 시작"); // 지우면 안 됨
    if (!isSoundPlaying) {
      //System.out.println("짜잔");

      isSoundPlaying = true;
      sound = new Thread(() -> {
        try {
          // 기존 mp3Player를 닫기 전에 반드시 종료 처리
          if (mp3Player != null) {
            mp3Player.close();  // 기존 재생 중인 노래 종료
            System.out.println("노래종료");

          }
          try (InputStream fis = getClass().getResourceAsStream("/audio/intro.mp3")) {
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


  //첫 화면에 캐릭터 도동하고 싶어서 이미지 따로 빼놓음
  static class CharacterPanel extends JPanel {

    private Image characterImage;
    private int xPosition = 0;
    private int yPosition = 0;
    private boolean moveUpRight = true;
    private Timer movementTimer;

    public CharacterPanel(String imagePath) {
      try {
        characterImage = ImageIO.read(
            Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
      } catch (IOException e) {
        e.printStackTrace();
      }
      setOpaque(false);

      movementTimer = new Timer(500, e -> moveCharacter());
    }

    private void moveCharacter() {
      if (moveUpRight) {
        xPosition += 10;
        yPosition += 10;
      } else {
        xPosition -= 10;
        yPosition -= 10;
      }

      moveUpRight = !moveUpRight;

      repaint();
    }

    public void startCharacterMovement() {
      if (!movementTimer.isRunning()) {
        movementTimer.start();
      }
    }

    public void stopCharacterMovement() {
      if (movementTimer.isRunning()) {
        movementTimer.stop();
      }
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (characterImage != null) {
        g.drawImage(characterImage, xPosition, yPosition, 508, 436, this);
      }
    }
  }

}
