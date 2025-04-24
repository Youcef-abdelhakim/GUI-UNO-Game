package gameLogique;

public class Card {
    
    public enum Color {
        Red, Blue, Green, Yellow, Wild;

        private static final Color[] colors = Color.values();

        public static Color getColors(int i) {
            return Color.colors[i];
        }
    }

    public enum Value {
        Zero, One, Two, Three, Four, Five, Six, Seven, Eight, Nine,
        Skip, Reverse, DrawTwo, Wild, WildDrawFour;

        private static final Value[] values = Value.values();

        public static Value getValues(int i) {
            return Value.values[i];
        }
    }


    private final Color color;
    private final Value value;

    public Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return color + " " + value;
    }

}
