package Game;

import Game.Enemies.*;
import Game.Items.*;
import Game.Map.*;
import Game.PlayerSrc.AbstractPlayer;
import Game.PlayerSrc.Warrior;
import Game.PlayerSrc.Wizard;
import Game.UI.LabelTranscriptReader;
import Game.UI.LabelTranscriptWriter;
import Game.UI.TextAreaTranscriptReader;
import Game.UI.TextAreaTranscriptWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class GameState implements LabelTranscriptWriter, TextAreaTranscriptWriter {

    private Camera camera;

    private Room currentRoom;
    private RoomMap roomMap;

    private AbstractPlayer player;
    private LinkedList<Enemy> enemies;
    private EnemyFactory enemyFactory;

    private Map<Position, UsableItem> usablesMap;
    private Map<Position, EquippableItem> equippablesMap;

    private LabelTranscriptReader labelTranscriptReader;
    private TextAreaTranscriptReader textAreaTranscriptReader;

    private final Map<String, Image> spritesMap;

    public GameState(LabelTranscriptReader labelTranscriptReader, TextAreaTranscriptReader textAreaTranscriptReader) {
        setLabelTranscriptReader(labelTranscriptReader);
        setTextAreaTranscriptReader(textAreaTranscriptReader);

        // Initialize Camera.
        this.camera = new Camera(41, 62);

        // Initializes spritesMap and loads all sprites from resources folder.
        this.spritesMap = new HashMap<>();
        initializeImagesMap();

        // Initializes labyrinth containing all the rooms and returns first room (the Pit).
        currentRoom = Labyrinth.initializeLabyrinth();

        // New player.
        player = new Warrior();

        // Initializes enemyFactory which will generate new enemies and the list containing them.
        enemyFactory = new EnemyFactory(player);
        enemies = new LinkedList<>();

        sendLabelTranscript(player.getStatsHTML());
        initializeRoom(currentRoom);

        test();

    }

    private void initializeImagesMap() {

        try {
            //List<String> filenames = getImageFiles("");

            List<String> filenames = Arrays.asList("astar.png",
                    "axe_0.png",
                    "axe_1.png",
                    "axe_2.png",
                    "axe_3.png",
                    "axe_4.png",
                    "axe_5.png",
                    "axe_6.png",
                    "axe_7.png",
                    "floor_0.png",
                    "floor_1.png",
                    "floor_2.png",
                    "floor_3.png",
                    "floor_4.png",
                    "floor_5.png",
                    "floor_6.png",
                    "floor_7.png",
                    "floor_hole.png",
                    "floor_ladder.png",
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
                    "wall_side_mid_left.png",
                    "wall_side_mid_right.png",
                    "wall_top_mid.png",
                    "wizard.png");

            System.out.println(filenames);
            for (String filename:filenames) {
                System.out.println(new StringBuilder().append("\"").append(filename).append("\"").toString());
            }
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
    public List<String> getImageFiles(String path) throws IOException {
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

    public void test() {
        if (player.getClass() == Wizard.class) {
            System.out.println("yer a wizard harry");
        }
        if (player.getClass() == Warrior.class) {
            System.out.println("yer a warrior harry");
        }

    }


    public void initializeRoom(Room room) {

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

        // Clears enemies' list.
        enemies.clear();
    }

    public void spawnEnemies(double prob) {
        if (Math.random() < prob) {

            //Enemy enemy = new OrcShaman();
            Enemy enemy = enemyFactory.makeEnemy();
            enemy.setStartingPosition(new Position(roomMap.getRandomPosition(MapTileType.FLOOR)));
            sendTextAreaTranscript(enemy.getAnnouncementText() + "\nEnemy " + enemy.getName() + " appeared.\n");

            roomMap.setOccupiedMapTile(enemy);
            enemies.add(enemy);
        }
    }

    public void movePlayer(String dir) {
        Position nextPosition = new Position(player.getPosition());
        nextPosition.shiftDir(dir);
        if (roomMap.validMove(nextPosition)) {

            if (roomMap.isExit(nextPosition)) {
                sendTextAreaTranscript("This ladder leads to " + currentRoom.getConnectedRoom(nextPosition).getName());
            }
            if (usablesMap.containsKey(nextPosition)) {
                player.pickUp(usablesMap.get(nextPosition));
                sendTextAreaTranscript("Picked up " + usablesMap.get(nextPosition));
                usablesMap.remove(nextPosition);
            }
            if (equippablesMap.containsKey(nextPosition)) {
                sendTextAreaTranscript(equippablesMap.get(nextPosition).toString());
            }
            roomMap.setEmptyMapTile(player);
            player.movePlayer(dir);
            roomMap.setOccupiedMapTile(player);
        } else {
            sendTextAreaTranscript("Ouch, hit wall (or an enemy).");
        }
    }

    public void rest() {
        player.rest();
    }

    public void use() {
        player.use();
    }

    public void dropItem() {
        player.dropItem();
    }

    public void moveInventoryCursor(int offset) {
        player.moveInventoryCursor(offset);
    }

    public String getPlayerStatsHTML() {
        return player.getStatsHTML();
    }

    public void moveEnemies() {
        if (enemies.size() > 0) {
            Collections.sort(enemies, new Comparator<Enemy>() {
                @Override
                public int compare(Enemy o1, Enemy o2) {
                    return Double.compare(o1.getPosition().getDistance(player.getPosition()), o2.getPosition().getDistance(player.getPosition()));
                }
            });

            for (Enemy enemy : enemies) {
                if (enemy.getPosition().getDistance(player.getPosition()) < 1.3) {
                    // The player's position is at a cardinal direction from the enemy's position. We check for the distance
                    // to be less than 1.3 to eliminate the possibility that they are at a diagonal orientation.
                    // It is a better approach from the surrounding map tiles method we implemented for the player attack.
                    // The surrounding map tiles method requires checking all the surrounding cardinal map tiles (4) for >1 enemies to
                    // locate the player, where here we only check the distances between the player and all the enemies.
                    player.takeDamage(enemy.dealDamage());
                    System.out.println("Slash");
                } else {
                    enemy.runAstar(roomMap, player.getPosition());

                    Position nextMove = enemy.getNextMove();
                    if (nextMove != null) {
                        if (roomMap.validMove(nextMove)) {
                            roomMap.setEmptyMapTile(enemy);
                            enemy.move(nextMove);
                            roomMap.setOccupiedMapTile(enemy);
                        } else {
                            System.out.println("Failed a*");
                        }
                    }
                }

            }
            for (Enemy enemy : enemies) {
                System.out.println("Enemies new positions" + enemy.getPosition());
            }
        }
    }

    public void goToNextRoom() {
        if (currentRoom.isExit(player.getPosition())) {

            if (!currentRoom.isFinalRoom()) {
                System.out.println(currentRoom.getConnectedRoom((player.getPosition())));
                // Go to next room.
                currentRoom = currentRoom.goTo(player.getPosition());
                sendTextAreaTranscript("Welcome to the " + currentRoom.getName() + "\n" + currentRoom.getDescription());
                System.out.println("New room " + currentRoom);
                System.out.println("Entrances: " + currentRoom.getRoomMap().getEntrance());
                System.out.println("Exits: " + currentRoom.getRoomMap().getExits());

                initializeRoom(currentRoom);
            }
        } else {
            System.out.println("You may not leave the room if you are standing at a non-ladder tile.");
        }
    }

    public void playerdealDamage() {
        List<MapTile> candidateMapTiles;

        if (player.getClass() == Wizard.class) {
            roomMap.findVisibleTiles(player.getPosition(), player.getVisibility());
            candidateMapTiles = roomMap.getVisibleFloors();
            System.out.println("test1u" + candidateMapTiles);
            System.out.println("test1u" + candidateMapTiles.size());
        } else {
            candidateMapTiles = roomMap.getSurroundingCardinal(
                    player.getPosition().getI(),
                    player.getPosition().getJ(),
                    Arrays.asList(MapTileType.FLOOR, MapTileType.EXIT, MapTileType.ENTRANCE));
        }

        for (MapTile mapTile : candidateMapTiles) {
            if ((mapTile.isOccupied()) && (!mapTile.getPosition().equals(player.getPosition()))) {
                System.out.println("Positions:" + mapTile.getPosition() + player.getPosition());
                Enemy enemy = (Enemy) mapTile.getOccupiedEntity();

                int dmg = player.dealDamage();
                enemy.takeDamage(dmg);
                sendTextAreaTranscript("Player attacked for " + dmg);
                if (enemy.getHitpoints() <= 0) {
                    player.addXP(enemy.getExperience());
                    enemies.remove(enemy);
                    mapTile.setEmpty();
                    break;
                }
            }
        }
    }

    public void equip() {
        if (equippablesMap.containsKey(player.getPosition())) {
            EquippableItem equippableItem = equippablesMap.get(player.getPosition());
            if (!player.isSlotEmpty(equippableItem.getSlot())) {
                EquippableItem droppedItem = (EquippableItem) player.remove(equippableItem.getSlot());
                equippablesMap.put(player.getPosition(), droppedItem);
                sendTextAreaTranscript("Player dropped " + droppedItem.getName() + ".");

            }

            if (player.equip(equippableItem.getSlot(), equippableItem)) {
                equippablesMap.remove(player.getPosition());
                sendTextAreaTranscript("Player equipped " + equippableItem.getName() + ".");
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

        // Sets VISIBLE tiles -> FOGGED for next drawing of map.
        roomMap.setFoggedTiles();

    }

    public void drawMap1(Graphics g) {


        // Background - flat gray.
        g.setColor(new Color(255, 255, 255, 127));
        g.fillRect(0, 0, 700, 700);
        g.setColor(new Color(0, 0, 0));
        //g.setFont();
        g.drawString("Hello", 350, 350);

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
                        (drawable.getDrawablePosition().getJ() - camera_j) * 16, null);
            }
        }
    }

    private void drawOverlay(Graphics g, List<MapTile> mapTiles) {
        int unknownBrightness = (int) (256 - 256 * 0.1);
        int foggedBrightness = (int) (256 - 256 * 0.5);

        int camera_i = camera.getPosition().getI();
        int camera_j = camera.getPosition().getJ();

        for (MapTile mapTile : mapTiles) {
            if ((mapTile.getPosition().getI() >= camera_i) && (mapTile.getPosition().getI() < camera_i + camera.getScreenSize()) &&
                    (mapTile.getPosition().getJ() >= camera_j) && (mapTile.getPosition().getJ() < camera_j + camera.getScreenSize())) {
                switch (mapTile.getMapTileState()) {
                    // Overlay fogged tiles with 10% transparent black.
                    case UNKNOWN:
                        g.setColor(new Color(0, 0, 0, unknownBrightness));
                        g.fillRect((mapTile.getPosition().getI() - camera_i) * 16,
                                (mapTile.getPosition().getJ() - camera_j) * 16,
                                16,
                                16);
                        break;
                    // Overlay fogged tiles with 50% transparent black.
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
    public void setLabelTranscriptReader(LabelTranscriptReader labelTranscriptReader) {
        this.labelTranscriptReader = labelTranscriptReader;
    }

    @Override
    public void sendLabelTranscript(String s) {
        this.labelTranscriptReader.readLabelTranscript(s);
    }

    @Override
    public void setTextAreaTranscriptReader(TextAreaTranscriptReader textAreaTranscriptReader) {
        this.textAreaTranscriptReader = textAreaTranscriptReader;
    }

    @Override
    public void sendTextAreaTranscript(String s) {
        this.textAreaTranscriptReader.readTextAreaTranscript(s);
    }
}
