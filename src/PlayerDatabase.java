import java.sql.*;

public class PlayerDatabase {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded in PlayerDatabase");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in PlayerDatabase!");
            e.printStackTrace();
        }
    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/guess_the_number?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "";

    public PlayerDatabase() {
        try {
            // Test kết nối
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Database connected successfully");
            conn.close();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    public void updatePlayerScore(String name, int score) {
        try {
            System.out.println("Connecting to database to update score...");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database!");
            
            String sql = "INSERT INTO players (name, score) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE score = IF(? > score, ?, score)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, score);
            stmt.setInt(3, score);
            stmt.setInt(4, score);
            stmt.executeUpdate();
            System.out.println("Score updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error saving score: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Player getPlayer(String name) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT * FROM players WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Player(
                    rs.getString("name"),
                    rs.getInt("score")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting player: " + e.getMessage());
        }
        return null;
    }

    public void addPlayer(Player player) {
        updatePlayerScore(player.getName(), player.getHighScore());
    }
} 