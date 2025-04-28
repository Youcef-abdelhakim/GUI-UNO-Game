package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.views.MainPage;
import ui.views.UnoNumberForm;

public class MainPageController {
    MainPage mainPage;

    public MainPageController(MainPage mainPage) {
        this.mainPage = mainPage;
        this.mainPage.getNewAndLoadButton().getNewGameButton().addActionListener(new NewGameButton());
    }
    class NewGameButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            UnoNumberForm page = new UnoNumberForm();
            PlayerNumberController gsc = new PlayerNumberController(page);
            mainPage.dispose();
        }
        
    }
}
