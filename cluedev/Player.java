package cluedev;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.Stack;

public class Player {

    String name;
    ArrayList<Card> cardList;
    Tile location;
    Boolean moveActive = false;
    Boolean madeSuggestion;
    Boolean madeAccusation = false;
    Boolean extraMove = false;
    Boolean extraSuggest = false;
    Stack<Tile> moveQueue = new Stack<Tile>();
    HashMap<Card, Boolean> detectiveCards = new HashMap<Card, Boolean>();
    int diceRollNum = 0;
    String image;
    String cardImage;

    /**
     * constructs the player object, setting their name and start tile
     *
     * @param name Name of the player will associate a specific sprite image for them
     * @param startTile Tile the user should start on
     */
    public Player(String name, Tile startTile) {
        this.name = name;
        this.location = startTile;
        this.cardList = new ArrayList<Card>();
        this.madeSuggestion = false;
        this.image = "/images/players/" + this.name + "/" + this.name + " " + "front" + ".png";
        //this.cardImage = "/images/players/" + this.name + "/" + this.name.toLowerCase() + " " + "card" + ".png";
    }
    
    /**
     * Alternative constructor, used when constructing custom players, start location must be later set
     *
     * @param name Name of the player will associate a specific sprite image for them
     */
    public Player(String name) {
        this.name = name;
        this.cardList = new ArrayList<Card>();
        this.madeSuggestion = false;
        this.image = "/images/players/" + this.name + "/" + this.name + " " + "front" + ".png";
        //this.cardImage = "/images/players/" + this.name + "/" + this.name.toLowerCase() + " " + "card" + ".png";
    }

    /**
     * Updates the sprite by setting the player image to the an image
     * corresponding to the direction they moved in
     *
     * @param String direction
     */
    public void setSprite(String direction) {
        String facing = "front";
        switch (direction) {
            case "n":
                facing = "back";
                break;
            case "e":
                facing = "right";
                break;
            case "s":
                facing = "front";
                break;
            case "w":
                facing = "left";
                break;
        }
        this.image = "/images/players/" + this.name + "/" + this.name + " " + facing + ".png";
    }

    /**
     * Ends the user move by updating class variables and wiping move queue.
     * Checks for which tile the use ends their move on, doors update the user
     * location, special tile activates their associated power up
     */
    public void endMove() {
        this.moveActive = false;
        this.moveQueue = new Stack<Tile>();

        if (this.location instanceof DoorTile) {
            DoorTile d = (DoorTile) this.location;
            this.location = d.getRoom().getRandomTile();
        }

        setSprite("s");
        this.diceRollNum = 0;
        this.location.setOccupied(true);
        System.out.println("move ended");

        if (this.location instanceof SpecialTile) {
            SpecialTile s = (SpecialTile) this.location;
            if (s.getSpecialType().equals("s")) {
                this.extraSuggest = true;
            } else if (s.getSpecialType().equals("r") && !this.extraMove) {
                this.extraMove = true;
                this.startMove(true);
            }
        }

    }

    /**
     * Starts the user move, sets move to active and allows user to suggest
     * again current location is pushed to move queue
     */
    public void startMove() {
        System.out.println(this.name + " HUMAN PLAYER MOVE STARTED");
        this.moveActive = true;
        this.madeSuggestion = false;
        this.extraMove = false;
        this.location.setOccupied(false);
        this.moveQueue.push(this.location);
    }

    /**
     * Starts the user move, sets move to active and allows user to suggest
     * again current location is pushed to move queue. Has usedExtraMove
     * parameter if the user used a reroll special tile.
     *
     * @param usedExtraMove boolean to check if the user has used a special tile reroll function 
     */
    public void startMove(boolean usedExtraMove) {
        System.out.println(this.name + " HUMAN PLAYER MOVE STARTED");
        this.moveActive = true;
        this.madeSuggestion = false;
        this.location.setOccupied(false);
        this.moveQueue.push(this.location);
    }

    /**
     * Used to check the cards in the user deck against 3 suggested cards. Will
     * build a list of however many of the cards that have been suggested are in
     * the users card list, allowing the user to choose which card is selected
     * when responding to a suggestion.
     *
     * @param card1 First card a different user suggests to check against own
     * list
     * @param card2 Second card a different user suggests to check against own
     * list
     * @param card3 Third card a different user suggests to check against own
     * list
     * @return An ArrayList containing the number of cards the player has of the
     * 3 suggested
     */
    public ArrayList<Card> checkSuggestion(Card card1, Card card2, Card card3) {
        ArrayList<Card> suggested = new ArrayList<>();
        if (this.cardList.contains(card1)) {
            suggested.add(card1);
        }
        if (this.cardList.contains(card2)) {
            suggested.add(card2);
        }
        if (this.cardList.contains(card3)) {
            suggested.add(card3);
        }

        return suggested;
    }

    /**
     * Used to check if the user can suggest, has to be in a room tile and not
     * suggested within their turn
     *
     * @return true or false depending on whether or not the user can make a
     * suggestion
     */
    public boolean canSuggest() {
        if (this.location instanceof RoomTile && !madeSuggestion) {
            return true;
        }
        return false;
    }

    /**
     * Makes the passed card seen in the players detective card
     *
     * @param card Card a user will have seen through suggestion
     */
    public void seenCard(Card card) {
        this.detectiveCards.put(card, true);
    }

    /**
     * Loops through the detective cards adding any seen cards to an array to be
     * returned
     *
     * @return An ArrayList containing the all the cards which are seen in the
     * player's detective cards
     */
    public ArrayList<Card> returnSeen() {
        ArrayList<Card> seenCards = new ArrayList<Card>();
        for (Card c : this.detectiveCards.keySet()) {
            if (this.detectiveCards.get(c)) {
                seenCards.add(c);
            }
        }
        return seenCards;
    }

    /**
     * Used to calculate the get the users diceRoll, can pass it number of dice
     * for future game updates
     *
     * @param numOfDice number of dice that a player will roll
     * @return total sum of the number of dice rolled
     */
    public int diceRoll(int numOfDice) {
        Random dice = new Random();
        int sum = 0;
        for (int n = 0; n < numOfDice; n++) {
            sum += dice.nextInt(6) + 1;
        }
        return sum;
    }

    /**
     * Checks a users desired move and returns true if it's valid. valid moves
     * are any moves onto a door or blank tile will check against the total
     * number of moves using the queue and dice roll parameter
     *
     * @param diceroll total roll, taken at the start of players turn
     * @param direction the direction in which the player wants to move
     * @return only returns true if the user makes a valid move
     */
    public boolean playerMove(int diceroll, String direction) {
        if (this.moveActive && this.diceRollNum != 0) {
            setSprite(direction);
            //System.out.println("BEFORE: " +this.location.getCol() + ", " + this.location.getRow());
            Tile temptile = this.location.getTileAtDirection(direction);
            //System.out.println("AFTER: " + temptile.getCol() + ", " + temptile.getRow());
            if (temptile == null) {
                System.out.println("Tile is out of bounds");
                return false;
            } else if (!(temptile instanceof BlankTile) && !(temptile instanceof DoorTile) && !(temptile instanceof SpecialTile)) {
                System.out.println("Cannot move onto this tile");
                return false;
            } else if (temptile.getOccupied()) {
                System.out.println("Tile is occupied");
                return false;
            } else {
                if (temptile == this.moveQueue.peek()) {
                    this.location = this.moveQueue.peek();
                    this.moveQueue.pop();
                    return true;
                } else if (this.moveQueue.size() > diceroll) {
                    System.out.println("Out of moves");
                    return false;
                } else {
                    this.moveQueue.push(this.location);
                    this.location = temptile;
                    System.out.println(this.location.getCol() + ", " + this.location.getRow());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates the users empty detective card by looping through the deck and 
     * putting all cards with false as a value into the detective hash map
     *
     * @param d The deck from which the detective is built, should be passed after the cards are selected.
     */
    public void createDetectiveCard(Deck d) {
        ArrayList<Card>[] sortedDeck = d.getSortedDeck();
        for (int i = 0; i < 3; i++) {
            for (int r = 0; r < sortedDeck[i].size(); r++) {
                this.detectiveCards.put(sortedDeck[i].get(r), false);
            }
        }
    }

    public String getImage() {
        return this.image;
    }

    public String getCardImage() {
        return this.cardImage;
    }

    public int getDiceRollNum() {
        return this.diceRollNum;
    }

    public void setDiceRollNum(int num) {
        this.diceRollNum = num;
    }

    public String getName() {
        return this.name;
    }

    public Tile getLocation() {
        return this.location;
    }

    public void setLocation(Tile l) {
        this.location = l;
    }

    public Tile getTopOfMoveQueue() {
        return this.moveQueue.peek();
    }

    public Boolean getMadeAccusation() {
        return madeAccusation;
    }

    public void setMadeAccusation(Boolean madeAccusation) {
        this.madeAccusation = madeAccusation;
    }

    public Boolean getMadeSuggestion() {
        return madeSuggestion;
    }

    public HashMap<Card, Boolean> getDetectiveCard() {
        return this.detectiveCards;
    }

    public void setMadeSuggestion(Boolean madeSuggestion) {
        this.madeSuggestion = madeSuggestion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    public void setCardList(ArrayList<Card> cardList) {
        this.cardList = cardList;
        for (int i = 0; i < cardList.size(); i++) {
            this.seenCard(cardList.get(i));
        }
    }

    public Stack<Tile> getMoveQueue() {
        return this.moveQueue;
    }
}
