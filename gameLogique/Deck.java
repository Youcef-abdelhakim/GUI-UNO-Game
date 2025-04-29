package gameLogique;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    
    private ArrayList<Card> cards;
    private int cardInDeck;

    public Deck() {
        cards = new ArrayList<>();
        Card.Color[] colors = Card.Color.values();
        cardInDeck = 0;

        // Add one zero card and two of each other number card for each color
        for (int i = 0; i < colors.length - 1; i++) {
            Card.Color color = colors[i];
            
            // Add one zero card
            cards.add(new Card(color, Card.Value.Zero));
            cardInDeck++;
            
            // Add two of each number card (1-9)
            for (int j = 1; j <= 9; j++) {
                cards.add(new Card(color, Card.Value.getValues(j)));
                cards.add(new Card(color, Card.Value.getValues(j)));
                cardInDeck += 2;
            }
            
            // Add two of each action card (Skip, Reverse, DrawTwo)
            for (int j = 10; j <= 12; j++) {
                cards.add(new Card(color, Card.Value.getValues(j)));
                cards.add(new Card(color, Card.Value.getValues(j)));
                cardInDeck += 2;
            }
        }

        // Add Wild cards (4 of each type)
        Card.Value[] wValues = {Card.Value.Wild, Card.Value.WildDrawFour};
        for (Card.Value value : wValues) {
            for (int i = 0; i < 4; i++) {
                cards.add(new Card(Card.Color.Wild, value));
                cardInDeck++;
            }
        }

        shuffleDeck();
        cardInDeck = cards.size() - 1;
    }

    // Method to shuffle the deck
    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cardInDeck < 0;
    }

    public Card drawCard() {
        if (isEmpty()) {
            throw new IllegalStateException("Deck is empty!");
        }
        return cards.get(cardInDeck--); 
    }

}