/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cluedev;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bobja
 */
public class Sprint2Tests {
    
    GameHandler g;

    public Sprint2Tests() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void occupiedSquare() {
        assertFalse("Tile occupied but player moved", g.currentPlayer.playerMove(1, "s"));
        //Refactored occupy square test
    }
    
    @Test
    public void enterRoom() {
        Player p = g.getPlayers()[2];
        DoorTile d = (DoorTile) p.location;
        p.setLocation(d.getRoom().getRandomTile());
        assertEquals("User not placed on correct room square", p.location, g.board.tileArray[1][1]);
    }
    
    @Test
    public void suggestion() {
        Deck d = new Deck();
        ArrayList<Card> m = new ArrayList<>();
        Card c1 = d.sortedDeck[0].get(0);
        Card c2 = d.sortedDeck[1].get(0);
        Card c3 = d.sortedDeck[2].get(0);
        Card c4 = d.sortedDeck[0].get(1);
        Card c5 = d.sortedDeck[1].get(1);
        Card c6 = d.sortedDeck[2].get(1);
        m.add(c1);
        m.add(c2);
        m.add(c3);
        g.currentPlayer.setCardList(m);
        assertTrue("User has a card but false returned", g.currentPlayer.checkSuggestion(c3, c4, c5).size() > 0);
        assertFalse("User has no cards but true returned", g.currentPlayer.checkSuggestion(c6, c4, c5).size() == 0);
        //Refactored occupy square test
    }
    
    @Test
    public void accusation() {
        Deck d = new Deck();
        ArrayList<Card> m = new ArrayList<>();
        Card c1 = d.sortedDeck[0].get(0);
        Card c2 = d.sortedDeck[1].get(0);
        Card c3 = d.sortedDeck[2].get(0);
        Card c4 = d.sortedDeck[2].get(1);
        m.add(c1);
        m.add(c2);
        m.add(c3);
        d.setMurderEnvelope(m);
        assertTrue("Guessed right cards but false returned", d.checkAccusation(c1, c2, c3));
        assertFalse("Guessed wrong cards but true returned", d.checkAccusation(c1, c2, c4));
        //Refactored occupy square test
    }

    @Test
    public void cardDealing() {
        Deck d = new Deck();
        assertEquals(d.cardList.size(), 21);
        //Test the full deck is constructed
        d.generateMurderEnvelope();
        ArrayList<Card> m = d.getMurderEnvelope();
        assertEquals(m.size(), 3);
        assertEquals(m.get(0).getType(), "Room");
        assertEquals(m.get(1).getType(), "Weapon");
        assertEquals(m.get(2).getType(), "Suspect");
        assertEquals(d.cardList.size(), 18);
        //Testing that the murder envelope is properly constructed with 1 of each type
        //also tests that cards are properly removed from the master card list
        ArrayList<ArrayList<Card>> playerCards1 = new ArrayList<ArrayList<Card>>();
        playerCards1 = d.dealCards(6);
        for (int i = 0; i < 6; i++) {
            assertEquals(3, playerCards1.get(i).size());
            
        }
        ArrayList<ArrayList<Card>> playerCards2 = new ArrayList<ArrayList<Card>>();
        playerCards2 = d.dealCards(3);
        for (int i = 0; i < 3; i++) {
            assertEquals(6, playerCards2.get(i).size());
        }
        //Tests the cards are dealt between players equally, tests with 3 and 6 players
    }

    @Before
    public void setUp() {
        Player[] testPlayers = new Player[6];
        testPlayers[0] = new Player("Miss Scarlet");
        testPlayers[1] = new Player("Prof Plum");
        testPlayers[2] = new Player("Mrs Peacock");
        testPlayers[3] = new Player("Mr Green");
        testPlayers[4] = new Player("Mrs White");
        testPlayers[5] = new Player("Col Mustard");
       
        this.g = new GameHandler(testPlayers);
        
        g.startGame();

    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
