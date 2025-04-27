package ui.views;

import components.Container;
import components.SquaredButton;
import components.Text;
import components.TextZone;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import ui.widgets.MainPage.GameTitle;
import ui.widgets.UnoFrame;
import util.Util;

public final class UnoNumberForm extends UnoFrame {

    private TextZone playersField;
    private TextZone humanField;
    private SquaredButton nextButton;
    private SquaredButton returnButton;

    public TextZone getPlayersField() {
        return playersField;
    }

    public TextZone getHumanField() {
        return humanField;
    }

    public SquaredButton getNextButton() {
        return nextButton;
    }

    public SquaredButton getReturnButton() {
        return returnButton;
    }

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
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));

        int size = 50;
        nextButton = new SquaredButton(size, new Color(0x93FF75), "→", Color.black);
        returnButton = new SquaredButton(size, new Color(0x93FF75), "↩", Color.black);

        // Force buttons to be square
        Dimension squareSize = new Dimension(size, size);
        nextButton.setPreferredSize(squareSize);
        nextButton.setMaximumSize(squareSize);
        nextButton.setMinimumSize(squareSize);

        returnButton.setPreferredSize(squareSize);
        returnButton.setMaximumSize(squareSize);
        returnButton.setMinimumSize(squareSize);

        wrapper.add(returnButton);
        wrapper.add(Box.createHorizontalGlue());
        wrapper.add(nextButton);

        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        wrapper.setOpaque(false);

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
        main.setPreferredSize(new Dimension(300, 300));
        add(main, gbc);
        SwingUtilities.invokeLater(() -> {
            validate();
            repaint();
        });

    }

}
