package org.example.Manager;

import org.example.entity.Coin1;
import org.example.entity.Icon;

public class IconManager {

  public void updateIcons() {
    for (Icon icon : Icon.iconList) {
      icon.fall();
    }
  }

  public void updateCoins() {
    for (Coin1 coin : Coin1.arraycoin) {
      coin.fall();
    }
  }
}
