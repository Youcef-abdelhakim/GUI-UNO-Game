package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import widgets.Container;
import widgets.Frame;
//! TODO : Theme Color must be in a special file as consts
public class MainPage{
    public MainPage() {
        Frame mainFrame = new Frame("Uno!!", Color.CYAN,new Dimension(900,650));
        Container cnt = new Container(new Dimension(150,600),Color.black);
        cnt.adjustDimensionByLayout(new Dimension(350, 150));
        mainFrame.addWidget(cnt,BorderLayout.WEST);
    }
    public static void main(String[] args) {
        new MainPage();
    }
    
}
