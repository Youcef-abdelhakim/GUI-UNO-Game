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

        for (int i = 0; i < colors.length - 1; i++) {
            Card.Color color = colors[i];

            cards.add(new Card(color, Card.Value.getValues(0)));
            cardInDeck++;

            for (int j = 0; j < 12; j++) {
                cards.add(new Card(color, Card.Value.getValues(j)));
                cards.add(new Card(color, Card.Value.getValues(j)));
                cardInDeck += 2;
            }
        }

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