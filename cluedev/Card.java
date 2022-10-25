package cluedev;

public class Card {

    String name;
    String type;
    String image;

    /**
     * Class to hold cards for suggestions/accusations/detective cards, image
     * path is set dependant on name
     *
     * @param name Name of the card relates to what card is representing
     * @param type Type of card will be suspect, room or weapon name
     */
    public Card(String name, String type) {
        this.name = name;
        this.type = type;
        this.image = "/images/cards/" + this.name.toLowerCase() + " card.png";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return this.image;
    }

    public void setType(String type) {
        this.type = type;
    }

}
