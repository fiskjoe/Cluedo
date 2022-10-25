package cluedev;

import java.util.HashMap;
import java.util.Map;

public class InvalidTile extends Tile {
    public InvalidTile(int r, int c) {
        this.row = r;
        this.col = c;
        this.image = "/images/background.png";
    }
    
}
