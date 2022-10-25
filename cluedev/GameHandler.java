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
public class GameHandler {

    Board board;
    Player[] players;
    Weapon[] weapons;
    Player currentPlayer;
    Deck cardDeck;

    /**
     * Creates an instance of GameHandler with the players and weapons which the user selects, will create detective cards for all players. 
     * Sets the location of players and weapons, dependant on the starting tiles in Board. 
     * 
     * @param selectedPlayers player list which is selected, can contain non standard clue characters
     * @param selectedWeapons weapon list, passed from customisation window.
     */
    public GameHandler(Player[] selectedPlayers, Weapon[] selectedWeapons) {
        this.board = new Board();
        this.board.generateBoard();
        this.players = selectedPlayers;
        this.weapons = selectedWeapons;
        this.cardDeck = new Deck(this.players, this.weapons);
        Tile[] wStarts = this.board.getWeaponStart();
        Tile[] pStarts = this.board.getPlayerStart();
        int i = 0;
        for (Weapon w : this.weapons) {
            w.setLocation(wStarts[i]);
            i++;
        }
        i = 0;
        for (Player p : this.players) {
            p.createDetectiveCard(this.cardDeck);
            p.setLocation(pStarts[i]);

            if (p instanceof ComputerPlayer) {
                ComputerPlayer temp = (ComputerPlayer) p;
                //temp.findGoal(this.board);
                temp.createDetectiveCard(this.cardDeck);
                temp.setLocation(pStarts[i]);
            }
            i++;
        }

    }

    /**
     * Starts an instance of a cluedo game. Deals all the cards to players and sets the current player to the first element in players array
     * 
     * @param diceroll total roll, taken at the start of players turn
     * @param direction the direction in which the player wants to move
     * @return only returns true if the user makes a valid move
     */
    public void startGame() {
        ArrayList<ArrayList<Card>> playerCards;
        playerCards = cardDeck.dealCards(players.length);
        int i = 0;
        for (Player p : this.players) {
            this.board.setOccupied(p.getLocation(), true);
            p.setCardList(playerCards.get(i));
            i = i + 1;
        }
        this.currentPlayer = this.players[0];
        if (this.currentPlayer instanceof ComputerPlayer) {
            System.out.println("starting computer move");
        }
        this.currentPlayer.startMove();
    }

    public Board getBoard() {
        return this.board;
    }

    
    /**
     * Moves onto the next players turn, called at the end turn of previous player. Checks that player hasn't accused and there are still players
     * who are in the game, if AI player automatically start their movement phase
     */
    public void nextTurn() {
        //this.currentPlayer.endMove();
        int playerIndex = java.util.Arrays.asList(this.players).indexOf(this.currentPlayer);
        playerIndex = (playerIndex + 1) % players.length;
        if (this.players[playerIndex].madeAccusation && !allPlayersOut()) {
            this.currentPlayer = this.players[playerIndex];
            nextTurn();
        } else {
            this.currentPlayer = this.players[playerIndex];
            if (!(this.currentPlayer instanceof ComputerPlayer)) {
                this.currentPlayer.startMove();
            }
        }
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Weapon[] getWeapons() {
        return this.weapons;
    }

    public Deck getDeck() {
        return this.cardDeck;
    }

    /**
     *
     * Method to handle when a user accuses, will end game if successful or all players failed
     * 
     * @param card1 First card that the current user has accused with
     * @param card2 Second card that the current user has accused with
     * @param card3 Third card that the current user has accused with
     * @return returns true if the game reaches an end state e.g. all players accused or player makes successful accusation
     */
    public boolean accusation(Card card1, Card card2, Card card3) {
        this.currentPlayer.setMadeAccusation(true);
        if (this.cardDeck.checkAccusation(card1, card2, card3)) {
            System.out.println("User" + this.currentPlayer.getName() + " guessed murder envelope correctly!");
            return true;
        } else if (allPlayersOut()) {
            System.out.println("All players have used accusation");
            return true;
        }
        System.out.println("User" + this.currentPlayer.getName() + " guessed murder envelope incorrectly!");
        this.currentPlayer.endMove();
        return false;
    }

    /**
     * Function to check whether or not all players have made an accusation
     * 
     * @return returns true if all players have accused
     */
    public boolean allPlayersOut() {
        for (Player p : this.players) {
            if (!p.getMadeAccusation()) {
                return false;
            }
        }
        return true;
    }

    public void suggestion(Card card1, Card card2, Card card3) {
        for (Player p : this.players) {
            if (p != this.currentPlayer) {
                if (p.checkSuggestion(card1, card2, card3).size() > 0) {
                    System.out.println("User" + p.getName() + "has suggested card");
                }
            }
        }
    }

}
