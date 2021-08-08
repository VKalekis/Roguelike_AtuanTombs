package Game.Map;

import Game.State.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;


public class RoomMap {

    private MapTile[][] mapTiles;
    private final int WIDTH;
    private final int HEIGHT;
    private final ArrayList<MapTile> walls;
    private final ArrayList<MapTile> floors;

    private MapTile entrance;
    private final ArrayList<MapTile> exits;

    public RoomMap() {
        this.WIDTH = 60;
        this.HEIGHT = 60;
        this.mapTiles = new MapTile[WIDTH + 2][HEIGHT + 2];
        this.walls = new ArrayList<>();
        this.floors = new ArrayList<>();

        this.exits = new ArrayList<>();
    }

    public void createTileLists() {
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
        System.out.println("Ended Cellular Automata.");
    }

    public void floodFill() {
        Position position = new Position();
        position.randomize();
        while (mapTiles[position.getI()][position.getJ()].getMapTileType() == MapTileType.WALL) {
            position.randomize();
        }

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
        createTileLists();
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
        return getSurroundingCardinal(current_i, current_j, MapTileType.WALL).size();
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

        for (MapTileType mapTileType : mapTileTypes) {
            cardinalMapTileArrayList.addAll(getSurroundingCardinal(current_i, current_j, mapTileType));
        }
        return cardinalMapTileArrayList;
    }

    public void setWallsSprites() {
        /* Sets the wall sprites. It's over-complicated and I'm not 100% satisfied with the final result.
           In some cases, a wall sprite is assigned to a floor tile, which doesn't seem correct.
         */
        for (MapTile wall : walls) {
            int wall_i = wall.getPosition().getI();
            int wall_j = wall.getPosition().getJ();

            if (getSurroundingWallsCardinalSum(wall_i, wall_j) != 4) {

                if ((wall_i > 0) && (wall_i < 60) && (mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.FLOOR) && (mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.WALL) && (mapTiles[wall_i][wall_j - 1].getMapTileType() == MapTileType.WALL)) {
                    /*
                     W1
                     W2 F
                     W3
                     Draw F as left wall side.
                  */
                    mapTiles[wall_i + 1][wall_j].addsprite("wall_side_mid_left.png");
                } else if ((wall_i < 61) && (wall_i > 1) && (mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.FLOOR) && (mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.WALL) && (mapTiles[wall_i][wall_j - 1].getMapTileType() == MapTileType.WALL)) {
                    /*
                         W1
                       F W2
                         W3
                       Draw F as right wall side.
                   */
                    mapTiles[wall_i - 1][wall_j].addsprite("wall_side_mid_right.png");
                } else {
                    /* W W<-
                       W F
                     */
                    if (wall_i > 1 && wall_j < 60 && mapTiles[wall_i - 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.FLOOR) {
                        wall.addsprite("wall_corner_left.png");
                        /* Subcase:
                           W W<- W
                           W F   W
                         */
                        if (wall_i < 61 && wall_j > 1 && mapTiles[wall_i + 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.WALL) {
                            wall.addsprite("wall_side_mid_right.png");
                        }
                    }
                    /* ->W W
                         F W
                     */
                    else if (wall_i < 60 && wall_j < 60 && mapTiles[wall_i + 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.FLOOR) {
                        wall.addsprite("wall_corner_right.png");
                        /* Subcase:
                           W ->W W
                           W   F W
                         */
                        if (wall_i > 1 && wall_j > 1 && mapTiles[wall_i - 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.WALL) {
                            wall.addsprite("wall_side_mid_left.png");
                        }
                    }
                    /* F W<-
                       W W
                     */
                    else if (wall_i > 1 && wall_j < 60 && mapTiles[wall_i - 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.FLOOR) {
                        wall.addsprite("wall_corner_left.png");
                    }
                    /* ->W F
                         W W
                     */
                    else if (wall_i < 60 && wall_j < 60 && mapTiles[wall_i + 1][wall_j + 1].getMapTileType() == MapTileType.WALL && mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.FLOOR) {
                        wall.addsprite("wall_corner_right.png");
                    } else {
                        // Default case.
                        wall.addsprite("wall_mid.png");

                        /* Extra (random) sprites for following configuration:
                           W <-
                           F
                         */
                        if (wall_j < 60 && mapTiles[wall_i][wall_j + 1].getMapTileType() == MapTileType.FLOOR) {

                            Random random = new Random();
                            int rnd = random.nextInt(50);

                            if (rnd == 49) {
                                wall.addsprite("wall_banner_blue.png");
                            } else if (rnd == 48) {
                                wall.addsprite("wall_banner_green.png");
                            } else if (rnd == 47) {
                                wall.addsprite("wall_banner_red.png");
                            } else if (rnd == 46) {
                                wall.addsprite("wall_banner_yellow.png");
                            } else if (rnd == 45) {
                                if (wall_i > 1 && wall_i < 60 && wall_j > 1 && mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.WALL && mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.WALL) {
                                    mapTiles[wall_i][wall_j + 1].addsprite("wall_fountain_basin_blue.png");
                                    wall.addsprite("wall_fountain_mid_blue.png");
                                    mapTiles[wall_i][wall_j - 1].addsprite("wall_fountain_top.png");
                                }
                            } else if (rnd == 44) {
                                if (wall_i > 1 && wall_i < 60 && wall_j > 1 && mapTiles[wall_i - 1][wall_j].getMapTileType() == MapTileType.WALL && mapTiles[wall_i + 1][wall_j].getMapTileType() == MapTileType.WALL) {

                                    mapTiles[wall_i][wall_j + 1].addsprite("wall_fountain_basin_red.png");
                                    wall.addsprite("wall_fountain_mid_red.png");
                                    mapTiles[wall_i][wall_j - 1].addsprite("wall_fountain_top.png");
                                }
                            }

                        }
                    }
                    // Insert top wall sprite at non-border walls. The sprite is added at the floor above of the wall.
                    if (wall_j > 1 && wall_i > 0 && wall_i < 61) {
                        mapTiles[wall_i][wall_j - 1].addsprite("wall_top_mid.png");
                    }
                }

            }

        }


    }

    public void setFloorsSprites() {
        // Sets the floor sprites. If the floor is not an EXIT or an ENTRANCE, then a random sprite is used.
        for (MapTile floor : floors) {
            if (floor.getMapTileType() == MapTileType.EXIT) {
                floor.addsprite("floor_exit.png");
            } else if (floor.getMapTileType() == MapTileType.ENTRANCE) {
                floor.addsprite("floor_entrance.png");
            } else {
                int rand = new Random().nextInt(100) + 1;
                /* Overcomplicated - didn't want to write big case statement.
                8 available floor textures.
                Random rand from: 1-100
                1-51 -> floor0 (rand/51=0 -> prob=0)
                52-100 -> assigned to floor1,...floor7 with the same probability using modulo function.
                 rand/51=1 -> rand-51 % 7
                 */
                int prob = (rand / 65) * ((rand - 65) % 7);

                floor.addsprite("floor_" + prob + ".png");
            }
        }

    }

    private boolean tileVisible(int i1, int j1, int i2, int j2, int playerVisibility) {
        /* Finds if a Position (i1,j1) is visible from a Position (i2,j2) using a Euclidean distance metric
         of radius playerVisibility */
        return Math.sqrt(Math.pow(i2 - i1, 2) + Math.pow(j2 - j1, 2)) < playerVisibility;
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

    public void setOccupiedMapTile(Entity entity) {
        mapTiles[entity.getPosition().getI()][entity.getPosition().getJ()].setOccupiedEntity(entity);
    }

    public void setEmptyMapTile(Entity entity) {
        mapTiles[entity.getPosition().getI()][entity.getPosition().getJ()].setEmpty();
    }

    public MapTile getMapTile(Position position) {
        return mapTiles[position.getI()][position.getJ()];
    }

    public Position getRandomPosition(MapTileType mapTileType) {
        Random random = new Random();
        if (mapTileType == MapTileType.FLOOR) {
            return floors.get(random.nextInt(floors.size())).getPosition();
        } else if (mapTileType == MapTileType.WALL) {
            return walls.get(random.nextInt(walls.size())).getPosition();
        }
        return null;
    }

    public void findVisibleTiles(Position position, int playerVisibility) {
        for (int i = 0; i < 62; i++) {
            for (int j = 0; j < 62; j++) {
                if (tileVisible(i, j, position.getI(), position.getJ(), playerVisibility)) {
                    mapTiles[i][j].setMapTileState(MapTileState.VISIBLE);
                }
            }
        }
    }

    public void setFoggedTiles() {
        for (int i = 0; i < 62; i++) {
            for (int j = 0; j < 62; j++) {
                if (mapTiles[i][j].getMapTileState() == MapTileState.VISIBLE) {
                    mapTiles[i][j].setMapTileState(MapTileState.FOGGED);
                }
            }
        }
    }

    public List<MapTile> getVisibleFloors() {
        List<MapTile> visibleFloorList = new ArrayList<>();
        for (MapTile floor : floors) {
            if (floor.getMapTileState() == MapTileState.VISIBLE) {
                visibleFloorList.add(floor);
            }
        }
        return visibleFloorList;
    }

    public ArrayList<MapTile> getWalls() {
        return walls;
    }

    public ArrayList<MapTile> getFloors() {
        return floors;
    }
}


