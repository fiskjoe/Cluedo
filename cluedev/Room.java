package cluedev;

import java.util.Random;
import java.util.ArrayList;

public class Room {

    ArrayList<Tile> roomTileArray = new ArrayList<Tile>();
    ArrayList<Tile> exitTileArray = new ArrayList<Tile>();

    public Room() {
    }

    public void addRoomTile(Tile t) {
        this.roomTileArray.add(t);
    }

    public void addExitTile(Tile t) {
        this.exitTileArray.add(t);
    }

    public ArrayList<Tile> getExitTileArray() {
        return this.exitTileArray;
    }

    public ArrayList getTileArray() {
        return this.roomTileArray;
    }
    
     /**
     * Gets an unoccupied tile from a room, will prefer to go for a tile in the centre of the rooms
     *
     * @return an unoccupied tile in the rooms
     */
    public Tile getRandomTile() {
        boolean ifCentre = false;
        for (Tile t : this.roomTileArray) {
            if (t.getTileAtDirection("n") instanceof RoomTile
                    && t.getTileAtDirection("s") instanceof RoomTile
                    && t.getTileAtDirection("e") instanceof RoomTile
                    && t.getTileAtDirection("w") instanceof RoomTile) {
                ifCentre = true;
            }
                
            if (!t.getOccupied()) {
                if (ifCentre) {
                    return t;
                } 
            }
        }
        return null;
    }

}
