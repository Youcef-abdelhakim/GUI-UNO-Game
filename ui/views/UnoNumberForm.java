package ui.views;

import components.Container;
import components.MessageDialog;
import components.SquaredButton;
import components.Text;
import components.TextZone;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import ui.widgets.MainPage.GameTitle;
import ui.widgets.UnoFrame;
import util.Util;

public final class UnoNumberForm extends UnoFrame {
    private TextZone playersField;
    private TextZone humanField;
    private int numberOfPlayers;
    private int numberOfHumans;

    public void createAndAdjustTitle(Container main) {
        GameTitle gameTitle = new GameTitle(new GridBagLayout());
        main.add(gameTitle);
    }

    public void createAndAdjustPlayersField(Container main) {
        Container wrapper = new Container();

        Text playersLabel = new Text("How many players (2-4):", Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Black.ttf", 10), Color.WHITE);
        playersLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));

        playersField = new TextZone(20);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        wrapper.add(playersLabel);
        wrapper.add(playersField);
        wrapper.setOpaque(false);
        main.add(wrapper);

    }

    public void createAndAdjustHumanField(Container main) {
        Container wrapper = new Container();
        Text humansLabel = new Text("How many humans:", Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Black.ttf", 10), Color.WHITE);
        humansLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        humanField = new TextZone(20);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        wrapper.add(humansLabel);
        wrapper.add(humanField);
        wrapper.setOpaque(false);
        main.add(wrapper);
    }

    public void createAndAdjustButtons(Container main) {
        Container wrapper = new Container();
        wrapper.setLayout(new BorderLayout());
        SquaredButton nextButton = new SquaredButton(50, new Color(0x93FF75), "→", Color.black);
        SquaredButton returnButton = new SquaredButton(50, new Color(0x93FF75), "↩", Color.black);
        wrapper.add(returnButton, BorderLayout.WEST);
        wrapper.add(nextButton, BorderLayout.EAST);

        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        wrapper.setOpaque(false);
        returnButton.addActionListener(e -> {
            new MainPage();
            dispose();
        });
        nextButton.addActionListener(e -> {
            try {
                numberOfPlayers = Integer.parseInt(playersField.getText());
                numberOfHumans = Integer.parseInt(humanField.getText());

                if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                    MessageDialog.showMessageDialog(this, "Enter a number of players between 2 and 4.");
                } else if (numberOfHumans < 0 || numberOfHumans > numberOfPlayers) {
                    MessageDialog.showMessageDialog(this, "Invalid number of humans.");
                } else {
                    dispose();
                    new UnoNameForm(numberOfHumans);
                }
            } catch (NumberFormatException ex) {
                MessageDialog.showMessageDialog(this, "Please enter valid numbers.");
            }
        });
        main.add(wrapper);
    }

    public UnoNumberForm() {
        super();
        Container main = new Container();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        setLayout(new GridBagLayout());
        createAndAdjustTitle(main);
        createAndAdjustPlayersField(main);
        createAndAdjustHumanField(main);
        createAndAdjustButtons(main);

        main.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(main, gbc);

        SwingUtilities.invokeLater(
                () -> {
                    this.validate();
                    this.repaint();
                }
        );
    }

    public static void main(String[] args) {

        new UnoNumberForm();

    }
}
