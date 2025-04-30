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
        
        players = game.getPlayers();
        playersName = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playersName[i] = players.get(i).getName();
        }

        playerListPanel = new PlayerListPanel(playersName);
        playerListPanel.setBounds(30, 30, 200, 200);
        gameFrame.addWidget(playerListPanel);

        lastCardLabel = new Label("LAST CARD PLAYED", Label.CENTER);
        lastCardLabel.adjustFont(new Font("Arial", Font.BOLD, 30));
        lastCardLabel.setTextColor(Color.WHITE);
        lastCardLabel.setBounds(0, 10, 900, 50);
        gameFrame.addWidget(lastCardLabel);

        lastCardPlayed = new Label();
        lastCardPlayed.setBounds(375, 110, 150, 230);
        gameFrame.addWidget(lastCardPlayed);

        playerHandPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        JScrollPane handScrollPane = new JScrollPane(playerHandPanel);
        handScrollPane.setBounds(50, 400, 800, 185);
        gameFrame.addWidget(handScrollPane);

        currentPlayerLabel = new Label("", Label.LEFT);
        currentPlayerLabel.adjustFont(new Font("Arial", Font.BOLD, 20));
        currentPlayerLabel.setTextColor(Color.WHITE);
        currentPlayerLabel.setBounds(50, 620, 300, 30);
        gameFrame.addWidget(currentPlayerLabel);

        drawCardBtn = new Button("Draw Card");
        drawCardBtn.setBounds(700, 610, 150, 50);
        drawCardBtn.setBGColor(new Color(0x1a73e8));
        drawCardBtn.setForeground(Color.WHITE);
        drawCardBtn.adjustFont(new Font("Arial", Font.BOLD, 20));
        drawCardBtn.addActionListener(e -> handleDrawCard());
        gameFrame.addWidget(drawCardBtn);

        drawCardLabel = new Label("", Label.CENTER);
        drawCardLabel.adjustFont(new Font("Arial", Font.BOLD, 16));
        drawCardLabel.setTextColor(Color.WHITE);
        drawCardLabel.setBounds(50, 650, 800, 30);
        gameFrame.addWidget(drawCardLabel);

        emptyDeckLabel = new Label("", Label.CENTER);
        emptyDeckLabel.adjustFont(new Font("Arial", Font.BOLD, 16));
        emptyDeckLabel.setTextColor(Color.WHITE);
        emptyDeckLabel.setBounds(50, 650, 800, 30);
        gameFrame.addWidget(emptyDeckLabel);
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
                // Check if player already has playable cards
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
                
                // Draw cards until a playable one is found or deck is empty
                StringBuilder drawMessage = new StringBuilder(currentPlayer.getName() + " drew: ");
                boolean playableCardFound = false;
                Card playableCard = null;
                int cardsDrawn = 0;
                
                while (!playableCardFound) {
                    try {
                        Card drawnCard = game.getDeck().drawCard();
                        currentPlayer.addToHand(drawnCard);
                        cardsDrawn++;
                        
                        if (cardsDrawn > 1) {
                            drawMessage.append(", ");
                        }
                        drawMessage.append(drawnCard);
                        
                        if (game.isValidMove(drawnCard)) {
                            playableCardFound = true;
                            playableCard = drawnCard;
                        }
                    } catch (IllegalStateException e) {
                        drawMessage.append("\nThe deck is empty!");
                        break;
                    }
                }
                
                if (playableCardFound && playableCard != null) {
                    if (playableCard.getValue() == Card.Value.Wild || playableCard.getValue() == Card.Value.WildDrawFour) {
                        // Handle Wild card color selection
                        handleWildCardSelection(playableCard, currentPlayer);
                    } else {
                        // Handle normal playable card
                        currentPlayer.getPlayerHnad().remove(playableCard);
                        game.getDiscardPile().add(playableCard);
                        game.applyCardEffect(playableCard, currentPlayer);
                        drawMessage.append("\n\n" + currentPlayer.getName() + " automatically played: " + playableCard);
                    }
                } else {
                    // No playable card found (deck empty or only non-playable cards drawn)
                    drawMessage.append("\n\n" + currentPlayer.getName() + " couldn't draw a playable card.");
                    game.nextPlayer(); // Move to next player
                }
                
                // Show the result of drawing
                JOptionPane.showMessageDialog(gameFrame,
                    drawMessage.toString(),
                    playableCardFound ? "Card Played" : "Cards Drawn",
                    playableCardFound ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                
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
        
        Card.Color selectedColor = Card.Color.Red;
        switch (choice) {
            case 0: selectedColor = Card.Color.Red; break;
            case 1: selectedColor = Card.Color.Blue; break;
            case 2: selectedColor = Card.Color.Green; break;
            case 3: selectedColor = Card.Color.Yellow; break;
        }
        
        Card coloredCard = new Card(selectedColor, wildCard.getValue());
        game.getDiscardPile().add(coloredCard);
        game.applyCardEffect(coloredCard, currentPlayer);
        
        // Show appropriate message based on card type
        String nextPlayerName = players.get(game.getCurrentPlayerIndex()).getName();
        String message;
        if (wildCard.getValue() == Card.Value.WildDrawFour) {
            message = currentPlayer.getName() + " played Wild Draw Four and chose " + selectedColor + "!\n" +
                     nextPlayerName + " must draw 4 cards and play a " + selectedColor + " card or a Wild card.";
        } else {
            message = currentPlayer.getName() + " chose " + selectedColor + "!\n" +
                     nextPlayerName + " must play a " + selectedColor + " card or a Wild card.";
        }
        JOptionPane.showMessageDialog(gameFrame, message, "Color Changed", JOptionPane.INFORMATION_MESSAGE);
        
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
            game.nextPlayer(); // Advance turn if no play
        }
    }

    private void handleDrawTwoOrFour(int numberOfCards) {
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
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
        
        JOptionPane.showMessageDialog(gameFrame, message.toString());
        // Turn advancement handled by applyCardEffect
        updateGameState();
    }
}