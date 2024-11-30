package org.example;

import org.example.Manager.GameManager;
import org.example.object.UserStatus;

public class Main {

  public static void main(String[] args) {

    System.setProperty("file.encoding", "UTF-8");

    UserStatus userStatus = new UserStatus(1, 4.5, 0, false);
    new GameManager(userStatus);
  }
}