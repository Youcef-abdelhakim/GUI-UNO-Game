package gameLogique;

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Game {

    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<Card> discardPile;
    private int currentPlayerIndex;
    private int lastPlayerIndex; // Track the player who played the last card
    private boolean isClockwise;

    @Override
    public String toString() {
        String text = "";
        for (Player elem : players) {
            text = text + elem.getPlayerName() + " "+ elem.getPlayerType() + " " + elem.getPlayerHnad() + '\n';
        }
        return text;
    }
    
    public Game(ArrayList<Player> players) {
        this.players = (ArrayList<Player>) players.clone();
        deck = new Deck();
        discardPile = new ArrayList<>();
        currentPlayerIndex = 0;
        isClockwise = true;
    }

    public void setupGame() {
        dealCards();
        discardPile.add(deck.drawCard());
        lastPlayerIndex = -1; // Initialize lastPlayerIndex
    }


    private void dealCards() {
        // First, ensure the deck has enough cards for all players
        int totalCardsNeeded = players.size() * 7;
        if (deck.getRemainingCards() < totalCardsNeeded) {
            throw new IllegalStateException("Not enough cards in the deck to deal to all players");
        }
    
        // Deal exactly 7 cards to each player
        for (Player player : players) {
            player.getPlayerHnad().clear(); // Clear any existing cards
            for (int i = 0; i < 7; i++) {
                Card card = deck.drawCard();
                if (card != null) {
                    player.addToHand(card);
                } else {
                    throw new IllegalStateException("Deck ran out of cards while dealing");
                }
            }
        }
    }

    public void startGame() {
        System.out.println("\nStarting the game...");
        System.out.println("First card on the discard pile: " + discardPile.get(discardPile.size() - 1));

        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("\n" + currentPlayer.getPlayerName() + "'s turn!");

            if (currentPlayer.getPlayerType().equals("Human")) {
                humanTurn(currentPlayer);
            } else {
                botTurn(currentPlayer);
            }

            if (currentPlayer.getPlayerHnad().isEmpty()) {
                System.out.println("\n" + currentPlayer.getPlayerName() + " wins the game!");
                break;
            }
            nextPlayer();
        }
    }

    private void humanTurn(Player player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your hand:");
        for (int i = 0; i < player.getPlayerHnad().size(); i++) {
            System.out.println(i + ": " + player.getPlayerHnad().get(i));
        }
        System.out.println("Top card on the discard pile: " + discardPile.get(discardPile.size() - 1));
    
        // Check if player has any valid moves
        boolean hasValidMove = player.getPlayerHnad().stream().anyMatch(this::isValidMove);
        
        if (!hasValidMove) {
            System.out.println("You don't have any playable cards. Drawing until you get one...");
            while (!hasValidMove && !deck.isEmpty()) {
                Card drawnCard = deck.drawCard();
                System.out.println("You drew: " + drawnCard);
                player.addToHand(drawnCard);
                if (isValidMove(drawnCard)) {
                    System.out.print("You can play the drawn card. Play it? (y/n): ");
                    String input = scanner.nextLine().trim().toLowerCase();
                    if (input.equals("y")) {
                        player.getPlayerHnad().remove(drawnCard);
                        discardPile.add(drawnCard);
                        applyCardEffect(drawnCard, player);
                        return;
                    }
                }
                hasValidMove = player.getPlayerHnad().stream().anyMatch(this::isValidMove);
            }
            System.out.println("No playable cards drawn or chose not to play them.");
            return;
        }
    
        while (true) {
            System.out.print("Enter the index of the card to play (or -1 to draw): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
    
            if (choice == -1) {
                handleVoluntaryDraw(player);
                return;
            } else if (choice >= 0 && choice < player.getPlayerHnad().size()) {
                Card chosenCard = player.getPlayerHnad().get(choice);
                if (isValidMove(chosenCard)) {
                    player.getPlayerHnad().remove(choice);
                    discardPile.add(chosenCard);
                    applyCardEffect(chosenCard, player);
                    return;
                } else {
                    System.out.println("Invalid move! You cannot play that card.");
                }
            } else {
                System.out.println("Invalid choice! Try again.");
            }
        }
    }
    
    private void botTurn(Player player) {
        System.out.println(player.getPlayerName() + " is thinking...");
        ArrayList<Card> hand = player.getPlayerHnad();
        
        // First check if bot has any valid moves
        boolean hasValidMove = hand.stream().anyMatch(this::isValidMove);
        
        if (!hasValidMove) {
            System.out.println(player.getPlayerName() + " has no playable cards. Drawing...");
            while (!hasValidMove && !deck.isEmpty()) {
                Card drawnCard = deck.drawCard();
                System.out.println(player.getPlayerName() + " drew a card.");
                player.addToHand(drawnCard);
                if (isValidMove(drawnCard)) {
                    player.getPlayerHnad().remove(drawnCard);
                    discardPile.add(drawnCard);
                    System.out.println(player.getPlayerName() + " played the drawn card: " + drawnCard);
                    applyCardEffect(drawnCard, player);
                    return;
                }
                hasValidMove = hand.stream().anyMatch(this::isValidMove);
            }
            System.out.println(player.getPlayerName() + " couldn't draw a playable card.");
            return;
        }
    
        // Play first valid card found (simple bot strategy)
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (isValidMove(card)) {
                hand.remove(i);
                discardPile.add(card);
                System.out.println(player.getPlayerName() + " played: " + card);
                applyCardEffect(card, player);
                return;
            }
        }
    }

    private void handleVoluntaryDraw(Player player) {
        Card drawnCard = deck.drawCard();
        System.out.println("You drew: " + drawnCard);
        player.addToHand(drawnCard);
        if (isValidMove(drawnCard)) {
            System.out.print("Play the drawn card? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                player.getPlayerHnad().remove(drawnCard);
                discardPile.add(drawnCard);
                applyCardEffect(drawnCard, player);
            }
        }
    }

    public boolean handleForcedDraw(Player player) {
        boolean drawnPlayableCard = false;
        int cardsDrawn = 0;
        
        while (!drawnPlayableCard && !deck.isEmpty()) {
            try {
                Card drawnCard = deck.drawCard();
                player.addToHand(drawnCard);
                cardsDrawn++;
                
                if (isValidMove(drawnCard)) {
                    drawnPlayableCard = true;
                    // Don't automatically play it - let player decide
                }
            } catch (IllegalStateException e) {
                // Deck is empty
                break;
            }
        }
        
        // Return whether a playable card was drawn
        return drawnPlayableCard;
    }

    public boolean isValidMove(Card card) {
        Card topCard = discardPile.get(discardPile.size() - 1);
        if (topCard.getValue() == Card.Value.Wild || topCard.getValue() == Card.Value.WildDrawFour) {
            return true;
        }
        return card.getColor() == topCard.getColor() || card.getValue() == topCard.getValue() || card.getColor() == Card.Color.Wild;
    }

    public void applyCardEffect(Card card, Player currentPlayer) {
        lastPlayerIndex = currentPlayerIndex;
        
        switch (card.getValue()) {
            case Skip -> {
                JOptionPane.showMessageDialog(null, "Next player is skipped!");
                nextPlayer();
            }
            case Reverse -> {
                JOptionPane.showMessageDialog(null, "Turn order reversed!");
                isClockwise = !isClockwise;
                if (players.size() == 2) {
                    nextPlayer(); // Acts like skip in 2-player game
                }
            }
            case DrawTwo -> {
                JOptionPane.showMessageDialog(null, "Next player draws 2 cards and is skipped!");
                nextPlayer();
                Player nextPlayer = players.get(currentPlayerIndex);
                for (int i = 0; i < 2; i++) {
                    if (!deck.isEmpty()) {
                        nextPlayer.addToHand(deck.drawCard());
                    }
                }
                nextPlayer(); // Skip their turn
            }
            case Wild -> {
                // For Wild cards, the color is already set before calling applyCardEffect
                // Just need to handle the turn transition
                nextPlayer();
            }
            case WildDrawFour -> {
                // For Wild Draw Four, the color is already set
                JOptionPane.showMessageDialog(null, "Next player draws 4 cards and is skipped!");
                nextPlayer();
                Player drawFourPlayer = players.get(currentPlayerIndex);
                for (int i = 0; i < 4; i++) {
                    if (!deck.isEmpty()) {
                        drawFourPlayer.addToHand(deck.drawCard());
                    }
                }
                nextPlayer(); // Skip their turn
            }
            default -> {
                // Normal card - just move to next player
                nextPlayer();
            }
        }
    }

    private Card.Color chooseMostCommonColor(Player player) {
        Card.Color[] colors = Card.Color.values();
        int[] colorCounts = new int[4]; // Red, Blue, Green, Yellow
        
        for (Card c : player.getPlayerHnad()) {
            if (c.getColor() != Card.Color.Wild) {
                colorCounts[c.getColor().ordinal()]++;
            }
        }
        
        int maxIndex = 0;
        for (int i = 1; i < colorCounts.length; i++) {
            if (colorCounts[i] > colorCounts[maxIndex]) {
                maxIndex = i;
            }
        }
        
        return colors[maxIndex];
    }

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

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private void prevPlayer() {
        currentPlayerIndex = (currentPlayerIndex - (isClockwise ? 1 : -1) + players.size()) % players.size();
    }

    public static void main(String[] args) {
        // Create a list of players for testing
        ArrayList<Player> testPlayers = new ArrayList<>();
        testPlayers.add(new Player("Player 1", "Human"));
        testPlayers.add(new Player("Player 2", "Bot"));
        
        Game game = new Game(testPlayers);
        game.setupGame();
        game.startGame();
    }
}
