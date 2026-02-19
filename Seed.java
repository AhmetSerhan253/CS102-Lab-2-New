package particles;

import simulation.World;

public class Seed extends Particle {

    @Override
    public void update(World world, int r, int c) {

        int down = r + 1;

        // Gravity: aşağı boşsa düş 7
        if (world.inBounds(down, c) && world.get(down, c) == null) {
            world.moveToEmpty(r, c, down, c);
            return;
        }

        // Artık düşemiyor => "rest" durumundayız:
        // Eğer altındaki cell sand ise cactus'a dönüş, değilse yok ol 8
        Particle below = world.inBounds(down, c) ? world.get(down, c) : null;

        if (below instanceof Sand) {
            world.set(r, c, new Cactus(1));
        } else {
            world.set(r, c, null);
        }
    }
}
