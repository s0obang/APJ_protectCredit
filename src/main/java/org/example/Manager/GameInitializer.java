package org.example.Manager;

import org.example.entity.Coin1;
import org.example.entity.Icon;
import org.example.object.Crash;
import org.example.object.IconCrash;

public class GameInitializer {

  public static void initializeEntities(Crash crash, IconCrash iconCrash) {
    for (int i = 0; i < 5; i++) {
      Coin1.createAndAddCoin(1080, 720);
      crash.addEntity(Coin1.arraycoin.get(Coin1.arraycoin.size() - 1));
    }
    for (int i = 0; i < 5; i++) {
      Icon.createAndAddIcon(1080, 720);
      iconCrash.addEntity(Icon.iconList.get(Icon.iconList.size() - 1));
    }
  }
}