package cluedev;

public class RoomTile extends Tile {
    char roomID;
    Room room;
    
    public RoomTile(int r, int c, char roomID, Room room) {
        this.row = r;
        this.col = c;
        this.roomID = roomID;
        this.room = room;
        this.image = "/images/room.png";
    }
    
    public Room getRoom() {
        return this.room;
    }
}
