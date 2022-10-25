package cluedev;


public class Weapon {
    String image;
    String name;
    Tile location;
    
    
    public Weapon(String name) {
        this.name = name;
        this.image = "/images/weapons/"+name+".png";
    }
    
    public String getImage() {
        return this.image;
    }
    
    public void setLocation(Tile t) {
        this.location = t;
        t.setOccupied(true);
    }
    
    public Tile getLocation() {
        return this.location;
    }
    
    public String getName() {
        return this.name;
    }
}
