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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import ui.widgets.GamePage.PlayerListPanel;

public class GamePage {
    private GameFrame gameFrame;
    private Label lastCardPlayed;
    private Label lastCardLabel;
    private Label currentPlayerLabel;
    private Label drawCardLabel;
    private Label emptyDeckLabel;
    private Panel hiddenHandPanel;
    private Button showCardsBtn;
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

        // Initialize message labels
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

        hiddenHandPanel = new Panel(new BorderLayout());
        hiddenHandPanel.setBackground(new Color(0x042e54));
        hiddenHandPanel.setBounds(50, 400, 800, 185);
        
        showCardsBtn = new Button("Click to Show Your Cards");
        showCardsBtn.setPreferredSize(new Dimension(200, 50));
        showCardsBtn.setBGColor(new Color(0x1a73e8));
        showCardsBtn.setForeground(Color.WHITE);
        showCardsBtn.adjustFont(new Font("Arial", Font.BOLD, 16));
        showCardsBtn.addActionListener(e -> {
            hiddenHandPanel.setVisible(false);
            playerHandPanel.setVisible(true);
        });
        
        hiddenHandPanel.add(showCardsBtn, BorderLayout.CENTER);
        gameFrame.addWidget(hiddenHandPanel);
        
        // Initially hide the hidden panel
        hiddenHandPanel.setVisible(false);
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
        
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
        
        // Show/hide card panels based on player type
        if (currentPlayer.getPlayerType().equals("Player")) {
            playerHandPanel.setVisible(true);
            hiddenHandPanel.setVisible(false);
            showPlayerHand();
        } else {
            playerHandPanel.setVisible(false);
        }
        
        // Check if the last played card was a Draw Two or Wild Draw Four card
        ArrayList<Card> discardPile = game.getDiscardPile();
        if (!discardPile.isEmpty()) {
            Card lastCard = discardPile.get(discardPile.size() - 1);
            if (lastCard.getValue() == Card.Value.DrawTwo) {
                // If the current player is the one who needs to draw
                if (game.getCurrentPlayerIndex() == (game.getLastPlayerIndex() + 1) % players.size()) {
                    handleDrawTwoOrFour(2);
                }
            } else if (lastCard.getValue() == Card.Value.WildDrawFour) {
                // If the current player is the one who needs to draw
                if (game.getCurrentPlayerIndex() == (game.getLastPlayerIndex() + 1) % players.size()) {
                    handleDrawTwoOrFour(4);
                }
            }
        }
        // Check if it's a bot's turn and handle it automatically
        if (currentPlayer.getPlayerType().equals("Bot")) {
            // Use a timer to delay the bot's action for better user experience
            javax.swing.Timer timer = new javax.swing.Timer(1000, e -> handleBotTurn());
            timer.setRepeats(false);
            timer.start();
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
        if (currentPlayer.getPlayerType().equals("Player") ) {
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
                
                // Player has no playable cards - force draw
                Card drawnCard = game.getDeck().drawCard();
                currentPlayer.addToHand(drawnCard);
                
                // Check if drawn card is playable
                if (game.isValidMove(drawnCard)) {
                    // Auto-play the drawn card
                    currentPlayer.getPlayerHnad().remove(drawnCard);
                    game.getDiscardPile().add(drawnCard);
                    game.applyCardEffect(drawnCard, currentPlayer);
                    JOptionPane.showMessageDialog(gameFrame,
                        "You drew: " + drawnCard + "\nThis card is playable and was automatically played!",
                        "Card Played",
                        JOptionPane.INFORMATION_MESSAGE);
                        game.nextPlayer();
                        updateGameState();
                } else {
                    JOptionPane.showMessageDialog(gameFrame,
                        "You drew: " + drawnCard + "\nThis card cannot be played.",
                        "Card Drawn",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
                // Move to next player
                game.nextPlayer();
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
    
    // Only allow card playing if it's the current player's turn and they're human
    if (!currentPlayer.getPlayerType().equals("Player")) {
        JOptionPane.showMessageDialog(gameFrame, 
            "It's not your turn!", 
            "Invalid Move", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Find the card in player's hand
    Card playedCard = null;
    for (Card card : currentPlayer.getPlayerHnad()) {
        if (card.toString().equals(cardName)) {
            playedCard = card;
            break;
        }
    }

    if (playedCard == null) {
        JOptionPane.showMessageDialog(gameFrame, 
            "Card not found in your hand!", 
            "Invalid Move", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validate the move
    if (!game.isValidMove(playedCard)) {
        JOptionPane.showMessageDialog(gameFrame, 
            "You cannot play " + playedCard + " on " + game.getDiscardPile().get(game.getDiscardPile().size()-1),
            "Invalid Move", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Handle Wild cards
    if (playedCard.getValue() == Card.Value.Wild || 
        playedCard.getValue() == Card.Value.WildDrawFour) {
        
        // Remove card immediately to prevent double plays
        currentPlayer.getPlayerHnad().remove(playedCard);
        handleWildCardSelection(playedCard, currentPlayer);
        return;
    }

    // Normal card play
    currentPlayer.getPlayerHnad().remove(playedCard);
    game.getDiscardPile().add(playedCard);
    game.applyCardEffect(playedCard, currentPlayer);
    updateGameState();
}

    private void handleWildCardSelection(Card wildCard, Player currentPlayer) {
        // Remove the card from hand first
        currentPlayer.getPlayerHnad().remove(wildCard);
        
        // Create a panel for color selection
        Panel colorPanel = new Panel(new GridLayout(2, 2, 10, 10));
        Button[] colorButtons = new Button[4];
        Card.Color[] colors = {Card.Color.Red, Card.Color.Blue, Card.Color.Green, Card.Color.Yellow};
        
        // Create color buttons
        for (int i = 0; i < 4; i++) {
            colorButtons[i] = new Button(colors[i].toString());
            colorButtons[i].setBGColor(getColorForCardColor(colors[i]));
            colorButtons[i].setForeground(Color.WHITE);
            colorButtons[i].adjustFont(new Font("Arial", Font.BOLD, 16));
            final int selectedIndex = i;
            colorButtons[i].addActionListener(e -> {
                Card.Color selectedColor = colors[selectedIndex];
                // Create a dummy card with the selected color
                Card coloredCard = new Card(selectedColor, wildCard.getValue());
                game.getDiscardPile().add(coloredCard);
                
                // Apply card effect
                game.applyCardEffect(coloredCard, currentPlayer);
                updateGameState();
                
                // Close the dialog
                Window window = SwingUtilities.getWindowAncestor(colorPanel);
                if (window != null) {
                    window.dispose();
                }
            });
            colorPanel.add(colorButtons[i]);
        }
        
        // Create and show the dialog
        JDialog colorDialog = new JDialog();
        colorDialog.setTitle("Choose a color");
        colorDialog.setModal(true);
        colorDialog.add(colorPanel);
        colorDialog.pack();
        colorDialog.setLocationRelativeTo(gameFrame);
        colorDialog.setVisible(true);
    }
    
    private Color getColorForCardColor(Card.Color cardColor) {
        switch (cardColor) {
            case Red: return Color.RED;
            case Blue: return Color.BLUE;
            case Green: return Color.GREEN;
            case Yellow: return Color.YELLOW;
            default: return Color.WHITE;
        }
    }

    private void showPlayerHand() {
        playerHandPanel.removeAll();
        hiddenHandPanel.removeAll(); // Clear previous components
        
        int cardWidth = 100;
        int cardHeight = 150;
        Player currentPlayer = players.get(game.getCurrentPlayerIndex());
    
        if (currentPlayer.getPlayerType().equals("Player")) {
            // Show actual cards for current human player
            hiddenHandPanel.setVisible(false);
            playerHandPanel.setVisible(true);
            
            for (String cardName : playerHand) {
                Button card = new Button();
                // Convert card name to image filename
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
        } else {
            // For other players (bots or other humans not currently playing)
            playerHandPanel.setVisible(false);
            hiddenHandPanel.setVisible(true);
            
            // Create a nice "hidden cards" display
            Panel hiddenCardsPanel = new Panel(new GridLayout(1, playerHand.size(), 5, 0));
            hiddenCardsPanel.setBackground(new Color(0x042e54));
            
            for (int i = 0; i < playerHand.size(); i++) {
                Panel cardBack = new Panel();
                cardBack.setBackground(new Color(0x1a73e8));
                cardBack.setPreferredSize(new Dimension(50, 150));
                cardBack.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                hiddenCardsPanel.add(cardBack);
            }
            
            Label hiddenLabel = new Label(currentPlayer.getName() + "'s Cards (" + playerHand.size() + ")", Label.CENTER);
            hiddenLabel.adjustFont(new Font("Arial", Font.BOLD, 16));
            hiddenLabel.setTextColor(Color.WHITE);
            
            hiddenHandPanel.removeAll();
            hiddenHandPanel.setLayout(new BorderLayout());
            hiddenHandPanel.add(hiddenLabel, BorderLayout.NORTH);
            hiddenHandPanel.add(hiddenCardsPanel, BorderLayout.CENTER);
            
            // Only show "Click to Show" button if it's another human player's hidden cards
            if (currentPlayer.getPlayerType().equals("Player") && 
                !players.get(game.getCurrentPlayerIndex()).equals(currentPlayer)) {
                
                Button showBtn = new Button("Click to Show Cards");
                showBtn.setPreferredSize(new Dimension(200, 40));
                showBtn.setBGColor(new Color(0x1a73e8));
                showBtn.setForeground(Color.WHITE);
                showBtn.addActionListener(e -> {
                    // Temporary show cards (for debugging/cheating)
                    playerHandPanel.setVisible(true);
                    hiddenHandPanel.setVisible(false);
                    
                    // Auto-hide after 3 seconds
                    Timer timer = new Timer(3000, ev -> {
                        playerHandPanel.setVisible(false);
                        hiddenHandPanel.setVisible(true);
                    });
                    timer.setRepeats(false);
                    timer.start();
                });
                
                hiddenHandPanel.add(showBtn, BorderLayout.SOUTH);
            }
        }
    
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
        hiddenHandPanel.revalidate();
        hiddenHandPanel.repaint();
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
            // Hide the player's hand panel during bot's turn
            playerHandPanel.setVisible(false);
            
            // First check for Draw Two or Wild Draw Four cards
            ArrayList<Card> discardPile = game.getDiscardPile();
            if (!discardPile.isEmpty()) {
                Card lastCard = discardPile.get(discardPile.size() - 1);
                if (lastCard.getValue() == Card.Value.DrawTwo) {
                    if (game.getCurrentPlayerIndex() == (game.getLastPlayerIndex() + 1) % players.size()) {
                        handleDrawTwoOrFour(2);
                        return;
                    }
                } else if (lastCard.getValue() == Card.Value.WildDrawFour) {
                    if (game.getCurrentPlayerIndex() == (game.getLastPlayerIndex() + 1) % players.size()) {
                        handleDrawTwoOrFour(4);
                        return;
                    }
                }
            }
            
            // Find a playable card
            Card cardToPlay = null;
            for (Card card : currentPlayer.getPlayerHnad()) {
                if (game.isValidMove(card)) {
                    cardToPlay = card;
                    break;
                }
            }
            
            if (cardToPlay != null) {
                // Play the card immediately
                currentPlayer.getPlayerHnad().remove(cardToPlay);
                game.getDiscardPile().add(cardToPlay);
                game.applyCardEffect(cardToPlay, currentPlayer);
                
                // Show what the bot played
                JOptionPane.showMessageDialog(gameFrame, 
                    currentPlayer.getName() + " played: " + cardToPlay,
                    "Bot's Move",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Update the game state to show the played card
                updateLastPlayedCard();
                
                // If the bot didn't win, continue to next player after a delay
                if (!currentPlayer.getPlayerHnad().isEmpty()) {
                    Timer timer = new Timer(1500, e -> {
                        game.nextPlayer();
                        playerHandPanel.setVisible(true); // Show hand panel again
                        updateGameState();
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    // Bot won
                    playerHandPanel.setVisible(true); // Show hand panel again
                    updateGameState();
                }
            } else {
                // Bot has no playable cards - draw cards
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
            // Bot plays the drawn card
            currentPlayer.getPlayerHnad().remove(playableCard);
            game.getDiscardPile().add(playableCard);
            game.applyCardEffect(playableCard, currentPlayer);
            
            JOptionPane.showMessageDialog(gameFrame, 
                message.toString() + "\n\n" + currentPlayer.getName() + " played the drawn card!",
                "Bot's Move",
                JOptionPane.INFORMATION_MESSAGE);
            
            updateLastPlayedCard();
            
            // If the bot didn't win, continue to next player after a delay
            if (!currentPlayer.getPlayerHnad().isEmpty()) {
                Timer timer = new Timer(1500, e -> {
                    game.nextPlayer();
                    updateGameState();
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                // Bot won
                updateGameState();
            }
        } else {
            JOptionPane.showMessageDialog(gameFrame, 
                message.toString() + "\n\n" + currentPlayer.getName() + " couldn't play any cards.",
                "Bot's Move",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Move to next player after a delay
            Timer timer = new Timer(1500, e -> {
                game.nextPlayer();
                updateGameState();
            });
            timer.setRepeats(false);
            timer.start();
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
        game.nextPlayer();
        updateGameState();
    }
}