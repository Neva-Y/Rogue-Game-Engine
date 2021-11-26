/**
 * Inherited player entity class for rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class PlayerEntity extends Entity {

    // Specific constants for player entity (movement and type)
    public static final String PLAYER_ENTITY = "player";
    public static final String NORTH = "w";
    public static final String EAST = "d";
    public static final String SOUTH = "s";
    public static final String WEST = "a";
    /*
     *  Constructor
     */
    public PlayerEntity(int locationX, int locationY, char symbol) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.symbol = symbol;
        this.type = PLAYER_ENTITY;
    }
}
