package ui.widgets.UnoNameForm;

import components.Container;
import components.Text;
import java.awt.Color;
import java.awt.GridBagLayout;
import util.Util;

public class Title extends Text {

    public Title(String text) {
        super(text,Util.loadCustomFont("src\\fonts\\Main font\\Roboto-Black.ttf", 30),Color.WHITE);
        Container container = new Container();
        container.setLayout(new GridBagLayout());
        container.setOpaque(false);
        container.add(this);
    }
}
