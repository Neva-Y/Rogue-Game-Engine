/**
 * Item functionality class for Rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class Item {

    // Private constants for item boost values
    private static final int DAMAGE_UP = 1;
    private static final int LEVEL_UP = 1;


    // Item functionality on player
    public void healingItem(Player player) {
        player.healPlayer();
        System.out.println("Healed!");
    }
    public void damageItem(Player player) {
        player.setDamage(player.getDamage() + DAMAGE_UP);
        System.out.println("Attack up!");
    }
    public void warpStone(Player player) {
        player.setLevel(player.getLevel() + LEVEL_UP);
        player.calculatePlayerStats();
        System.out.println("World complete! (You leveled up!)");
    }
}
