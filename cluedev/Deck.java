package cluedev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

    ArrayList<Card> cardList;
    ArrayList<Card>[] sortedDeck;
    ArrayList<Card> murderEnvelope;

    
    /**
     * Generates the default cards, includes the original players and weapons
     * 
     */
    public Deck() {
        this.cardList = new ArrayList<Card>();
        this.cardList.add(new Card("Study", "Room"));
        this.cardList.add(new Card("Billiard room", "Room"));
        this.cardList.add(new Card("Library", "Room"));
        this.cardList.add(new Card("Dining room", "Room"));
        this.cardList.add(new Card("Kitchen", "Room"));
        this.cardList.add(new Card("Lounge", "Room"));
        this.cardList.add(new Card("Ballroom", "Room"));
        this.cardList.add(new Card("Conservatory", "Room"));
        this.cardList.add(new Card("Hall", "Room"));

        this.cardList.add(new Card("Dagger", "Weapon"));
        this.cardList.add(new Card("Candlestick", "Weapon"));
        this.cardList.add(new Card("Revolver", "Weapon"));
        this.cardList.add(new Card("Rope", "Weapon"));
        this.cardList.add(new Card("Lead pipe", "Weapon"));
        this.cardList.add(new Card("Spanner", "Weapon"));

        this.cardList.add(new Card("Miss Scarlet", "Suspect"));
        this.cardList.add(new Card("Prof Plum", "Suspect"));
        this.cardList.add(new Card("Mrs Peacock", "Suspect"));
        this.cardList.add(new Card("Mr Green", "Suspect"));
        this.cardList.add(new Card("Ms White", "Suspect"));
        this.cardList.add(new Card("Col Mustard", "Suspect"));

        this.sortedDeck = this.sortSplitCards(this.cardList);

        generateMurderEnvelope();
    }

    /**
     *Creates a deck using the custom player and weapon lists, associated card should be included in the game
     * 
     * @param players Custom list of players
     * @param weapons Custom list of weapons
     */
    public Deck(Player[] players, Weapon[] weapons) {
        this.cardList = new ArrayList<Card>();
        this.cardList.add(new Card("Study", "Room"));
        this.cardList.add(new Card("Billiard room", "Room"));
        this.cardList.add(new Card("Library", "Room"));
        this.cardList.add(new Card("Dining room", "Room"));
        this.cardList.add(new Card("Kitchen", "Room"));
        this.cardList.add(new Card("Lounge", "Room"));
        this.cardList.add(new Card("Ballroom", "Room"));
        this.cardList.add(new Card("Conservatory", "Room"));
        this.cardList.add(new Card("Hall", "Room"));

        for (Weapon w : weapons) {
            this.cardList.add(new Card(w.getName(), "Weapon"));
        }

        Player[] allPlayers = new Player[6];
        ArrayList<Player> allPlayersList = new FullPlayerList(12).getAllPlayers();
        ArrayList<String> playerNames = new ArrayList<String>();
        for (Player p : players) {
            playerNames.add(p.getName());
        }
        for (int i = 0; i < 6; i++) {
            if (i >= players.length) {
                for (Player newPlayer : allPlayersList) {
                    if (!playerNames.contains(newPlayer.getName())) {
                        allPlayers[i] = newPlayer;
                        playerNames.add(newPlayer.getName());
                        break;
                    }
                }
            } else {
                allPlayers[i] = players[i];
            }
        }
        for (Player p : allPlayers) {
            System.out.println("NAME: " + p.getName());
            this.cardList.add(new Card(p.getName(), "Suspect"));
        }

        this.sortedDeck = this.sortSplitCards(this.cardList);

        generateMurderEnvelope();
    }

    public Deck(ArrayList<Card> cardList) {
        this.cardList = cardList;

        this.sortedDeck = this.sortSplitCards(this.cardList);
    }

    /**
     * Sorts and splits cards into their respective category and returns it for
     * uses when 1 of each card type is needed
     *
     * @param cardList ArrayList of the full cards in a game instance
     * @return 3 ArrayList each one corresponding to a specific card type
     */
    public ArrayList[] sortSplitCards(ArrayList<Card> cardList) {
        ArrayList<Card> roomList = new ArrayList<Card>();
        ArrayList<Card> weaponList = new ArrayList<Card>();
        ArrayList<Card> suspectList = new ArrayList<Card>();
        Card tempCard;
        for (int i = 0; i < cardList.size(); i++) {
            tempCard = cardList.get(i);
            switch (tempCard.getType()) {
                case "Room":
                    roomList.add(tempCard);
                    break;
                case "Weapon":
                    weaponList.add(tempCard);
                    break;
                case "Suspect":
                    suspectList.add(tempCard);
                    break;
                default:
                    break;
            }
        }
        ArrayList[] sortedCards = new ArrayList[]{
            roomList, weaponList, suspectList};
        return sortedCards;
    }

    /**
     * Generates the murder envelope by selecting 3 random cards from one of
     * each category
     */
    public void generateMurderEnvelope() {
        ArrayList<Card> murderEnvelope = new ArrayList<Card>();
        Random random = new Random();
        int randomIndex;
        for (int i = 0; i < 3; i++) {
            randomIndex = random.nextInt(this.sortedDeck[i].size());
            murderEnvelope.add(this.sortedDeck[i].get(randomIndex));
            //this.cardList.remove(this.sortedDeck[i].get(randomIndex));
        }
        this.murderEnvelope = murderEnvelope;
    }

    /**
     * Checks the passed cards against the murder envelope returns true if the
     * accusation is correct
     *
     * @param card1 First card to check against murder envelope
     * @param card2 Second card to check against murder envelope
     * @param card3 Third card to check against murder envelope
     * @return true or false dependant on the result of checking the accusation
     */
    public boolean checkAccusation(Card card1, Card card2, Card card3) {
        if (this.murderEnvelope.contains(card1) && this.murderEnvelope.contains(card2) && this.murderEnvelope.contains(card3)) {
            return true;
        }
        return false;
    }

    /**
     * Takes in the current number of players returning that number of card
     * lists which can be dealt to players Cards are dealt randomly in a round
     * robin style
     *
     * @param numberOfPlayers First card to check against murder envelope
     * @return An ArrayList of card lists, each one will be then dealt with to a
     * player
     */
    public ArrayList<ArrayList<Card>> dealCards(int numberOfPlayers) {
        ArrayList<Card> shuffledCards = (ArrayList) this.cardList.clone();
        for (Card c : this.murderEnvelope) {
            shuffledCards.remove(c);
        }
        ArrayList<ArrayList<Card>> playerCards = new ArrayList<ArrayList<Card>>();
        Collections.shuffle(shuffledCards);

        for (int i = 0; i < numberOfPlayers; i++) {
            playerCards.add(new ArrayList<Card>());
        }

        int playerIndex;
        for (int i = 0; i < shuffledCards.size(); i++) {
            playerIndex = i % numberOfPlayers;
            playerCards.get(playerIndex).add(shuffledCards.get(i));
        }
        return playerCards;
    }

    public ArrayList<Card> getMurderEnvelope() {
        return murderEnvelope;
    }

    public ArrayList<Card>[] getSortedDeck() {
        return this.sortedDeck;
    }

    public ArrayList<Card> getCardList() {
        return this.cardList;
    }

    public void setMurderEnvelope(ArrayList<Card> murderEnvelope) {
        this.murderEnvelope = murderEnvelope;
    }

}
