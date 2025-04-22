package ui.widgets.GamePage;

public class Card {
    private String imagePath;
    private String color;
    private String value;

    public Card(String imagePath, String color, String value) {
        this.imagePath = imagePath;
        this.color = color;
        this.value = value;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }
}
