package controller;

import components.MessageDialog;
import components.TextZone;
import gameLogique.Game;
import gameLogique.Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import ui.views.GamePage;
import ui.views.UnoNameForm;
import ui.views.UnoNumberForm;

public class PlayersNameController {
    Game model;
    UnoNameForm view;
    int numberOfPlayers;
    int numberOfHumans;
    public PlayersNameController(UnoNameForm view,int numberOfPlayers,int numberOfHumans){
        this.view = view;
        this.numberOfHumans = numberOfHumans;
        this.numberOfPlayers = numberOfPlayers;
        this.view.getButtons().getReturnButton().addActionListener(new ReturnButtonAction());
        this.view.getButtons().getPlayButton().addActionListener(null);
    }


    class ReturnButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            UnoNumberForm view1 = new UnoNumberForm();
            PlayerNumberController pnc = new PlayerNumberController(view1);
            view.dispose();
        }
    }

    class PlayButtonAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean test = false;
            MessageDialog messageDialog = new MessageDialog();
            ArrayList<Player> players = new ArrayList<>(numberOfPlayers);
            for (TextZone elem : view.getNamesForm().getPlayerFields()) {
                if ("".equals(elem.getText())){
                    messageDialog.show("Empty Field,fill it");
                    players.clear();
                    break;
                }else{
                    Player player = new Player(elem.getText(), "Player");
                    players.add(player);
                    test = true;
                }
            }
            if (test){
                for (int i = numberOfHumans; i < numberOfPlayers ; i++) {
                    Player player = new Player("Bot"+(numberOfPlayers-i),"Bot");
                    players.add(player);
                }
                model = new Game(players);
                model.setupGame();
                new GamePage();
                view.dispose();
            }
            
        }
        
    }
}
