package ui.views;

import components.Button;
import components.ColorOptionPanel;
import components.GameFrame;
import components.Imagecon;
import components.Label;
import components.Panel;
import components.ScrollPane;
import components.TimeBot;
import gameLogique.Card;
import gameLogique.Game;
import gameLogique.Player;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
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

    private ArrayList<Imagecon> cardIcons = new ArrayList<>();
    private HashMap<String, Imagecon> cardImageMap = new HashMap<>();

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
        gbcCenter.anchor = GridBagConstraints.NORTHWEST;
        gbcCenter.insets = new Insets(45, 0, 0, 0);
        lastCardLabel = new Label("LAST CARD PLAYED", Label.CENTER);
        lastCardLabel.adjustFont(new Font("Arial", Font.BOLD, 30));
        lastCardLabel.setTextColor(Color.WHITE);
        centerTopPanel.add(lastCardLabel, gbcCenter);

        // Configure constraints for lastCardPlayed
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 1;
        gbcCenter.anchor = GridBagConstraints.CENTER;
        gbcCenter.insets = new Insets(0, 0, 0, 0);
        lastCardPlayed = new Label();
        centerTopPanel.add(lastCardPlayed, gbcCenter);

        topPanel.add(centerTopPanel, BorderLayout.CENTER);
        gameFrame.addWidget(topPanel, BorderLayout.NORTH);

        // Center panel with GridBagLayout to hold the handScrollPane
        Panel centerPanel = new Panel(new GridBagLayout());
        centerPanel.setBackground(new Color(0x042e54));

        // Player Hand Panel with FlowLayout for horizontal arrangement
        playerHandPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        playerHandPanel.setBackground(new Color(0xE0E0E0));
        ScrollPane handScrollPane = new ScrollPane(playerHandPanel);
        handScrollPane.setHorizontalScrollBarPolicy(ScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        handScrollPane.setVerticalScrollBarPolicy(ScrollPane.VERTICAL_SCROLLBAR_NEVER);
        handScrollPane.setPreferredSize(new Dimension(800, 165));

        // Add handScrollPane to centerPanel
        GridBagConstraints gbcHand = new GridBagConstraints();
        gbcHand.gridx = 0;
        gbcHand.gridy = 0;
        gbcHand.anchor = GridBagConstraints.CENTER;
        gbcHand.insets = new Insets(20, 0, 0, 0);
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

        // Additional labels
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
            TimeBot.createBotTimer(1000, () -> {
                handleBotTurn();
                if (!currentPlayer.getPlayerHnad().isEmpty()) {
                    updateGameState();
                }
            }).start();
        }

        if (currentPlayer.getPlayerHnad().isEmpty()) {
            ColorOptionPanel.showMessageDialog(gameFrame,
                currentPlayer.getName() + " wins the game!",
                "Game Over",
                ColorOptionPanel.INFORMATION_MESSAGE);
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
                if (game.getDeck().isEmpty()) {
                    game.getDeck().reshuffle(game.getDiscardPile());
                }
                
                boolean hasValidMove = false;
                for (Card card : currentPlayer.getPlayerHnad()) {
                    if (game.isValidMove(card)) {
                        hasValidMove = true;
                        break;
                    }
                }
                
                if (hasValidMove) {
                    ColorOptionPanel.showMessageDialog(gameFrame, 
                        "You already have playable cards! You must play one before drawing.",
                        "Cannot Draw", 
                        ColorOptionPanel.WARNING_MESSAGE);
                    return;
                }
                
                Card drawnCard = game.getDeck().drawCard();
                currentPlayer.addToHand(drawnCard);
                
                StringBuilder drawMessage = new StringBuilder(currentPlayer.getName() + " drew: " + drawnCard);
                
                if (game.isValidMove(drawnCard)) {
                    int choice = ColorOptionPanel.showConfirmDialog(gameFrame,
                        "You drew a playable card: " + drawnCard + "\nDo you want to play it?",
                        "Play Drawn Card?",
                        ColorOptionPanel.YES_NO_OPTION);
                    
                    if (choice == ColorOptionPanel.YES_OPTION) {
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
                
                ColorOptionPanel.showMessageDialog(gameFrame,
                    drawMessage.toString(),
                    "Card Drawn",
                    ColorOptionPanel.INFORMATION_MESSAGE);
                
                updateGameState();
                
            } catch (IllegalStateException e) {
                ColorOptionPanel.showMessageDialog(gameFrame, 
                    "The deck is empty!", 
                    "Error", 
                    ColorOptionPanel.ERROR_MESSAGE);
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
            ColorOptionPanel.showMessageDialog(gameFrame, "Invalid move! You cannot play that card.");
        }
    }

    private void handleWildCardSelection(Card wildCard, Player currentPlayer) {
        currentPlayer.getPlayerHnad().remove(wildCard);
        
        String[] colorOptions = {"Red", "Blue", "Green", "Yellow"};
        Card.Color[] enumColors = {
            Card.Color.Red, Card.Color.Blue, 
            Card.Color.Green, Card.Color.Yellow
        };

        String selectedColorName = (String) JOptionPane.showInputDialog(
            gameFrame,
            "Choose a color for the Wild card:",
            "Color Selection",
            JOptionPane.QUESTION_MESSAGE,
            null,
            colorOptions,
            colorOptions[0]
        );

        Card.Color selectedColor = Card.Color.Red;
        if (selectedColorName != null) {
            for (int i = 0; i < colorOptions.length; i++) {
                if (colorOptions[i].equals(selectedColorName)) {
                    selectedColor = enumColors[i];
                    break;
                }
            }
        }

        Card coloredCard = new Card(selectedColor, wildCard.getValue());
        game.getDiscardPile().add(coloredCard);
        game.setCurrentColor(selectedColor);
        game.applyCardEffect(coloredCard, currentPlayer);

        
        String message = currentPlayer.getName() + " played " + wildCard.getValue();
        if (wildCard.getValue() == Card.Value.WildDrawFour) {
            Player nextPlayer = game.getPlayers().get(game.getCurrentPlayerIndex());
            message += " and chose " + selectedColor + "!\n" + nextPlayer.getName() + " must draw 4 cards!";
        } else {
            message += " and chose " + selectedColor + "!";
        }
        
        ColorOptionPanel.showMessageDialog(gameFrame, message, "Card Played", 
            ColorOptionPanel.INFORMATION_MESSAGE);
        
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
            
            Imagecon icon = cardImageMap.get(filename);
            
            if (icon != null) {
                Image scaled = icon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
                card.setIcon(new Imagecon(scaled));
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
                Imagecon icon = new Imagecon(imageUrl);
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
        
        Imagecon icon = cardImageMap.get(filename);
        
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(150, 230, Image.SCALE_SMOOTH);
            label.setIcon(new Imagecon(scaled));
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
                if (game.getDeck().isEmpty()) {
                    game.getDeck().reshuffle(game.getDiscardPile());
                }
                
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
                ColorOptionPanel.showMessageDialog(gameFrame, "The deck is empty!");
                break;
            }
        }
        
        if (drawnPlayableCard && playableCard != null) {
            currentPlayer.getPlayerHnad().remove(playableCard);
            game.getDiscardPile().add(playableCard);
            game.applyCardEffect(playableCard, currentPlayer);
            
            updateLastPlayedCard();
        } else {
            ColorOptionPanel.showMessageDialog(gameFrame, 
                message.toString() + "\n\n" + currentPlayer.getName() + " couldn't play any cards.",
                "Bot's Move",
                ColorOptionPanel.INFORMATION_MESSAGE);
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
                if (game.getDeck().isEmpty()) {
                    game.getDeck().reshuffle(game.getDiscardPile());
                }
                
                Card drawnCard = game.getDeck().drawCard();
                currentPlayer.addToHand(drawnCard);
                drawnCards.add(drawnCard);
                
                if (i > 0) {
                    message.append(", ");
                }
                message.append(drawnCard);
            } catch (IllegalStateException e) {
                ColorOptionPanel.showMessageDialog(gameFrame, "The deck is empty!");
                break;
            }
        }
        
        if (numberOfCards == 4) {
            message.append("\n\nAfter drawing, you must play a " + requiredColor + " card.");
        }
        
        ColorOptionPanel.showMessageDialog(gameFrame, message.toString(), "Cards Drawn", 
            ColorOptionPanel.INFORMATION_MESSAGE);
        game.nextPlayer();
        updateGameState();
    }
}
