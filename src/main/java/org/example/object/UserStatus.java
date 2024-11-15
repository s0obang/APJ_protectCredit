package org.example.object;

import lombok.Getter;

@Getter
public class UserStatus {

  private int userGrade;
  private double userScore;
  private int userPoints;
  private boolean isGraduated;

  public UserStatus(int userGrade, double userScore, int userPoints, boolean isGraduated) {
    this.userGrade = userGrade;
    this.userScore = userScore;
    this.userPoints = userPoints;
    this.isGraduated = isGraduated;
  }

}
