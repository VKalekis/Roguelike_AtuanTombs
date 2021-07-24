package Game.Map;

import Game.Astar;
import Game.Enemies.Enemy;
import Game.Enemies.GiantRat;
import Game.GameState;
import Game.PlayerSrc.Wizard;
import Game.UI.LabelTranscriptReader;
import Game.UI.LabelTranscriptWriter;
import Game.UI.TextAreaTranscriptReader;
import Game.UI.TextAreaTranscriptWriter;


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

class MyJLabel extends JLabel implements LabelTranscriptReader {
    public MyJLabel() {
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    @Override
    public void readLabelTranscript(String s) {
        super.setText(s);
    }
}

class MyTextArea extends JScrollPane implements TextAreaTranscriptReader {
    static JTextArea jTextArea = new JTextArea(8, 30);

    public MyTextArea() {
        super(jTextArea);
        jTextArea.setEditable(false);
    }

    @Override
    public void readTextAreaTranscript(String s) {
        jTextArea.append(s);
    }
}

class MyFrame1 extends JFrame {
    public MyFrame1() throws HeadlessException {
        MyJLabel jLabel = new MyJLabel();
        MyTextArea myTextArea = new MyTextArea();
        MapComponent1 mapComponent = new MapComponent1(jLabel, myTextArea);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel secondaryPanel = new JPanel();
        secondaryPanel.setLayout(new GridLayout(2,1));

//        JButton button1 = new JButton("IAM1");
//        JButton button2 = new JButton("IAM2");
//        JButton button3 = new JButton("N");
//        JButton button4 = new JButton("S");
//        JButton button5 = new JButton("E");
//        JButton button6 = new JButton("W");
//        JButton buttona = new JButton("a*");
//
//
//        button1.addActionListener(e -> {
//            mapComponent.runAutomata();
//        });
//
//        button2.addActionListener(e -> {
//            mapComponent.floodFill();
//        });
//
//        button3.addActionListener(e -> {
//            mapComponent.movePlayer("N");
//        });
//
//        button4.addActionListener(e -> {
//            mapComponent.movePlayer("S");
//        });
//
//        button5.addActionListener(e -> {
//            mapComponent.movePlayer("E");
//        });
//
//        button6.addActionListener(e -> {
//            mapComponent.movePlayer("W");
//        });
//
//        buttona.addActionListener(e -> {
//            mapComponent.runastar();
//        });
//
//        mainPanel.add(button1);
//        mainPanel.add(button2);
//        mainPanel.add(button3);
//        mainPanel.add(button4);
//        mainPanel.add(button5);
//        mainPanel.add(button6);
//        mainPanel.add(buttona);



        mainPanel.add(mapComponent, BorderLayout.WEST);

        secondaryPanel.add(jLabel);
        secondaryPanel.add(myTextArea);
        mainPanel.add(secondaryPanel,BorderLayout.EAST);

        add(mainPanel);
        pack();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100,700);
    }
}

class MapComponent1 extends JComponent implements LabelTranscriptWriter, TextAreaTranscriptWriter {

    GameState gameState;
    LabelTranscriptReader labelTranscriptReader;
    TextAreaTranscriptReader textAreaTranscriptReader;

    public MapComponent1(LabelTranscriptReader labelTranscriptReader,
                         TextAreaTranscriptReader textAreaTranscriptReader) {

        setLabelTranscriptReader(labelTranscriptReader);
        setTextAreaTranscriptReader(textAreaTranscriptReader);

        gameState = new GameState(labelTranscriptReader,textAreaTranscriptReader);
        //gameState.initialize();

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
                        gameState.movePlayer("N");
                        sendTextAreaTranscript("Pressed W\n");
                        break;
                    case 's':
                    case 'S':
                        gameState.movePlayer("S");
                        sendTextAreaTranscript("Pressed S\n");
                        break;
                    case 'd':
                    case 'D':
                        gameState.movePlayer("E");
                        sendTextAreaTranscript("Pressed D\n");
                        break;
                    case 'a':
                    case 'A':
                        gameState.movePlayer("W");
                        sendTextAreaTranscript("Pressed A\n");
                        break;
                    case 'g':
                    case 'G':
                        gameState.goToNextRoom();
                        sendTextAreaTranscript("Pressed G\n");
                        break;
                    case 'x':
                    case 'X':
                        gameState.playerdealDamage();
                        sendTextAreaTranscript("Pressed X\n");
                        break;
                    case 'u':
                    case 'U':
                        gameState.getPlayer().use();
                        sendTextAreaTranscript("Pressed U\n");
                        break;
                    case 'p':
                    case 'P':
                        gameState.getPlayer().dropItem();
                        sendTextAreaTranscript("Pressed P\n");
                        break;
                    case '[':
                        gameState.getPlayer().moveInventoryCursor(-1);
                        sendTextAreaTranscript("Pressed [\n");
                        break;
                    case ']':
                        gameState.getPlayer().moveInventoryCursor(+1);
                        sendTextAreaTranscript("Pressed ]\n");
                        break;
                    case 'h':
                    case 'H':
                        gameState.equip();
                        sendTextAreaTranscript("Pressed P\n");
                        break;
                    case 'r':
                    case 'R':
                        gameState.getPlayer().rest();
                        sendTextAreaTranscript("Rested");
                        break;
                    default:
                        break;
                }
                if (e.getKeyChar()!='[' && e.getKeyChar()!=']') {
                    gameState.moveEnemies();
                    gameState.spawnEnemies(0.01);

                }
                sendLabelTranscript(gameState.getPlayer().getStatsHTML());
                repaint();


            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("Pressed " + e.getKeyChar());

            }
        });

    }







    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //map.drawMap(g);
        gameState.drawMap(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 700);
    }

    @Override
    public void setLabelTranscriptReader(LabelTranscriptReader r) {
        this.labelTranscriptReader = r;
    }

    @Override
    public void sendLabelTranscript(String s) {
        this.labelTranscriptReader.readLabelTranscript(s);
    }

    @Override
    public void setTextAreaTranscriptReader(TextAreaTranscriptReader r) {
        this.textAreaTranscriptReader = r;
    }

    @Override
    public void sendTextAreaTranscript(String s) {
        this.textAreaTranscriptReader.readTextAreaTranscript(s);
    }
}
