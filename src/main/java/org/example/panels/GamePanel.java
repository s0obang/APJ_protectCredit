package org.example.panels;

import org.example.entity.Coin1;
import org.example.entity.Icon;
import org.example.entity.Entity;
import org.example.object.Crash;
import org.example.object.IconCrash;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.example.Manager.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;
import org.example.entity.Player;


public class GamePanel extends JPanel {

  private Player player; //이거 메인캐릭터임^^
  private static final Logger LOGGER = Logger.getLogger(GamePanel.class.getName()); // 강한 로그 사용
  private BufferedImage coinImage;
  private Crash crash;
  private IconCrash iconCrash;

  public GamePanel(GameManager manager) {

    setLayout(new BorderLayout());
    JButton endButton = new JButton("End Game (Game Over)");
    endButton.addActionListener(e -> manager.showEndScreen(false)); // 게임 오버로 종료 화면으로 이동
    JButton successButton = new JButton("End Game (Success)");
    successButton.addActionListener(e -> manager.showEndScreen(true)); // 성공으로 종료 화면으로 이동
//    add(endButton, BorderLayout.WEST);
//    add(successButton, BorderLayout.EAST);

    //어떻게 넘어가는지 감잡으슈 이해했으면 지워도 됨
    //게임 종료 화면으로 전환 시 boolean 값으로 게임 오버인지 졸업인지 알려줘야함
    //true -> 졸업, false -> 실패

    // Player 객체 생성 (초기 위치와 크기 설정)
    player = new Player(500, 500, 70, 70);

    // Crash 객체 생성 -> 충돌 감지에 저장
    crash = new Crash(this);
    iconCrash = new IconCrash(this, player);

    iconCrash.addEntity(player);
    crash.addEntity(player);

    //플레이어 방향키로 이동하느느거!!!
    setFocusable(true);
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        int dx = 0, dy = 0;

        // 방향키에 따라 이동 방향 설정
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP -> dy = -1;
          case KeyEvent.VK_DOWN -> dy = 1;
          case KeyEvent.VK_LEFT -> dx = -1;
          case KeyEvent.VK_RIGHT -> dx = 1;
        }

        player.move(dx, dy); // Player 이동

        repaint(); // 이동 후 다시 그리기
      }
    });
    addHierarchyListener(e -> {
      if (isShowing()) {
        requestFocusInWindow();
      }
    });

    setPreferredSize(new Dimension(1080, 720));
    setOpaque(true);

    for (int i = 0; i < 5; i++) {
      Coin1.createAndAddCoin(1080, 720);
      crash.addEntity(Coin1.arraycoin.get(Coin1.arraycoin.size() - 1));
    }

    for (int i = 0; i < 5; i++) {
      Icon.createAndAddIcon(1080, 720);
      iconCrash.addEntity(Icon.iconList.get(Icon.iconList.size() - 1));
    }

    // 타이머 설정 (30밀리초마다 업데이트)
    Timer timer = new Timer(30, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateIcons();// 아이콘 위치 업데이트
        updateCoins();
        crash.checkCollisions();
        iconCrash.checkCollisions();
        repaint(); // 화면 다시 그리기
      }
    });
    timer.start(); // 타이머 시작
    requestFocusInWindow();


  }

  // 패널에 아이콘을 그리는 메서드
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g); // 부모 패널 초기화

    // 배경색을 설정
    g.setColor(Color.LIGHT_GRAY); // 배경색을 연한 회색으로 설정
    g.fillRect(0, 0, getWidth(), getHeight()); // 패널 전체에 색칠

    // Player 그리기
    player.draw(g);

    for (Icon icon : Icon.iconList) {
      icon.draw(g);
    }
    for (Coin1 coin : Coin1.arraycoin) {
      coin.draw(g);
    }

  }

  private void updateIcons() {
    for (Icon icon : Icon.iconList) {
      icon.fall();
    }
  }
  private void updateCoins() {
    for (Coin1 coin : Coin1.arraycoin) {
      coin.fall();
    }
  }


}
