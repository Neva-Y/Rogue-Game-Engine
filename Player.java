/**
 * Player class for Rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class Player {

    // Public static constant for default player name
    public static final String PLAYER_DEFAULT_NAME = "Bilbo";
    public static final String PLAYER_SAVE_FILE = "player.dat";

    // Private player constants to initialise a player
    private static final int STARTING_LEVEL = 1;
    private static final int HP_BASE = 17;
    private static final int HP_MULTIPLIER = 3;
    private static final int DAMAGE_OFFSET = 1;

    // Public static variable to check if player is defined from the game engine and loading save file.
    public static boolean playerDefined = false;
    public static final int LOADING_FIELDS = 2;

    // Player instance variables
    private String name;
    private int currentHealth;
    private int maxHealth;
    private int damage;
    private int level;
    /*
     *  Constructor method
     */
    public Player(String name) {
        setName(name);
        setLevel(STARTING_LEVEL);
        calculatePlayerStats();
        healPlayer();
    }
    /*
     *  Calculate the player stats depending on the current player level
     */
    public void calculatePlayerStats() {
        damage = DAMAGE_OFFSET + level;
        maxHealth = HP_BASE + (level*HP_MULTIPLIER);
    }
    /*
     *  Getter methods
     */
    public String getName() {
        return name;
    }
    public int getDamage() {
        return damage;
    }
    public int getCurrentHealth() {
        return currentHealth;
    }
    public int getLevel() {
        return level;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    /*
     *  Setter methods
     */
    public void setName(String name) {
        this.name = name;
    }
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    /*
     *  Method to print player stats
     */
    public void printDescription() {
        System.out.printf("%s (Lv. %d)\n", name, level);
        System.out.println("Damage: "+ damage);
        System.out.printf("Health: %d/%d\n", currentHealth, maxHealth);
    }
    /*
     *  Method to heal player to max health
     */
    public void healPlayer() {
        currentHealth = maxHealth;
    }
}
