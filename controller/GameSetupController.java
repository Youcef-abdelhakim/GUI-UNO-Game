package controller;

import components.MessageDialog;
import gameLogique.Game;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.views.MainPage;
import ui.views.UnoNameForm;
import ui.views.UnoNumberForm;

public class GameSetupController {

    Game gameModel;
    UnoNumberForm gameSetupView;

    public GameSetupController(Game gameModel, UnoNumberForm gameSetupView) {
        this.gameModel = gameModel;
        this.gameSetupView = gameSetupView;
        this.gameSetupView.getReturnButton().addActionListener(new ReturnButtonAction());
        this.gameSetupView.getNextButton().addActionListener(new NextButtonController());
    }

    class ReturnButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new MainPage();
            gameSetupView.dispose();
        }
    }

    class NextButtonController implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MessageDialog messageDialog = new MessageDialog();
            try{
                int numberOfPlayers = Integer.parseInt(gameSetupView.getPlayersField().getText());
                int numberOfHumans = Integer.parseInt(gameSetupView.getHumanField().getText());

                if (numberOfPlayers < 2 || numberOfPlayers > 4) {

                    messageDialog.show("Enter a number of players between 2 and 4.");
                } else if (numberOfHumans < 0 || numberOfHumans > numberOfPlayers) {
                    messageDialog.show("Invalid number of humans.");
                } else {
                    new UnoNameForm(numberOfHumans);
                    gameSetupView.dispose();
                }
            } catch (NumberFormatException ex) {
                messageDialog.show("Please enter valid numbers.");
            }
        }

    }
}
