package org.example.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.io.File;
import java.io.IOException;

import org.example.Manager.*;
import org.example.entity.User;

public class StartPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel; // 패널들을 담을 메인 패널
    private DatabaseManager dbManager = DatabaseManager.getInstance();
    private LoginManager loginManager;

    public StartPanel(GameManager manager) {
        this.loginManager = loginManager;

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);


        cardPanel.add(startGamePanel(manager), "main");
        cardPanel.add(selectSign_(), "selectSign_");
        cardPanel.add(signIn(), "signIn");
        cardPanel.add(signUp(), "signUp");
        cardPanel.add(intro(manager), "intro");


        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel startGamePanel(GameManager manager) {
        JPanel panel = new BackgroundPanel("src/main/java/org/example/img/backgrounds/startBackground.png"); // 배경 패널
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
        playButton.setBorderPainted(false);
        playButton.setFocusPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 로그인 되어있으면 바로 게임 시작
                if (LoginManager.getLoggedInUser() != null) {
                    cardLayout.show(cardPanel, "intro");
                } else {
                    cardLayout.show(cardPanel, "selectSign_");
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

        CharacterPanel characterPanel = new CharacterPanel("src/main/java/org/example/img/character/main_char_left.png");
        characterPanel.setBounds(360, 75, 410, 435);

        panel.add(bPanel);
        panel.add(characterPanel);

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

    private JPanel selectSign_() {
        JPanel panel = new BackgroundPanel("src/main/java/org/example/img/backgrounds/selectSign_.png");
        panel.setLayout(null);

        ImageIcon buttonIcon = new ImageIcon("src/main/java/org/example/img/intro/BackButton.png");
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
                cardLayout.show(cardPanel, "signIn");
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
                cardLayout.show(cardPanel, "signUp");
            }
        });

        panel.add(backButton);
        panel.add(signInButton);
        panel.add(signUpButton);

        return panel;
    }

    private JPanel signIn() {
        Font labelFont = new Font("Neo둥근모", Font.PLAIN, 34);

        JPanel panel = new BackgroundPanel("src/main/java/org/example/img/backgrounds/signInBackground.png");
        panel.setLayout(null);

        ImageIcon buttonIcon1 = new ImageIcon("src/main/java/org/example/img/intro/BackButton.png");
        Image img1 = buttonIcon1.getImage().getScaledInstance(65, 60, Image.SCALE_SMOOTH);
        buttonIcon1 = new ImageIcon(img1);
        JButton backButton = new JButton(buttonIcon1);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBounds(20, 20, 65, 60);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "selectSign_");
            }
        });

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

                boolean isSuccess = dbManager.signIn(dbManager, nick, pass);

                if (isSuccess) {
                    System.out.println(LoginManager.getLoggedInUser());
                    nickname.setText("");
                    password.setText("");
                    cardLayout.show(cardPanel, "intro");
                } else {
                    JOptionPane.showMessageDialog(panel, "로그인 실패. 닉네임이나 비밀번호를 확인하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(nickname);
        panel.add(password);
        panel.add(playButton);
        panel.add(backButton);

        return panel;
    }

    private JPanel signUp() {
        Font labelFont = new Font("Neo둥근모", Font.PLAIN, 34);

        JPanel panel = new BackgroundPanel("src/main/java/org/example/img/backgrounds/signUpBackground.png");
        panel.setLayout(null);

        ImageIcon buttonIcon1 = new ImageIcon("src/main/java/org/example/img/intro/BackButton.png");
        Image img1 = buttonIcon1.getImage().getScaledInstance(65, 60, Image.SCALE_SMOOTH);
        buttonIcon1 = new ImageIcon(img1);
        JButton backButton = new JButton(buttonIcon1);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBounds(20, 20, 65, 60);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "selectSign_");
            }
        });

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

                if (nick.isEmpty() || pass.isEmpty() || passCheck.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "모든 필드를 채워주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!pass.equals(passCheck)) {
                    JOptionPane.showMessageDialog(panel, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(panel, "회원가입 실패. 다시 시도해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(nickname);
        panel.add(password);
        panel.add(passwordCheck);
        panel.add(signUpButton);
        panel.add(backButton);

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
            cardLayout.show(cardPanel, "main"); // 이거 왜 안 되냐
            introCardLayout.show(panel, "intro1"); //처음으로 돌려놓기

            //manager.getGamePanel().startGame();
            manager.startGameSequence();// 아이콘 떨어짐 동작 시작
            manager.getGamePanel().startGame(); // 아이콘 떨어짐 동작 시작
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
        private int xPosition = 0;
        private int yPosition = 0;
        private boolean moveUpRight = true;
        private Timer movementTimer;

        public CharacterPanel(String imagePath) {
            try {
                characterImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            setOpaque(false);


            movementTimer = new Timer(500, e -> moveCharacter());
        }

        private void moveCharacter() {
            if (moveUpRight) {
                xPosition -= 10;
                yPosition -= 10;
            } else {
                xPosition += 10;
                yPosition += 10;
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
                g.drawImage(characterImage, xPosition, yPosition, 410, 425, this);
            }
        }
    }

}
