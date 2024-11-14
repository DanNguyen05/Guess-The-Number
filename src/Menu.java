import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.*;
import java.util.List;

public class Menu extends JPanel {

    static {
        try {
            // Load driver một lần khi class được load
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully in Menu");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in Menu!");
            e.printStackTrace();
        }
    }

    // Declaring the Game class for changing the scene
    final private Game game;
    private final String basePath;

    // Create an instance of ScoreFiles class to handle the .txt Files
    ScoreFiles scoreFiles = new ScoreFiles();

    // Thêm các hằng số này
    private static final String DB_URL = "jdbc:mysql://localhost:3306/guess_the_number";
    private static final String USER = "root";
    private static final String PASS = "";

    // Constructor of the Class
    public Menu(Game game){
        this.game = game;
        this.basePath = System.getProperty("user.dir") + "/Guess-The-Number";

        // Layout to be used in this panel
        BoxLayout boxlayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxlayout);

        // Called the method for adding the components in JPanel
        createGUI();
    }

    // All the components are found here
    public void createGUI(){
        try {
            System.out.println("Creating Menu GUI...");
            
            // Debug file paths
            System.out.println("Loading logo from: " + new File(basePath, "res/guess_the_number_logo.png").getAbsolutePath());
            System.out.println("Loading high score from: " + new File("scores/high_score.txt").getAbsolutePath());
            
            JLabel logoImage, scoreLabel, scoreText, playButton, playText;
            
            logoImage = new JLabel(new ImageIcon(basePath + "/res/guess_the_number_logo.png"));
            logoImage.setBorder(new EmptyBorder(100, 0, 0, 0));
            logoImage.setAlignmentX(CENTER_ALIGNMENT);
            add(logoImage);

            // Setting up and Display the High Score Label Image
            scoreLabel = new JLabel(new ImageIcon(basePath + "/res/high_score_label.png"));
            scoreLabel.setBorder(new EmptyBorder(35, 0, 0, 0));
            scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
            add(scoreLabel);

            // Setting up and Display the High Score
            scoreText = new JLabel(scoreFiles.showScore("scores/high_score.txt") + " points for " + scoreFiles.showGames("scores/high_score.txt") + " games");
            scoreText.setFont(new Font("Arial", Font.BOLD, 18));
            scoreText.setForeground(Color.WHITE);
            scoreText.setBorder(new EmptyBorder(10,0,0,0));
            scoreText.setAlignmentX(CENTER_ALIGNMENT);
            add(scoreText);

            // Setting up and Display the Play Button
            playButton = new JLabel(new ImageIcon(basePath + "/res/play_button.png"));
            playButton.setBorder(new EmptyBorder(30, 0, 0, 0));
            playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            playButton.setAlignmentX(CENTER_ALIGNMENT);
            linkPlay(playButton);
            add(playButton);

            // Setting up and Display the "Click to Play" Text Image
            playText = new JLabel(new ImageIcon(basePath + "/res/click_play.png"));
            playText.setBorder(new EmptyBorder(20,0,0,0));
            playText.setAlignmentX(CENTER_ALIGNMENT);
            add(playText);

            JButton leaderboardButton = new JButton("Leaderboard");
            leaderboardButton.setAlignmentX(CENTER_ALIGNMENT);
            leaderboardButton.addActionListener(e -> showLeaderboard());
            add(leaderboardButton);

            System.out.println("Menu GUI created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating Menu GUI:");
            e.printStackTrace();
        }
    }

    // Method for Linking to Play Section
    public void linkPlay(JLabel jLabel){
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Set the data inside to zero
                scoreFiles.write("scores/current_score.txt", 0);
                scoreFiles.write("scores/num_game.txt", 0);
                game.showView(new Play(game));
            }
        });
    }

    // For changing the background of JPanel
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(new ImageIcon(basePath + "/res/background.jpg").getImage(), 0, 0, null);
    }

    private void showLeaderboard() {
        try {
            // Load driver trực tiếp trong method
            System.out.println("Loading MySQL driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");

            System.out.println("Connecting to database...");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database!");

            String sql = "SELECT * FROM players ORDER BY score DESC LIMIT 10";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            StringBuilder sb = new StringBuilder("Top Players:\n\n");
            int rank = 1;
            
            if (!rs.isBeforeFirst()) {
                sb.append("No scores yet!");
            }
            
            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                System.out.println("Found: " + name + " - " + score);
                sb.append(String.format("%d. %s - %d points\n", rank++, name, score));
            }

            JOptionPane.showMessageDialog(this, sb.toString(), "Leaderboard", 
                JOptionPane.INFORMATION_MESSAGE);

        } catch (ClassNotFoundException e) {
            System.err.println("Driver error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "MySQL Driver not found!\nPlease check your installation.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Database error!\nPlease check if MySQL is running.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
