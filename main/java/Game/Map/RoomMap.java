package Game.Map;

import Game.Enemies.Enemy;
import Game.PlayerSrc.AbstractPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;


public class RoomMap {

    private MapTile[][] mapTiles;
    private final int WIDTH;
    private final int HEIGHT;
    private final Camera camera;
    private final ArrayList<MapTile> walls;
    private final ArrayList<MapTile> floors;
    private final Map<String, Image> imagesMap;
    private MapTile entrance;
    private final ArrayList<MapTile> exits;

    public RoomMap() {
        this.WIDTH = 60;
        this.HEIGHT = 60;
        this.mapTiles = new MapTile[WIDTH + 2][HEIGHT + 2];
        this.camera = new Camera(41, 62);
        this.walls = new ArrayList<>();
        this.floors = new ArrayList<>();

        this.imagesMap = new HashMap<>();
        this.exits = new ArrayList<>();
        initializeImagesMap();

    }

    private void initializeImagesMap() {

        try {
            //List<String> filenames = getImageFiles("");

            List<String> filenames = new LinkedList<>();
            filenames = Arrays.asList(new String[]{"astar.png", "enemy.png", "floor_0.png", "floor_1.png",
                    "floor_2.png", "floor_3.png", "floor_4.png", "floor_5.png", "floor_6.png", "floor_7.png", "floor_hole.png", "floor_ladder.png", "knight.png", "wall_banner_blue.png", "wall_banner_green.png", "wall_banner_red.png", "wall_banner_yellow.png", "wall_corner_left.png", "wall_corner_right.png", "wall_fountain_basin_blue.png", "wall_fountain_basin_red.png", "wall_fountain_mid_blue.png", "wall_fountain_mid_red.png", "wall_fountain_top.png", "wall_left.png", "wall_mid.png", "wall_right.png", "wall_side_mid_left.png", "wall_side_mid_right.png", "wall_top_mid.png", "wizard.png"});

            System.out.println(filenames);
            ClassLoader classLoader = getClass().getClassLoader();

            for (String filename : filenames) {
                this.imagesMap.put(filename, ImageIO.read(classLoader.getResource(filename)));
            }
            System.out.println(imagesMap);


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

    public void collectLists() {
        walls.clear();
        floors.clear();
        for (int i = 0; i < WIDTH + 2; i++) {
            for (int j = 0; j < WIDTH + 2; j++) {
                switch (mapTiles[i][j].getMapTileType()) {
                    case FLOOR:
                        floors.add(mapTiles[i][j]);
                        break;
                    case WALL:
                        walls.add(mapTiles[i][j]);
                        break;
                }
            }
        }
    }

    public void initializeMap() {
        for (int i = 0; i < WIDTH + 2; i++) {
            for (int j = 0; j < HEIGHT + 2; j++) {
                mapTiles[i][j] = new MapTile(MapTileType.WALL, new Position(i, j));
            }
        }
    }

    public void generateRandomMap(int fill_value) {
        Random random = new Random();

        for (int i = 1; i < WIDTH + 1; i++) {
            for (int j = 1; j < HEIGHT + 1; j++) {
                if (random.nextInt(100) < fill_value) {
                    mapTiles[i][j] = new MapTile(MapTileType.WALL, new Position(i, j));
                } else {
                    mapTiles[i][j] = new MapTile(MapTileType.FLOOR, new Position(i, j));
                }
            }
        }
        //collectLists();
    }

    //http://pixelenvy.ca/wa/ca_cave.html
    //http://www.roguebasin.com/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels
    public void cellularAutomata() {

        for (int i = 1; i < WIDTH + 1; i++) {
            for (int j = 1; j < HEIGHT + 1; j++) {
                switch (getSurroundingWallsSum(i, j)) {
                    case 1:
                    case 2:
                    case 3:
                        mapTiles[i][j].setMapTileType(MapTileType.FLOOR);
                        break;
                    case 4:
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        mapTiles[i][j].setMapTileType(MapTileType.WALL);
                        break;
                }
            }
        }
        System.out.println("Ended");
        //collectLists();
    }

    public void floodFill() {
        Position position = new Position();
        position.randomize();
        while (mapTiles[position.getI()][position.getJ()].getMapTileType() == MapTileType.WALL) {
            position.randomize();
        }
        System.out.println(position);

        Stack<MapTile> mapTileStack = new Stack<>();
        mapTileStack.push(mapTiles[position.getI()][position.getJ()]);


        while (!mapTileStack.isEmpty()) {
            MapTile poppedMapTile = mapTileStack.pop();
            ArrayList<MapTile> cardinalMapTileArrayList = new ArrayList<>();
            if ((poppedMapTile.getMapTileType() == MapTileType.FLOOR) && (poppedMapTile.getColor() == 0)) {

                poppedMapTile.setColor(1);

                cardinalMapTileArrayList = getSurroundingCardinal(poppedMapTile.getPosition().getI(), poppedMapTile.getPosition().getJ(), MapTileType.FLOOR);
                for (MapTile mapTile : cardinalMapTileArrayList) {
                    mapTileStack.push(mapTile);
                }
            }
        }

        for (int i = 1; i < WIDTH + 1; i++) {
            for (int j = 1; j < HEIGHT + 1; j++) {
                if (mapTiles[i][j].getColor() == 1) {
                    mapTiles[i][j].setMapTileType(MapTileType.FLOOR);
                } else {
                    mapTiles[i][j].setMapTileType(MapTileType.WALL);

                }
            }
        }
        collectLists();

    }

    public double getFillRate() {
        int fillcount = 0;
        for (int i = 1; i < WIDTH + 1; i++) {
            for (int j = 1; j < HEIGHT + 1; j++) {
                if (mapTiles[i][j].getMapTileType() == MapTileType.FLOOR) {
                    fillcount++;
                }
            }
        }
        return (double) fillcount / (WIDTH * HEIGHT);
    }

    public int getSurroundingWallsSum(int current_i, int current_j) {
        // Only used in Cellurar Automata, which acts only on available space [1,WIDTH+1][1,HEIGHT+1].
        // Thus, it does not need border checks for variables current_i, current_j.

        // N1-N2-N3
        // N4-x -N5
        // N6-N7-N8

        int sum = 0;
        for (int i = current_i - 1; i <= current_i + 1; i++) {
            //N1,N2,N3
            if (mapTiles[i][current_j - 1].getMapTileType() == MapTileType.WALL) {
                sum++;
            }
            //N6,N7,N8
            if (mapTiles[i][current_j + 1].getMapTileType() == MapTileType.WALL) {
                sum++;
            }
        }
        //N4,N5
        for (int i = current_i - 1; i <= current_i + 1; i = i + 2) {
            if (mapTiles[i][current_j].getMapTileType() == MapTileType.WALL) {
                sum++;
            }
        }
        return sum;
    }

    public int getSurroundingWallsCardinalSum(int current_i, int current_j) {
        int sum = 0;

        if (current_i > 0) {
            if (mapTiles[current_i - 1][current_j].getMapTileType() == MapTileType.WALL) {
                sum++;
            }
        }
        if (current_i < 61) {
            if (mapTiles[current_i + 1][current_j].getMapTileType() == MapTileType.WALL) {
                sum++;
            }
        }
        if (current_j > 0) {
            if (mapTiles[current_i][current_j - 1].getMapTileType() == MapTileType.WALL) {
                sum++;
            }
        }
        if (current_j < 61) {
            if (mapTiles[current_i][current_j + 1].getMapTileType() == MapTileType.WALL) {
                sum++;
            }
        }
        return sum;
    }

    public ArrayList<MapTile> getSurroundingCardinal(int current_i, int current_j, MapTileType mapTileType) {
        ArrayList<MapTile> cardinalMapTileArrayList = new ArrayList<>();

        if (current_i > 0) {
            if (mapTiles[current_i - 1][current_j].getMapTileType() == mapTileType) {
                cardinalMapTileArrayList.add(mapTiles[current_i - 1][current_j]);
            }
        }
        if (current_i < 61) {
            if (mapTiles[current_i + 1][current_j].getMapTileType() == mapTileType) {
                cardinalMapTileArrayList.add(mapTiles[current_i + 1][current_j]);
            }
        }
        if (current_j > 0) {
            if (mapTiles[current_i][current_j - 1].getMapTileType() == mapTileType) {
                cardinalMapTileArrayList.add(mapTiles[current_i][current_j - 1]);
            }
        }
        if (current_j < 61) {
            if (mapTiles[current_i][current_j + 1].getMapTileType() == mapTileType) {
                cardinalMapTileArrayList.add(mapTiles[current_i][current_j + 1]);
            }
        }
        return cardinalMapTileArrayList;
    }

    public ArrayList<MapTile> getSurroundingCardinal(int current_i, int current_j, List<MapTileType> mapTileTypes) {
        ArrayList<MapTile> cardinalMapTileArrayList = new ArrayList<>();

        for (MapTileType mapTileType : mapTileTypes
        ) {
            cardinalMapTileArrayList.addAll(getSurroundingCardinal(current_i,current_j,mapTileType));
        }
        return cardinalMapTileArrayList;
    }

//    public void RandomWalk() {
//
//        double perc = 0.4;
//        double fill_rate = 0.0;
//        int fill_count = 0;
//
//        String[] dirs = {"N", "E", "S", "W"};
//        String randomdir;
//        Position randomPosition, currentPosition;
//
//        Random random = new Random();
//        int random_i = random.nextInt(WIDTH) + 1;
//        int random_j = random.nextInt(HEIGHT) + 1;
//        currentPosition = new Position(random_i, random_j);
//
//        mapTiles[currentPosition.getI()][currentPosition.getJ()].setMapTileType(MapTileType.FLOOR);
//
//        while (fill_rate < perc) {
//            randomdir = dirs[random.nextInt(4)];
//            randomPosition = currentPosition.shiftPosition(randomdir);
//            if (!randomPosition.validPosition()) {
//                System.out.println("Invalid " + randomPosition);
//                System.out.println(fill_rate);
//                System.out.println(fill_rate < perc);
//                continue;
//            }
//
//            currentPosition = randomPosition;
//            //System.out.println(randomPosition);
//
//            if (mapTiles[currentPosition.getI()][currentPosition.getJ()].getMapTileType() != MapTileType.FLOOR) {
//                mapTiles[currentPosition.getI()][currentPosition.getJ()].setMapTileType(MapTileType.FLOOR);
//                fill_count += 1;
//                System.out.println(fill_count);
//                fill_rate = (double) fill_count / (WIDTH * HEIGHT);
//            }
////            try
////            {
////                Thread.sleep(1000);
////            }
////            catch(InterruptedIOException ex)
////            {
////                Thread.currentThread().interrupt();
////            }
//
//        }
//
//
//    }

    public void drawMap(Graphics g) {
        for (int i = 0; i < WIDTH + 2; i++) {
            for (int j = 0; j < HEIGHT + 2; j++) {
                mapTiles[i][j].drawMapTile(g);
            }
        }
    }

    public void drawFocusedMap(Graphics g, AbstractPlayer player, List<Enemy> enemies) {
        Graphics2D g2d = (Graphics2D) g;

        System.out.println("Player position:" + player.getPosition());

        camera.setNewPosition(player);

        System.out.println("Camera " + camera.getPosition());
        int camera_i = camera.getPosition().getI();
        int camera_j = camera.getPosition().getJ();

        int unknownBrightness = (int) (256 - 256 * 0.1);
        int foggedBrightness = (int) (256 - 256 * 0.5);


        // Find visible tiles.
        for (int i = camera_i; i < camera_i + camera.getScreenSize(); i++) {
            for (int j = camera_j; j < camera_j + camera.getScreenSize(); j++) {
                if (tileVisible(i, j, player.getPosition().getI(), player.getPosition().getJ())) {
                    mapTiles[i][j].setMapTileState(MapTileState.VISIBLE);
                }
            }
        }

        // Background - flat gray.
        g.setColor(new Color(28, 28, 28));
        g.fillRect(0, 0, 16 * camera.getScreenSize(), 16 * camera.getScreenSize());

        // Draw Floors
        drawFloors(g, camera_i, camera_j);
        // Draw Walls
        drawWalls(g, camera_i, camera_j);
        // Draw Player
        drawPlayer(g, player, camera_i, camera_j);
        // Draw Enemy
        drawEnemies(g, enemies, camera_i, camera_j);

        // Draw visibility overlay over each tile.
        for (int i = camera_i; i < camera_i + camera.getScreenSize(); i++) {
            for (int j = camera_j; j < camera_j + camera.getScreenSize(); j++) {

                switch (mapTiles[i][j].getMapTileState()) {
                    // Overlay unknown tiles with 20% transparent black.
                    case UNKNOWN:
                        g.setColor(new Color(0, 0, 0, unknownBrightness));
                        g.fillRect((i - camera_i) * 16, (j - camera_j) * 16, 16, 16);
                        break;
                    // Overlay fogged tiles with 40% transparent black.
                    case FOGGED:
                        g.setColor(new Color(0, 0, 0, foggedBrightness));
                        g.fillRect((i - camera_i) * 16, (j - camera_j) * 16, 16, 16);
                        break;
                    // Set current visible tiles to fogged in order to be ready for the next player move.
                    case VISIBLE:
                        mapTiles[i][j].setMapTileState(MapTileState.FOGGED);
                        break;
                }
            }
        }


    }

    public void setWallsTextures() {

        for (MapTile wall : walls) {
            int wall_i = wall.getPosition().getI();
            int wall_j = wall.getPosition().getJ();

            if (getSurroundingWallsCardinalSum(wall_i, wall_j) != 4) {
                // W1
                // W2 F
                // W3
                // Draw W2 as left wall side.
                if ((wall_i > 0) && (wall_i < 60) && (mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.FLOOR) && (mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.WALL) && (mapTiles[wall_i][wall_j - 1].getMapTileType() == MapTileType.WALL)) {
                    //wall.addTextureName("img_side_left");
                    mapTiles[wall_i + 1][wall_j].addTextureName("wall_side_mid_right.png");
                }
                //   W1
                // F W2
                //   W3
                // Draw W2 as right wall side.
                else if ((wall_i < 61) && (wall_i > 1) && (mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.FLOOR) && (mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.WALL) && (mapTiles[wall_i][wall_j - 1].getMapTileType() == MapTileType.WALL)) {
                    //wall.addTextureName("img_side_right");
                    mapTiles[wall_i - 1][wall_j].addTextureName("wall_side_mid_left.png");
                } else {

                    // W W<-
                    // W F
                    if (wall_i > 1 && wall_j < 60 && mapTiles[wall_i - 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.FLOOR) {
                        wall.addTextureName("wall_corner_left.png");
//                        // Subcase:
//                        // W W W
//                        // W F W
//                        if (wall_i<61 && wall_j>1 && mapTiles[wall_i+1][wall_j-1].getMapTileType()==MapTileType.WALL && mapTiles[wall_i+1][wall_j].getMapTileType()==MapTileType.WALL) {
//                            wall.addTextureName("wall_side_mid_left.png");
//                        }
                    }
                    // ->W W
                    //   F W
                    else if (wall_i < 60 && wall_j < 60 && mapTiles[wall_i + 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.FLOOR) {
                        wall.addTextureName("wall_corner_right.png");
                    }
                    //F W<-
                    //W W
                    else if (wall_i > 1 && wall_j < 60 && mapTiles[wall_i - 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.FLOOR) {
                        wall.addTextureName("wall_corner_left.png");
                    }
                    // ->W F
                    //   W W
                    else if (wall_i < 60 && wall_j < 60 && mapTiles[wall_i + 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.FLOOR) {
                        wall.addTextureName("wall_corner_right.png");
                    } else {
                        wall.addTextureName("wall_mid.png");

                        if (wall_j < 60 && mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.FLOOR) {

                            Random random = new Random();
                            int rnd = random.nextInt(50);

                            if (rnd == 49) {
                                wall.addTextureName("wall_banner_blue.png");
                            } else if (rnd == 48) {
                                wall.addTextureName("wall_banner_green.png");
                            } else if (rnd == 47) {
                                wall.addTextureName("wall_banner_red.png");
                            } else if (rnd == 46) {
                                wall.addTextureName("wall_banner_yellow.png");
                            } else if (rnd == 45) {
                                if (wall_i > 1 && wall_i < 60 && wall_j > 1 && wall_j < 60 && mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.WALL
                                        && mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.WALL) {

                                    mapTiles[wall_i][wall_j + 1].addTextureName("wall_fountain_basin_blue.png");
                                    wall.addTextureName("wall_fountain_mid_blue.png");
                                    mapTiles[wall_i][wall_j - 1].addTextureName("wall_fountain_top.png");
                                }
                            } else if (rnd == 44) {
                                if (wall_i > 1 && wall_i < 60 && wall_j > 1 && wall_j < 60 && mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.WALL
                                        && mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.WALL) {

                                    mapTiles[wall_i][wall_j + 1].addTextureName("wall_fountain_basin_red.png");
                                    wall.addTextureName("wall_fountain_mid_red.png");
                                    mapTiles[wall_i][wall_j - 1].addTextureName("wall_fountain_top.png");
                                }
                            } else {
                                // Default
                                wall.addTextureName("wall_mid.png");
                            }
                        } else {
                            // Default
                            wall.addTextureName("wall_mid.png");
                        }

                    }
                    // Insert top wall texture at non-border walls.
                    if (wall_j > 1 && wall_i > 0 && wall_i < 61) {
                        mapTiles[wall_i][wall_j - 1].addTextureName("wall_top_mid.png");
                    }
                }

            }

        }


    }

    public void setFloorsTextures() {
        for (MapTile floor : floors) {
            if (floor.getMapTileType() == MapTileType.EXIT) {
                floor.addTextureName("floor_ladder.png");
            } else if (floor.getMapTileType() == MapTileType.ENTRANCE) {
                System.out.println(floor.getPosition() + "Im an Entrance");
                floor.addTextureName("floor_hole.png");
                System.out.println(floor.getTextureNames());
            } else {
                int rand = new Random().nextInt(100) + 1;
                int prob = (rand / 65) * ((rand - 65) % 7);
                //Overcomplicated - didn't want to write big case statement.
                //8 available floor textures.
                //Random rand from: 1-100
                //1-51 -> floor0 (rand/51=0 -> prob=0)
                //52-100 -> assigned to floor1,...floor7 with the same probability using modulo function.
                // rand/51=1 -> rand-51 % 7
                floor.addTextureName("floor_" + prob + ".png");
            }

        }

    }

    public void drawWalls(Graphics g, int camera_i, int camera_j) {
        Graphics2D g2d = (Graphics2D) g;

        for (MapTile wall : walls) {
            int wall_i = wall.getPosition().getI();
            int wall_j = wall.getPosition().getJ();
            if ((wall_i >= camera_i) && (wall_i < camera_i + camera.getScreenSize()) &&
                    (wall_j >= camera_j) && (wall_j < camera_j + camera.getScreenSize())) {
                for (String texture : wall.getTextureNames()) {
                    g2d.drawImage(imagesMap.get(texture), (wall_i - camera_i) * 16, (wall_j - camera_j) * 16, null);
                }
            }
        }

    }

    public void drawFloors(Graphics g, int camera_i, int camera_j) {
        Graphics2D g2d = (Graphics2D) g;


        for (MapTile floor : floors) {
            if ((floor.getPosition().getI() >= camera_i) && (floor.getPosition().getI() < camera_i + camera.getScreenSize()) &&
                    (floor.getPosition().getJ() >= camera_j) && (floor.getPosition().getJ() < camera_j + camera.getScreenSize())) {
                int floor_i = floor.getPosition().getI();
                int floor_j = floor.getPosition().getJ();

                for (String texture : floor.getTextureNames()) {
                    if (texture.equals("floor_hole.png")) {
                        System.out.println("FOUNDIT" + floor.getPosition());
                    }
                    g2d.drawImage(imagesMap.get(texture), (floor_i - camera_i) * 16, (floor_j - camera_j) * 16, null);

                }
            }
        }

    }

    public void drawPlayer(Graphics g, AbstractPlayer player, int camera_i, int camera_j) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(imagesMap.get(player.getTextureName()), (player.getPosition().getI() - camera_i) * 16, (player.getPosition().getJ() - 1 - camera_j) * 16, null);
        //getJ - 1 : the mage texture is > 16x16. We draw it at the tile above.
    }

    public void drawEnemies(Graphics g, List<Enemy> enemies, int camera_i, int camera_j) {
        Graphics2D g2d = (Graphics2D) g;

        for (Enemy enemy : enemies) {
            g2d.drawImage(imagesMap.get(enemy.getTextureName()), (enemy.getPosition().getI() - camera_i) * 16, (enemy.getPosition().getJ() - camera_j) * 16, null);
        }
    }

    public boolean tileVisible(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) < 15;
    }


    public Position addEntrance() {
        Random random = new Random();
        int rnd = random.nextInt(floors.size());
        floors.get(rnd).setMapTileType(MapTileType.ENTRANCE);
        entrance = floors.get(rnd);
        return entrance.getPosition();
    }

    public Position addExit() {
        Random random = new Random();
        MapTile exit = floors.get(random.nextInt(floors.size()));
        while ((exit.getMapTileType() == MapTileType.ENTRANCE) || (exit.getMapTileType() == MapTileType.EXIT) || (exit.getDistance(entrance) < 20)) {
            exit = floors.get(random.nextInt(floors.size()));
        }
        exit.setMapTileType(MapTileType.EXIT);
        exits.add(exit);
        System.out.println(exit);
        return exit.getPosition();
    }

    public boolean validMove(Position position) {
        return (!(mapTiles[position.getI()][position.getJ()].getMapTileType() == MapTileType.WALL) && (!mapTiles[position.getI()][position.getJ()].isOccupied()));
    }

    public boolean isExit(Position position) {
        return mapTiles[position.getI()][position.getJ()].getMapTileType() == MapTileType.EXIT;
    }

    public MapTile getEntrance() {
        return entrance;
    }

    public ArrayList<MapTile> getExits() {
        return exits;
    }

    public MapTile getMapTile(Position position) {
        return mapTiles[position.getI()][position.getJ()];
    }

    public MapTile getMapTile(int i, int j) {
        return mapTiles[i][j];
    }

    public MapTile getRandom(MapTileType mapTileType) {
        Random random = new Random();
        if (mapTileType == MapTileType.FLOOR) {
            return floors.get(random.nextInt(floors.size()));
        } else if (mapTileType == MapTileType.WALL) {
            return walls.get(random.nextInt(walls.size()));
        }
        return null;
    }


}


