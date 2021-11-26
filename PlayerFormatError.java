/**
 * Player format error exception for rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class PlayerFormatError extends Exception{
    public PlayerFormatError() {
        super("The save file format is incompatible");
    }
}
