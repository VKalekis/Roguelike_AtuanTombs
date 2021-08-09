package Game.State;

import Game.Enemies.Enemy;
import Game.Enemies.EnemyFactory;
import Game.Items.EquippableItem;
import Game.Items.UsableItem;
import Game.Map.*;
import Game.Player.*;
import Game.UI.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.*;

public class GameState implements PlayerStatusTranscriptWriter, GameLogTranscriptWriter, ChangePanelSubject {

    private final Camera camera;

    private Room currentRoom;
    private RoomMap roomMap;

    private AbstractPlayer player;
    private LinkedList<Enemy> enemies;
    private EnemyFactory enemyFactory;

    private Map<Position, UsableItem> usablesMap;
    private Map<Position, EquippableItem> equippablesMap;

    private PlayerStatusTranscriptReader playerStatusTranscriptReader;
    private GameLogTranscriptReader gameLogTranscriptReader;
    private ChangePanelObserver changePanelObserver;

    private final Map<String, Image> spritesMap;

    public GameState(PlayerStatusTranscriptReader playerStatusTranscriptReader, GameLogTranscriptReader gameLogTranscriptReader, ChangePanelObserver changePanelObserver) {
        setPlayerStatusTranscriptReader(playerStatusTranscriptReader);
        setGameLogTranscriptReader(gameLogTranscriptReader);
        setChangePanelObserver(changePanelObserver);

        // Initialize Camera.
        this.camera = new Camera(41, 62);

        // Initializes spritesMap and loads all sprites from resources folder.
        this.spritesMap = new HashMap<>();
        initializeImagesMap();

        // Initializes labyrinth containing all the rooms and returns first room (the Pit).
        currentRoom = Labyrinth.initializeLabyrinth();
    }

    public void setInitialPlayerVariables(String playerClass, String playerName, boolean debug) {
        // New player.
        if (playerClass.equals("wizard")) {
            if (debug) {
                player = new WizardDebug(playerName);
            } else {
                player = new Wizard(playerName);
            }

        } else if (playerClass.equals("warrior")) {
            if (debug) {
                player = new WarriorDebug(playerName);
            } else {
                player = new Warrior(playerName);
            }
        }

        // Initializes enemyFactory which will generate new enemies and the list containing them.
        enemyFactory = new EnemyFactory(player);
        enemies = new LinkedList<>();

        sendPlayerStatusTranscript(player.getStatsHTML());
        initializeRoom(currentRoom);
    }


    private void initializeImagesMap() {
        try {
            // Uncomment if you want to dynamically get the sprite names from the resources folder.
            // I've found that this specific method doesn't work for jar files so I've hardcoded the sprites' filenames.
            // List<String> filenames = getImageFiles("");

            List<String> filenames = Arrays.asList("axe_0.png",
                    "axe_1.png",
                    "axe_2.png",
                    "axe_3.png",
                    "axe_4.png",
                    "axe_5.png",
                    "axe_6.png",
                    "axe_7.png",
                    "demon.png",
                    "floor_0.png",
                    "floor_1.png",
                    "floor_2.png",
                    "floor_3.png",
                    "floor_4.png",
                    "floor_5.png",
                    "floor_6.png",
                    "floor_7.png",
                    "floor_entrance.png",
                    "floor_exit.png",
                    "goblin.png",
                    "ice_zombie.png",
                    "knight.png",
                    "Minor_healing.png",
                    "Minor_mana.png",
                    "Minor_Restoration.png",
                    "necromancer.png",
                    "Normal_healing.png",
                    "Normal_mana.png",
                    "Normal_restoration.png",
                    "orc_shaman.png",
                    "shield_0.png",
                    "shield_1.png",
                    "shield_2.png",
                    "shield_3.png",
                    "shield_4.png",
                    "staff_0.png",
                    "staff_1.png",
                    "staff_2.png",
                    "staff_3.png",
                    "Supreme_healing.png",
                    "Supreme_mana.png",
                    "Supreme_restoration.png",
                    "swampy.png",
                    "sword_0.png",
                    "sword_1.png",
                    "sword_10.png",
                    "sword_11.png",
                    "sword_12.png",
                    "sword_2.png",
                    "sword_3.png",
                    "sword_4.png",
                    "sword_5.png",
                    "sword_6.png",
                    "sword_7.png",
                    "sword_8.png",
                    "sword_9.png",
                    "wall_banner_blue.png",
                    "wall_banner_green.png",
                    "wall_banner_red.png",
                    "wall_banner_yellow.png",
                    "wall_corner_left.png",
                    "wall_corner_right.png",
                    "wall_fountain_basin_blue.png",
                    "wall_fountain_basin_red.png",
                    "wall_fountain_mid_blue.png",
                    "wall_fountain_mid_red.png",
                    "wall_fountain_top.png",
                    "wall_left.png",
                    "wall_mid.png",
                    "wall_right.png",
                    "wall_side_mid_right.png",
                    "wall_side_mid_left.png",
                    "wall_top_mid.png",
                    "wizard.png");

            ClassLoader classLoader = getClass().getClassLoader();

            for (String filename : filenames) {
                this.spritesMap.put(filename, ImageIO.read(classLoader.getResource(filename)));
            }
            System.out.println(spritesMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
    // For loading images from resources folder. Not necessary now, I've hardcoded the image paths in the previous method.
    private List<String> getImageFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            InputStream in = classLoader.getResourceAsStream(path);
            if (in != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String resource;

                while ((resource = br.readLine()) != null) {
                    filenames.add(resource);

                }
            }
            return filenames;
        } catch (IOException exception) {
            System.out.println(exception.getStackTrace());

        }
        return null;
    }

    public void initializeRoom(Room room) {

        sendGameLogTranscript(new StringBuilder().append("Welcome to ").append(room.getName())
                .append("\n").append(room.getDescription()).append(".\n").toString());

        System.out.println("New room " + currentRoom);
        System.out.println("Entrances: " + currentRoom.getRoomMap().getEntrance());
        System.out.println("Exits: " + currentRoom.getRoomMap().getExits());

        // Generate new Usables and Equippables, deleting the previous items if the player has visited the room before.
        room.generateUsables(player);
        usablesMap = room.getUsablesMap();
        room.generateEquippables(player);
        equippablesMap = room.getEquippablesMap();

        // Get roomMap from current room.
        roomMap = room.getRoomMap();

        // Set player starting position and occupied tile.
        player.setStartingPosition(new Position(room.getEntrance()));
        roomMap.setOccupiedMapTile(player);
    }

    public void clearOldRoom(Room room) {
        // Sets empty map tiles where the enemies stood & clears enemies' list.
        if (enemies.size() > 0) {
            for (Enemy enemy : enemies) {
                roomMap.setEmptyMapTile(enemy);
            }
        }
        enemies.clear();

        // Sets empty map tile where the player stood.
        roomMap.setEmptyMapTile(player);

        // Clears old room's item maps.
        room.clearUsables();
        room.clearEquippables();
    }

    public void spawnEnemies() {
        double prob = 0.25 * (1 - Math.exp(-(4.0 * player.getHitPoints()) / player.getMaxHitPoints()));
        //double prob = (0.2 * player.getHitPoints()) / player.getMaxHitPoints();
        if ((Math.random() < prob) && (enemies.size() < 5)) {
            Enemy enemy = enemyFactory.makeEnemy();
            enemy.setStartingPosition(new Position(roomMap.getRandomPosition(MapTileType.FLOOR)));
            sendGameLogTranscript(enemy.getAnnouncementText() + "\n    Enemy " + enemy.getName() + " appeared.\n");

            roomMap.setOccupiedMapTile(enemy);
            enemies.add(enemy);
        }
    }

    public void movePlayer(String dir) {
        Position nextPosition = new Position(player.getPosition());
        nextPosition.shiftDir(dir);
        if (roomMap.validMove(nextPosition)) {
            // If the next maptile is an exit, print the room that it leads to. (For easier labyrinth navigation).
            if (roomMap.isExit(nextPosition)) {
                sendGameLogTranscript("This ladder leads to " + currentRoom.getConnectedRoom(nextPosition).getName() + ".\n");
            }

            // If the next maptile contains a potion/usable, pick it up (if it fits in the inventory).
            if (usablesMap.containsKey(nextPosition)) {
                if (player.pickUp(usablesMap.get(nextPosition))) {
                    sendGameLogTranscript("Picked up " + usablesMap.get(nextPosition) + ".\n");
                    usablesMap.remove(nextPosition);
                } else {
                    sendGameLogTranscript("Cannot pick up potion. Inventory full.\n");
                }
            }

            // If the next maptile contains an equippable, print it to the gamelog.
            if (equippablesMap.containsKey(nextPosition)) {
                sendGameLogTranscript(equippablesMap.get(nextPosition).toString());
            }

            // Set the current maptile as empty and set the next one as occupied.
            // Move the player.
            roomMap.setEmptyMapTile(player);
            player.move(nextPosition);
            roomMap.setOccupiedMapTile(player);

            System.out.println(player.getStats() + "---------------------------------\n");
        } else {
            sendGameLogTranscript("Ouch, hit wall (or an enemy).\n");
        }
    }

    public void moveEnemies() {

        // The enemies are sorted in increasing distance from the player. The first enemy who will move/attack the player
        // will be the one closest to him.
        if (enemies.size() > 0) {
            Collections.sort(enemies, new Comparator<Enemy>() {
                @Override
                public int compare(Enemy o1, Enemy o2) {
                    return Double.compare(o1.getPosition().getDistance(player.getPosition()), o2.getPosition().getDistance(player.getPosition()));
                }
            });

            for (Enemy enemy : enemies) {
                // An enemy can attack the player if the player is at a cardinal maptile with respect to the enemy (distance L=1).
                // We check that the difference of the distances to be less than 1.1 due to double imprecision at equality checks.

                // It is a better approach from the surrounding map tiles method we implemented for the player attack.
                // The surrounding map tiles method requires checking the surrounding cardinal map tiles (4) for all of the enemies to
                // locate the player. In contrast, here we only calculate the distances between the player and all the enemies.
                if (enemy.getPosition().getDistance(player.getPosition()) < 1.1) {
                    int dmg = enemy.dealDamage();
                    player.takeDamage(dmg);
                    sendGameLogTranscript("Enemy " + enemy.getName() + " attacked the player for " + dmg + " damage.\n");

                    if (!player.isAlive()) {
                        notify("LOSE");
                    }
                } else {
                    // Run A* for each enemy in order to find the next move.
                    enemy.runAstar(roomMap, player.getPosition());

                    Position nextMove = enemy.getNextMove();
                    if (nextMove != null) {
                        if (roomMap.validMove(nextMove)) {
                            roomMap.setEmptyMapTile(enemy);
                            enemy.move(nextMove);
                            roomMap.setOccupiedMapTile(enemy);
                        } else {
                            System.out.println("Failed A*.");
                        }
                    }
                }
            }

            for (Enemy enemy : enemies) {
                System.out.println("Enemy " + enemy.getName() + " position: " + enemy.getPosition());
            }
            System.out.println("Player position: " + player.getPosition());
            System.out.println("---------------------------------\n");
        }
    }

    public void rest() {
        player.rest();
        sendGameLogTranscript("Player rested. +5 HP/MP\n");
    }

    public void useUsable() {
        UsableItem usableItem = (UsableItem) player.use();
        sendGameLogTranscript("Player used " + usableItem.getName() + ".\n");
        System.out.println(player.getStats() + "---------------------------------\n");
    }

    public void dropUsable() {
        UsableItem droppedItem = (UsableItem) player.drop();
        sendGameLogTranscript("Player dropped " + droppedItem.getName() + ".\n");
        System.out.println(player.getStats() + "---------------------------------\n");
    }

    public void moveInventoryCursor(int offset) {
        player.moveInventoryCursor(offset);
    }

    public String getPlayerStatsHTML() {
        return player.getStatsHTML();
    }

    public void goToNextRoom() {
        if (currentRoom.isExit(player.getPosition())) {

            if (!currentRoom.isFinalRoom()) {
                // Go to next room.
                Room oldRoom = currentRoom;
                currentRoom = currentRoom.goTo(player.getPosition());

                // Initialize next Room.
                initializeRoom(currentRoom);
                // Clear old Room.
                clearOldRoom(oldRoom);
            } else {
                notify("WIN");
            }
        } else {
            System.out.println("You may not leave the room if you are standing at a non-ladder tile.\n");
        }
    }

    public void playerdealDamage() {
        List<MapTile> candidateMapTiles;

        // Wizard can attack an enemy located at a visible floor.
        if (player.getClass() == Wizard.class || player.getClass() == WizardDebug.class) {
            roomMap.findVisibleTiles(player.getPosition(), player.getVisibility());
            candidateMapTiles = roomMap.getVisibleFloors();

            // Sorts map tiles so that the Wizard will attack the closest enemy first.
            candidateMapTiles.sort(new Comparator<MapTile>() {
                @Override
                public int compare(MapTile o1, MapTile o2) {
                    return Double.compare(player.getPosition().getDistance(o1.getPosition()),
                            player.getPosition().getDistance(o2.getPosition()));
                }
            });
        }

        // Warrior can attack an enemy located at a cardinal floor/exit/entrance with respect to his location.
        else {
            candidateMapTiles = roomMap.getSurroundingCardinal(
                    player.getPosition().getI(),
                    player.getPosition().getJ(),
                    Arrays.asList(MapTileType.FLOOR, MapTileType.EXIT, MapTileType.ENTRANCE));
        }

        for (MapTile mapTile : candidateMapTiles) {
            if ((mapTile.isOccupied()) && (!mapTile.getPosition().equals(player.getPosition()))) {
                System.out.println("Player Attack : Enemy - Player Positions:" + mapTile.getPosition() + player.getPosition());
                Enemy enemy = (Enemy) mapTile.getOccupiedEntity();

                int dmg = player.dealDamage();
                enemy.takeDamage(dmg);

                StringBuilder sb = new StringBuilder();

                if (player.getClass() == Wizard.class || player.getClass() == WizardDebug.class) {
                    sb.append("Zap! ");
                } else {
                    sb.append("Slash! ");
                }
                sb.append("Player attacked ").append(enemy.getName()).append(" for ").append(dmg).append(".");

                if (!enemy.isAlive()) {
                    if (player.addXP(enemy.getExperience())) {
                        sendGameLogTranscript("Player leveled up! Restored HP/MP to maximum!\n");
                    }
                    enemies.remove(enemy);
                    mapTile.setEmpty();
                    sb.append("\nEnemy ").append(enemy.getName()).append(" is dead.");

                    Random random = new Random();
                    // 25% chance to drop a potion.
                    if (random.nextInt(4) == 0) {
                        currentRoom.addUsable(player, mapTile.getPosition());
                        sb.append(" Enemy dropped potion");
                    }
                }
                sendGameLogTranscript(sb.append("\n").toString());
                return;
            }
        }
        sendGameLogTranscript("Player missed. No enemy nearby.\n");
    }

    public void equipEquippable(SlotType slotType) {
        if (equippablesMap.containsKey(player.getPosition())) {
            EquippableItem equippableItem = equippablesMap.get(player.getPosition());
            if (equippableItem.getSlot() == slotType) {
                EquippableItem droppedItem = null;
                if (!player.isSlotEmpty(slotType)) {
                    droppedItem = (EquippableItem) player.remove(slotType);
                    droppedItem.setNewPosition(player.getPosition());
                    sendGameLogTranscript("Player dropped " + droppedItem.getName() + ".\n");
                }

                if (player.equip(slotType, equippableItem)) {
                    equippablesMap.remove(player.getPosition());

                    if (droppedItem != null) {
                        equippablesMap.put(player.getPosition(), droppedItem);
                    }
                    sendGameLogTranscript("Player equipped " + equippableItem.getName() + ".\n");
                }
                System.out.println(player.getStats() + "---------------------------------\n");
            } else {
                sendGameLogTranscript("Slot types do not match.\n");
            }
        }
    }

    public void drawMap(Graphics g) {
        // Updates camera position according to current player position.
        camera.setNewPosition(player.getPosition());

        // Background - flat gray.
        g.setColor(new Color(28, 28, 28));
        g.fillRect(0, 0, 16 * camera.getScreenSize(), 16 * camera.getScreenSize());

        // Find visible tiles according to current player position.
        roomMap.findVisibleTiles(player.getPosition(), player.getVisibility());

        // Draw floors.
        for (MapTile floor : roomMap.getFloors()) {
            draw(g, floor);
        }

        // Draw walls.
        for (MapTile wall : roomMap.getWalls()) {
            draw(g, wall);
        }

        // Draw player.
        draw(g, player);

        // Draw enemies.
        for (Enemy enemy : enemies) {
            draw(g, enemy);
        }

        // Draw usables.
        for (Map.Entry<Position, UsableItem> entry : usablesMap.entrySet()) {
            draw(g, entry.getValue());
        }

        // Draw equippables.
        for (Map.Entry<Position, EquippableItem> entry : equippablesMap.entrySet()) {
            draw(g, entry.getValue());
        }

        // Draw overlay over FOGGED, UNKNOWN walls & floors.
        drawOverlay(g, roomMap.getFloors());
        drawOverlay(g, roomMap.getWalls());

        // Set VISIBLE tiles -> FOGGED for next drawing of map.
        roomMap.setFoggedTiles();

    }

    private void draw(Graphics g, Drawable drawable) {
        Graphics2D g2d = (Graphics2D) g;
        int camera_i = camera.getPosition().getI();
        int camera_j = camera.getPosition().getJ();

        if ((drawable.getDrawablePosition().getI() >= camera_i) && (drawable.getDrawablePosition().getI() < camera_i + camera.getScreenSize()) &&
                (drawable.getDrawablePosition().getJ() >= camera_j) && (drawable.getDrawablePosition().getJ() < camera_j + camera.getScreenSize())) {
            for (String texture : drawable.getSprites()) {
                g2d.drawImage(spritesMap.get(texture),
                        (drawable.getDrawablePosition().getI() - camera_i) * 16,
                        (drawable.getDrawablePosition().getJ() - camera_j) * 16,
                        null);
            }
        }
    }

    private void drawOverlay(Graphics g, List<MapTile> mapTiles) {
        // Overlays FOGGED and UNKNOWN tiles.

        int unknownBrightness = (int) (256 - 256 * 0.05);
        int foggedBrightness = (int) (256 - 256 * 0.4);

        int camera_i = camera.getPosition().getI();
        int camera_j = camera.getPosition().getJ();

        for (MapTile mapTile : mapTiles) {
            if ((mapTile.getPosition().getI() >= camera_i) && (mapTile.getPosition().getI() < camera_i + camera.getScreenSize()) &&
                    (mapTile.getPosition().getJ() >= camera_j) && (mapTile.getPosition().getJ() < camera_j + camera.getScreenSize())) {
                switch (mapTile.getMapTileState()) {
                    // Overlay UNKNOWN tiles with 5% transparent black.
                    case UNKNOWN:
                        g.setColor(new Color(0, 0, 0, unknownBrightness));
                        g.fillRect((mapTile.getPosition().getI() - camera_i) * 16,
                                (mapTile.getPosition().getJ() - camera_j) * 16,
                                16,
                                16);
                        break;
                    // Overlay FOGGED tiles with 40% transparent black.
                    case FOGGED:
                        g.setColor(new Color(0, 0, 0, foggedBrightness));
                        g.fillRect((mapTile.getPosition().getI() - camera_i) * 16,
                                (mapTile.getPosition().getJ() - camera_j) * 16,
                                16,
                                16);
                        break;
                }
            }
        }
    }

    @Override
    public void setPlayerStatusTranscriptReader(PlayerStatusTranscriptReader playerStatusTranscriptReader) {
        this.playerStatusTranscriptReader = playerStatusTranscriptReader;
    }

    @Override
    public void sendPlayerStatusTranscript(String s) {
        this.playerStatusTranscriptReader.readPlayerStatusTranscript(s);
    }

    @Override
    public void setGameLogTranscriptReader(GameLogTranscriptReader gameLogTranscriptReader) {
        this.gameLogTranscriptReader = gameLogTranscriptReader;
    }

    @Override
    public void sendGameLogTranscript(String s) {
        this.gameLogTranscriptReader.readGameLogTranscript(s);
    }


    @Override
    public void setChangePanelObserver(ChangePanelObserver changePanelObserver) {
        this.changePanelObserver = changePanelObserver;
    }

    @Override
    public void notify(String s) {
        this.changePanelObserver.update(s);
    }
}
