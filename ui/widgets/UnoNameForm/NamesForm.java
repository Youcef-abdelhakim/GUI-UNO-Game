package ui.widgets.UnoNameForm;

import components.Container;
import components.Padding;
import components.Text;
import components.TextZone;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;

public class NamesForm extends Container {
    TextZone[] playerFields;
    public NamesForm(int numOfHuman) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        playerFields = new TextZone[numOfHuman];
        for (int i = 0; i < numOfHuman; i++) {
            Text label = new Text("Player " + (i + 1) + "'s Name" + ":");
            label.setForeground(Color.WHITE);

            Container labelWrapper = new Container();
            labelWrapper.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0)); // left-align
            labelWrapper.setOpaque(false);
            labelWrapper.add(label);

            add(labelWrapper);

            playerFields[i] = new TextZone(20);
            playerFields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            add(playerFields[i]);
            add(new Padding(0, 2));
            System.out.println(playerFields);
        }
        setOpaque(false);
    }

    public TextZone[] getPlayerFields() {
        return playerFields;
    }
}
