/**
 * Inherited item entity class for rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class ItemEntity extends Entity{

    // Specific constants for item entity (items and type)
    public static final String ITEM_ENTITY = "item";
    public static final char WARP_STONE = '@';
    public static final char DAMAGE_PERK = '^';
    public static final char HEALING_SALVE = '+';
    public static final char INVALID_ITEM = '\0';
    /*
     *  Constructor
     */
    public ItemEntity(int locationX, int locationY, char symbol) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.symbol = symbol;
        this.type = ITEM_ENTITY;
    }
}
