package org.example.Manager;

import org.example.entity.Coin;
import org.example.entity.Icon;
import org.example.object.CoinCrash;
import org.example.object.IconCrash;

public class GameInitializer {

  public static void initializeEntities(CoinCrash crash, IconCrash iconCrash) {
    for (int i = 0; i < 5; i++) {
      Coin.createAndAddCoin(1080, 720, 30, 30);
      crash.addEntity(Coin.arraycoin.get(Coin.arraycoin.size() - 1));
    }
    for (int i = 0; i < 5; i++) {
      Icon.createAndAddIcon(1080, 720);
      iconCrash.addEntity(Icon.iconList.get(Icon.iconList.size() - 1));
    }
  }
}