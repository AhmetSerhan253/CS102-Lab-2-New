// File: src/ui/SimulationPanel.java
package ui;

import simulation.World;
import particles.*;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class SimulationPanel extends JPanel {

    private final int rows = 60;
    private final int cols = 90;
    private final int cellSize = 10;

    private final World world = new World(rows, cols);

    // "sand", "water", "stone", "smoke", "erase"
    private String currentTool = "sand";

    private final Timer timer;
    private boolean autoRunning = false;

    // Daire yarıçapı (hücre cinsinden)
    private final int radius = 2; // dr^2 + dc^2 <= 4  (radius^2)

    public SimulationPanel() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.BLACK);
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                applyTool(e);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                applyTool(e);
            }
        });

        timer = new Timer(35, e -> step());
        timer.setRepeats(true);
    }

    public void step() {
        world.updateStep();
        repaint();
    }

    public void toggleAuto() {
        autoRunning = !autoRunning;
        if (autoRunning) timer.start();
        else timer.stop();
    }

    public boolean isAutoRunning() {
        return autoRunning;
    }

    public void setCurrentTool(String tool) {
        currentTool = tool;
    }

    private void applyTool(MouseEvent e) {
        int c = e.getX() / cellSize;
        int r = e.getY() / cellSize;

        for (int dr = -radius; dr <= radius; dr++) {
            for (int dc = -radius; dc <= radius; dc++) {

                int nr = r + dr;
                int nc = c + dc;

                if (!world.inBounds(nr, nc)) continue;

                // daire içinde mi? distance^2 <= radius^2  :contentReference[oaicite:1]{index=1}
                if (dr * dr + dc * dc <= radius * radius) {

                    if ("erase".equals(currentTool)) {
                        world.set(nr, nc, null); // sil
                    } else {
                        // ekle
                        switch (currentTool) {
                            case "water":
                                world.set(nr, nc, new Water());
                                break;
                            case "stone":
                                world.set(nr, nc, new Stone());
                                break;
                            case "smoke":
                                world.set(nr, nc, new Smoke());
                                break;
                            default:
                                world.set(nr, nc, new Sand());
                        }
                    }
                }
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                Particle p = world.get(r, c);
                if (p == null) continue;

                if (p instanceof Sand) {
                    g.setColor(Color.YELLOW);
                } else if (p instanceof Water) {
                    g.setColor(Color.BLUE);
                } else if (p instanceof Stone) {
                    g.setColor(Color.GRAY);
                } else if (p instanceof Smoke) {
                    // dark gray -> white fade :contentReference[oaicite:2]{index=2}
                    Smoke s = (Smoke) p;
                    float ratio = (float) s.getAge() / s.getMaxAge();
                    int gray = (int) (60 + ratio * 195);
                    if (gray < 0) gray = 0;
                    if (gray > 255) gray = 255;
                    g.setColor(new Color(gray, gray, gray));
                } else {
                    g.setColor(Color.MAGENTA);
                }

                g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
    }
}