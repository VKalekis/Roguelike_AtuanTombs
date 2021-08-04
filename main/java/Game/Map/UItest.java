package Game.Map;

import Game.Player;

import javax.swing.*;
import java.awt.*;

public class UItest {
    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(() -> {
            MyFrame frame = new MyFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class MyFrame extends JFrame {
    public MyFrame() throws HeadlessException {
        MapComponent mapComponent = new MapComponent();



        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        JButton button1 = new JButton("IAM1");
        JButton button2 = new JButton("IAM2");
        JButton button3 = new JButton("N");
        JButton button4 = new JButton("S");
        JButton button5 = new JButton("E");
        JButton button6 = new JButton("W");

        button1.addActionListener(e -> {
            mapComponent.runAutomata();
        });

        button2.addActionListener(e->{
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


        mainPanel.add(button1);
        mainPanel.add(button2);
        mainPanel.add(button3);
        mainPanel.add(button4);
        mainPanel.add(button5);
        mainPanel.add(button6);
        mainPanel.add(mapComponent);
        add(mainPanel);
        pack();
    }
}

class MapComponent extends JComponent {
    private RoomMap roomMap;
    private Player player;

    public MapComponent() {

        player = new Player(new Position(31,31));


        roomMap = new RoomMap();
        roomMap.initializeMap();
        //map.RandomWalk();
        roomMap.generateRandomMap(48);
    }

    public void movePlayer(String dir) {
        player.movePlayer(dir);
        repaint();
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
       // roomMap.drawMap(g);
        //roomMap.drawFocusedMap(g,player);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }
}
