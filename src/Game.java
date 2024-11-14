import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Game extends JFrame {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded in Game");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found in Game!");
            e.printStackTrace();
        }
    }

    // create a JPanel inside the JFrame
    private JPanel viewPanel;
    private final String basePath;
    private PlayerDatabase playerDB;
    private Player currentPlayer;

    // Constructor of the Class
    public Game() {
        basePath = System.getProperty("user.dir");
        System.out.println("Base path: " + basePath);

        try {
            viewPanel = new JPanel(new BorderLayout());
            this.setTitle("Guess the Number");
            this.setPreferredSize(new Dimension(350, 660));
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Sửa đường dẫn icon
            File iconFile = new File(basePath, "res/icon.png");
            System.out.println("Loading icon from: " + iconFile.getAbsolutePath());
            if (iconFile.exists()) {
                this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconFile.getAbsolutePath()));
            } else {
                System.err.println("Icon file not found at: " + iconFile.getAbsolutePath());
            }
            
            // Khởi tạo database và xử lý lỗi
            try {
                playerDB = new PlayerDatabase();
                
                // Hiển thị dialog đăng nhập
                LoginDialog loginDialog = new LoginDialog(this, playerDB);
                loginDialog.setVisible(true);
                
                String playerName = loginDialog.getPlayerName();
                if (playerName == null) {
                    System.exit(0);
                }
                
                currentPlayer = playerDB.getPlayer(playerName);
                if (currentPlayer == null) {
                    // Nếu không thể kết nối server, tạo player tạm thời
                    currentPlayer = new Player(playerName);
                }
            } catch (Exception e) {
                System.err.println("Error connecting to server:");
                e.printStackTrace();
                // Tạo player tạm thời nếu không thể kết nối
                currentPlayer = new Player("Guest");
            }
            
            showView(new Menu(this));
            this.add(viewPanel, BorderLayout.CENTER);
            this.setVisible(true);
            this.pack();
            this.setResizable(false);
            this.setLocationRelativeTo(null);
            
            background_music();
            
        } catch (Exception e) {
            System.err.println("Error during game initialization:");
            e.printStackTrace();
        }
    }

    // Method To View the Panel being assigned
    public void showView(JPanel jPanel){
        viewPanel.removeAll();
        viewPanel.add(jPanel, BorderLayout.CENTER);
        viewPanel.revalidate();
        viewPanel.repaint();
    }

    // Method For Background Music
    public void background_music() {
        try {
            File soundFile = new File(basePath + "/res/background_music.wav");
            System.out.println("Loading music from: " + soundFile.getAbsolutePath());
            
            if (!soundFile.exists()) {
                System.err.println("Music file not found at: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
            clip.start();
            System.out.println("Background music started successfully");
            
        } catch (Exception e) {
            System.err.println("Error playing background music");
            e.printStackTrace();
        }
    }

    // Thêm getter cho currentPlayer
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public PlayerDatabase getPlayerDB() {
        return playerDB;
    }
}
