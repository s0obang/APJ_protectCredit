package org.example.Manager;

public class PointsManager {

  public boolean isFirstFive = false;
  private int points; // 포인트 값 저장

  public PointsManager() {
    this.points = 0; // 초기 포인트 값
  }

  public void increasePoints(int value) {
    points += value;
  }

  public int getPoints() {
    return points;
  }

  public void resetPoints() {
    this.points = 0;
  }

  public boolean getisFirstFive() {
    return isFirstFive;
  }
}
