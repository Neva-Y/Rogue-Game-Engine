/**
 * Game-world class for Rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

public class World {

    // Static variables of the initial positions and grid constants along with world map details
    public static final int WORLD_WIDTH = 6;
    public static final int WORLD_HEIGHT = 4;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_Y = 1;
    public static final int MONSTER_X = 4;
    public static final int MONSTER_Y = 2;
    private static Boolean loadedMap = null;

    // Scanner to load map files
    Scanner inputStream = null;

    // Private variables
    private char[][] mapGrid;
    private int worldWidth = 0;
    private int worldHeight = 0;
    private final ArrayList<Entity> entities = new ArrayList<Entity>();

    /*
     *  Getter methods to access private variables from game engine
     */
    public static Boolean getLoadedMap() {
        return loadedMap;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }
    /*
     *  Read map with world name
     */
    public Boolean readMap(String worldName) {

        try {
            File f = new File(worldName + ".dat");

            // Check if the file exists or is not directory, output exception if not
            if(!f.exists() || f.isDirectory()) {
                throw new GameLevelNotFoundException();
            }
            inputStream = new Scanner(new FileInputStream(f));
            String line = null;

            if (inputStream.hasNextLine()) {
                worldWidth = inputStream.nextInt();
                worldHeight = inputStream.nextInt();
                inputStream.nextLine();
            }
            // Read map grid line by line
            mapGrid = new char[worldWidth][worldHeight];
            for (int j=0; j < worldHeight; j++) {
                line = inputStream.nextLine();
                for (int i = 0; i < worldWidth; i++) {
                    mapGrid[i][j] = line.charAt(i);
                }
            }
            // Set map to a loaded type
            loadedMap = true;
        } catch (GameLevelNotFoundException e) {
            String message = e.getMessage();
            System.out.println(message);
            return false;

        } catch (IOException e) {
            System.err.println("An error occurred while loading the file.");
            return false;
        }
        return true;
    }
    /*
     *  Generate a new map
     */
    public void generateNewMap() {
        // Set map to a non-loaded type
        loadedMap = false;

        // Initialise world map to default settings
        worldWidth = WORLD_WIDTH;
        worldHeight = WORLD_HEIGHT;
        mapGrid = new char[WORLD_WIDTH][WORLD_HEIGHT];
        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j<WORLD_HEIGHT; j++) {
                mapGrid[i][j] = Map.GROUND;
            }
        }
    }
    /*
     *  Read entities
     */
    public boolean readEntity(char playerSymbol) {
        String line = null;
        String[] words = null;

        // Remove all existing entities
        clearEntities();
        try {
            while (inputStream.hasNextLine()) {
                line = inputStream.nextLine();
                words = line.split(" ");

                // For each entity read, create the specific entity type and add it to an array list
                if (words[0].equals(PlayerEntity.PLAYER_ENTITY)) {
                    entities.add(new PlayerEntity(Integer.parseInt(words[1]), Integer.parseInt(words[2]), playerSymbol));
                }
                else if (words[0].equals(MonsterEntity.MONSTER_ENTITY)) {
                    entities.add(new MonsterEntity(Integer.parseInt(words[1]), Integer.parseInt(words[2]), words[3],
                            Integer.parseInt(words[4]), Integer.parseInt(words[5])));

                }
                else if (words[0].equals(ItemEntity.ITEM_ENTITY)) {
                    entities.add(new ItemEntity(Integer.parseInt(words[1]), Integer.parseInt(words[2]),
                            words[3].charAt(0)));
                }
                else {
                    throw new UndefinedItemException();
                }
            }
            // Close file
            inputStream.close();

        } catch (UndefinedItemException e) {
            String message = e.getMessage();
            System.out.println(message);
            return false;
        }
        return true;
    }
    /*
     *  Make player and monster entities for default map
     */
    public void makeEntity(String monsterName, int monsterHealth, int monsterDamage, char playerSymbol) {

        // Clear entities and add the monster and player into the entity array list
        clearEntities();
        entities.add(new PlayerEntity(World.PLAYER_X, World.PLAYER_Y, playerSymbol));
        entities.add(new MonsterEntity(World.MONSTER_X, World.MONSTER_Y, monsterName, monsterHealth, monsterDamage));
    }
    /*
     *  Monster movement
     */
    public void monsterMove(Entity playerEntity) {

        // Loop through all monster entities
        for (Entity entity : entities) {
            if (entity.type.equals(MonsterEntity.MONSTER_ENTITY)) {

                // Check if monster is in range of the player
                if (Math.abs(entity.locationY - playerEntity.locationY) <= MonsterEntity.DETECT_RANGE &&
                        Math.abs(entity.locationX - playerEntity.locationX) <= MonsterEntity.DETECT_RANGE) {

                    // Calculate monster position relative to player to determine direction
                    // if yDir>0, monster will move down
                    int yDir = entity.locationY - playerEntity.locationY;
                    // if xDir>0, monster will move left
                    int xDir = entity.locationX - playerEntity.locationX;

                    // West East Preference, move North or South if blocked. Don't move if both directions are blocked
                    if (xDir!=0) {
                        if (xDir > 0 && (mapGrid[entity.locationX-1][entity.locationY] != Map.MOUNTAINOUS &&
                                mapGrid[entity.locationX-1][entity.locationY] != Map.WATER)) {
                            entity.locationX -= 1;
                        } else if (xDir < 0 && (mapGrid[entity.locationX+1][entity.locationY] != Map.MOUNTAINOUS &&
                                mapGrid[entity.locationX+1][entity.locationY] != Map.WATER)) {
                            entity.locationX += 1;
                        } else if (yDir > 0 && (mapGrid[entity.locationX][entity.locationY-1] != Map.MOUNTAINOUS &&
                                mapGrid[entity.locationX][entity.locationY-1] != Map.WATER)) {
                            entity.locationY -=1;
                        } else if (yDir < 0 && (mapGrid[entity.locationX][entity.locationY+1] != Map.MOUNTAINOUS &&
                                mapGrid[entity.locationX][entity.locationY+1] != Map.WATER)) {
                            entity.locationY +=1;
                        }
                    }

                    // North South movement, don't move if direction is blocked
                    if (xDir==0) {
                        if (yDir > 0 && (mapGrid[entity.locationX][entity.locationY-1] != Map.MOUNTAINOUS &&
                                mapGrid[entity.locationX][entity.locationY-1] != Map.WATER)) {
                            entity.locationY -= 1;
                        }
                        else if (yDir < 0 && (mapGrid[entity.locationX][entity.locationY+1] != Map.MOUNTAINOUS &&
                                mapGrid[entity.locationX][entity.locationY+1] != Map.WATER)) {
                            entity.locationY += 1;
                        }
                    }
                }
            }
        }
    }
    /*
     *  Player movement
     */
    public void playerMove(String direction) {

        Entity playerEntity = getPlayerEntity();

        // Allow monster to move before player since monster movement is based on pre-player move
        // Monster only moves for loaded maps, stationary for generated maps
        if (loadedMap) {
            monsterMove(playerEntity);
        }

        // Move player for all four directions, ensure there are no obstacles or exceeding map bounds
        // Invalid direction makes the player stay still
        if (direction.equals(PlayerEntity.NORTH)) {
            if (playerEntity.locationY-1 >=0 && (mapGrid[playerEntity.locationX][playerEntity.locationY-1] !=
                    Map.MOUNTAINOUS && mapGrid[playerEntity.locationX][playerEntity.locationY-1] != Map.WATER)) {
                playerEntity.locationY -= 1;
            }
        }
        else if (direction.equals(PlayerEntity.SOUTH)) {
            if (playerEntity.locationY+1 < worldHeight && (mapGrid[playerEntity.locationX][playerEntity.locationY+1] !=
                    Map.MOUNTAINOUS && mapGrid[playerEntity.locationX][playerEntity.locationY+1] != Map.WATER)) {
                playerEntity.locationY += 1;
            }
        }
        else if (direction.equals(PlayerEntity.EAST)) {
            if (playerEntity.locationX+1 < worldWidth && (mapGrid[playerEntity.locationX+1][playerEntity.locationY] !=
                    Map.MOUNTAINOUS && mapGrid[playerEntity.locationX + 1][playerEntity.locationY] != Map.WATER)) {
                playerEntity.locationX += 1;
            }
        }
        else if (direction.equals(PlayerEntity.WEST)) {
            if (playerEntity.locationX-1 >= 0 && (mapGrid[playerEntity.locationX-1][playerEntity.locationY] !=
                    Map.MOUNTAINOUS && mapGrid[playerEntity.locationX-1][playerEntity.locationY] != Map.WATER)) {
                playerEntity.locationX -= 1;
            }
        }
    }
    /*
     *  Check if player has encountered a monster, return the monster entity or null
     */
    public MonsterEntity checkMonsterEncounter() {
        Entity playerEntity = getPlayerEntity();
        for (Entity entity : entities) {
            if (entity.type.equals(MonsterEntity.MONSTER_ENTITY)) {
                if (entity.locationX == playerEntity.locationX && entity.locationY == playerEntity.locationY) {
                    return (MonsterEntity) entity;
                }
            }
        }
        return null;
    }
    /*
     *  Get the player entity
     */
    public Entity getPlayerEntity() {
        try {
            for (Entity entity : entities) {
                if (entity.type.equals(PlayerEntity.PLAYER_ENTITY)) {
                    return entity;
                }
            }
            throw new NoPlayerDefinedException();
        } catch(NoPlayerDefinedException e) {
            String message = e.getMessage();
            System.out.println(message);
            System.exit(1);
        }
        return null;
    }
    /*
     *  Render the world based on the map and entities
     */
    public void renderMap(int worldWidth, int worldHeight) {

        // Make a duplicate of the map grid to add entities for render
        char [][] worldGrid = deepCopy(mapGrid);

        // Priority to render entities added first so input entities in world from last to first
        for (int i = entities.size() - 1 ; i >= 0 ; i--) {
            worldGrid[entities.get(i).locationX][entities.get(i).locationY] = entities.get(i).symbol;
        }

        // Render the map
        for (int j = 0; j<worldHeight; j++) {
            for (int i = 0; i<worldWidth; i++) {
                System.out.print(worldGrid[i][j]);
            }
            System.out.println("");
        }
        System.out.println();
        System.out.print("> ");
    }
    /*
     *  Make a duplicate map (deep copy) to insert entities for render without affecting base map.
     */
    public static char[][] deepCopy(char[][] original) {
        if (original == null) {
            return null;
        }
        // Initialise memory for the new char array
        final char[][] result = new char[original.length][];

        // Copy the original array into a new array (not pointer reference)
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
    /*
     *  Remove a monster from the entity arraylist
     */
    public void removeMonster(Entity monster) {
        entities.remove(monster);
    }
    /*
     *  Check if the player encounters an item
     */
    public char checkItemEncounter() {
        Entity playerEntity = getPlayerEntity();
        for (Entity entity : entities) {
            if (entity.type.equals(ItemEntity.ITEM_ENTITY)) {
                if (entity.locationX == playerEntity.locationX && entity.locationY == playerEntity.locationY) {
                    entities.remove(entity);
                    return entity.symbol;
                }
            }
        }
        return ItemEntity.INVALID_ITEM;
    }
    /*
     *  Clear all entities in the arraylist
     */
    public void clearEntities() {
        entities.clear();
    }
}
