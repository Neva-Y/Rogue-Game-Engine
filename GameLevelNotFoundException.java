/**
 * Game level not found exception for rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class GameLevelNotFoundException extends Exception {
    public GameLevelNotFoundException() {
        super("Map not found.");
    }
}
