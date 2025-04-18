package ui.widgets.MainPage;

import components.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import util.*;

public class NewAndLoadButton extends JComponent {
    private void createAndAdjustNewButton(){
        Button newGameButton = new Button("New game", new Dimension(300, 50));
        newGameButton.setBGColor(new Color(0x93FF75));
        newGameButton.adjustFont(Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Bold.ttf", 20));
        GridBagConstraints newGameButtonConst = new GridBagConstraints();
        newGameButtonConst.gridx = 0;
        newGameButtonConst.gridy = 0;
        newGameButtonConst.insets = new Insets(10, 0, 10, 0);
        newGameButton.setMinimumSize(new Dimension(300, 50));
        newGameButtonConst.fill = GridBagConstraints.NONE;
        newGameButtonConst.anchor = GridBagConstraints.CENTER;
        newGameButton.onHoverBrighten(-0.1f);
        add(newGameButton, newGameButtonConst);

    }
    private void createAndAdjustLoadButton(){
        Button loadGameButton = new Button("Load game", new Dimension(300, 50));
        loadGameButton.setBGColor(new Color(0xCACACA));
        loadGameButton.adjustFont(Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Bold.ttf", 20));
        GridBagConstraints loadGameButtonConst = new GridBagConstraints();
        loadGameButtonConst.gridx = 0;
        loadGameButtonConst.gridy = 1;
        loadGameButtonConst.fill = GridBagConstraints.CENTER;
        loadGameButton.setMinimumSize(new Dimension(300, 50));
        loadGameButtonConst.anchor = GridBagConstraints.CENTER;
        loadGameButton.onHoverBrighten(-0.1f);
        add(loadGameButton, loadGameButtonConst);

    }
    public NewAndLoadButton() {
        super();
        setLayout(new GridBagLayout());
        createAndAdjustNewButton();
        createAndAdjustLoadButton();
        setOpaque(false);
    }
    public void setMargin(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}
