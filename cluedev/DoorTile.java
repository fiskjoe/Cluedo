package cluedev;

public class DoorTile extends Tile {
    char roomID;
    Room room;
    
    public DoorTile(int r, int c, char roomID, Room room) {
        this.row = r;
        this.col = c;
        this.roomID = roomID;
        this.room = room;
        this.image = "/images/door.png";
    }
    
    public Room getRoom() {
        return this.room;
    }
    
    public char getRoomID() {
        return this.roomID;
    }
}
