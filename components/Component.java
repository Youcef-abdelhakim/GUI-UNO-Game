package components;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class Component extends JComponent{
    public Component(){
        super();
    }
    public void setMargin(int top,int left,int bottom,int right){
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}
