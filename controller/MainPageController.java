package controller;

import gameLogique.Game;
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
            Game game = new Game();
            UnoNumberForm page = new UnoNumberForm();
            GameSetupController gsc = new GameSetupController(game, page);
            mainPage.dispose();
        }
        
    }
}
