package gameLogique;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    ArrayList<Player> players;
    Deck deck;
    ArrayList<Card> discardPile;
    int currentPlayerIndex;
    boolean isClockwise;

    public Game() {
        players = new ArrayList<>();
        deck = new Deck();
        discardPile = new ArrayList<>();
        currentPlayerIndex = 0;
        isClockwise = true;
    }

    public void setupGame() {
        Scanner scanner = new Scanner(System.in);
        int numPlayers;
        do {
            System.out.print("Enter the number of players (2-4): ");
            numPlayers = scanner.nextInt();
        } while (numPlayers < 2 || numPlayers > 4);

        int numHumans;
        do {
            System.out.print("Enter the number of human players (1-" + numPlayers + "): ");
            numHumans = scanner.nextInt();
        } while (numHumans < 1 || numHumans > numPlayers);

        scanner.nextLine();

        for (int i = 1; i <= numHumans; i++) {
            System.out.print("Enter the name of human player " + i + ": ");
            String playerName = scanner.nextLine();
            players.add(new Player(playerName, "Human"));
        }

        int numBots = numPlayers - numHumans;
        for (int i = 1; i <= numBots; i++) {
            players.add(new Player("Bot" + i, "Bot"));
        }

        dealCards();
        discardPile.add(deck.drawCard());

        System.out.println("\nGame setup complete! Here are the players:");
        for (Player player : players) {
            System.out.println(player.getPlayerName() + " (" + player.getPlayerType() + ")");
        }
    }

    private void dealCards() {
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.addToHand(deck.drawCard());
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

        while (true) {
            System.out.print("Enter the index of the card to play (or -1 to draw): ");
            int choice = scanner.nextInt();

            if (choice == -1) {
                Card drawnCard = deck.drawCard();
                System.out.println("You drew: " + drawnCard);
                player.addToHand(drawnCard);
                return;
            } else if (choice >= 0 && choice < player.getPlayerHnad().size()) {
                Card chosenCard = player.getPlayerHnad().get(choice);
                if (isValidMove(chosenCard)) {
                    player.getPlayerHnad().remove(choice);
                    discardPile.add(chosenCard);
                    applyCardEffect(chosenCard);
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
        for (Card card : player.getPlayerHnad()) {
            if (isValidMove(card)) {
                player.getPlayerHnad().remove(card);
                discardPile.add(card);
                System.out.println(player.getPlayerName() + " played: " + card);
                applyCardEffect(card);
                return;
            }
        }

        Card drawnCard = deck.drawCard();
        player.addToHand(drawnCard);
        System.out.println(player.getPlayerName() + " drew a card.");
    }

    private boolean isValidMove(Card card) {
        Card topCard = discardPile.get(discardPile.size() - 1);
        if (topCard.getValue() == Card.Value.Wild || topCard.getValue() == Card.Value.WildDrawFour) {
            return true;
        }
        return card.getColor() == topCard.getColor() || card.getValue() == topCard.getValue() || card.getColor() == Card.Color.Wild;
    }

    private void applyCardEffect(Card card) {
        switch (card.getValue()) {
            case Skip:
                System.out.println("Next player is skipped!");
                nextPlayer();
                break;
            case Reverse:
                System.out.println("Turn order reversed!");
                isClockwise = !isClockwise;
                if (players.size() > 2) {
                    prevPlayer();
                }
                break;
            case DrawTwo:
                System.out.println("Next player draws 2 cards!");
                nextPlayer();
                Player nextPlayer = players.get(currentPlayerIndex);
                nextPlayer.addToHand(deck.drawCard());
                nextPlayer.addToHand(deck.drawCard());
                break;
            case Wild:
                System.out.println("Wild card played! You can play any card next.");
                break;
            case WildDrawFour:
                System.out.println("Wild Draw Four card played! Next player draws 4 cards or stacks.");
                int drawCount = 4;
                while (true) {
                    nextPlayer();
                    Player next = players.get(currentPlayerIndex);
                    boolean stacked = false;
                    for (Card c : next.getPlayerHnad()) {
                        if (c.getValue() == Card.Value.WildDrawFour) {
                            next.getPlayerHnad().remove(c);
                            discardPile.add(c);
                            System.out.println(next.getPlayerName() + " stacked WildDrawFour!");
                            drawCount += 4;
                            stacked = true;
                            break;
                        }
                    }
                    if (!stacked) {
                        System.out.println(next.getPlayerName() + " draws " + drawCount + " cards!");
                        for (int i = 0; i < drawCount; i++) {
                            next.addToHand(deck.drawCard());
                        }
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + (isClockwise ? 1 : -1) + players.size()) % players.size();
    }

    private void prevPlayer() {
        currentPlayerIndex = (currentPlayerIndex - (isClockwise ? 1 : -1) + players.size()) % players.size();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setupGame();
        game.startGame();
    }
}
