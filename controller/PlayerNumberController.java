package controller;

import components.MessageDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ui.views.MainPage;
import ui.views.UnoNameForm;
import ui.views.UnoNumberForm;

public class PlayerNumberController {

    UnoNumberForm gameSetupView;

    public PlayerNumberController(UnoNumberForm gameSetupView) {
        this.gameSetupView = gameSetupView;
        this.gameSetupView.getReturnButton().addActionListener(new ReturnButtonAction());
        this.gameSetupView.getNextButton().addActionListener(new NextButtonController());
    }

    class ReturnButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainPage view = new MainPage();
            MainPageController mgc = new MainPageController(view);
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
                    gameSetupView.getPlayersField().setText("");
                    gameSetupView.getHumanField().setText("");
                    messageDialog.show("Enter a number of players between 2 and 4.");
                } else if (numberOfHumans < 0 || numberOfHumans > numberOfPlayers) {
                    gameSetupView.getPlayersField().setText("");
                    gameSetupView.getHumanField().setText("");
                    messageDialog.show("Invalid number of humans.");
                } else {
                    UnoNameForm view1 = new UnoNameForm(numberOfPlayers,numberOfHumans);
                    PlayersNameController pnc = new PlayersNameController(view1,numberOfPlayers,numberOfHumans);
                    gameSetupView.dispose();
                }
            } catch (NumberFormatException ex) {
                gameSetupView.getPlayersField().setText("");
                gameSetupView.getHumanField().setText("");
                messageDialog.show("Please enter valid numbers.");
            }
        }

    }
}
