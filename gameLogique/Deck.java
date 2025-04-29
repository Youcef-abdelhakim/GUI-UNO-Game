package gameLogique;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;
    private int cardInDeck; // Points to the next card to be drawn

    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffleDeck();
    }

    private void initializeDeck() {
        Card.Color[] colors = Card.Color.values();
        
        // Add number cards (1-9) and action cards
        for (Card.Color color : colors) {
            if (color == Card.Color.Wild) continue;
            
            // One zero per color
            cards.add(new Card(color, Card.Value.Zero));
            
            // Two of each number (1-9) per color
            for (int value = 1; value <= 9; value++) {
                cards.add(new Card(color, Card.Value.getValues(value)));
                cards.add(new Card(color, Card.Value.getValues(value)));
            }
            
            // Two of each action card per color
            cards.add(new Card(color, Card.Value.Skip));
            cards.add(new Card(color, Card.Value.Skip));
            cards.add(new Card(color, Card.Value.Reverse));
            cards.add(new Card(color, Card.Value.Reverse));
            cards.add(new Card(color, Card.Value.DrawTwo));
            cards.add(new Card(color, Card.Value.DrawTwo));
        }
        
        // Add wild cards (4 of each)
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(Card.Color.Wild, Card.Value.Wild));
            cards.add(new Card(Card.Color.Wild, Card.Value.WildDrawFour));
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
        cardInDeck = 0; // Reset draw pointer
    }

    public boolean isEmpty() {
        return cardInDeck >= cards.size();
    }

    public Card drawCard() {
        if (isEmpty()) {
            throw new IllegalStateException("Deck is empty!");
        }
        return cards.get(cardInDeck++);
    }

    public int getRemainingCards() {
        return cards.size() - cardInDeck;
    }

    public void reshuffle(ArrayList<Card> discardPile) {
        if (discardPile.size() > 1) {
            Card topCard = discardPile.remove(discardPile.size() - 1);
            cards.clear();
            cards.addAll(discardPile);
            discardPile.clear();
            discardPile.add(topCard);
            shuffleDeck();
        }
    }
}