package ui.views;

import components.Button;
import components.GameFrame;
import components.Label;
import components.Panel;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import ui.widgets.GamePage.PlayerListPanel;
import gameLogique.Game;
import gameLogique.Player;
import gameLogique.Card;

public class GamePage {
    private GameFrame gameFrame;
    private Label lastCardPlayed;
    private Label lastCardLabel;
    private Label currentPlayerLabel;
    private Button drawCardBtn;
    private Panel playerHandPanel;
    private PlayerListPanel playerListPanel;
    private ArrayList<Player> players;
    private String[] playersName;
    private int currentPlayerIndex = 0;
    private ArrayList<String> playerHand;
    private Game game;

    private ArrayList<ImageIcon> cardIcons = new ArrayList<>();
    private HashMap<String, ImageIcon> cardImageMap = new HashMap<>();

    public GamePage(Game game) {
        this.game = game;
        initializeUI();
        initializeGame();
    }

    private void initializeUI() {
        // Initialize frame
        gameFrame = new GameFrame("UNO Game", new Color(0x042e54), new Dimension(900, 700), null);
        
        // Initialize players list
        players = game.getPlayers();
        playersName = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playersName[i] = players.get(i).getName();
        }

        // Create player list panel
        playerListPanel = new PlayerListPanel(playersName);
        playerListPanel.setBounds(30, 30, 200, 200);
        gameFrame.addWidget(playerListPanel);

        // Create last card played label
        lastCardLabel = new Label("LAST CARD PLAYED", Label.CENTER);
        lastCardLabel.adjustFont(new Font("Arial", Font.BOLD, 30));
        lastCardLabel.setTextColor(Color.WHITE);
        lastCardLabel.setBounds(0, 10, 900, 50);
        gameFrame.addWidget(lastCardLabel);

        // Create last played card display
        lastCardPlayed = new Label();
        lastCardPlayed.setBounds(375, 110, 150, 230);
        gameFrame.addWidget(lastCardPlayed);

        // Create player's hand panel
        playerHandPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        JScrollPane handScrollPane = new JScrollPane(playerHandPanel);
        handScrollPane.setBounds(50, 400, 800, 185);
        gameFrame.addWidget(handScrollPane);

        // Create current player label
        currentPlayerLabel = new Label("", Label.LEFT);
        currentPlayerLabel.adjustFont(new Font("Arial", Font.BOLD, 20));
        currentPlayerLabel.setTextColor(Color.WHITE);
        currentPlayerLabel.setBounds(50, 620, 300, 30);
        gameFrame.addWidget(currentPlayerLabel);

        // Create Draw Card button
        drawCardBtn = new Button("Draw Card");
        drawCardBtn.setBounds(700, 610, 150, 50);
        drawCardBtn.setBGColor(new Color(0x1a73e8));
        drawCardBtn.setForeground(Color.WHITE);
        drawCardBtn.adjustFont(new Font("Arial", Font.BOLD, 20));
        drawCardBtn.addActionListener(e -> handleDrawCard());
        gameFrame.addWidget(drawCardBtn);
    }

    private void initializeGame() {
        // Initialize game state
        game.setupGame();
        loadAllCardImages();
        updateGameState();
    }

    private void updateGameState() {
        updatePlayerHand();
        updateLastPlayedCard();
        updateCurrentPlayer();
        showPlayerHand();
    }

    private void updatePlayerHand() {
        playerHand = new ArrayList<>();
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
        for (Card card : currentPlayer.getPlayerHnad()) {
            playerHand.add(card.toString());
        }
    }

    private void updateLastPlayedCard() {
        ArrayList<Card> discardPile = game.getDiscardPile();
        if (!discardPile.isEmpty()) {
            Card lastCard = discardPile.get(discardPile.size() - 1);
            // Convert card to the correct format for image lookup
            String cardName = lastCard.getColor() + " " + lastCard.getValue();
            setCardImage(lastCardPlayed, cardName);
        }
    }

    private void updateCurrentPlayer() {
        currentPlayerIndex = game.getCurrentPlayerIndex();
        currentPlayerLabel.setText(playersName[currentPlayerIndex] + "'s turn");
        playerListPanel.updateCurrentPlayer(currentPlayerIndex);
    }

    private void handleDrawCard() {
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
        if (currentPlayer.getPlayerType().equals("Human")) {
            try {
                Card drawnCard = game.getDeck().drawCard();
                currentPlayer.addToHand(drawnCard);
                updateGameState();
                JOptionPane.showMessageDialog(gameFrame, "You drew: " + drawnCard);
                // Move to next player after drawing
                game.nextPlayer();
                updateGameState();
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(gameFrame, "The deck is empty!");
            }
        }
    }

    private void playCard(String cardName) {
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
        Card playedCard = null;
        for (Card card : currentPlayer.getPlayerHnad()) {
            if (card.toString().equals(cardName)) {
                playedCard = card;
                break;
            }
        }

        if (playedCard != null) {
            // Try to play the card through game logic
            if (game.isValidMove(playedCard)) {
                currentPlayer.getPlayerHnad().remove(playedCard);
                game.getDiscardPile().add(playedCard);
                game.applyCardEffect(playedCard);
                // Move to next player after playing a card
                game.nextPlayer();
                updateGameState();
            } else {
                JOptionPane.showMessageDialog(gameFrame, "Invalid move! You cannot play that card.");
            }
        }
    }

    private void showPlayerHand() {
        playerHandPanel.removeAll();
        int cardWidth = 100;
        int cardHeight = 150;

        for (String cardName : playerHand) {
            Button card = new Button();
            // Convert card name to the correct image filename format
            String[] parts = cardName.split(" ");
            String color = parts[0];
            String value = parts[1];
            
            // Handle special cases for Wild cards
            if (value.equals("Wild")) {
                value = "Wild";
            } else if (value.equals("WildDrawFour")) {
                value = "WildDrawFour";
            }
            
            String filename = value + "_" + color + ".png";
            ImageIcon icon = cardImageMap.get(filename);
            
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(scaled));
                card.setPreferredSize(new Dimension(cardWidth, cardHeight));
                card.setMaximumSize(new Dimension(cardWidth, cardHeight));
                card.setBorder(null);
                card.setContentAreaFilled(false);
                card.addActionListener(e -> playCard(cardName));
                playerHandPanel.add(card);
            } else {
                System.err.println("Could not find image for card: " + cardName);
            }
        }

        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    private void loadAllCardImages() {
        Card.Value[] values = Card.Value.values();
        Card.Color[] colors = Card.Color.values();

        try {
            for (Card.Value value : values) {
                if (value == Card.Value.Wild || value == Card.Value.WildDrawFour) {
                    loadCardImage("Wild_" + value + ".png");
                } else {
                    for (Card.Color color : colors) {
                        if (color != Card.Color.Wild) {
                            loadCardImage(value + "_" + color + ".png");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading card images");
            e.printStackTrace();
        }
    }

    private void loadCardImage(String filename) {
        try {
            // Use the correct path to the cardimages directory
            URL imageUrl = getClass().getClassLoader().getResource("cardimages/" + filename);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                cardIcons.add(icon);
                // Store the full filename as the key
                cardImageMap.put(filename, icon);
            } else {
                System.err.println("Could not find image: " + filename);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + filename);
            e.printStackTrace();
        }
    }

    private void setCardImage(Label label, String cardName) {
        // Convert card name to the correct image filename format
        String[] parts = cardName.split(" ");
        String color = parts[0];
        String value = parts[1];
        
        // Handle special cases for Wild cards
        if (value.equals("Wild")) {
            value = "Wild";
        } else if (value.equals("WildDrawFour")) {
            value = "WildDrawFour";
        }
        
        String filename = value + "_" + color + ".png";
        ImageIcon icon = cardImageMap.get(filename);
        
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(150, 230, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        } else {
            System.err.println("Image not found in map: " + filename);
        }
    }
}
