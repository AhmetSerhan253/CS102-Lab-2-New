// File: src/ui/ControlPanel.java
package ui;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

    private final JButton autoBtn;

    public ControlPanel(SimulationPanel panel) {

        JButton sand = new JButton("Sand");
        JButton water = new JButton("Water");
        JButton stone = new JButton("Stone");
        JButton smoke = new JButton("Smoke");
        JButton erase = new JButton("Erase"); // ✅ silme modu

        sand.addActionListener(e -> panel.setCurrentTool("sand"));
        water.addActionListener(e -> panel.setCurrentTool("water"));
        stone.addActionListener(e -> panel.setCurrentTool("stone"));
        smoke.addActionListener(e -> panel.setCurrentTool("smoke"));
        erase.addActionListener(e -> panel.setCurrentTool("erase"));

        JButton stepBtn = new JButton("Step");
        stepBtn.addActionListener(e -> panel.step());

        autoBtn = new JButton("Auto: OFF");
        autoBtn.addActionListener(e -> {
            panel.toggleAuto();
            autoBtn.setText(panel.isAutoRunning() ? "Auto: ON" : "Auto: OFF");
        });

        add(sand);
        add(water);
        add(stone);
        add(smoke);
        add(erase);

        add(stepBtn);
        add(autoBtn);
    }
}