package org.example.Manager;

import org.example.Security.HashUtil;
import org.example.entity.GameResult;
import org.example.entity.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private static DatabaseManager instance;
    private String url = "jdbc:mysql://localhost:3306/game"; // 로컬 DB 주소
    private String user = "root"; // DB user
    private String password = "0428"; // DE password

    private DatabaseManager() {
        createTables();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createTables() {
        String userTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "nickname VARCHAR(50) NOT NULL,"
                + "password VARCHAR(255) NOT NULL"
                + ");";

        String historyTableSql = "CREATE TABLE IF NOT EXISTS history ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "userId INT NOT NULL,"
                + "points INT NOT NULL,"
                + "date DATETIME NOT NULL,"
                + "FOREIGN KEY (userId) REFERENCES users(id)"
                + ");";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(userTableSql);
            System.out.println("users 테이블 생성 성공");

            stmt.execute(historyTableSql);
            System.out.println("history 테이블 생성 성공");
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

                // userId를 조회하여 User 객체 생성
                int userId = rs.getInt("id");  // id (PRIMARY KEY)
                User user = new User(userId, nickname, password);  // User 객체 생성

                // 로그인한 유저 정보 저장
                LoginManager.saveLoggedInUser(user);

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

    public int saveScoreAndGetRank(GameResult gameResult, User user) {
        String insertSql = "INSERT INTO history (userId, points, date) VALUES (?, ?, ?)";
        String rankSql =
                "WITH ranked_history AS (" +
                        "    SELECT userId, points, RANK() OVER (ORDER BY points DESC) AS `rank` " +
                        "    FROM history" +
                        ") " +
                        "SELECT `rank` FROM ranked_history WHERE userId = ? AND points = ? LIMIT 1";

        int rank = -1;

        try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
            // 게임 기록 저장
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, user.getUserId());
                insertStmt.setLong(2, gameResult.getPoints());

                // 현재 날짜만 가져오기 (시간 없이)
                String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                insertStmt.setString(3, formattedDate);

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("게임 기록이 성공적으로 저장되었습니다.");
                } else {
                    System.out.println("게임 기록 저장 실패.");
                    return -1;
                }
            }

            // 순위 조회
            try (PreparedStatement rankStmt = conn.prepareStatement(rankSql)) {
                rankStmt.setInt(1, user.getUserId());
                rankStmt.setLong(2, gameResult.getPoints());

                try (ResultSet rs = rankStmt.executeQuery()) {
                    if (rs.next()) {
                        rank = rs.getInt("rank");
                    }
                }

                if (rank == -1) {
                    System.out.println("순위를 가져오는 데 실패했습니다.");
                } else {
                    System.out.println("현재 순위: " + rank);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL 처리 중 오류 발생: " + e.getMessage());
        }

        return rank;
    }


    public List<Map<String, Object>> getRanking() {
        String sql = "SELECT nickname, max_points FROM ranking_view";
        List<Map<String, Object>> rankings = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> rank = new HashMap<>();
                rank.put("nickname", rs.getString("nickname"));
                rank.put("points", rs.getInt("max_points"));
                rankings.add(rank);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rankings;
    }


}
