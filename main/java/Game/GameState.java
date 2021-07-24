package Game;

import Game.Enemies.Enemy;
import Game.Enemies.GiantRat;
import Game.Items.*;
import Game.Map.*;
import Game.PlayerSrc.AbstractPlayer;
import Game.PlayerSrc.SlotType;
import Game.PlayerSrc.Warrior;
import Game.PlayerSrc.Wizard;
import Game.UI.LabelTranscriptReader;
import Game.UI.LabelTranscriptWriter;
import Game.UI.TextAreaTranscriptReader;
import Game.UI.TextAreaTranscriptWriter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameState implements LabelTranscriptWriter, TextAreaTranscriptWriter {

    private Room currentRoom;
    private RoomMap roomMap;
    private AbstractPlayer player;
    private LinkedList<Enemy> enemies;
    private Map<Position, UsableItem> potionMap;
    private Map<Position, EquippableItem> itemMap;

    private LabelTranscriptReader labelTranscriptReader;
    private TextAreaTranscriptReader textAreaTranscriptReader;

    public GameState(LabelTranscriptReader labelTranscriptReader, TextAreaTranscriptReader textAreaTranscriptReader) {
        setLabelTranscriptReader(labelTranscriptReader);
        setTextAreaTranscriptReader(textAreaTranscriptReader);

        currentRoom = Labyrinth.initializeLabyrinth();
        player = new Wizard();
        enemies = new LinkedList<>();
        sendLabelTranscript(player.getStatsHTML());
        initializeRoom(currentRoom);
        initializePotions();
        initializeWeapons();
        test();

    }

    public void initializePotions() {
        potionMap = new HashMap<>();
        String[] adjectives = {"Minor", "Normal", "Supreme"};
        int[] healthpoints = {20, 25, 30};
        int[] manapoints = {15, 20, 25};

        int index;
        UsableItem potion;

        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            int rnd = random.nextInt(10);
            if (rnd == 9) {
                index = 2;
            } else if (rnd >= 5) {
                index = 1;
            } else {
                index = 0;
            }

            rnd = random.nextInt(11);
            if (rnd == 10) {
                potion = new UsableItem.UsableBuilder()
                        .named(adjectives[index] + " Restoration Potion")
                        .withDescription("")
                        .withSprite(adjectives[index] + "_restoration.png")
                        .withItemEffect(new ItemEffect(EffectType.HP_REPLENISH, healthpoints[index]))
                        .withItemEffect(new ItemEffect(EffectType.MP_REPLENISH, manapoints[index]))
                        .withUses(2)
                        .build();
            } else if (rnd >= 5) {
                potion = new UsableItem.UsableBuilder()
                        .named(adjectives[index] + " Mana Potion")
                        .withDescription("")
                        .withSprite(adjectives[index] + "_mana.png")
                        .withItemEffect(new ItemEffect(EffectType.MP_REPLENISH, manapoints[index]))
                        .withUses(2)
                        .build();
            } else {
                potion = new UsableItem.UsableBuilder()
                        .named(adjectives[index] + " Health Potion")
                        .withDescription("")
                        .withSprite(adjectives[index] + "_healing.png")
                        .withItemEffect(new ItemEffect(EffectType.HP_REPLENISH, healthpoints[index]))
                        .withUses(2)
                        .build();
            }
            potionMap.put(roomMap.getRandom(MapTileType.FLOOR).getPosition(), potion);


        }

    }


    public void test() {
        if (player.getClass() == Wizard.class) {
            System.out.println("yer a wizard harry");
        }
        if (player.getClass() == Warrior.class) {
            System.out.println("yer a warrior harry");
        }

    }

    public void initializeWeapons() {
        itemMap = new HashMap<>();
        int itemLevel = 5 * player.getLevel() + 5;
        StringBuilder name = new StringBuilder();
        Random random = new Random();

        String[] adjectives = {"Fierce", "Stubborn", "Pure", "Ancient"};

        Map<Class, String[]> classStringMap = new HashMap<>();
        classStringMap.put(Warrior.class, new String[]{"Sword", "Axe"});
        classStringMap.put(Wizard.class, new String[]{"Staff"});

        String[] surnames = {"of the Champion", "of Erreth-Akbe", "of Rok"};

        name.append(adjectives[random.nextInt(adjectives.length)]).append(" ");

        if (player.getClass() == Wizard.class) {
            name.append(classStringMap.get(Wizard.class)[0]).append(" ");
        } else {
            name.append(classStringMap.get(Warrior.class)[random.nextInt(classStringMap.get(Warrior.class).length)]).append(" ");
        }
        name.append(surnames[random.nextInt(surnames.length)]);


        EffectType mainEffect;
        EffectType[] otherEffects;

        if (player.getClass() == Wizard.class) {
            mainEffect = EffectType.INT_BONUS;
            otherEffects = new EffectType[]{EffectType.HP_BONUS, EffectType.MP_BONUS};
        } else {
            mainEffect = EffectType.STR_BONUS;
            otherEffects = new EffectType[]{EffectType.HP_BONUS, EffectType.DEFENSE};
        }

        LinkedList<ItemEffect> effects = new LinkedList<>();
        int stat = random.nextInt(itemLevel) + 1;
        effects.add(new ItemEffect(mainEffect, stat));
        itemLevel = itemLevel - stat;
        EffectType secondStat = otherEffects[random.nextInt(otherEffects.length)];
        effects.add(new ItemEffect(secondStat, itemLevel));

        EquippableItem item = new EquippableItem.EquippableBuilder()
                .named(name.toString())
                .withDescription("Giant sword")
                .withSprite("sword_1.png")
                .withItemEffects(effects)
                .atSlot(SlotType.MAIN_HAND)
                .build();
        itemMap.put(roomMap.getRandom(MapTileType.FLOOR).getPosition(),item);
        item = new EquippableItem.EquippableBuilder()
                .named(name.toString())
                .withDescription("Giant sword")
                .withSprite("axe_1.png")
                .withItemEffects(effects)
                .atSlot(SlotType.MAIN_HAND)
                .build();
        itemMap.put(roomMap.getRandom(MapTileType.FLOOR).getPosition(),item);
    }

    public void initializeRoom(Room room) {
        roomMap = room.getMap();
        player.setStartingPosition(new Position(room.getEntrance()));
        roomMap.setOccupiedMapTile(player);
        enemies.clear();
    }

    public void spawnEnemies(double prob) {

//        Map<Integer, List<Class>> levelEnemyMap =
//                Map.of(1, List.of(GiantRat.class),
//                        2, List.of(GiantRat.class, MadGuard.class),
//                        3, List.of(GiantRat.class, MadGuard.class, Skeleton.class),
//                        4, List.of(MadGuard.class, Skeleton.class, SkeletonLord.class),
//                        5, List.of(Skeleton.class, SkeletonLord.class),
//                        6, List.of(Skeleton.class, SkeletonLord.class, Shade.class));
//        private AbstractEnemy makeEnemy() {
//
//            List<Class> possibleEnemies = levelEnemyMap.get(player.getLevel());
//            Class enemyClass = possibleEnemies.get(rng.nextInt(possibleEnemies.size()));
//            try {
//                var enemy = (AbstractEnemy) enemyClass.getDeclaredConstructor().newInstance();
//                writeTranscript(enemy.getDescription());
//                return enemy;
//            } catch(Exception ex) {
//                ex.printStackTrace();
//            }
//            return


        if (Math.random() < prob) {

            Enemy enemy = new GiantRat();
            enemy.setStartingPosition(new Position(roomMap.getRandom(MapTileType.FLOOR).getPosition()));
            sendTextAreaTranscript(enemy.getAnnouncementText() + "\n Enemy " + enemy.getName() + " appeared.");

            roomMap.setOccupiedMapTile(enemy);
            enemies.add(enemy);
        }
    }

    public void movePlayer(String dir) {
        Position nextPosition = new Position(player.getPosition());
        nextPosition.shiftDir(dir);
        if (roomMap.validMove(nextPosition)) {

            if (roomMap.isExit(nextPosition)) {
                sendTextAreaTranscript("This ladder leads to " + currentRoom.getFromMap(nextPosition).getName());
            }
            if (potionMap.containsKey(nextPosition)) {
                player.pickUp(potionMap.get(nextPosition));
                sendTextAreaTranscript("Picked up " + potionMap.get(nextPosition));
                potionMap.remove(nextPosition);
            }
            if (itemMap.containsKey(nextPosition)){
                sendTextAreaTranscript("Item " + itemMap.get(nextPosition));
            }
            roomMap.setEmptyMapTile(player);
            player.movePlayer(dir);
            roomMap.setOccupiedMapTile(player);
        } else {
            sendTextAreaTranscript("Ouch, hit wall (or an enemy).");
        }
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

            System.out.println(currentRoom.getFromMap((player.getPosition())));
            currentRoom = currentRoom.goTo(player.getPosition());
            sendTextAreaTranscript("Welcome to the " + currentRoom.getName() + "\n" + currentRoom.getDescription());
            System.out.println("New room " + currentRoom);
            System.out.println("Entrances: " + currentRoom.getMap().getEntrance());
            System.out.println("Exits: " + currentRoom.getMap().getExits());

            initializeRoom(currentRoom);
            initializePotions();

        } else {
            System.out.println("You may not leave the room if you are standing at a non-ladder tile.");
        }
    }

    public void playerdealDamage() {
        ArrayList<MapTile> surroundingMapTiles = roomMap.getSurroundingCardinal(
                player.getPosition().getI(),
                player.getPosition().getJ(),
                Arrays.asList(MapTileType.FLOOR, MapTileType.EXIT, MapTileType.ENTRANCE));

        for (MapTile mapTile : surroundingMapTiles) {
            if (mapTile.isOccupied()) {
                Enemy enemy = (Enemy) mapTile.getOccupiedEntity();

                int dmg = player.dealDamage();
                enemy.takeDamage(dmg);
                sendTextAreaTranscript("Player attacked for " + dmg);
                if (enemy.getHitpoints() <= 0) {
                    player.addXP(enemy.getExperience());
                    enemies.remove(enemy);
                    mapTile.setEmpty();
                    return;
                }
            }
        }

    }

    public void equip() {
        if (itemMap.containsKey(player.getPosition())) {
            EquippableItem item = itemMap.get(player.getPosition());
            if (!player.isSlotEmpty(item.getSlot())) {
                EquippableItem droppedItem = (EquippableItem) player.remove(item.getSlot());
                itemMap.put(player.getPosition(),droppedItem);

            }
            player.equip(item.getSlot(),item);
            itemMap.remove(item);
        }
    }

    public void drawMap(Graphics g) {
        roomMap.drawFocusedMap(g, player, enemies, potionMap, itemMap);
    }

    public RoomMap getRoomMap() {
        return roomMap;
    }

    public AbstractPlayer getPlayer() {
        return player;
    }

    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    public Map<Position, UsableItem> getPotionMap() {
        return potionMap;
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
