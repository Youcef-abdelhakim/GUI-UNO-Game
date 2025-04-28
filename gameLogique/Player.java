package gameLogique;

import java.util.ArrayList;

public class Player {
    private String PlayerType;
    private String playerName;
    private ArrayList<Card> playerHnad;

    public Player(String playerName, String playerType) {
        this.playerName = playerName;
        this.PlayerType = playerType;
        this.playerHnad = new ArrayList<>();
    }


    public String getPlayerType() {
        return PlayerType;
    }
    public String getPlayerName() {
        return playerName;
    }

    public ArrayList<Card> getPlayerHnad() {
        return playerHnad;
    }
    
    public void addToHand(Card card) {
        playerHnad.add(card);
    }

    public String getName() {
        return playerName;
    }

}
