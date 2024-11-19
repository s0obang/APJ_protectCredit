package org.example.Manager;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class PointsManager {

    private int points; // 포인트 값 저장

    public PointsManager() {
        this.points = 0; // 초기 포인트 값
    }

    // 포인트 증가
    public void increasePoints(int value) {
        points += value;
        System.out.println("포인트: " + points);
    }

    // 현재 포인트 반환
    public int getPoints() {
        return points;
    }

    //게임 종료되면 이걸로 초기화
    public void resetPoints() {
        this.points = 0;
    }
}
