// File: src/simulation/Main.java
package simulation;

import javax.swing.JFrame;
import ui.ControlPanel;
import ui.SimulationPanel;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Falling Sand Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SimulationPanel simPanel = new SimulationPanel();
        ControlPanel control = new ControlPanel(simPanel);

        frame.add(simPanel);
        frame.add(control, "South");

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}