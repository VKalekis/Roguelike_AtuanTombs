package Game.Map;

import Game.Astar;
import Game.Enemies.Enemy;
import Game.Enemies.GiantRat;
import Game.PlayerSrc.Wizard;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class UIRoomTest {
    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(() -> {
            MyFrame1 frame = new MyFrame1();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class MyFrame1 extends JFrame {
    public MyFrame1() throws HeadlessException {
        MapComponent1 mapComponent = new MapComponent1();


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        JButton button1 = new JButton("IAM1");
        JButton button2 = new JButton("IAM2");
        JButton button3 = new JButton("N");
        JButton button4 = new JButton("S");
        JButton button5 = new JButton("E");
        JButton button6 = new JButton("W");
        JButton buttona = new JButton("a*");

        button1.addActionListener(e -> {
            mapComponent.runAutomata();
        });

        button2.addActionListener(e -> {
            mapComponent.floodFill();
        });

        button3.addActionListener(e -> {
            mapComponent.movePlayer("N");
        });

        button4.addActionListener(e -> {
            mapComponent.movePlayer("S");
        });

        button5.addActionListener(e -> {
            mapComponent.movePlayer("E");
        });

        button6.addActionListener(e -> {
            mapComponent.movePlayer("W");
        });

        buttona.addActionListener(e -> {
            mapComponent.runastar();
        });


        mainPanel.add(button1);
        mainPanel.add(button2);
        mainPanel.add(button3);
        mainPanel.add(button4);
        mainPanel.add(button5);
        mainPanel.add(button6);
        mainPanel.add(buttona);
        mainPanel.add(mapComponent);
        add(mainPanel);
        pack();
    }
}

class MapComponent1 extends JComponent {
    private Room currentRoom;
    private RoomMap roomMap;
    //private Player player;
    private Wizard wizard;
    private LinkedList<Enemy> enemies;
    private Astar astar;

    public MapComponent1() {


        currentRoom = Labyrinth.initializeLabyrinth();

        roomMap = currentRoom.getMap();
        roomMap.setFloorsTextures();
        roomMap.setWallsTextures();

        //player = new Player(new Position(roomMap.getStartingPosition()));
        wizard = new Wizard(new Position(roomMap.getEntrance().getPosition()));
        roomMap.getMapTile(wizard.getPosition()).setOccupiedEntity(wizard);

        enemies = new LinkedList<>();
        enemies.add(new GiantRat());
        enemies.add(new GiantRat());


        for (Enemy enemy : enemies) {
            enemy.setStartingPosition(new Position(roomMap.getRandom(MapTileType.FLOOR).getPosition()));
            roomMap.getMapTile(enemy.getPosition()).setOccupiedEntity(enemy);
        }


        System.out.println("beepboop" + currentRoom.getConnectedRooms());

        this.setFocusable(true);
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println("Pressed " + e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyChar());
                switch (e.getKeyChar()) {
                    case 'w':
                    case 'W':
                        movePlayer("N");moveEnemies();

                        break;
                    case 's':
                    case 'S':
                        movePlayer("S");moveEnemies();
                        break;
                    case 'd':
                    case 'D':
                        movePlayer("E");moveEnemies();
                        break;
                    case 'a':
                    case 'A':
                        movePlayer("W");moveEnemies();
                        break;
                    case 'g':
                    case 'G':
                        goTo(wizard.getPosition());moveEnemies();
                        break;
                    case 'x':
                    case 'X':
                        playerdealDamage();moveEnemies();
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("Pressed " + e.getKeyChar());

            }
        });

    }

    public void runastar() {

        repaint();
    }

    public void goTo(Position position) {
        if (roomMap.isExit(position)) {

            System.out.println(currentRoom.getFromMap((position)));
            currentRoom = currentRoom.goTo(currentRoom.getFromMap(position).getName());
            System.out.println("New room " + currentRoom);
            System.out.println("Entrances: " + currentRoom.getMap().getEntrance());
            System.out.println("Exits: " + currentRoom.getMap().getExits());

            roomMap = currentRoom.getMap();
            wizard.setStartingPosition(new Position(roomMap.getEntrance().getPosition()));
            roomMap.setFloorsTextures();
            roomMap.setWallsTextures();
            repaint();

        } else {
            System.out.println("Boo, stoopid.");
        }
    }

    public void movePlayer(String dir) {
        Position position = new Position(wizard.getPosition());
        position.shiftDir(dir);
        if (roomMap.validMove(position)) {
            if (roomMap.isExit(position)) {
                System.out.println(currentRoom.getFromMap(position));
            }
            roomMap.getMapTile(position).setOccupiedEntity(wizard);
            roomMap.getMapTile(wizard.getPosition()).setEmpty();
            wizard.movePlayer(dir);
            repaint();
        } else {
            System.out.println("Ouch, hit wall");
        }
    }

    public void playerdealDamage() {
        ArrayList<MapTile> surroundingMapTiles = roomMap.getSurroundingCardinal(wizard.getPosition().getI(),
                wizard.getPosition().getJ(),
                Arrays.asList(MapTileType.FLOOR,MapTileType.EXIT,MapTileType.ENTRANCE));

        for (MapTile mapTile : surroundingMapTiles) {
            if (mapTile.isOccupied()) {
                Enemy enemy = (Enemy) mapTile.getOccupiedEntity();

                enemy.takeDamage(wizard.dealDamage());
                if (enemy.getHitpoints() < 1) {
                    enemies.remove(enemy);
                    repaint();
                    return;


                }
            }
        }

    }

    public void moveEnemies() {
        System.out.println("In");
        //System.out.println(giantRat.getPosition());
        System.out.println(wizard.getPosition());

        if (enemies.size() > 0) {
            Collections.sort(enemies, new Comparator<Enemy>() {
                @Override
                public int compare(Enemy o1, Enemy o2) {
                    return Double.compare(o1.getPosition().getDistance(wizard.getPosition()), o2.getPosition().getDistance(wizard.getPosition()));
                }
            });

            for (Enemy enemy : enemies) {
                if (enemy.getPosition().getDistance(wizard.getPosition())<1.4){
                    //Practically not a diagonal
                    wizard.takeDamage(enemy.dealDamage());
                    System.out.println("Slash");
                }
                else {
                    enemy.runAstar(roomMap, roomMap.getMapTile(wizard.getPosition()));
                    enemy.moveEnemy(roomMap);
                }
                //System.out.println("Enemy new post"+enemy.getPosition());
            }
            for (Enemy enemy : enemies) {
                System.out.println("Enemies new positions" + enemy.getPosition());
            }
        }
        repaint();
        System.out.println("Out");
    }

    public void runAutomata() {
        roomMap.cellularAutomata();
        repaint();
    }

    public void floodFill() {
        roomMap.floodFill();
        //roomMap.addExit(new Room("Test","Test",new RoomMap()));

        roomMap.setFloorsTextures();
        roomMap.setWallsTextures();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //map.drawMap(g);
        roomMap.drawFocusedMap(g, wizard, enemies);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }
}
