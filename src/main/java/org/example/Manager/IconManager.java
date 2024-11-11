package org.example.Manager;

import org.example.entity.Coin;
import org.example.entity.Icon;

public class IconManager {

  public void updateIcons() {
    for (Icon icon : Icon.iconList) {
      icon.fall();
    }
  }

  public void updateCoins() {
    for (Coin coin : Coin.arraycoin) {
      coin.fall();
    }
  }
}
