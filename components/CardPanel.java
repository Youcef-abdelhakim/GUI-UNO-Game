package components;
import gameLogique.Card.Color;
import gameLogique.Card.Value;
import java.awt.Dimension;
import java.util.HashMap;


public class CardPanel {
    private Value value;
    private Color color;
    private ImagePanel cardImage ;
    private static HashMap<String, ImagePanel> cardImages = new HashMap<>();

    public CardPanel(Color color, Value value) {
        this.value = value;
        this.color = color;
        String key = color + "_" + value;

        if(!cardImages.containsKey(key)) {
            loadImage(key);
        }

        this.cardImage = cardImages.get(key);
    }

    private void loadImage(String key) {
        String imagePath = "src/mages/" + key + ".png";
        Dimension dimension = new Dimension(100, 150);
        ImagePanel imagePanel = new ImagePanel(imagePath, dimension);
        cardImages.put(key, imagePanel);
    }
    
    public ImagePanel getCardImage() {
        return cardImage;
    }
}
