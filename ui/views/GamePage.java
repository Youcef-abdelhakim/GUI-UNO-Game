package ui.views;

import components.Button;
import components.GameFrame;
import components.Label;
import components.Panel;
import gameLogique.Card;
import gameLogique.Game;
import gameLogique.Player;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import ui.widgets.GamePage.PlayerListPanel;

public class GamePage {
    private GameFrame gameFrame;
    private Label lastCardPlayed;
    private Label lastCardLabel;
    private Label currentPlayerLabel;
    private Label drawCardLabel;
    private Label emptyDeckLabel;
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
        gameFrame = new GameFrame("UNO Game", new Color(0x042e54), new Dimension(900, 700), null);
        gameFrame.setLayout(new BorderLayout());

        players = game.getPlayers();
        playersName = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playersName[i] = players.get(i).getName();
        }

        // Top Panel with BorderLayout for centering
        Panel topPanel = new Panel(new BorderLayout());
        
        // Player List Panel on the left
        playerListPanel = new PlayerListPanel(playersName);
        topPanel.add(playerListPanel, BorderLayout.WEST);

        // Center panel for last card label and last card played
        Panel centerTopPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbcCenter = new GridBagConstraints();

        // Configure constraints for lastCardLabel
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.anchor = GridBagConstraints.NORTHWEST; // Align to top-left of the cell
        gbcCenter.insets = new Insets(45, 0, 0, 0); // Move 20px up, 30px left
        lastCardLabel = new Label("LAST CARD PLAYED", Label.CENTER);
        lastCardLabel.adjustFont(new Font("Arial", Font.BOLD, 30));
        lastCardLabel.setTextColor(Color.WHITE);
        centerTopPanel.add(lastCardLabel, gbcCenter);

        // Configure constraints for lastCardPlayed (unchanged)
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 1;
        gbcCenter.anchor = GridBagConstraints.CENTER; // Reset anchor for the card
        gbcCenter.insets = new Insets(0, 0, 0, 0); // Reset insets
        lastCardPlayed = new Label();
        centerTopPanel.add(lastCardPlayed, gbcCenter);

        topPanel.add(centerTopPanel, BorderLayout.CENTER);
        gameFrame.addWidget(topPanel, BorderLayout.NORTH);

        // Center panel with GridBagLayout to hold the handScrollPane
        Panel centerPanel = new Panel(new GridBagLayout());
        centerPanel.setBackground(new Color(0x042e54)); // Matches frame background

        // Player Hand Panel with FlowLayout for horizontal arrangement
        playerHandPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        playerHandPanel.setBackground(new Color(0xE0E0E0)); // Light gray background
        JScrollPane handScrollPane = new JScrollPane(playerHandPanel);
        handScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        handScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        handScrollPane.setPreferredSize(new Dimension(800, 150)); // Fixed height matching cards

        // Add handScrollPane to centerPanel with a top margin to move it downward
        GridBagConstraints gbcHand = new GridBagConstraints();
        gbcHand.gridx = 0;
        gbcHand.gridy = 0;
        gbcHand.anchor = GridBagConstraints.CENTER;
        gbcHand.insets = new Insets(20, 0, 0, 0); // 20px top margin to move it down
        centerPanel.add(handScrollPane, gbcHand);

        gameFrame.addWidget(centerPanel, BorderLayout.CENTER);

        // Bottom Panel with GridBagLayout for controls
        Panel bottomPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbcBottom = new GridBagConstraints();

        currentPlayerLabel = new Label("", Label.LEFT);
        currentPlayerLabel.adjustFont(new Font("Arial", Font.BOLD, 20));
        currentPlayerLabel.setTextColor(Color.WHITE);
        gbcBottom.gridx = 0;
        gbcBottom.gridy = 0;
        gbcBottom.weightx = 1.0;
        gbcBottom.anchor = GridBagConstraints.WEST;
        bottomPanel.add(currentPlayerLabel, gbcBottom);

        drawCardBtn = new Button("Draw Card");
        drawCardBtn.setBGColor(new Color(0x1a73e8));
        drawCardBtn.setForeground(Color.WHITE);
        drawCardBtn.adjustFont(new Font("Arial", Font.BOLD, 20));
        drawCardBtn.addActionListener(e -> handleDrawCard());
        gbcBottom.gridx = 1;
        gbcBottom.anchor = GridBagConstraints.EAST;
        bottomPanel.add(drawCardBtn, gbcBottom);

        gameFrame.addWidget(bottomPanel, BorderLayout.SOUTH);

        // Additional labels (if needed)
        drawCardLabel = new Label("", Label.CENTER);
        drawCardLabel.adjustFont(new Font("Arial", Font.BOLD, 16));
        drawCardLabel.setTextColor(Color.WHITE);

        emptyDeckLabel = new Label("", Label.CENTER);
        emptyDeckLabel.adjustFont(new Font("Arial", Font.BOLD, 16));
        emptyDeckLabel.setTextColor(Color.WHITE);

        gameFrame.pack();
        gameFrame.setSize(new Dimension(900, 700));
        gameFrame.setVisible(true);
    }

    private void initializeGame() {
        game.setupGame();
        loadAllCardImages();
        updateGameState();
    }

    private void updateGameState() {
        updatePlayerHand();
        updateLastPlayedCard();
        updateCurrentPlayer();
        showPlayerHand();
        
        ArrayList<Card> discardPile = game.getDiscardPile();
        if (!discardPile.isEmpty()) {
            Card lastCard = discardPile.get(discardPile.size() - 1);
            if (lastCard.getValue() == Card.Value.DrawTwo || lastCard.getValue() == Card.Value.WildDrawFour) {
                if (game.getCurrentPlayerIndex() == (game.getLastPlayerIndex() + 1) % players.size()) {
                    int cardsToDraw = lastCard.getValue() == Card.Value.DrawTwo ? 2 : 4;
                    handleDrawTwoOrFour(cardsToDraw);
                    return;
                }
            }
        }
        
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
        if (currentPlayer.getPlayerType().equals("Bot")) {
            Timer timer = new Timer(1000, e -> {
                handleBotTurn();
                if (!currentPlayer.getPlayerHnad().isEmpty()) {
                    updateGameState();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }

        if (currentPlayer.getPlayerHnad().isEmpty()) {
            JOptionPane.showMessageDialog(gameFrame,
                currentPlayer.getName() + " wins the game!",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        }
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
        if (currentPlayer.getPlayerType().equals("Player")) {
            try {
                boolean hasValidMove = false;
                for (Card card : currentPlayer.getPlayerHnad()) {
                    if (game.isValidMove(card)) {
                        hasValidMove = true;
                        break;
                    }
                }
                
                if (hasValidMove) {
                    JOptionPane.showMessageDialog(gameFrame, 
                        "You already have playable cards! You must play one before drawing.",
                        "Cannot Draw", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Card drawnCard = game.getDeck().drawCard();
                currentPlayer.addToHand(drawnCard);
                
                StringBuilder drawMessage = new StringBuilder(currentPlayer.getName() + " drew: " + drawnCard);
                
                if (game.isValidMove(drawnCard)) {
                    int choice = JOptionPane.showConfirmDialog(gameFrame,
                        "You drew a playable card: " + drawnCard + "\nDo you want to play it?",
                        "Play Drawn Card?",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (choice == JOptionPane.YES_OPTION) {
                        currentPlayer.getPlayerHnad().remove(drawnCard);
                        game.getDiscardPile().add(drawnCard);
                        
                        if (drawnCard.getValue() == Card.Value.Wild || drawnCard.getValue() == Card.Value.WildDrawFour) {
                            handleWildCardSelection(drawnCard, currentPlayer);
                        } else {
                            game.applyCardEffect(drawnCard, currentPlayer);
                            drawMessage.append("\n\n" + currentPlayer.getName() + " played the drawn card!");
                        }
                    } else {
                        game.nextPlayer();
                        drawMessage.append("\n\n" + currentPlayer.getName() + " chose not to play the drawn card.");
                    }
                } else {
                    game.nextPlayer();
                    drawMessage.append("\n\n" + currentPlayer.getName() + " couldn't play the drawn card.");
                }
                
                JOptionPane.showMessageDialog(gameFrame,
                    drawMessage.toString(),
                    "Card Drawn",
                    JOptionPane.INFORMATION_MESSAGE);
                
                updateGameState();
                
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(gameFrame, 
                    "The deck is empty!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
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

        if (playedCard != null && game.isValidMove(playedCard)) {
            if (playedCard.getValue() == Card.Value.Wild || 
                playedCard.getValue() == Card.Value.WildDrawFour) {
                handleWildCardSelection(playedCard, currentPlayer);
            } else {
                currentPlayer.getPlayerHnad().remove(playedCard);
                game.getDiscardPile().add(playedCard);
                game.applyCardEffect(playedCard, currentPlayer);
                updateGameState();
            }
        } else {
            JOptionPane.showMessageDialog(gameFrame, "Invalid move! You cannot play that card.");
        }
    }

    private void handleWildCardSelection(Card wildCard, Player currentPlayer) {
        currentPlayer.getPlayerHnad().remove(wildCard);
        
        Object[] options = {"Red", "Blue", "Green", "Yellow"};
        int choice = JOptionPane.showOptionDialog(gameFrame,
            "Choose a color for the Wild card:",
            "Color Selection",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == JOptionPane.CLOSED_OPTION) {
            // If dialog was closed, default to Red
            choice = 0;
        }
        
        Card.Color selectedColor = Card.Color.Red;
        switch (choice) {
            case 0: selectedColor = Card.Color.Red; break;
            case 1: selectedColor = Card.Color.Blue; break;
            case 2: selectedColor = Card.Color.Green; break;
            case 3: selectedColor = Card.Color.Yellow; break;
        }
        
        // Create a new card with the selected color
        Card coloredCard = new Card(selectedColor, wildCard.getValue());
        game.getDiscardPile().add(coloredCard);
        
        // Apply the card effect before showing the message
        game.applyCardEffect(coloredCard, currentPlayer);
        
        String nextPlayerName = players.get(game.getCurrentPlayerIndex()).getName();
        String message;
        if (wildCard.getValue() == Card.Value.WildDrawFour) {
            message = currentPlayer.getName() + " played Wild Draw Four and chose " + selectedColor + "!\n" +
                     nextPlayerName + " must draw 4 cards and play any " + selectedColor + " card.";
        } else {
            message = currentPlayer.getName() + " played Wild card and chose " + selectedColor + "!\n" +
                     nextPlayerName + " must play any " + selectedColor + " card.";
        }
        
        // Show the message in a dialog
        JOptionPane.showMessageDialog(gameFrame, message, "Color Changed", JOptionPane.INFORMATION_MESSAGE);
        
        // Update the last card played display to show the new color
        updateLastPlayedCard();
        
        // Update the game state
        updateGameState();
    }

    private void showPlayerHand() {
        playerHandPanel.removeAll();
        int cardWidth = 100;
        int cardHeight = 150;

        for (String cardName : playerHand) {
            Button card = new Button();
            String[] parts = cardName.split(" ");
            String color = parts[0];
            String value = parts[1];
            
            String filename;
            if (value.equals("Wild")) {
                filename = "Wild_Wild.png";
            } else if (value.equals("WildDrawFour")) {
                filename = "Wild_WildDrawFour.png";
            } else {
                filename = value + "_" + color + ".png";
            }
            
            ImageIcon icon = cardImageMap.get(filename);
            
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
                card.setIcon(new ImageIcon(scaled));
                card.setPreferredSize(new Dimension(cardWidth, cardHeight));
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
            URL imageUrl = getClass().getClassLoader().getResource("cardimages/" + filename);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                cardIcons.add(icon);
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
        String[] parts = cardName.split(" ");
        String color = parts[0];
        String value = parts[1];
        
        String filename;
        if (value.equals("Wild")) {
            filename = "Wild_Wild.png";
        } else if (value.equals("WildDrawFour")) {
            filename = "Wild_WildDrawFour.png";
        } else {
            filename = value + "_" + color + ".png";
        }
        
        ImageIcon icon = cardImageMap.get(filename);
        
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(150, 230, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
        } else {
            System.err.println("Image not found in map: " + filename);
        }
    }

    private void handleBotTurn() {
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
        if (currentPlayer.getPlayerType().equals("Bot")) {
            Card cardToPlay = null;
            for (Card card : currentPlayer.getPlayerHnad()) {
                if (game.isValidMove(card)) {
                    cardToPlay = card;
                    break;
                }
            }
            
            if (cardToPlay != null) {
                currentPlayer.getPlayerHnad().remove(cardToPlay);
                game.getDiscardPile().add(cardToPlay);
                game.applyCardEffect(cardToPlay, currentPlayer);
                
                updateLastPlayedCard();
            } else {
                handleBotDraw();
            }
        }
    }
    
    private void handleBotDraw() {
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
        boolean drawnPlayableCard = false;
        int cardsDrawn = 0;
        StringBuilder message = new StringBuilder(currentPlayer.getName() + " drew: ");
        Card playableCard = null;
        
        while (!drawnPlayableCard && cardsDrawn < 10) {
            try {
                Card drawnCard = game.getDeck().drawCard();
                currentPlayer.addToHand(drawnCard);
                cardsDrawn++;
                
                if (cardsDrawn > 1) {
                    message.append(", ");
                }
                message.append(drawnCard);
                
                if (game.isValidMove(drawnCard)) {
                    drawnPlayableCard = true;
                    playableCard = drawnCard;
                }
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(gameFrame, "The deck is empty!");
                break;
            }
        }
        
        if (drawnPlayableCard && playableCard != null) {
            currentPlayer.getPlayerHnad().remove(playableCard);
            game.getDiscardPile().add(playableCard);
            game.applyCardEffect(playableCard, currentPlayer);
            
            updateLastPlayedCard();
        } else {
            JOptionPane.showMessageDialog(gameFrame, 
                message.toString() + "\n\n" + currentPlayer.getName() + " couldn't play any cards.",
                "Bot's Move",
                JOptionPane.INFORMATION_MESSAGE);
            game.nextPlayer();
        }
    }

    private void handleDrawTwoOrFour(int numberOfCards) {
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
        ArrayList<Card> discardPile = game.getDiscardPile();
        Card lastCard = discardPile.get(discardPile.size() - 1);
        Card.Color requiredColor = lastCard.getColor();
        
        StringBuilder message = new StringBuilder(currentPlayer.getName() + " must draw " + numberOfCards + " cards: ");
        ArrayList<Card> drawnCards = new ArrayList<>();
        
        for (int i = 0; i < numberOfCards; i++) {
            try {
                Card drawnCard = game.getDeck().drawCard();
                currentPlayer.addToHand(drawnCard);
                drawnCards.add(drawnCard);
                
                if (i > 0) {
                    message.append(", ");
                }
                message.append(drawnCard);
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(gameFrame, "The deck is empty!");
                break;
            }
        }
        
        if (numberOfCards == 4) {
            message.append("\n\nAfter drawing, you must play a " + requiredColor + " card.");
        }
        
        JOptionPane.showMessageDialog(gameFrame, message.toString());
        game.nextPlayer(); // Move to the next player after drawing cards
        updateGameState();
    }
}