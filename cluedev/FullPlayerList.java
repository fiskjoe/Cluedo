/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cluedev;

import java.util.ArrayList;

/**
 *
 * @author bobja
 */
public class FullPlayerList {

    ArrayList<Player> allPlayers = new ArrayList<Player>();
    int playersToSelect;
    Player[] selectedPlayers;

    /**
     * Constructor that adds all custom players to a master list and presets the selectedPlayer variables to the default characters
     *
     * @param numOfPlayers number of players that the user selects for their game
     */
    public FullPlayerList(int numOfPlayers) {
        allPlayers.add(new Player("Miss Scarlet"));
        allPlayers.add(new Player("Prof Plum"));
        allPlayers.add(new Player("Mrs Peacock"));
        allPlayers.add(new Player("Mr Green"));
        allPlayers.add(new Player("Mrs White"));
        allPlayers.add(new Player("Col Mustard"));
        allPlayers.add(new Player("Capt Navy"));
        allPlayers.add(new Player("Dr Dusk"));
        allPlayers.add(new Player("Lady Lime"));
        allPlayers.add(new Player("Mr Brown"));
        allPlayers.add(new Player("Miss Rose"));
        allPlayers.add(new Player("Sir Pumpkin"));

        this.playersToSelect = numOfPlayers;
        this.selectedPlayers = new Player[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            this.selectedPlayers[i] = this.allPlayers.get(i);
        }
    }

    public void setPlayer(Player newPlayer, int oldIndex) {
        this.selectedPlayers[oldIndex] = newPlayer;
    }

    public ArrayList<Player> getAllPlayers() {
        return this.allPlayers;
    }

}
