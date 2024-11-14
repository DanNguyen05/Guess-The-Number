import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ScoreFiles {
    private final String basePath;

    public ScoreFiles() {
        basePath = System.getProperty("user.dir") + "/Guess-The-Number";
    }

    // Show the 1st Line Score in a filename and returns a String
    public String showScore(String filename){
        String score = "0";
        try{
            File text = new File(basePath, filename);
            System.out.println("Reading score from: " + text.getAbsolutePath());
            
            if (!text.exists() || text.length() == 0) {
                text.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(text);
                writer.write("0\n0");
                writer.close();
            }
            
            Scanner scan = new Scanner(text);
            if (scan.hasNextLine()) {
                score = scan.nextLine();
            }
            scan.close();
        } catch (Exception e) {
            System.err.println("Error reading score file: " + filename);
            e.printStackTrace();
        }
        return score;
    }

    // Show the 2st Line Attempt in a filename and returns a String
    public String showGames(String filename){
        String games = "0";
        try{
            File text = new File(basePath, filename);
            Scanner scan = new Scanner(text);
            
            // Skip first line (score)
            if (scan.hasNextLine()) {
                scan.nextLine();
                // Read second line (games)
                if (scan.hasNextLine()) {
                    games = scan.nextLine();
                }
            }
            scan.close();
        } catch (Exception e) {
            System.err.println("Error reading games from file: " + filename);
            e.printStackTrace();
        }
        return games;
    }

    // Convert the showScore method to int
    public int intScore(String filename){
        return Integer.parseInt(showScore(filename));
    }

    // Convert the showsGames method to int
    public int intGames(String filename){
        return Integer.parseInt(showGames(filename));
    }

    // Overwrites the text file
    // Used in current_score.txt and num_game.txt
    public void write(String filename, int score){
        try {
            File file = new File(basePath, filename);
            file.getParentFile().mkdirs();
            FileWriter score_writer = new FileWriter(file);
            score_writer.write(String.valueOf(score));
            score_writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filename);
            e.printStackTrace();
        }
    }

    // Overwrites the text file
    // Used in high_score.txt
    public void writeScoreAttempts(String filename, int score, int attempts){
        try {
            File file = new File(basePath, filename);
            file.getParentFile().mkdirs();
            FileWriter score_writer = new FileWriter(file);
            score_writer.write(String.valueOf(score));
            score_writer.write("\n");
            score_writer.write(String.valueOf(attempts));
            score_writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filename);
            e.printStackTrace();
        }
    }

    // Compare the score if it is a high score
    public void compareScore(String high_score, String current_score, String current_played_games){
        // If the current score is higher than inside the high_score.txt file
        if (intScore(high_score) < intScore(current_score)){
            // Overwrite with new Score
            writeScoreAttempts(high_score, intScore(current_score), intScore(current_played_games));
        }
        // Else if the current score and the score inside the high_score.txt is equals
        else if(intScore(high_score) == intScore(current_score)){
            // If the number of games played by the user is lower than inside the high_score.txt
            if (intGames(high_score) > intScore(current_played_games)){
                // Overwrite with new Score
                writeScoreAttempts(high_score, intScore(current_score), intScore(current_played_games));
            }
        }
    }
}
