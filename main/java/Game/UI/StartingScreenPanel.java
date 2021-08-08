package Game.UI;

import javax.swing.*;
import java.awt.*;

public class StartingScreenPanel extends JPanel implements ChangePanelSubject {

    private final GridBagConstraints gbc;
    private ChangePanelObserver changePanelObserver;

    private JTextField playerClass;
    private JTextField playerName;
    private JTextField debug;
    private JButton startButton;


    public StartingScreenPanel(ChangePanelObserver changePanelObserver) {
        super();
        this.setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
        this.gbc.weightx = 1;
        this.gbc.weighty = 1;
        this.gbc.fill = GridBagConstraints.HORIZONTAL;

        this.setChangePanelObserver(changePanelObserver);
    }

    public void addComponent(JComponent component, int gridx, int gridy, int weight) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = weight;
        this.add(component, gbc);
    }

    public void addAllComponents() {
        JLabel jLabel = new JLabel("<html><h1 style=\"color: #f28500;\">Escape the Atuan Tombs!</h1>\n" +
                "<h2 style=\"color: #ad5f00;\">(a kinda generic roguelike dungeon game)</h2>\n" +
                "<h3 style=\"color: #211200;\"> Vasilis Kalekis - 2020507</h3></html>");

        this.playerClass = new JTextField(20);
        this.playerName = new JTextField(20);
        this.debug = new JTextField(20);
        this.startButton = new JButton("Start");

        startButton.addActionListener(e -> {
            if (((playerClass.getText().equalsIgnoreCase("wizard") || (playerClass.getText().equalsIgnoreCase("warrior")))) &&
                    (playerName.getText().length() > 0)) {
                notify("GAME");
            }

        });

        this.addComponent(jLabel, 1, 0, 3);

        this.addComponent(new JLabel("<html><h3>Please enter Player Class (Warrior or Wizard).</h3></html>"), 0, 1, 2);
        this.addComponent(playerClass, 2, 1, 1);

        this.addComponent(new JLabel("<html><h3>Please enter Player name. (>=1 characters) </h3></html>"), 0, 2, 2);
        this.addComponent(playerName, 2, 2, 1);

        this.addComponent(new JLabel("<html><h3>Please enter \"debug\" for one-shooting enemies.</h3></html>"), 0, 3, 2);
        this.addComponent(debug, 2, 3, 1);

        this.addComponent(startButton, 1, 4, 1);
    }

    public String[] getValues() {
        return new String[]{playerClass.getText(), playerName.getText(), debug.getText()};
    }

    @Override
    public void setChangePanelObserver(ChangePanelObserver changePanelObserver) {
        this.changePanelObserver = changePanelObserver;
    }

    @Override
    public void notify(String s) {
        changePanelObserver.update(s);
    }
}