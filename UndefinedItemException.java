/**
 * Undefined item exception for rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
public class UndefinedItemException extends Exception {
    public UndefinedItemException() {
        super("This Entity does not exist.");
    }
}