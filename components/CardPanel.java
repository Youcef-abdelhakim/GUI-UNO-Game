package components;
import gameLogique.Card;
import java.util.HashMap;
import java.awt.Dimension;


public class CardPanel {
    private Card card;
    private ImagePanel cardImage ;
    private static HashMap<Card, ImagePanel> cardImages = new HashMap<>();

    public CardPanel(Card card) {
        Card key = card;

        if(!cardImages.containsKey(key)) {
            loadImage(key);
        }

        this.cardImage = cardImages.get(key);
    }

    private void loadImage(Card key) {
        Dimension dimension = new Dimension(100, 150);
        ImagePanel imagePanel = new ImagePanel(card.getImagePath(), dimension);
        cardImages.put(key, imagePanel);


    }
    
    public ImagePanel getCardImage() {
        return cardImage;
    }
}
