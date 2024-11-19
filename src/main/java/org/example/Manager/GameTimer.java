package org.example.Manager;

import javax.swing.Timer;

import org.example.entity.Coin;
import org.example.entity.Professor;
import org.example.object.CoinCrash;
import org.example.object.IconCrash;
import org.example.panels.GamePanel;


public class GameTimer extends Timer {

  public GameTimer(IconManager iconManager, CoinCrash crash, IconCrash iconCrash,
                   ProfessorManager professorManager, GamePanel panel) {
    super(30, e -> {
      // 아이콘과 코인 업데이트
      iconManager.updateIcons();
      iconManager.updateCoins();

      // Player 객체의 blanket 상태 확인
      if (!panel.getPlayer().isBlanketActive()) {
        // blanket 상태가 아니면 충돌 체크
        crash.checkCollisions();  // Coin과의 충돌 체크
        iconCrash.checkCollisions();  // 아이콘과의 충돌 체크
        // 교수님 업데이트
        professorManager.update();
      }

      // 패널 다시 그리기
      panel.repaint();
    });
  }
}