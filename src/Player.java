public class Player {
    private String name;
    private int highScore;

    public Player(String name) {
        this.name = name;
        this.highScore = 0;
    }

    public Player(String name, int highScore) {
        this.name = name;
        this.highScore = highScore;
    }

    public String getName() {
        return name;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int score) {
        if (score > highScore) {
            highScore = score;
        }
    }
} 