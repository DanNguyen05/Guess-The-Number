import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private String playerName = null;
    private final PlayerDatabase playerDB;

    public LoginDialog(JFrame parent, PlayerDatabase playerDB) {
        super(parent, "Player Login", true);
        this.playerDB = playerDB;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Player Name: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(nameLabel, cs);

        JTextField nameField = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(nameField, cs);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name");
                return;
            }

            Player player = playerDB.getPlayer(name);
            if (player == null) {
                playerDB.updatePlayerScore(name, 0);
            }
            playerName = name;
            dispose();
        });

        JPanel bp = new JPanel();
        bp.add(loginButton);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String getPlayerName() {
        return playerName;
    }
} 