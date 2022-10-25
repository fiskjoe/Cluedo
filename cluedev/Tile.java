package cluedev;
        
import java.util.HashMap;
import java.util.Map;

public class Tile {
    HashMap<String, Tile> surrounding;
    boolean occupied;
    int row;
    int col;
    String image;
    
    /**
     * Creates a hashmap of all the surrounding tiles, used to make movement simpler
     * 
     * @param tileArray the board tile array, used to find surrounding tiles
     */
    public void generateSurrounding(Tile[][] tileArray) {
        Tile nTile, sTile, eTile, wTile;
        if (this.row == 0) { nTile = null; 
        } else { nTile = tileArray[this.row-1][this.col]; }
        if (this.row == tileArray.length-1) { sTile = null;
        } else { sTile = tileArray[this.row+1][this.col]; }
        if (this.col == tileArray[0].length-1) { eTile = null;
        } else { eTile = tileArray[this.row][this.col+1]; }
        if (this.col == 0) { wTile = null;
        } else { wTile = tileArray[this.row][this.col-1]; }
        
        this.surrounding = new HashMap<String,Tile>();
        this.surrounding.put("n", nTile);
        this.surrounding.put("s", sTile);
        this.surrounding.put("e", eTile);
        this.surrounding.put("w", wTile);
    }
    
    public void setOccupied(Boolean b) {
        this.occupied = b;
    }

    public HashMap<String, Tile> getSurrounding() {
        return surrounding;
    }
    
    public String getImage() {
        return this.image;
    }
    
    public Tile getTileAtDirection(String d) {
        return this.surrounding.get(d);
    }
    
    public Boolean getOccupied() {
        return this.occupied;
    }
    
    public int getRow() {
        return this.row;
    }
    
    public int getCol() {
        return this.col;
    }
}
