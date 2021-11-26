/**
 * Main game engine to run the game Rogue
 * @author
 * Name: Xing Yang Goh
 * Email: xingyangg@student.unimelb.edu.au
 * ID: 1001969
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.Scanner;

public class GameEngine {

	// Instance variables for the player, monster and input (scanner) objects.
	public Player player;
	Monster monster;
	Scanner input = new Scanner(System.in);
	World world = new World();
	Item item = new Item();

	public static void main(String[] args) {

		// Creates an instance of the game engine.
		GameEngine gameEngine = new GameEngine();
		// Runs the main game loop.
		gameEngine.runGameLoop();
	}
	/*
	 *  Logic for running the main game loop.
	 */
	private void runGameLoop() {

		// Print out the title text.
		displayTitleText();
		// Display main menu screen and instructions
		displayHealthStatus();
		displayInstructions();
	}
	/*
	 *  Displays the title text.
	 */
	private void displayTitleText() {

		String titleText = " ____                        \n" +
				"|  _ \\ ___   __ _ _   _  ___ \n" +
				"| |_) / _ \\ / _` | | | |/ _ \\\n" +
				"|  _ < (_) | (_| | |_| |  __/\n" +
				"|_| \\_\\___/ \\__, |\\__,_|\\___|\n" +
				"COMP90041   |___/ Assignment ";

		System.out.println(titleText);
		System.out.println();
	}
	/*
	 *  Displays the health status of the player and monster
	 */
	private void displayHealthStatus() {
		if (!Player.playerDefined && !Monster.monsterDefined) {
			System.out.println("Player: [None]  | Monster: [None]");
		}
		else if (!Monster.monsterDefined) {
			System.out.printf("Player: %s %d/%d  | Monster: [None]\n", player.getName(), player.getCurrentHealth(),
					player.getMaxHealth());
		}
		else if (!Player.playerDefined) {
			System.out.printf("Player: [None]  | Monster: %s %d/%d\n", monster.getName(), monster.getCurrentHealth(),
					monster.getMaxHealth());
		}
		else {
			System.out.printf("Player: %s %d/%d  | Monster: %s %d/%d\n", player.getName(), player.getCurrentHealth(),
					player.getMaxHealth(), monster.getName(), monster.getCurrentHealth(), monster.getMaxHealth());
		}
		System.out.println();
	}
	/*
	 *  Initialise the command the user inputs
	 */
	private String initialiseCommand() {
		String command ="exit";
		if (input.hasNextLine()) {
			command = input.nextLine();
		}
		return command;
	}
	/*
	 *  Display the main menu instructions
	 */
	private void displayInstructions() {
		System.out.println("Please enter a command to continue.");
		System.out.println("Type 'help' to learn how to get started.");
		System.out.println();
		System.out.print("> ");
		String command = initialiseCommand();
		executeCommand(command);
	}
	/*
	 *  Method to return to the main menu after an 'enter' input
	 */
	private void returnMenu() {
		System.out.println();
		System.out.println("(Press enter key to return to main menu)");
		input.nextLine();
		runGameLoop();
	}
	/*
	 *  Await additional command
	 */
	private void nextCommand() {
		System.out.println();
		System.out.print("> ");
		String nextCommand = initialiseCommand();
		executeCommand(nextCommand);
	}
	/*
	 *  Execute the given commands
	 */
	private void executeCommand(String command) {

		// Basic instructions on how to run the game
		if (command.equals("help")) {
			System.out.println("Type 'commands' to list all available commands");
			System.out.println("Type 'start' to start a new game");
			System.out.println("Create a character, battle monsters, and find treasure!");
			nextCommand();
		}
		// Lists out all available commands
		else if (command.equals("commands")) {
			System.out.print("help\nplayer\nmonster\nstart\nexit\n");
			nextCommand();
		}
		// Player creation
		else if (command.equals("player")) {
			playerCreator();
		}
		// Monster creation
		else if (command.equals("monster")) {
			monsterCreator();
		}
		// Initiate start game
		else if (command.equals("start")) {
			initiateStart();
		}
		// Exit the game with status 0
		else if (command.equals("exit")) {
			System.out.println("Thank you for playing Rogue!");
			System.exit(0);
		}
		// Load game from world file
		else if (command.matches("start+( +\\w+)+$")) {
			initiateLoad(command);
		}
		// Load player data
		else if (command.equals("load")) {
			loadPlayer();
		}
		// Save player data
		else if (command.equals("save")){
			savePlayer();
		}
		// Unrecognised input, await the next command
		else {
			String nextCommand = initialiseCommand();
			executeCommand(nextCommand);
		}
	}
	/*
	 *  Start game if there is a player and a monster, else provide instructions on initialisation
	 */
	private void initiateStart() {
		if (Player.playerDefined && Monster.monsterDefined) {
			player.healPlayer();
			monster.setCurrentHealth(monster.getMaxHealth());
			initiateNewWorld();
			startGame();
		}
		else if (!Player.playerDefined) {
			System.out.println("No player found, please create a player with 'player' first.");
			returnMenu();
		}
		else {
			System.out.println("No monster found, please create a monster with 'monster' first.");
			returnMenu();
		}
	}
	/*
	 *  Create a new player if uninitialised (Bilbo if input is unrecognised), else show current player stats
	 */
	private void playerCreator() {

		// Check if player has been created, print description if the player has already been created.
		if (Player.playerDefined) {
			player.printDescription();
		}
		// Create a new player
		else {
			System.out.println("What is your character's name?");
			String playerName = Player.PLAYER_DEFAULT_NAME;

			// Name of the player, initialise to default name if input is unrecognised
			if (input.hasNextLine()) {
				playerName = input.nextLine();
				if (playerName.isEmpty()) {
					playerName = Player.PLAYER_DEFAULT_NAME;
				}
			}

			// Create the player and set the player public static playerDefined to true
			player = new Player(playerName);
			Player.playerDefined = true;
			System.out.printf("Player '%s' created.\n", player.getName());
		}
		returnMenu();
	}
	/*
	 *  Create a new monster, initialise default monster with unrecognised inputs
	 */
	private void monsterCreator() {

		// Name of the monster, initialise to default name if input is unrecognised
		System.out.print("Monster name: ");
		String monsterName = Monster.MONSTER_DEFAULT_NAME;
		if (input.hasNextLine()) {
			monsterName = input.nextLine();
			if (monsterName.isEmpty()) {
				monsterName = Monster.MONSTER_DEFAULT_NAME;
			}
		}

		// Health stat of the monster, initialise to default value if health is 0 or under
		System.out.print("Monster health: ");
		int monsterHealth = Monster.MONSTER_DEFAULT_HEALTH;
		if (input.hasNextInt()) {
			monsterHealth = input.nextInt();
			if (monsterHealth<=0) {
				monsterHealth = Monster.MONSTER_DEFAULT_HEALTH;
			}
		}
		// Remove the new line character (\n) after taking the integer
		if (input.hasNextLine()) {
			input.nextLine();
		}

		// Damage stat of the monster, initialise to default value if damage is 0 or under
		System.out.print("Monster damage: ");
		int monsterDamage = Monster.MONSTER_DEFAULT_DAMAGE;
		if (input.hasNextInt()) {
			monsterDamage = input.nextInt();
			if (monsterDamage<=0) {
				monsterDamage = Monster.MONSTER_DEFAULT_DAMAGE;
			}
		}
		// Remove the \n after taking the integer
		if (input.hasNextLine()) {
			input.nextLine();
		}

		// Create the monster and set the Monster public static monsterDefined to true
		monster = new Monster(monsterName, monsterHealth, monsterDamage);
		System.out.printf("Monster '%s' created.\n", monster.getName());
		Monster.monsterDefined = true;
		returnMenu();
	}
	/*
	 *  Start the Game World
	 */
	private void startGame() {

		// Check if there is an encounter with a monster, removing it if the monster had been fought, else fight it
		MonsterEntity monsterEntity = world.checkMonsterEncounter();
		while (monsterEntity!=null) {
			if (monsterEntity.fought) {
				world.removeMonster(monsterEntity);
			}
			else {
				monster = new Monster(monsterEntity);
				monsterEntity.fought = true;
				beginBattle();
			}
			monsterEntity = world.checkMonsterEncounter();
		}

		// Render the world map
		world.renderMap(world.getWorldWidth(), world.getWorldHeight());

		// Player remains in the same position if they attempt to move out of map bound, 'home' returns to main menu
		while (true) {

			// Initialise direction as 'home' if there is no direction input
			String direction = "home";
			if (input.hasNextLine()) {
				direction = input.nextLine();
			}
			if (direction.equals("home")) {
				System.out.println("Returning home...");
				returnMenu();
			}
			// Directional inputs to move the player around
			else {
				world.playerMove(direction);
			}
			// Check for an encounter after player moves, fight the monster if there is an encounter
			monsterEntity = world.checkMonsterEncounter();
			if (monsterEntity!=null) {
				monster = new Monster(monsterEntity);
				monsterEntity.fought = true;
				break;
			}
			// Check if the player has acquired any items
			itemInteraction();
			world.renderMap(world.getWorldWidth(), world.getWorldHeight());
		}
		// Begin the battle loop of the game if the player encounters the monster on the map
		beginBattle();
	}
	/*
	 *  Begin the battle loop after encounter
	 */
	private void beginBattle() {

		// Initial encounter dialog
		System.out.printf("%s encountered a %s!\n", player.getName(), monster.getName());
		System.out.println();

		// Turn occurs only if the player and monster are still alive (current health greater than 0)
		while (player.getCurrentHealth()> 0 && monster.getCurrentHealth()>0) {

			// Print current health of the monster and player before the turn
			System.out.printf("%s %d/%d | %s %d/%d\n", player.getName(), player.getCurrentHealth(),
					player.getMaxHealth(), monster.getName(), monster.getCurrentHealth(), monster.getMaxHealth());

			// Player can always attack first, reduce monster's health by player damage
			System.out.printf("%s attacks %s for %d damage.\n", player.getName(), monster.getName(),
					player.getDamage());
			monster.setCurrentHealth(monster.getCurrentHealth()- player.getDamage());

			// If monster is not alive, monster cannot attack and player wins the battle
			if (monster.getCurrentHealth()<=0) {
				System.out.printf("%s wins!\n", player.getName());

				// If the map is not loaded, return to menu after the battle is finished
				if (!World.getLoadedMap()) {
					returnMenu();
				}
				// Return to the world if the map is loaded
				System.out.println();
				startGame();
			}

			// Monster is still alive and attacks the player, reduce player's health by monster damage
			System.out.printf("%s attacks %s for %d damage.\n", monster.getName(), player.getName(),
					monster.getDamage());
			player.setCurrentHealth(player.getCurrentHealth()- monster.getDamage());

			// If player is not alive, monster wins the battle
			if (player.getCurrentHealth()<=0) {
				System.out.printf("%s wins!\n", monster.getName());
				returnMenu();
			}
			System.out.println();
		}
	}
	/*
	 *  Save player data
	 */
	public void savePlayer() {
		try {
			// Check if player has been defined
			if (!Player.playerDefined) {
				throw new NoPlayerDefinedException();
			}
			// Opening or creating the player.dat file, then saving to it (name and level). Close the file after.
			PrintWriter outputStream = null;
			outputStream = new PrintWriter(new FileOutputStream(Player.PLAYER_SAVE_FILE));
			outputStream.printf("%s %d", player.getName(), player.getLevel());
			outputStream.close();
			System.out.println("Player data saved.");

		} catch(FileNotFoundException e) {
			// Unable to save to the player.dat file.
			System.err.print  ("File " + Player.PLAYER_SAVE_FILE + " was not found ");
			System.err.println("or could not be opened.");

		} catch(NoPlayerDefinedException e) {
			// No player has been defined
			String message = e.getMessage();
			System.out.println(message);
		}
		nextCommand();
	}
	/*
	 *  Load player data
	 */
	private void loadPlayer() {
		try {
			BufferedReader inputStream = new BufferedReader(new FileReader(Player.PLAYER_SAVE_FILE));
			String line = inputStream.readLine();

			// Split into name and level
			String[] part = line.split("(?<=\\D)(?=\\d)");

			// Check format compatibility
			if (part.length != Player.LOADING_FIELDS || !isInteger(part[1])) {
				throw new PlayerFormatError();
			}

			// Load player then close the file
			player = new Player(part[0]);
			player.setLevel(Integer.parseInt(part[1]));
			player.calculatePlayerStats();
			player.healPlayer();
			Player.playerDefined = true;
			inputStream.close();
			System.out.println("Player data loaded.");

			// Catch errors in loading player file
		} catch(FileNotFoundException e) {
			System.err.print  ("File " + Player.PLAYER_SAVE_FILE + " was not found ");
			System.err.println("or could not be opened.");
		} catch (PlayerFormatError e) {
			String message = e.getMessage();
			System.out.println(message);
		} catch(NumberFormatException e) {
			System.err.println("File " + Player.PLAYER_SAVE_FILE + " has incompatible format");
		} catch (IOException e) {
			System.err.println("Error reading from " + Player.PLAYER_SAVE_FILE + ".");
		}
		nextCommand();
	}
	/*
	 *  Helper function to determine if string input is an integer
	 */
	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	/*
	 *  Load the map data
	 */
	public void initiateLoad(String command) {

		// Regex to separate out the map name
		String mapName = command.replaceFirst("^start ", "");
		// Ensure player is defined before loading map
		if (Player.playerDefined) {
			player.healPlayer();
			// Read map and entity data
			if (!world.readMap(mapName) || !world.readEntity(player.getName().toUpperCase().charAt(0))) {
				returnMenu();
			}
			startGame();
		}
		else {
			System.out.println("No player found, please create a player with 'player' first.");
			returnMenu();
		}
	}
	/*
	 *  Make a new standard world
	 */
	public void initiateNewWorld() {
		world.makeEntity(monster.getName(), monster.getMaxHealth(), monster.getDamage(),
				player.getName().toUpperCase().charAt(0));
		world.generateNewMap();
	}
	/*
	 *  Check if the player is on an item
	 */
	public void itemInteraction() {
		char itemEntity = world.checkItemEncounter();

		// Apply item effects onto player
		while (itemEntity!=ItemEntity.INVALID_ITEM) {
			if (itemEntity == ItemEntity.DAMAGE_PERK) {
				item.damageItem(player);
			}
			else if (itemEntity == ItemEntity.HEALING_SALVE) {
				item.healingItem(player);
			}
			else if (itemEntity == ItemEntity.WARP_STONE) {
				world.clearEntities();
				Monster.monsterDefined = false;
				item.warpStone(player);
				returnMenu();
			}
			itemEntity = world.checkItemEncounter();
		}
	}
}