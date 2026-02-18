package simulation;

import java.util.Random;
import particles.Particle;

public class World {
    private final int rows;
    private final int cols;
    private final Particle[][] grid;

    private final Random random = new Random();

    public World(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Particle[rows][cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean inBounds(int i, int j) {
        return i >= 0 && j >= 0 && i < rows && j < cols;
    }

    public Particle get(int i, int j) {
        if (!inBounds(i, j))
            return null;
        return grid[i][j];
    }

    public void set(int i, int j, Particle p) {
        if (!inBounds(i, j))
            return;
        grid[i][j] = p;
    }

    public boolean isEmpty(int i, int j) {
        return inBounds(i, j) && grid[i][j] == null;
    }

    public boolean moveToEmpty(int srcRow, int srcCol, int dstRow, int dstCol) {
        if (!inBounds(srcRow, srcCol) || !inBounds(dstRow, dstCol))
            return false;
        if (grid[srcRow][srcCol] == null)
            return false;
        if (grid[dstRow][dstCol] != null)
            return false;

        grid[dstRow][dstCol] = grid[srcRow][srcCol];
        grid[srcRow][srcCol] = null;
        return true;
    }

    public boolean swap(int x1, int y1, int x2, int y2) {
        if (!inBounds(x1, y1) || !inBounds(x2, y2))
            return false;

        Particle a = grid[x1][y1];
        Particle b = grid[x2][y2];
        if (a == null || b == null)
            return false;

        grid[x1][y1] = b;
        grid[x2][y2] = a;
        return true;
    }

    public void clear() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = null;
            }
        }
    }

    private void resetUpdatedFlags() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Particle p = grid[r][c];
                if (p != null)
                    p.setUpdated(false);
            }
        }
    }

    private void shuffleIndices(int[] idx) {
        for (int i = idx.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = idx[i];
            idx[i] = idx[j];
            idx[j] = tmp;
        }
    }

    public void updateStep() {
        resetUpdatedFlags();

        int total = rows * cols;
        int[] order = new int[total];

        for (int i = 0; i < total; i++)
            order[i] = i;

        for (int pass = 0; pass < 8; pass++) {
            shuffleIndices(order);

            for (int k = 0; k < total; k++) {
                int index = order[k];
                int r = index / cols;
                int c = index % cols;

                Particle p = grid[r][c];
                if (p == null)
                    continue;
                if (p.isUpdated())
                    continue;

                p.update(this, r, c);
                p.setUpdated(true);
            }
        }
    }
}