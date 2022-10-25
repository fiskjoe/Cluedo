/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cluedev;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.SplittableRandom;
import java.util.Stack;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author bobja
 */
public class ComputerPlayer extends Player {

    Tile goalTile;
    ArrayList<Tile> discovered;
    ArrayList<Tile> parents;
    ArrayList<Tile> path;
    ArrayList<Room> visited = new ArrayList<Room>();

    public ComputerPlayer(String name, Tile startTile) {
        super(name, startTile);
    }

    public ComputerPlayer(String name) {
        super(name);
    }

    @Override
    public void startMove() {
        System.out.println(this.name + " COMPUTER PLAYER MOVE STARTED");
        this.moveActive = true;
        this.madeSuggestion = false;
        this.location.setOccupied(false);
        this.moveQueue.push(this.location);
        this.diceRollNum = diceRoll(2);
        BFS();
        moveHandler(this.diceRollNum);
        endMove();
    }

    @Override
    public void endMove() {
        this.moveActive = false;
        this.moveQueue = new Stack<Tile>();

        if (this.location instanceof DoorTile) {
            DoorTile d = (DoorTile) this.location;
            this.location = d.getRoom().getRandomTile();
            visited.add(d.getRoom());
        }

        setSprite("s");
        this.diceRollNum = 0;
        this.location.setOccupied(true);
        System.out.println("move ended");
    }

    /**
     * Method to get 3 unseen cards from an AI player for
     * suggestions/accusations Will be 1 of each type selected
     *
     * @return An Array containing the 3 cards the AI wants to pick for
     * suggestion/accusation
     */
    public Card[] getComputerCards() {
        Card[] selection = new Card[3];
        ArrayList<Card> unseenCards = new ArrayList<>();
        for (Card c : this.detectiveCards.keySet()) {
            unseenCards.add(c);
        }
        Collections.shuffle(unseenCards);
        ArrayList<Card>[] splitCards = sortSplitCards(unseenCards);
        selection[0] = splitCards[0].get(0);
        selection[1] = splitCards[1].get(0);
        selection[2] = splitCards[2].get(0);
        return selection;
    }

    /**
     * Method to move the AI player to their target tile, will follow shortest
     * path until ran out of moves
     *
     * @param numOfMoves the number of moves the AI player gets from their
     * diceroll
     */
    public void moveHandler(int numOfMoves) {
        String[] dirs = new String[]{"n", "e", "s", "w"};
        System.out.println("CURRENT GOAL: " + this.goalTile);
        for (int i = 0; i < numOfMoves; i++) {
            if (this.location instanceof DoorTile && i > 0) {
                break;
            } else {
                if (i < this.path.size() - 1) {
                    for (String d : dirs) {
                        if (this.path.get(i).getTileAtDirection(d) == this.path.get(i + 1)) {
                            playerMove(numOfMoves, d);
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }

     /**
     * Performs a breadth first search from the users current location and finds the closest 
     * door for which room they haven't visited, then calls genPath to create a path
     *
     */
    public void BFS() {
        Tile start = this.location;
        Queue<Tile> q = new ArrayDeque<>();
        this.discovered = new ArrayList<>();
        this.parents = new ArrayList<>();
        q.add(start);

        while (q.size() > 0) {
            Tile current = q.poll();
            for (Tile tile : current.getSurrounding().values()) {
                if ((!this.discovered.contains(tile)) && ((tile instanceof BlankTile) || tile instanceof DoorTile || tile instanceof SpecialTile)) {
                    this.discovered.add(tile);
                    this.parents.add(current);
                    q.add(tile);
                }
            }
        }
        for (Tile t : this.discovered) {
            if (t instanceof DoorTile) {
                DoorTile temp = (DoorTile) t;
                if (!(this.visited.contains(temp.getRoom()))) {
                    this.goalTile = t;
                    this.path = genPath(start);
                    break;
                }
            }
        }
    }

    /**
     * Generates the shortest path from the AI current location to their goal
     * tile
     *
     * @param start Tile that the AI starts on
     * @return An ArrayList of the shortest path from start to goal tile
     */
    public ArrayList<Tile> genPath(Tile start) {
        this.path = new ArrayList<Tile>();
        Tile current = this.goalTile;
        ArrayList<Tile> pathTaken = new ArrayList<Tile>();
        pathTaken.add(current);
        while (!pathTaken.contains(start)) {
            current = this.parents.get(this.discovered.indexOf(current));
            pathTaken.add(current);
        }
        Collections.reverse(pathTaken);
        return pathTaken;
    }

    public double distanceToTiles(Tile start, Tile end) {
        int startx = start.getCol();
        int starty = start.getRow();
        int endx = end.getCol();
        int endy = end.getRow();
        //int distance = abs((startx - endx)) + abs((starty - endy));
        double deltax = (endx - startx) * (endx - startx);
        double deltay = (endy - starty) * (endy - starty);
        double distance = Math.round(Math.sqrt(deltax + deltay));
        return distance;
    }

    /**
     * Generates the shortest path from the AI current location to their goal
     * tile
     *
     * @return returns true or false depending on the number of unseen cards and
     * uses this to calculate a probability of making an accusation will always
     * return true if all cards besides a murder envelope is seen
     */
    public boolean shouldAccuse() {
        SplittableRandom random = new SplittableRandom();
        int remainingCards;
        int chance;
        remainingCards = 21 - (this.returnSeen().size() + 3);
        chance = 100 - (remainingCards * 6);
        boolean timeToAccuse = random.nextInt(1, 101) <= chance;
        return timeToAccuse;
    }

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

}
