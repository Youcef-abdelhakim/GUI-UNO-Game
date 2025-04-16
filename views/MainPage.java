package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import widgets.*;

public class MainPage {
    public MainPage() {
        // Create frame with FlowLayout so card is shown properly
        Frame mainFrame = new Frame("Uno!!", Color.BLUE, new Dimension(900, 650), new FlowLayout());

        // Create a red card with value 9
        UnoCard card = new UnoCard("red", "9");

        // Add the card to the frame
        mainFrame.addWidget(card);
    }

    public static void main(String[] args) {
        new MainPage();
    }
}
