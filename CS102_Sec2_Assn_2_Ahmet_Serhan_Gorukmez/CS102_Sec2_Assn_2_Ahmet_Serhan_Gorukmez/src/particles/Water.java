package particles;

import simulation.World;
import java.util.Random;

public class Water extends Particle {

    private static final Random rand = new Random();
    private int direction = (int) (Math.random() * 2 - 1);

    public void update(World world, int r, int c) {

        int down = r + 1;

        if (world.isEmpty(down, c)) {
            world.moveToEmpty(r, c, down, c);
            return;
        }

        boolean moveToLeft = rand.nextBoolean();

        if (moveToLeft) {
            if (world.isEmpty(down, c - 1)) {
                world.moveToEmpty(r, c, down, c - 1);
                return;
            }
            if (world.isEmpty(down, c + 1)) {
                world.moveToEmpty(r, c, down, c + 1);
                return;
            }
        } else {
            if (world.isEmpty(down, c + 1)) {
                world.moveToEmpty(r, c, down, c + 1);
                return;
            }
            if (world.isEmpty(down, c - 1)) {
                world.moveToEmpty(r, c, down, c - 1);
                return;
            }
        }

        if (direction == 1) {
            if (world.isEmpty(r, c + 1)) {
                world.moveToEmpty(r, c, r, c + 1);
                return;
            }
            if (!world.isEmpty(r, c + 1) && world.isEmpty(r, c - 1)) {
                world.moveToEmpty(r, c, r, c - 1);
            }
        } else {
            if (world.isEmpty(r, c - 1)) {
                world.moveToEmpty(r, c, r, c - 1);
                return;
            }
            if (!world.isEmpty(r, c - 1) && world.isEmpty(r, c + 1)) {
                world.moveToEmpty(r, c, r, c + 1);
            }
        }
    }
}