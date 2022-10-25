package cluedev;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    int bWidth = 26;
    int bHeight = 27;
    String boardLayout[][];
    Tile tileArray[][] = new Tile[bHeight][bWidth];
    Tile[] playerStarts = new Tile[6];
    Tile[] weaponStarts = new Tile[6];
    HashMap<Character, String> roomNames;

    /**
     * Calls generate board to create tileArray, assigns starting squares for players and weapons
     * 
     */
    public Board() {
        this.tileArray = generateBoard();

        weaponStarts[0] = tileArray[3][3];
        weaponStarts[1] = tileArray[21][3];
        weaponStarts[2] = tileArray[9][4];
        weaponStarts[3] = tileArray[15][22];
        weaponStarts[4] = tileArray[3][22];
        weaponStarts[5] = tileArray[22][23];

        playerStarts[0] = tileArray[1][17];
        playerStarts[1] = tileArray[6][1];
        playerStarts[2] = tileArray[19][1];
        playerStarts[3] = tileArray[25][10];
        playerStarts[4] = tileArray[25][15];
        playerStarts[5] = tileArray[8][24];
    }

    /**
     * Takes in a string representing the board layout will create tile arrays and link all rooms to their respective tiles
     *
     * @return A tile array which will be used for representing the board and for checking all movement logic
     */
    public Tile[][] generateBoard() {

        this.boardLayout = new String[][]{
            {"b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b"},
            {"b", "r1", "r1", "r1", "r1", "r1", "r1", "b", "t", "b", "r2", "r2", "r2", "r2", "r2", "r2", "b", "t", "b", "r3", "r3", "r3", "r3", "r3", "r3", "b"},
            {"b", "r1", "r1", "r1", "r1", "r1", "r1", "r1", "t", "t", "r2", "r2", "r2", "r2", "r2", "r2", "t", "t", "r3", "r3", "r3", "r3", "r3", "r3", "r3", "b"},
            {"b", "r1", "r1", "r1", "r1", "r1", "r1", "r1", "t", "a", "r2", "r2", "r2", "r2", "r2", "r2", "t", "t", "r3", "r3", "r3", "r3", "r3", "r3", "r3", "b"},
            {"b", "r1", "r1", "r1", "r1", "r1", "r1", "d1", "t", "t", "r2", "r2", "r2", "r2", "r2", "r2", "t", "t", "r3", "r3", "r3", "r3", "r3", "r3", "r3", "b"},
            {"b", "b", "t", "t", "t", "t", "t", "t", "t", "t", "d2", "r2", "r2", "r2", "r2", "r2", "t", "t", "r3", "r3", "r3", "r3", "r3", "r3", "r3", "b"},
            {"b", "t", "t", "t", "t", "t", "t", "t", "t", "t", "r2", "r2", "r2", "r2", "r2", "r2", "t", "t", "d3", "r3", "r3", "r3", "r3", "r3", "r3", "b"},
            {"b", "b", "r4", "r4", "r4", "r4", "r4", "t", "t", "t", "r2", "r2", "d2", "d2", "r2", "r2", "t", "t", "s", "t", "t", "t", "t", "t", "b", "b"},
            {"b", "r4", "r4", "r4", "r4", "r4", "r4", "r4", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "b"},
            {"b", "r4", "r4", "r4", "r4", "r4", "r4", "d4", "t", "t", "r0", "r0", "r0", "r0", "r0", "t", "t", "t", "t", "t", "t", "t", "t", "t", "b", "b"},
            {"b", "r4", "r4", "r4", "r4", "r4", "r4", "r4", "t", "t", "r0", "r0", "r0", "r0", "r0", "t", "t", "r5", "d5", "r5", "r5", "r5", "r5", "r5", "r5", "b"},
            {"b", "b", "r4", "r4", "d4", "r4", "r4", "t", "t", "t", "r0", "r0", "r0", "r0", "r0", "t", "t", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "b"},
            {"b", "b", "t", "s", "t", "t", "t", "t", "t", "t", "r0", "r0", "r0", "r0", "r0", "t", "t", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "b"},
            {"b", "r6", "d6", "r6", "r6", "r6", "r6", "t", "t", "t", "r0", "r0", "r0", "r0", "r0", "t", "t", "d5", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "b"},
            {"b", "r6", "r6", "r6", "r6", "r6", "r6", "t", "t", "t", "r0", "r0", "r0", "r0", "r0", "t", "t", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "b"},
            {"b", "r6", "r6", "r6", "r6", "r6", "r6", "t", "t", "t", "r0", "r0", "r0", "r0", "r0", "t", "t", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "r5", "b"},
            {"b", "r6", "r6", "r6", "r6", "r6", "d6", "t", "t", "t", "t", "t", "t", "t", "s", "t", "t", "t", "t", "t", "r5", "r5", "r5", "r5", "r5", "b"},
            {"b", "r6", "r6", "r6", "r6", "r6", "r6", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "t", "b", "b"},
            {"b", "b", "t", "t", "t", "t", "t", "t", "t", "r8", "d8", "r8", "r8", "r8", "r8", "d8", "r8", "t", "t", "t", "t", "t", "t", "a", "t", "b"},
            {"b", "t", "t", "t", "t", "t", "t", "t", "a", "r8", "r8", "r8", "r8", "r8", "r8", "r8", "r8", "t", "t", "r9", "d9", "r9", "r9", "r9", "b", "b"},
            {"b", "b", "r7", "r7", "r7", "d7", "t", "t", "t", "d8", "r8", "r8", "r8", "r8", "r8", "r8", "d8", "t", "t", "r9", "r9", "r9", "r9", "r9", "r9", "b"},
            {"b", "r7", "r7", "r7", "r7", "r7", "r7", "t", "t", "r8", "r8", "r8", "r8", "r8", "r8", "r8", "r8", "t", "t", "r9", "r9", "r9", "r9", "r9", "r9", "b"},
            {"b", "r7", "r7", "r7", "r7", "r7", "r7", "t", "t", "r8", "r8", "r8", "r8", "r8", "r8", "r8", "r8", "t", "t", "r9++", "r9", "r9", "r9", "r9", "r9", "b"},
            {"b", "r7", "r7", "r7", "r7", "r7", "r7", "t", "t", "r8", "r8", "r8", "r8", "r8", "r8", "r8", "r8", "t", "t", "r9", "r9", "r9", "r9", "r9", "r9", "b"},
            {"b", "r7", "r7", "r7", "r7", "r7", "r7", "b", "t", "t", "t", "r8", "r8", "r8", "r8", "t", "t", "t", "b", "r9", "r9", "r9", "r9", "r9", "r9", "b"},
            {"b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "t", "b", "b", "b", "b", "t", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b"},
            {"b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b", "b"}
        };

        HashMap<Character, String> roomNames = new HashMap<>();

        roomNames.put('0', "Centre");
        roomNames.put('1', "Study");
        roomNames.put('2', "Hall");
        roomNames.put('3', "Lounge");
        roomNames.put('4', "Library");
        roomNames.put('5', "Dining Room");
        roomNames.put('6', "Billiard Room");
        roomNames.put('7', "Conservatory");
        roomNames.put('8', "Ball Room");
        roomNames.put('9', "Kitchen");

        Room[] rooms = new Room[roomNames.size()];
        for (int n = 0; n < roomNames.size(); n++) {
            rooms[n] = new Room();
        }

        for (int row = 0; row < this.boardLayout.length; row++) {
            for (int col = 0; col < this.boardLayout[0].length; col++) {
                String currentTile = boardLayout[row][col];
                int tempRoomID;
                switch (currentTile.charAt(0)) {
                    case 'b':
                        this.tileArray[row][col] = new InvalidTile(row, col);
                        break;
                    case 's':
                        this.tileArray[row][col] = new SpecialTile(row, col, "s");
                        break;
                    case 'a':
                        this.tileArray[row][col] = new SpecialTile(row, col, "r");
                        break;
                    case 't':
                        this.tileArray[row][col] = new BlankTile(row, col);
                        break;
                    case 'd':
                        tempRoomID = currentTile.charAt(1) - '0';
                        this.tileArray[row][col] = new DoorTile(row, col, currentTile.charAt(1), rooms[tempRoomID]);
                        rooms[tempRoomID].addExitTile(this.tileArray[row][col]);
                        break;
                    case 'r':
                        tempRoomID = currentTile.charAt(1) - '0';
                        this.tileArray[row][col] = new RoomTile(row, col, currentTile.charAt(1), rooms[tempRoomID]);
                        rooms[tempRoomID].addRoomTile(this.tileArray[row][col]);
                        break;
                }
            }
        }

        for (int row = 0; row < this.tileArray.length; row++) {
            for (int col = 0; col < this.tileArray[0].length; col++) {
                this.tileArray[row][col].generateSurrounding(this.tileArray);
            }
        }

        this.roomNames = roomNames;

        return this.tileArray;
    }

    public Tile[] getPlayerStart() {
        return this.playerStarts;
    }

    public Tile[] getWeaponStart() {
        return this.weaponStarts;
    }

    public Tile[][] getTileArray() {
        return this.tileArray;
    }

    public int getWidth() {
        return this.bWidth;
    }

    public int getHeight() {
        return this.bHeight;
    }

    public void setOccupied(int r, int c, Boolean b) {
        this.tileArray[r][c].setOccupied(b);
    }

    public void setOccupied(Tile t, Boolean b) {
        this.tileArray[t.getRow()][t.getCol()].setOccupied(b);
    }

}
