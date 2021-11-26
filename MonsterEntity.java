/**
 * Inherited monster entity class for rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class MonsterEntity extends Entity {

    // Specific constants for monster entity (detection range and type)
    public static final String MONSTER_ENTITY = "monster";
    public static final int DETECT_RANGE = 2;

    // Specific instance variables for monster entity
    private final String name;
    private final int health;
    private final int damage;
    public boolean fought = false;

    /*
     *  Constructor
     */
    public MonsterEntity(int locationX, int locationY, String name, int health, int damage) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.name = name;
        this.symbol = name.toLowerCase().charAt(0);
        this.health = health;
        this.damage = damage;
        this.type = MONSTER_ENTITY;
    }
    /*
     *  Getters
     */
    public String getName() {
        return name;
    }
    public int getHealth() {
        return health;
    }
    public int getDamage() {
        return damage;
    }
}
