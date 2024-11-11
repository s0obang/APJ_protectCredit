package org.example.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.example.Manager.BackgroundPanel;
import org.example.Manager.DatabaseManager;
import org.example.Manager.GameManager;
import org.example.Manager.LoginManager;
import org.example.entity.User;

public class StartPanel extends JPanel {

  private CardLayout cardLayout;
  private JPanel cardPanel; // 패널들을 담을 메인 패널
  private DatabaseManager dbManager = DatabaseManager.getInstance();
  private LoginManager loginManager;
  private GamePanel gamePanel;


  public StartPanel(GameManager manager, LoginManager loginManager) {
    this.loginManager = loginManager;

    cardLayout = new CardLayout();
    cardPanel = new JPanel(cardLayout);

    cardPanel.add(startGamePanel(manager), "start");
    //cardPanel.add(selectSign_(), "selectSign_");
    //cardPanel.add(signIn(), "signIn");
    //cardPanel.add(signUp(), "signUp");
    //cardPanel.add(intro(manager), "intro");

    setLayout(new BorderLayout());
    add(cardPanel, BorderLayout.CENTER);
  }

  private JPanel startGamePanel(GameManager manager) {
    JPanel panel = new BackgroundPanel(
        "src/main/java/org/example/img/backgrounds/startBackground.png"); // 배경 패널
    panel.setLayout(null); // bPanel 절대 위치 정하려고

    JPanel bPanel = new JPanel(new GridBagLayout()); // 버튼 감싸는 패널
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.weighty = 0;
    bPanel.setOpaque(false);

    Font labelFont = new Font("Neo둥근모", Font.PLAIN, 26);

    ImageIcon buttonIcon1 = new ImageIcon("src/main/java/org/example/img/intro/historyButton.png");
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

    ImageIcon buttonIcon2 = new ImageIcon("src/main/java/org/example/img/intro/playButton.png");
    Image img2 = buttonIcon2.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
    buttonIcon2 = new ImageIcon(img2);
    JButton playButton = new JButton(buttonIcon2);
    playButton.setPreferredSize(new Dimension(75, 75));

    // 게임 시작 버튼 클릭 시 아이콘 떨어짐 기능 활성화
    playButton.addActionListener(e -> {
      cardLayout.show(cardPanel, "start");
      manager.showScreen("game");
      //manager.getGamePanel().startGame();
      manager.startGameSequence();// 아이콘 떨어짐 동작 시작
    });

//        playButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                cardLayout.show(cardPanel, "selectSign_"); // 버튼 클릭 시 지정된 패널로 전환
//            }
//        });

    //테스트용으로 로그인 없앰
    playButton.addActionListener(e -> {
      cardLayout.show(cardPanel, "start");
      manager.showScreen("game");
    });

    playButton.setBorderPainted(false);
    playButton.setFocusPainted(false);
    playButton.setContentAreaFilled(false);

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

    CharacterPanel characterPanel = new CharacterPanel(
        "src/main/java/org/example/img/character/main_char_left.png");
    characterPanel.setBounds(360, 75, 410, 435);

    panel.add(bPanel);
    panel.add(characterPanel);

    return panel;
  }

  private JPanel selectSign_() {
    JPanel panel = new BackgroundPanel("src/main/java/org/example/img/backgrounds/selectSign_.png");
    panel.setLayout(null);

    ImageIcon buttonIcon1 = new ImageIcon("src/main/java/org/example/img/intro/signInButton.png");
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
        cardLayout.show(cardPanel, "signIn"); // 버튼 클릭 시 지정된 패널로 전환
      }
    });

    ImageIcon buttonIcon2 = new ImageIcon("src/main/java/org/example/img/intro/signUpButton.png");
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
        cardLayout.show(cardPanel, "signUp"); // 버튼 클릭 시 지정된 패널로 전환
      }
    });

    panel.add(signInButton);
    panel.add(signUpButton);

    return panel;
  }

  private JPanel signIn() {
    Font labelFont = new Font("Neo둥근모", Font.PLAIN, 34);

    JPanel panel = new BackgroundPanel(
        "src/main/java/org/example/img/backgrounds/signInBackground.png");
    panel.setLayout(null);

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

    ImageIcon buttonIcon = new ImageIcon("src/main/java/org/example/img/intro/playButton.png");
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

        //User user = new User(nick, pass);
        boolean isSuccess = dbManager.signIn(dbManager, nick, pass);

        if (isSuccess) {
          cardLayout.show(cardPanel, "intro");
        } else {
          JOptionPane.showMessageDialog(panel, "로그인 실패. 닉네임이나 비밀번호를 확인하세요.", "오류",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    panel.add(nickname);
    panel.add(password);
    panel.add(playButton);

    return panel;
  }

  private JPanel signUp() {
    Font labelFont = new Font("Neo둥근모", Font.PLAIN, 34);

    JPanel panel = new BackgroundPanel(
        "src/main/java/org/example/img/backgrounds/signUpBackground.png");
    panel.setLayout(null);

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

    ImageIcon buttonIcon = new ImageIcon("src/main/java/org/example/img/intro/playButton.png");
    Image img = buttonIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
    buttonIcon = new ImageIcon(img);
    JButton signUpButton = new JButton(buttonIcon);
    signUpButton.setBorderPainted(false);
    signUpButton.setFocusPainted(false);
    signUpButton.setContentAreaFilled(false);
    signUpButton.setBounds(500, 580, 70, 70);
    signUpButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String nick = nickname.getText();
        String pass = new String(password.getPassword());
        String passCheck = new String(passwordCheck.getPassword());

        if (!pass.equals(passCheck)) {
          JOptionPane.showMessageDialog(panel, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
          return;
        }

        User newUser = new User(nick, pass);
        boolean isSuccess = dbManager.signUp(nick, pass);

        if (isSuccess) {
          loginManager.saveLoggedInUser(newUser);
          cardLayout.show(cardPanel, "start");
        } else {
          JOptionPane.showMessageDialog(panel, "회원가입 실패. 다시 시도해주세요.", "오류",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    panel.add(nickname);
    panel.add(password);
    panel.add(passwordCheck);
    panel.add(signUpButton);

    return panel;
  }


  private JPanel intro(GameManager manager) {
    JPanel panel = new JPanel(new CardLayout());
    CardLayout introCardLayout = (CardLayout) panel.getLayout();

    JPanel intro1 = new BackgroundPanel("src/main/java/org/example/img/backgrounds/intro1.png");
    JPanel intro2 = new BackgroundPanel("src/main/java/org/example/img/backgrounds/intro2.png");
    JPanel intro3 = new BackgroundPanel("src/main/java/org/example/img/backgrounds/intro3.png");

    intro1.setLayout(null);
    intro2.setLayout(null);
    intro3.setLayout(null);

    ImageIcon buttonIcon = new ImageIcon("src/main/java/org/example/img/intro/playButton.png");
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
      cardLayout.show(cardPanel, "start");
      manager.showScreen("game");
    }); // GameScreen으로 이동

    intro3.add(playButton);

    panel.add(intro1, "intro1");
    panel.add(intro2, "intro2");
    panel.add(intro3, "intro3");

    return panel;
  }

  //첫 화면에 캐릭터 도동하고 싶어서 이미지 따로 빼놓음
  static class CharacterPanel extends JPanel {

    private Image characterImage;

    public CharacterPanel(String imagePath) {
      try {
        characterImage = ImageIO.read(new File(imagePath));
      } catch (IOException e) {
        e.printStackTrace();
      }
      setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (characterImage != null) {
        g.drawImage(characterImage, 0, 0, 410, 425, this);
      }
    }
  }


}
