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
    private String url = "jdbc:mysql://localhost:3306/db"; // 로컬 DB 주소
    private String user = "root"; // DB user
    private String password = "pw"; // DE password

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
            System.out.println("users 테이블 생성 및 연결 성공");

            stmt.execute(historyTableSql);
            System.out.println("history 테이블 생성 및 연결 성공");
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

    public boolean isNicknameDuplicate(String nickname) {
        String query = "SELECT COUNT(*) FROM users WHERE nickname = ?";
        try (Connection conn = DriverManager.getConnection(url, user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nickname);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

                int userId = rs.getInt("id");
                User user = new User(userId, nickname, password);

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

                String hashedPassword = HashUtil.hashPassword(password);

                if (storedHashedPassword.equals(hashedPassword)) {
                    System.out.println("로그인 성공!");
                    return user;
                } else {
                    System.out.println("비밀번호가 일치하지 않습니다.");
                    return null;
                }
            } else {
                System.out.println("사용자를 찾을 수 없습니다.");
                return null;
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
                        "    SELECT userId, MAX(points) AS max_points, RANK() OVER (ORDER BY MAX(points) DESC) AS `rank` " +
                        "    FROM history " +
                        "    GROUP BY userId" +
                        ") " +
                        "SELECT `rank` FROM ranked_history WHERE userId = ? LIMIT 1";

        int rank = -1;

        try (Connection conn = DriverManager.getConnection(url, this.user, password)) {
            // 기록 저장
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, user.getUserId());
                insertStmt.setLong(2, gameResult.getPoints());

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

            // 최고 점수 기반 순위 조회
            try (PreparedStatement rankStmt = conn.prepareStatement(rankSql)) {
                rankStmt.setInt(1, user.getUserId());

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



    public List<Map<String, String>> getRanking() {
        String sql =
                "WITH user_max_scores AS (" +
                        "    SELECT h.userId, u.nickname, MAX(h.points) AS max_points " +
                        "    FROM history h " +
                        "    INNER JOIN users u ON h.userId = u.id " +
                        "    GROUP BY h.userId, u.nickname" +
                        "), " +
                        "ranked_scores AS (" +
                        "    SELECT nickname, max_points, RANK() OVER (ORDER BY max_points DESC) AS `rank` " +
                        "    FROM user_max_scores" +
                        ") " +
                        "SELECT nickname, max_points, `rank` " +
                        "FROM ranked_scores " +
                        "ORDER BY `rank` " +
                        "LIMIT 11"; // 상위 11명만 가져오기

        List<Map<String, String>> rankings = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> rank = new HashMap<>();
                rank.put("nickname", rs.getString("nickname"));
                rank.put("points", String.valueOf(rs.getInt("max_points")));
                rank.put("rank", String.valueOf(rs.getInt("rank")));
                rankings.add(rank);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rankings;
    }



    public List<Map<String, String>> getRecentRecords(User user) {
        String nickname = user.getNickname();

        String userIdSql = "SELECT id FROM users WHERE nickname = ?";
        int userId = -1;

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(userIdSql)) {
            pstmt.setString(1, nickname);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (userId == -1) {
            System.out.println("유저를 찾을 수 없습니다.");
            return new ArrayList<>();
        }


        String sql = "SELECT points, date FROM history WHERE userId = ? ORDER BY date DESC, id DESC LIMIT 4";
        List<Map<String, String>> recentRecords = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> record = new HashMap<>();
                    record.put("points", String.valueOf(rs.getInt("points")));
                    record.put("date", rs.getString("date"));
                    recentRecords.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recentRecords;
    }

    public Map<String, String> getHighestScore(User user) {
        String sql = "SELECT points, date FROM history h " +
                "JOIN users u ON h.userId = u.id " +
                "WHERE u.nickname = ? " +
                "ORDER BY points DESC, date DESC LIMIT 1";

        Map<String, String> result = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(url, this.user, this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getNickname());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result.put("points", String.valueOf(rs.getInt("points")));
                    result.put("date", rs.getString("date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("최고 점수를 가져오는 중 오류 발생: " + e.getMessage());
        }

        if (result.isEmpty()) {
            System.out.println(user.getNickname() + "님의 점수 기록이 없습니다.");
        }

        return result;
    }


}
