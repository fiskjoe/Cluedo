package cluedev;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bobja
 */
public class Sprint1 {
    
    public Sprint1() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void validMovement() {
        Board b = new Board();
        Player p = new Player("playerTest", b.getTileArray()[6][8]);
        //Initialise player and board
        p.startMove();
        assertTrue(p.playerMove(4, "n"));
        assertEquals(p.getLocation(), b.getTileArray()[5][8]);
        assertTrue(p.playerMove(4, "e"));
        assertEquals(p.getLocation(), b.getTileArray()[5][9]);
        assertTrue(p.playerMove(4, "s"));
        assertEquals(p.getLocation(), b.getTileArray()[6][9]);
        assertTrue(p.playerMove(4, "w"));
        assertEquals(p.getLocation(), b.getTileArray()[6][8]);
        assertFalse("Dice roll not decremented,", p.playerMove(4, "w"));
        //Test movement in every direction with a final move to test decerment in moves
    }
    
    @Test
    public void occupiedSquare(){
        Board b = new Board();
        Player p1 = new Player("playerTest1", b.getTileArray()[6][8]);
        Player p2 = new Player("playerTest2", b.getTileArray()[5][8]);
        p1.startMove();
        //assertFalse("Tile occupied but player moved",p1.playerMove(1, "n"));
    }
    
    @Test
    public void invalidMoves(){
        Board b = new Board();
        Player p = new Player("playerTest", b.getTileArray()[6][8]);
        p.startMove();
        
        assertTrue(p.playerMove(8, "e"));
        assertFalse(p.playerMove(8, "e"));
        //Tests trying to move into a room through the wall
        
        assertTrue(p.playerMove(8, "n"));
        assertTrue(p.playerMove(8, "n"));
        assertTrue(p.playerMove(8, "n"));
        assertTrue(p.playerMove(8, "n"));
        assertFalse(p.playerMove(8, "n"));
        //Tests trying to move out of the board limits   
    }
    
    @Test
    public void moveIncrement(){
        Board b = new Board();
        Player p = new Player("playerTest", b.getTileArray()[6][8]);
        p.startMove();
        assertTrue(p.playerMove(4, "e"));
        assertTrue(p.playerMove(4, "w"));
        //Move east once then west once to decrement then icrement coutner
        assertTrue(p.playerMove(4, "w"));
        assertTrue(p.playerMove(4, "w"));
        assertTrue(p.playerMove(4, "w"));
        assertTrue(p.playerMove(4, "w"));
        assertFalse(p.playerMove(4, "w"));
        //Move 4 times west, with final move being false
    }
   
    
}
