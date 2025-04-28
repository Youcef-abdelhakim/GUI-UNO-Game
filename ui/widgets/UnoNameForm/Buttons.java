package ui.widgets.UnoNameForm;

import components.Button;
import components.Component;
import components.Container;
import components.Frame;
import components.Padding;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import ui.views.GamePage;

public class Buttons extends Container {
    Button returnButton;
    Button playButton;
    public Buttons(Frame frame){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        //return botton
        returnButton = new Button("Return");
        returnButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnButton.setBackground(Color.GREEN);
        add(returnButton);


        Button playButton = new Button("PLAY!!");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setForeground(Color.BLACK);
        playButton.setBackground(Color.GREEN);
        playButton.setFocusPainted(false);
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        add(new Padding(0, 10));
        add(playButton);
        setOpaque(false);

        // Action du bouton
        playButton.addActionListener((ActionEvent e) -> {
            new GamePage();
            frame.dispose();
        });
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public Button getPlayButton() {
        return playButton;
    }
}
