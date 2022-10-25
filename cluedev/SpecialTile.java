/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cluedev;

/**
 *
 * @author bobja
 */
public class SpecialTile extends Tile {
    
    String specialType;
    
    /**
     * Constructor for special tiles, behaviour is handled within player methods
     *
     * @param r row of tile
     * @param c column of tile
     * @param specialType type of special tile: s/r for suggest again/roll again
     */
    public SpecialTile(int r, int c, String specialType) {
        this.row = r;
        this.col = c;
        this.specialType = specialType;
        this.image = "/images/" + specialType + "special_tile.png";
    }
    
    public String getSpecialType() {
        return specialType;
    }

    public void setSpecialType(String specialType) {
        this.specialType = specialType;
    }
    
    
}
