package org.example.Manager;

import org.example.Security.HashUtil;
import org.example.entity.User;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private String url = "jdbc:mysql://localhost:3306/로컬 DB 주소"; // 로컬 DB 주소
    private String user = ""; // DB user
    private String password = ""; // DE password

    private DatabaseManager() {
        createUserTable();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nickname VARCHAR(50) NOT NULL,"
                + "password VARCHAR(255) NOT NULL"
                + ");";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("테이블 생성 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean signUp(String nickname, String password) {
        String hashedPassword = HashUtil.hashPassword(password);
        String sql = "INSERT INTO users (nickname, password) VALUES (?, ?);";

        try (Connection conn = DriverManager.getConnection(url, user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nickname);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            System.out.println("회원가입 성공");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("회원가입 실패: " + e.getMessage());
            return false;
        }
    }

    public boolean signIn(DatabaseManager dbManager, String nickname, String password) {
        String hashedPassword = HashUtil.hashPassword(password); // 비번 해싱
        String sql = "SELECT * FROM users WHERE nickname = ? AND password = ?;";

        try (Connection conn = DriverManager.getConnection(url, user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nickname);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("로그인 성공");
                return true;
            } else {
                System.out.println("로그인 실패: 사용자 정보가 없습니다.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("로그인 처리 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    public User findUser(User user) {
        String nickname = user.getNickname();
        String password = user.getPassword();

        String sql = "SELECT password FROM users WHERE nickname = ?";

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nickname);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                // 입력한 비밀번호 해싱 후 비교
                String hashedPassword = HashUtil.hashPassword(password);

                if (storedHashedPassword.equals(hashedPassword)) {
                    System.out.println("로그인 성공!");
                    return user; // 일치하는 경우 사용자 반환
                } else {
                    System.out.println("비밀번호가 일치하지 않습니다.");
                    return null; // 비밀번호가 일치하지 않음
                }
            } else {
                System.out.println("사용자를 찾을 수 없습니다.");
                return null; // 사용자가 없음
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
