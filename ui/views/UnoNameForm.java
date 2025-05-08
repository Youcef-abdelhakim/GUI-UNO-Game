package ui.views;

import components.*;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import ui.widgets.UnoFrame;
import ui.widgets.UnoNameForm.Buttons;
import ui.widgets.UnoNameForm.NamesForm;
import ui.widgets.UnoNameForm.Title;

public class UnoNameForm extends UnoFrame {
    NamesForm namesForm;
    Buttons buttons;
    
    public UnoNameForm(int numOfPlayer , int numOfHuman) {
        
        super();
        Container container = new Container();
        container.setBackground(new Color(14, 62, 110)); 
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        Container form = new Container();
        
        form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));

        form.add(new Title("Enter Players Names"));
        form.add(new Padding(0, 20));
        namesForm = new NamesForm(numOfHuman);
        form.add(namesForm);
        form.setOpaque(false);
        container.add(form);
        buttons = new Buttons(this);
        container.add(buttons);
        this.add(container);
    }

    public NamesForm getNamesForm() {
        return namesForm;
    }

    public Buttons getButtons() {
        return buttons;
    }

}
