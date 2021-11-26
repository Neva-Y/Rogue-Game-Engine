/**
 * Monster class for Rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class Monster {

    // Public static constants for default monster
    public static final String MONSTER_DEFAULT_NAME = "Slime";
    public static final int MONSTER_DEFAULT_HEALTH = 10;
    public static final int MONSTER_DEFAULT_DAMAGE = 1;

    // Public static variable for game engine to check if player is defined
    public static boolean monsterDefined = false;

    // Monster instance variables
    private String name;
    private int currentHealth;
    private int maxHealth;
    private int damage;
    /*
     *  Constructor
     */
    public Monster(String name, int health, int damage) {
        setName(name);
        setMaxHealth(health);
        setCurrentHealth(health);
        setDamage(damage);
    }
    /*
     *  Overloaded constructor created with a monster entity
     */
    public Monster(MonsterEntity monsterEntity) {
        setName(monsterEntity.getName());
        setMaxHealth(monsterEntity.getHealth());
        setCurrentHealth(monsterEntity.getHealth());
        setDamage(monsterEntity.getDamage());
    }
    /*
     *  Getter methods
     */
    public String getName() {
        return name;
    }
    public int getCurrentHealth() {
        return currentHealth;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getDamage() {
        return damage;
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
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
}

