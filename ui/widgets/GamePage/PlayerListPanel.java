package ui.widgets.GamePage;

import java.awt.*;
import javax.swing.*;

public class PlayerListPanel extends JPanel {
    private JLabel[] playerLabels;

    public PlayerListPanel(String[] players) {
        setLayout(new GridLayout(players.length, 1, 0, 10)); // Vertical layout with 10px gap
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerLabels = new JLabel[players.length];

        for (int i = 0; i < players.length; i++) {
            JLabel label = new JLabel(players[i], SwingConstants.LEFT);
            label.setFont(new Font("Arial", Font.PLAIN, 22));
            label.setForeground(i == 0 ? Color.RED : Color.LIGHT_GRAY); // First player is current by default
            playerLabels[i] = label;
            add(label);
        }
    }

    public void updateCurrentPlayer(int currentPlayerIndex) {
        for (int i = 0; i < playerLabels.length; i++) {
            playerLabels[i].setForeground(i == currentPlayerIndex ? Color.RED : Color.LIGHT_GRAY);
        }
    }
}