package gameLogique;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<Card> discardPile;
    private int currentPlayerIndex;
    private int lastPlayerIndex;
    private boolean isClockwise;
    private Card.Color currentColor;

    public Game(ArrayList<Player> players) {
        this.players = new ArrayList<>(players);
        this.deck = new Deck();
        this.discardPile = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.isClockwise = true;
        this.currentColor = null; // Will be set when first card is played
    }

    public void setupGame() {
        dealCards();
        Card firstCard = deck.drawCard();
        discardPile.add(firstCard);
        currentColor = firstCard.getColor(); // Set initial color
        lastPlayerIndex = -1;
    }

    public void setCurrentColor(Card.Color color) {
        this.currentColor = color;
    }
    
    public Card.Color getCurrentColor() {
        return currentColor;
    }

    private void dealCards() {
        for (Player player : players) {
            player.getPlayerHnad().clear();
            for (int i = 0; i < 7; i++) {
                player.addToHand(deck.drawCard());
            }
        }
    }

    public boolean isValidMove(Card card) {
        if (discardPile.isEmpty()) return true;
        
        Card topCard = discardPile.get(discardPile.size() - 1);
        
        // Wild cards are always playable
        if (card.getValue() == Card.Value.Wild || 
            card.getValue() == Card.Value.WildDrawFour) {
            return true;
        }
        
        // After wild card, match selected color
        if (topCard.getValue() == Card.Value.Wild || 
            topCard.getValue() == Card.Value.WildDrawFour) {
            return card.getColor() == currentColor;
        }
        
        // Normal case: match color or value
        return card.getColor() == topCard.getColor() || 
               card.getValue() == topCard.getValue();
    }

    public void applyCardEffect(Card card, Player currentPlayer) {
        lastPlayerIndex = currentPlayerIndex;
        
        // Handle color setting for wild cards
        if (card.getValue() == Card.Value.Wild || card.getValue() == Card.Value.WildDrawFour) {
            currentColor = card.getColor();
        }
        
        // Handle special card effects
        switch (card.getValue()) {
            case Skip:
                nextPlayer(); // Skip next player
                break;
            case Reverse:
                isClockwise = !isClockwise;
                if (players.size() == 2) {
                    nextPlayer(); // With 2 players, Reverse acts like Skip
                }
                break;
            case DrawTwo:
                handleDrawCards(2);
                break;
            case WildDrawFour:
                handleDrawCards(4);
                break;
            default:
                nextPlayer();
        }
    }
    
    private void handleDrawCards(int numCards) {
        int nextPlayerIndex = (currentPlayerIndex + (isClockwise ? 1 : -1) + players.size()) % players.size();
        Player nextPlayer = players.get(nextPlayerIndex);
        
        for (int i = 0; i < numCards; i++) {
            if (!deck.isEmpty()) {
                nextPlayer.addToHand(deck.drawCard());
            }
        }
        
        // Skip the player who had to draw cards
        currentPlayerIndex = (nextPlayerIndex + (isClockwise ? 1 : -1) + players.size()) % players.size();
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + (isClockwise ? 1 : -1) + players.size()) % players.size();
    }

    // Getters
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getLastPlayerIndex() {
        return lastPlayerIndex;
    }

    public boolean isClockwise() {
        return isClockwise;
    }
}